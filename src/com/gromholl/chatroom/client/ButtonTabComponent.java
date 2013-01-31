package com.gromholl.chatroom.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

public class ButtonTabComponent extends JPanel {

    private static final long serialVersionUID = 1L;
    private ChatRoomHandle handle;

    public ButtonTabComponent(ChatRoomHandle handle) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.handle = handle;
        setOpaque(false);
        
        JLabel label = new JLabel(handle.getName());        
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
                
        add(new LoadTabButton());
        add(new CloseTabButton());
    
    }    
    
    private class CloseTabButton extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public CloseTabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close this chatroom");
            setIcon(new ImageIcon(MainPanel.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = handle.getTabbedPane().indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                handle.getTabbedPane().remove(i);
                handle.unsubscribe();
            }
        }

        public void updateUI() {
            // Do nothing
        }
    }
    
    private class LoadTabButton extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;
    
        public LoadTabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Load all messages");
            setIcon(new ImageIcon(MainPanel.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            handle.loadAllMessages();
        }

        public void updateUI() {
            // Do nothing
        }
    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}
