package com.gromholl.chatroom.client;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import rmchatroom.ChatRoom;

public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField tfRoomName;
    
    private ChatRoomListModel listModel;
    
    private JButton btnRefresh;        
    private JButton btnSubsribe;
    private JButton btnCreateRoom;
    private JButton btnSend;
    
    private JTextArea taMessage;
    
    private JList<String> listRoom;
    
    private ChatRoomPanel chatRoomPanel;
    
    /**
     * Create the panel.
     */
    public MainPanel() {
        
        listModel = new ChatRoomListModel();
        
        listRoom = new JList<String>(listModel);
        
        btnRefresh = new JButton("Refresh");        
        btnSubsribe = new JButton("Subsribe");
        btnCreateRoom = new JButton("Create room");
        btnSend = new JButton("SEND");
        
        taMessage = new JTextArea();
        
        tfRoomName = new JTextField();
        tfRoomName.setColumns(10);
               
        chatRoomPanel = new ChatRoomPanel();
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(taMessage, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                        .addComponent(chatRoomPanel, GroupLayout.PREFERRED_SIZE, 496, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                        .addComponent(listRoom, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRefresh, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSubsribe, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfRoomName, Alignment.LEADING, 172, 172, 172)
                        .addComponent(btnCreateRoom, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(listRoom, GroupLayout.PREFERRED_SIZE, 345, GroupLayout.PREFERRED_SIZE)
                        .addComponent(chatRoomPanel, GroupLayout.PREFERRED_SIZE, 339, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(taMessage, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                            .addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                            .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(btnRefresh)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(btnSubsribe)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(tfRoomName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCreateRoom))))
                    .addContainerGap())
        );
        groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {listRoom, btnRefresh, btnSubsribe, btnCreateRoom, tfRoomName});
        setLayout(groupLayout);        
    }
    
    public void setChatRoomList(List<ChatRoom> list) {
        listModel.setChatRooms(list);
    }
    
    public void addActionListnerForSendButton(ActionListener l) {
        if(l != null) {
            btnSend.addActionListener(l);
        }
    }
    public void addActionListnerForRefreshButton(ActionListener l) {
        if(l != null) {
            btnRefresh.addActionListener(l);
        }
    }
    public void addActionListnerForSubscribeButton(ActionListener l) {
        if(l != null) {
            btnSubsribe.addActionListener(l);
        }
    }
    public void addActionListnerForCreateRoomButton(ActionListener l) {
        if(l != null) {
            btnCreateRoom.addActionListener(l);
        }
    }
    
    public String getNewChatRoomName() {
        return tfRoomName.getText();
    }
    public ChatRoom getSelectedRoom() {
        return listModel.getChatRoomAt(listRoom.getSelectedIndex());
    }
    
    public void addChatRoom(ChatRoom room) {
        if(room == null) {
            return;
        }
        chatRoomPanel.addChatRoom(room);
    }
    
    public void unsibscribeAll() {
        chatRoomPanel.unsibscribeAll();
    }

    public void sendSelectedChatRoom() {
        Component c = chatRoomPanel.getSelectedComponent();
        if(c == null) {
            return;
        }
        
        ChatRoomHandle h = (ChatRoomHandle) c;
        h.sendMessage(taMessage.getText());
    }
    
}
