package com.gromholl.chatroom.client.rmi.imp;

import java.io.Serializable;
import java.rmi.RemoteException;

import rmchatroom.ChatRoomInvalidStateException;
import rmchatroom.ChatRoomMessage;
import rmchatroom.ChatRoomMessageListener;

import com.gromholl.chatroom.client.ChatRoomHandle;

public class ChatRoomMessageListenerImp implements ChatRoomMessageListener, Serializable {

    private static final long serialVersionUID = -6395044274802593732L;
    
    private ChatRoomHandle handle;
    
    public ChatRoomMessageListenerImp(ChatRoomHandle handle) {
        this.handle = handle;
    }
    
    @Override
    public void processNewMessage(ChatRoomMessage msg) throws RemoteException,
            ChatRoomInvalidStateException {        
        handle.addMessage(msg);
    }
    
}
