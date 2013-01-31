package com.gromholl.chatroom.client;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class LoginPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private JTextField tfLogin;
    private JPasswordField pfPassword;
    
    private JButton btnLogin;    
    
    private JLabel lblServerInfo;
    private JLabel lblError;

    /**
     * Create the panel.
     */
    public LoginPanel() {
        
        JLabel lblLogin = new JLabel("Login:");
        
        JLabel lblPassword = new JLabel("Password:");
        
        tfLogin = new JTextField();
        tfLogin.setColumns(10);
        
        pfPassword = new JPasswordField();
        pfPassword.setColumns(10);
        
        lblServerInfo = new JLabel("Server Info");
        lblServerInfo.setVerticalAlignment(SwingConstants.TOP);
        lblServerInfo.setForeground(Color.BLACK);
        lblServerInfo.setHorizontalAlignment(SwingConstants.CENTER);
        
        btnLogin = new JButton("Login");
        
        lblError = new JLabel();
        lblError.setForeground(Color.RED);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(237)
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(btnLogin, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                                .addComponent(lblPassword, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblLogin, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(pfPassword, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                                .addComponent(tfLogin, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))))
                    .addGap(237))
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(51)
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(lblError, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblServerInfo, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 598, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(51, Short.MAX_VALUE))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(198)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblLogin)
                        .addComponent(tfLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblPassword)
                        .addComponent(pfPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(btnLogin)
                    .addGap(11)
                    .addComponent(lblError, GroupLayout.PREFERRED_SIZE, 12, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(lblServerInfo, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
                    .addGap(33))
        );
        setLayout(groupLayout);

    }
    
    public void addActionListenerForLoginButton(ActionListener l) {
        if(l != null)
            btnLogin.addActionListener(l);
    }

    public void setServerInfo(String info) {
        if(info != null) {
            lblServerInfo.setText(info);
        }
    }
    public void setErrorInfo(String error) {
        if(error != null) {
            lblError.setText(error);
        }
    }
    
    public String getLogin() {
        return tfLogin.getText();
    }
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }
    
    public boolean isFillField() {
        if(getLogin().isEmpty() || getPassword().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
