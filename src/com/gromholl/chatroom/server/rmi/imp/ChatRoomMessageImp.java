package com.gromholl.chatroom.server.rmi.imp;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Time;
import java.util.Date;

import rmchatroom.ChatRoomInvalidStateException;
import rmchatroom.ChatRoomMessage;

public class ChatRoomMessageImp implements ChatRoomMessage, Serializable {

    private static final long serialVersionUID = -2442978233038013284L;
    
    String author;
    String message;
    Time time;
    
    public ChatRoomMessageImp(String author, String message, Time time) {
        this.author = author;
        this.message = message;
        this.time = time;
    }
    
    @Override
    public String getAuthor() throws RemoteException,
            ChatRoomInvalidStateException {        
        return author;
    }

    @Override
    public String getText() throws RemoteException,
            ChatRoomInvalidStateException {
        return message;
    }

    @Override
    public Date getTime() throws RemoteException, ChatRoomInvalidStateException {
        return time;
    }

}
