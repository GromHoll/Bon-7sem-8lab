package com.gromholl.chatroom.server;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import rmchatroom.ChatRoomInvalidStateException;
import rmchatroom.ChatRoomMessage;

import com.gromholl.chatroom.server.rmi.imp.ChatRoomMessageImp;

public class DBConnector {
    
    private static final String CREATE_USERS = "Create table if not exists USERS " +
                                               "(NAME varchar(32) primary key, " +
    		                                   "PASSWORD varchar(32) not null)";
    
    private static final String CREATE_CHATROOMS = "Create table if not exists CHATROOMS " +
                                                   "(NAME varchar(32) primary key)";
    
    private static final String CREATE_MESSAGES = "Create table if not exists MESSAGES " +
                                                  "(ID INTEGER primary key autoincrement, " +
                                                  "USER_NAME varchar(32) references USERS(NAME), " +
                                                  "CHATROOM_NAME varchar(32) references CHATROOMS(NAME), " +
                                                  "MSG_TIME time not null, " +
                                                  "MSG text not null)";
    
    private static final String ADD_USER     = "Insert into USERS VALUES(?, ?)";    
    private static final String ADD_CHATROOM = "Insert into CHATROOMS VALUES(?)";
    private static final String ADD_MESSAGE  = "Insert into MESSAGES " +
    		                                   "(USER_NAME, CHATROOM_NAME, MSG_TIME, MSG) " +
    		                                   "VALUES(?, ?, ?, ?)";
    
    private static final String GET_USERS     = "Select * from USERS";    
    private static final String GET_CHATROOMS = "Select * from CHATROOMS";
    private static final String GET_MESSAGES  = "Select * from MESSAGES " +
    		                                    "where CHATROOM_NAME = ?";
    
    private Connection db;
    
    public DBConnector(String dbFile) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        db = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        initDB();
    }
    private void initDB() throws SQLException {
        Statement st;

        st = db.createStatement();
        st.execute(CREATE_USERS);
        st.close();
        
        st = db.createStatement();
        st.execute(CREATE_CHATROOMS);
        st.close();
        
        st = db.createStatement();
        st.execute(CREATE_MESSAGES);
        st.close();
    }
    
    public void addUser(String name, String password) throws SQLException {     
        
        PreparedStatement pst;    
        
        pst = db.prepareStatement(ADD_USER);
        pst.setString(1, name);
        pst.setString(2, password);
        pst.executeUpdate();
        pst.close();        
    }
    public HashMap<String, String> getUsersList() throws SQLException {
        
        Statement st;
        ResultSet res;
        HashMap<String, String> users;
        
        st = db.createStatement();
        res = st.executeQuery(GET_USERS);        
        users = new HashMap<String, String>();
        
        while (res.next()) {  
            users.put(res.getString("NAME"), res.getString("PASSWORD"));
        }        
        res.close();
        st.close();
        
        return users;
    }

    public ChatRoomMessage addMessage(String user, String chatroom, String text) throws SQLException {
        
        ChatRoomMessage msg = new ChatRoomMessageImp(user, text, new Time(System.currentTimeMillis()));
        
        PreparedStatement pst = null;

        pst = db.prepareStatement(ADD_MESSAGE);
        try {
            pst.setString(1, msg.getAuthor());
            pst.setString(2, chatroom);
            pst.setTime(3, new Time(msg.getTime().getTime()));
            pst.setString(4, msg.getText());
            pst.executeUpdate();
        } catch(RemoteException | ChatRoomInvalidStateException e) {
            e.printStackTrace();
        }
        pst.close();
        
        return msg;
    }
    public List<ChatRoomMessage> getMessages(String chatroom) throws SQLException {
        
        PreparedStatement pst;
        ResultSet res;
        List<ChatRoomMessage> messages;
        
        pst = db.prepareStatement(GET_MESSAGES);
        pst.setString(1, chatroom);
        res = pst.executeQuery();
        messages = new LinkedList<ChatRoomMessage>();
        
        while (res.next()) {  
            messages.add(new ChatRoomMessageImp(res.getString("USER_NAME"),
                                                res.getString("MSG"),
                                                res.getTime("MSG_TIME")));
        }   
        
        res.close();
        pst.close();
        
        return messages;
    }
    
    public void addChatRoom(String chatRoomName) throws SQLException {
        
        PreparedStatement pst;    
        
        pst = db.prepareStatement(ADD_CHATROOM);
        pst.setString(1, chatRoomName);
        pst.executeUpdate();
        pst.close();
    }    
    public List<String> getChatRooms() throws SQLException {

        Statement st;
        ResultSet res;
        List<String> chatRooms;
        
        st = db.createStatement();
        res = st.executeQuery(GET_CHATROOMS);        
        chatRooms = new LinkedList<String>();
        
        while (res.next()) {  
            chatRooms.add(res.getString("NAME"));
        }        
        res.close();
        st.close();
        
        return chatRooms;        
    }

    public void close() throws SQLException {
        db.close();
    }
}
