package com.gromholl.chatroom.client;

import java.rmi.RemoteException;
import java.util.List;

import javax.swing.AbstractListModel;

import rmchatroom.ChatRoom;
import rmchatroom.ChatRoomInvalidStateException;

public class ChatRoomListModel extends AbstractListModel<String> {

    private static final long serialVersionUID = 1L;
    
    private List<ChatRoom> list = null;
        
    public void setChatRooms(List<ChatRoom> list) {
        this.list = list;
        fireContentsChanged(this, 0, this.list.size()-1);
    }
    
    @Override
    public String getElementAt(int index) {
        if(list == null) {
            return null;
        } 
        
        try {
            return list.get(index).getName();
        } catch(RemoteException | ChatRoomInvalidStateException e ) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public int getSize() {
        if(list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public ChatRoom getChatRoomAt(int index) {
        if(list == null) {
            return null;
        }
        if(index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }
    
}
