package com.gromholl.chatroom.client;

import java.rmi.RemoteException;

import javax.swing.JTabbedPane;

import rmchatroom.ChatRoom;
import rmchatroom.ChatRoomInvalidStateException;

public class ChatRoomPanel extends JTabbedPane {

    private static final long serialVersionUID = 1L;

    public void addChatRoom(ChatRoom room) {
                
        try {
            ChatRoomHandle handler = new ChatRoomHandle(room, this);
            addTab(room.getName(), handler);
            int index = indexOfComponent(handler);
            setTabComponentAt(index, handler.getButtonTabComponent());
        } catch(RemoteException | ChatRoomInvalidStateException e) {
            e.printStackTrace();
        }
    }
    
    public void unsibscribeAll() {
        for(int i = 0; i < getTabCount(); i++) {
            ChatRoomHandle h = (ChatRoomHandle) getComponentAt(i);
            h.unsubscribe();
        }
        removeAll();
    }

}
