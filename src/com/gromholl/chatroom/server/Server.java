package com.gromholl.chatroom.server;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rmchatroom.ChatRoomAlreadyExistsException;
import rmchatroom.ChatRoomEntry;
import rmchatroom.ChatRoomLoginException;
import rmchatroom.ChatRoomSession;

import com.gromholl.chatroom.server.rmi.imp.ChatRoomServer;
import com.gromholl.chatroom.server.rmi.imp.ChatRoomSessionImp;

public class Server implements ChatRoomEntry {

    static public final int DEFAULT_PORT = 5555;    

    public static final String INFO = "RMI ChatRooms. Created by GromHoll. 2013.";
    
    private int serverPort;
    private DBConnector dbConnector;
    
    private List<ChatRoomServer> rooms;
    private Map<String, String> users;
    
    public Server(int port) throws SQLException, ClassNotFoundException {
        serverPort = port;
        dbConnector = new DBConnector("ChatRoom.db");
    }
    
    public void start() throws SQLException, AccessException, RemoteException, AlreadyBoundException {
        System.out.println("Start server...");
        
        loadDB();        
        ChatRoomEntry stub = (ChatRoomEntry) UnicastRemoteObject.exportObject(this, 0);
       
        Registry registry = LocateRegistry.createRegistry(serverPort);
        registry.bind("ChatRoomEntry", stub);        

        System.out.println("Ready.");
    }
    
    private void loadDB() throws SQLException {
        System.out.println("Load database...");
        
        users = dbConnector.getUsersList();
                
        rooms = new LinkedList<ChatRoomServer>();        
        for(String name : dbConnector.getChatRooms()) {
            rooms.add(new ChatRoomServer(name, dbConnector));
        }        
    }
    
    public ChatRoomServer createChatRoom(String chatRoomName) throws ChatRoomAlreadyExistsException {
        try {
            dbConnector.addChatRoom(chatRoomName);
        } catch(SQLException e) {
            throw new ChatRoomAlreadyExistsException();
        }
        
        ChatRoomServer room = new ChatRoomServer(chatRoomName, dbConnector);
        rooms.add(room);
        return room;
    }
    public List<ChatRoomServer> getChatRoomList() {
        return rooms;
    }
    
    /* Main */
    public static void main(String[] args) {
        
        final int port;
        
        if(args.length == 0) {
            port = DEFAULT_PORT;
        } else if(args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Port number error: " + args[0]);
                return;
            }
        } else {
            System.err.println("Incorrect arguments count (" + args.length + ").");
            return;
        }
                
        try {
            Server s = new Server(port);            
            s.start();            
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    @Override
    public String info() throws RemoteException {
        return INFO;
    }

    @Override
    public ChatRoomSession login(String username, String password)
            throws ChatRoomLoginException, RemoteException {
        
        if(users.containsKey(username)) {
            if(!users.get(username).equals(password)) {
                throw new ChatRoomLoginException();
            }
        } else {
            try {
                dbConnector.addUser(username, password);
                users.put(username, password);
            } catch(SQLException e) {
                throw new ChatRoomLoginException();
            }
        }
        
        ChatRoomSessionImp session = new ChatRoomSessionImp(username, this);
        ChatRoomSession stub = (ChatRoomSession) UnicastRemoteObject.exportObject(session, 0);
                
        return stub;        
    }

}
