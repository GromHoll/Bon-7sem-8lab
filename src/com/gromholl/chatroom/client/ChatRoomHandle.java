package com.gromholl.chatroom.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import rmchatroom.ChatRoom;
import rmchatroom.ChatRoomInvalidStateException;
import rmchatroom.ChatRoomMessage;
import rmchatroom.ChatRoomMessageListener;

import com.gromholl.chatroom.client.rmi.imp.ChatRoomMessageListenerImp;

public class ChatRoomHandle extends JTextArea {

    private ChatRoomMessageListenerImp listner;
    private ChatRoom room;
    private ButtonTabComponent tabComponent;
    private JTabbedPane pane;
    
    private static final long serialVersionUID = 1L;
    
    public ChatRoomHandle(ChatRoom room, JTabbedPane pane) throws RemoteException, ChatRoomInvalidStateException {
        this.room = room;
        this.pane = pane;
        this.setName(room.getName());
        tabComponent = new ButtonTabComponent(this);
        
        listner = new ChatRoomMessageListenerImp(this);
        ChatRoomMessageListener stub = (ChatRoomMessageListener) UnicastRemoteObject.exportObject(listner, 0);
        room.addMessageListener(stub);
    }
    
    public ButtonTabComponent getButtonTabComponent() {
        return tabComponent;
    }
    
    public JTabbedPane getTabbedPane() {
        return pane;
    }
    
    public void unsubscribe() {
        try {
            room.removeMessageListener(listner);
        } catch(RemoteException | ChatRoomInvalidStateException e) {
            e.printStackTrace();
        }
    }

    public void loadAllMessages() {
        try {
            List<ChatRoomMessage> messages = room.list();
            
            setText("");
            for(ChatRoomMessage msg : messages) {
                addMessage(msg);
            }            
        } catch(RemoteException | ChatRoomInvalidStateException e) {
            e.printStackTrace();
        }     
    }    
    public void addMessage(ChatRoomMessage msg) throws RemoteException, ChatRoomInvalidStateException {
        append(msg.getAuthor() + " (" + msg.getTime() + ") : " + msg.getText() + "\n\n");
    }

    public void sendMessage(String msg) {
        if(msg == null) {
            return;
        }
        if(msg.isEmpty()) {
            return;
        }
        
        try {
            room.newMessage(msg);
        } catch(RemoteException | ChatRoomInvalidStateException e) {
            e.printStackTrace();
        }
    }
}
