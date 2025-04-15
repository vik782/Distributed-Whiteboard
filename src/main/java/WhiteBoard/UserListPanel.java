/**
 * Vincent Kurniawan
 *
 * WhiteBoard.UserListPanel.java: Swing Panel for user list
 */

package WhiteBoard;
import RMI.*;
import Response.ErrorExceptions;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserListPanel extends JPanel {
    private JTextArea users;
    private JScrollPane scrollPane;
    private IRemoteUsers remoteUsers;
    private int count = 0;
    private JButton kickout;
    private JList<String> list;
    private String selectedUser;
    private String userType;

    public UserListPanel(Color color, int width, int height, IRemoteUsers remoteUsers, String userType) {
        this.remoteUsers = remoteUsers;
        this.userType = userType;

        setBackground(color);
        setPreferredSize(new Dimension(width, height));

        JLabel userLabel = new JLabel();
        userLabel.setBounds(Constants.LABEL_X, Constants.LABEL_Y,
                Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        userLabel.setFont(new Font(Constants.FONT_TYPE, Font.PLAIN, Constants.FONT_LABEL_SIZE));
        userLabel.setText("Active User List");
        userLabel.setForeground(Color.WHITE);
        this.add(userLabel);

        JTextPane managerName = new JTextPane();
        managerName.setBounds(Constants.MANAGER_NAME_X, Constants.MANAGER_NAME_Y,
                                Constants.MANAGER_NAME_WIDTH, Constants.MANAGER_NAME_HEIGHT);
        managerName.setFont(new Font(Constants.FONT_TYPE, Font.PLAIN, Constants.FONT_LABEL_SIZE));
        managerName.setForeground(Color.BLACK);
        managerName.setEditable(false);
        try {
            managerName.setText(remoteUsers.getManagerName() + " [Manager]");
        } catch (RemoteException e) {
            ErrorExceptions.remoteExceptionError(e);
        }
        this.add(managerName);

        if (userType.equals(Constants.USER)) {
            setLayout(null);
            users = new JTextArea();
            users.setFont(new Font(Constants.FONT_TYPE, Font.PLAIN, Constants.FONT_LIST_SIZE));
            users.setEditable(false);
            scrollPane = new JScrollPane(users);
            scrollPane.setBounds(Constants.SP_LIST_X, Constants.SP_LIST_Y, Constants.SP_LIST_WIDTH, Constants.SP_LIST_HEIGHT);
            this.add(scrollPane);

            try {
                count = remoteUsers.getCounter();
                users.setText(remoteUsers.getUsernames());
            } catch (RemoteException e) {
                ErrorExceptions.remoteExceptionError(e);
            }
        }

        if (userType.equals(Constants.MANAGER)){

            try {
                setLayout(null);
                count = remoteUsers.getCounter();

                list = new JList();
                list.setListData(remoteUsers.getUserStrings());
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            String selectedValue = list.getSelectedValue();
                            if (selectedValue != null) {
                                selectedUser = selectedValue;
                            }
                        }
                    }
                });
                list.setFont(new Font(Constants.FONT_TYPE, Font.PLAIN, Constants.FONT_LIST_SIZE));
                JScrollPane scrollPane = new JScrollPane(list);
                scrollPane.setBounds(Constants.SP_LIST_X, Constants.SP_LIST_Y, Constants.SP_LIST_WIDTH, Constants.SP_LIST_HEIGHT);
                this.add(scrollPane, BorderLayout.LINE_START);

                kickout = new JButton("Kickout");
                kickout.setBounds(Constants.KICK_X, Constants.KICK_Y, Constants.KICK_WIDTH, Constants.KICK_HEIGHT);
                this.add(kickout);
                kickout.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        if (selectedUser != null) {
                            int prompt = JOptionPane.showConfirmDialog(
                                    null,
                                    "Confirm kicking out: " + selectedUser,
                                    "Kickout Confirmation",
                                    JOptionPane.OK_CANCEL_OPTION
                            );
                            if (prompt == 0) {
                                try {
                                    remoteUsers.kickoutUser(selectedUser);
                                    remoteUsers.removeUser(selectedUser);
                                    selectedUser=null;
                                } catch (RemoteException ex) {
                                    ErrorExceptions.remoteExceptionError(ex);
                                }
                            }
                        } else {
                            ErrorExceptions.noUserKick();
                        }
                    }
                });
            } catch (RemoteException e) {
                ErrorExceptions.remoteExceptionError(e);
            }
        }

        // thread to always update on active users
        Thread userThreads = new Thread(new userListener());
        userThreads.start();
    }

    private class userListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    int countAll;
                    try {
                        countAll = remoteUsers.getCounter();
                        if (count != countAll) {
                            count = countAll;

                            if (userType.equals(Constants.MANAGER)){
                                list.setListData(remoteUsers.getUserStrings());
                            }
                            else {
                                users.setText(remoteUsers.getUsernames());
                            }
                        }
                    } catch (RemoteException e) {
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }
}
