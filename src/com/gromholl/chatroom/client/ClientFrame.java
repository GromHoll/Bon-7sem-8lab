package com.gromholl.chatroom.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import rmchatroom.ChatRoom;
import rmchatroom.ChatRoomAlreadyExistsException;
import rmchatroom.ChatRoomEntry;
import rmchatroom.ChatRoomInvalidStateException;
import rmchatroom.ChatRoomLoginException;
import rmchatroom.ChatRoomSession;


public class ClientFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    public static final String APP_NAME = "GromHoll's ChatRoom";
    
    public static final Dimension WINDOW_SIZE = new Dimension(720, 520); 

    /* GUI components */
    
    private LoginPanel loginPanel;
    private MainPanel mainPanel;
    
    /* Other components */

    private String nickname;
    private String host;
    private int port;
    
    private ChatRoomEntry chatRoomEntry;
    private ChatRoomSession session = null;
    
    public ClientFrame(String host, int port) throws RemoteException, NotBoundException {
        super(APP_NAME);
        
        this.host = host;
        this.port = port;
        
        loginPanel = new LoginPanel();
        mainPanel = new MainPanel();
        
        Registry registry = LocateRegistry.getRegistry(this.host, this.port);
        chatRoomEntry = (ChatRoomEntry) registry.lookup("ChatRoomEntry");        
        
        loginPanel.setServerInfo(chatRoomEntry.info());
        
        setContentPane(loginPanel);
        
        loginPanel.addActionListenerForLoginButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                login();
            }
        });
        
        mainPanel.addActionListnerForRefreshButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                refreshChatRoomList();
            }
        });
        mainPanel.addActionListnerForCreateRoomButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewRoom();
            }
        });
        mainPanel.addActionListnerForSendButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        mainPanel.addActionListnerForSubscribeButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subscribe();
            }
        });
        
        setPreferredSize(WINDOW_SIZE);
        pack();
        setResizable(false);
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeApp();
            }
        });
        
        this.setVisible(true);
    }
    
    private void login() {
        
        if(!loginPanel.isFillField()) {
            loginPanel.setErrorInfo("Fill all fields");
            return;
        }
        
        try {
            session = chatRoomEntry.login(loginPanel.getLogin(), loginPanel.getPassword());
        } catch(RemoteException e) {
            fatalErrorExit("Fatal error", e);
        } catch(ChatRoomLoginException e) {
            loginPanel.setErrorInfo("Login error");
            return;
        }

        nickname = loginPanel.getLogin();
        setTitle(APP_NAME + " : " + nickname);
        setContentPane(mainPanel);
        revalidate();        

        refreshChatRoomList();
    }
    private void refreshChatRoomList() {
        if(session == null) {
            return;
        }
        
        try {
            List<ChatRoom> list = session.list();
            mainPanel.setChatRoomList(list);
        } catch(RemoteException | ChatRoomInvalidStateException e) {
            fatalErrorExit("Fatal error", e);
        }
    }
    private void createNewRoom() {
        
        String name = mainPanel.getNewChatRoomName();
        if(name == null) {
            return;
        }
        if(name.isEmpty()) {
            return;
        }
        
        try {
            ChatRoom room = session.create(name);
            mainPanel.addChatRoom(room);
        } catch(RemoteException e) {
            fatalErrorExit("Fatal error", e);
        } catch(ChatRoomInvalidStateException e) {
            fatalErrorExit("Fatal error", e);
        } catch(ChatRoomAlreadyExistsException e) {
            warning("Room already exist.");
        }
    }
    private void sendMessage() {
        mainPanel.sendSelectedChatRoom();
    }
    private void subscribe() {
        mainPanel.addChatRoom(mainPanel.getSelectedRoom());
    }
    
    private void closeApp() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the application?",
            "Exit Application",
            JOptionPane.YES_NO_OPTION);

        if(result == JOptionPane.YES_OPTION) {
            mainPanel.unsibscribeAll();
            if(session != null) {
                try {
                    session.logout();
                } catch(RemoteException | ChatRoomInvalidStateException e) {
                    e.printStackTrace();
                }
            }
            dispose();
            System.exit(0);
        }
    }
    
    private void fatalErrorExit(String error, Exception e) {
        e.printStackTrace();
        JOptionPane.showConfirmDialog(this, error, "Error!", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        this.dispose();
    }
    private void warning(String text) {
        JOptionPane.showConfirmDialog(this, text, "Warning.", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
    }
    
    public static void main(String[] args) {
        
        final String host; 
        final int port;
        
        if(args.length == 2) {
            
            host = args[0];

            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Port number error: " + args[1]);
                return;
            }
        } else {
            System.err.println("Incorrect arguments count (" + args.length + ").");
            return;
        }
    
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ClientFrame(host, port);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
