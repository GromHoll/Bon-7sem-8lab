package com.gromholl.chatroom.server.rmi.imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import rmchatroom.ChatRoomInvalidStateException;
import rmchatroom.ChatRoomMessage;
import rmchatroom.ChatRoomMessageListener;

import com.gromholl.chatroom.server.DBConnector;

public class ChatRoomServer {
    
    private String chatRoomName;
    private DBConnector dbConnector;
    private List<ChatRoomMessageListener> listners;
    
    public ChatRoomServer(String chatRoomName, DBConnector dbConnector) {
        this.chatRoomName = chatRoomName;
        this.dbConnector = dbConnector;
        listners = new LinkedList<ChatRoomMessageListener>();
    }
    
    public void addMessageListener(ChatRoomMessageListener listner)
            throws ChatRoomInvalidStateException {
        listners.add(listner);
    }

    public String getName() throws ChatRoomInvalidStateException {
        return chatRoomName;
    }

    public List<ChatRoomMessage> list() throws ChatRoomInvalidStateException, RemoteException {
        
        List<ChatRoomMessage> res = new LinkedList<ChatRoomMessage>();
        try {
           for(ChatRoomMessage msg : dbConnector.getMessages(chatRoomName)) {
               ChatRoomMessage stub = (ChatRoomMessage) UnicastRemoteObject.exportObject(msg, 0);
               res.add(stub);               
           }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatRoomInvalidStateException();
        }
        
        return res;
    }

    public void addMessage(String user, String message) throws ChatRoomInvalidStateException {
        try {
            ChatRoomMessage msg = dbConnector.addMessage(user, chatRoomName, message);
            ChatRoomMessage stub = (ChatRoomMessage) UnicastRemoteObject.exportObject(msg, 0);
            
            for(ChatRoomMessageListener l : listners) {
                try {
                    l.processNewMessage(stub);
                } catch(RemoteException e) {
                    e.printStackTrace();
                }
            }
            
        } catch (SQLException | RemoteException e) {
            throw new ChatRoomInvalidStateException();
        }             
    }

    public void removeMessageListener(ChatRoomMessageListener listner) throws ChatRoomInvalidStateException {
        listners.remove(listner);
    }
}
