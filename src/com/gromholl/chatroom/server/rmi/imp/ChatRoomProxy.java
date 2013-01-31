package com.gromholl.chatroom.server.rmi.imp;

import java.rmi.RemoteException;
import java.util.List;

import rmchatroom.ChatRoom;
import rmchatroom.ChatRoomInvalidStateException;
import rmchatroom.ChatRoomMessage;
import rmchatroom.ChatRoomMessageListener;

public class ChatRoomProxy implements ChatRoom {

    private String username;
    private ChatRoomServer realRoom;
    
    public ChatRoomProxy(String username, ChatRoomServer realRoom) {
        this.username = username;
        this.realRoom = realRoom;
    }
    
    @Override
    public void addMessageListener(ChatRoomMessageListener listner)
            throws RemoteException, ChatRoomInvalidStateException {
        if(realRoom == null) {
            throw new ChatRoomInvalidStateException();
        }
        
        realRoom.addMessageListener(listner);
    }

    @Override
    public String getName() throws RemoteException,
            ChatRoomInvalidStateException {
        if(realRoom == null) {
            throw new ChatRoomInvalidStateException();
        }
        
        return realRoom.getName();
    }

    @Override
    public List<ChatRoomMessage> list() throws RemoteException,
            ChatRoomInvalidStateException {
        if(realRoom == null) {
            throw new ChatRoomInvalidStateException();
        }
        return realRoom.list();
    }

    @Override
    public void newMessage(String message) throws RemoteException,
            ChatRoomInvalidStateException {
        if(realRoom == null) {
            throw new ChatRoomInvalidStateException();
        }
        
        realRoom.addMessage(username, message);
    }

    @Override
    public void removeMessageListener(ChatRoomMessageListener listner)
            throws RemoteException, ChatRoomInvalidStateException {
        if(realRoom == null) {
            throw new ChatRoomInvalidStateException();
        }
        realRoom.removeMessageListener(listner);        
    }

}
