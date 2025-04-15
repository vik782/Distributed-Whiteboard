/**
 * Vincent Kurniawan
 *
 * WhiteBoard.GroupChatPanel.java: Swing Panel for group chat
 */

package WhiteBoard;
import RMI.*;
import Response.ErrorExceptions;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroupChatPanel extends JPanel {
    private JTextArea chatLog;
    private JScrollPane scrollPane;
    private IRemoteChat remoteChat;
    private JButton sendChat;
    private JTextField userText;
    private int count = 0;

    public GroupChatPanel(Color color, int width, int height, IRemoteChat remoteChat, String userName) {
        this.remoteChat = remoteChat;

        setBackground(color);
        setPreferredSize(new Dimension(width, height));
        setLayout(null);

        JLabel chatLabel = new JLabel();
        chatLabel.setBounds(Constants.LABEL_X, Constants.LABEL_Y,
                Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        chatLabel.setFont(new Font(Constants.FONT_TYPE, Font.PLAIN, Constants.FONT_LABEL_SIZE));
        chatLabel.setText("Group Chat");
        chatLabel.setForeground(Color.WHITE);
        this.add(chatLabel);

        // display format of chat area
        createChatArea();

        try {
            count = remoteChat.getSize();
            chatLog.setText(remoteChat.getChatString());
        } catch (RemoteException e) {
            ErrorExceptions.remoteExceptionError(e);
        }

        sendChat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // user text
                String inputText = userText.getText();

                if (!inputText.isEmpty()){
                    // time when text was sent
                    LocalDateTime dateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String time = dateTime.format(formatter);
                    try {
                        appendChatMessage(userName, inputText, time);
                    } catch (RemoteException e2) {
                        ErrorExceptions.remoteExceptionError(e2);
                    }
                    userText.setText("");
                }
                else {
                    ErrorExceptions.emptyInput();
                }
            }
        });

        // thread to always check for new incoming chat
        Thread chatThread = new Thread(new chatListener());
        chatThread.start();
    }

    public void createChatArea(){
        chatLog = new JTextArea();
        chatLog.setFont(new Font(Constants.FONT_TYPE, Font.PLAIN, Constants.FONT_CHAT_SIZE));
        chatLog.setEditable(false);
        scrollPane = new JScrollPane(chatLog);
        scrollPane.setBounds(Constants.SP_CHAT_X, Constants.SP_CHAT_Y, Constants.SP_CHAT_WIDTH, Constants.SP_CHAT_HEIGHT);
        this.add(scrollPane);

        userText = new JTextField();
        userText.setBounds(Constants.UT_X, Constants.UT_Y, Constants.UT_WIDTH, Constants.UT_HEIGHT);
        this.add(userText);

        sendChat = new JButton("Send");
        sendChat.setBounds(Constants.SC_X, Constants.SC_Y, Constants.SC_WIDTH, Constants.SC_HEIGHT);
        this.add(sendChat);
    }

    // append chat messages to the text area
    public void appendChatMessage(String username, String message, String timestamp) throws RemoteException {
        String dialogue = username + ": " + message + " [" + timestamp + "]";
        remoteChat.sendChat(dialogue);
    }

    // scrolls down to the bottom most chat (newest)
    private void scrollDown() {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    private class chatListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    int countAll;
                    try {
                        countAll = remoteChat.getSize();
                        if (count != countAll) {
                            count = countAll;
                            chatLog.setText(remoteChat.getChatString());
                            scrollDown();
                        }
                    } catch (RemoteException e) {
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }
}
