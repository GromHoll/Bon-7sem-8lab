package com.gromholl.chatroom.server.rmi.imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import rmchatroom.ChatRoom;
import rmchatroom.ChatRoomAlreadyExistsException;
import rmchatroom.ChatRoomInvalidStateException;
import rmchatroom.ChatRoomSession;

import com.gromholl.chatroom.server.Server;

public class ChatRoomSessionImp implements ChatRoomSession {
    
    private String username;
    private Server server;
    
    public ChatRoomSessionImp(String username, Server server) {
        this.username = username;
        this.server = server;
    }
    
    @Override
    public ChatRoom create(String chatRoomName) throws RemoteException,
            ChatRoomInvalidStateException, ChatRoomAlreadyExistsException {
        if(server == null) {
            throw new ChatRoomInvalidStateException();
        }
        
        ChatRoomServer realRoom = server.createChatRoom(chatRoomName);        
        ChatRoomProxy proxy = new ChatRoomProxy(username, realRoom);
        ChatRoom stub = (ChatRoom) UnicastRemoteObject.exportObject(proxy, 0);
        
        return stub;
    }

    @Override
    public List<ChatRoom> list() throws RemoteException,
            ChatRoomInvalidStateException {
        
        List<ChatRoomServer> realRooms = server.getChatRoomList();        
        if(realRooms == null) {
            throw new ChatRoomInvalidStateException();
        }
        
        List<ChatRoom> res = new LinkedList<ChatRoom>();
        
        for(ChatRoomServer realRoom : realRooms) {
            ChatRoomProxy proxy = new ChatRoomProxy(username, realRoom);
            ChatRoom stub = (ChatRoom) UnicastRemoteObject.exportObject(proxy, 0);
            res.add(stub);
        }
        
        return res;
    }

    @Override
    public void logout() throws RemoteException, ChatRoomInvalidStateException {
        System.out.println("User " + username + " leave chat.");
    }

}
