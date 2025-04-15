/**
 * Vincent Kurniawan
 *
 * WhiteBoard.GUIWhiteBoard.java: GUI for the application
 */

package WhiteBoard;
import RMI.*;
import Response.ErrorExceptions;
import User.Middleware;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class GUIWhiteBoard {

    private JFrame frame;
    private JPanel wbPanel;
    private JPanel gcPanel;
    private JPanel ulPanel;
    private IRemoteWhiteBoard whiteBoard;
    private IRemoteChat groupChat;
    private IRemoteUsers userList;
    private Border border;
    private String userName;

    public GUIWhiteBoard(Middleware api, String appName, String userName, String userType,
                         IRemoteWhiteBoard whiteBoard, IRemoteChat groupChat, IRemoteUsers userList) {

        this.whiteBoard = whiteBoard;
        this.groupChat = groupChat;
        this.userList = userList;
        this.userName = userName;
        this.border = BorderFactory.createLineBorder(Color.BLACK, Constants.FRAME_BORDER_SIZE);

        frame = new JFrame(appName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        frame.setResizable(false);

        // main panel consists of left and right inner-panels
        JPanel mainPanel = new JPanel(new BorderLayout());

        // left panel contains the whiteboard panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JPanel toolsPanel = new JPanel();
        toolsPanel.setLayout(new FlowLayout());
        toolsPanel.setBackground(Color.DARK_GRAY);

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(4, 4, 5, 5));
        colorPanel.setBackground(Color.DARK_GRAY);
        colorPanel.setForeground(Color.GRAY);
        colorPanel.setBorder(new EmptyBorder(0, 0, 5, 0));


        if (userType.equals(Constants.MANAGER)){
            toolsPanel.setPreferredSize(new Dimension(Constants.TOOL_WIDTH, Constants.TOOL_HEIGHT_MANAGER));
            colorPanel.setPreferredSize(new Dimension(Constants.COLOR_WIDTH, Constants.COLOR_HEIGHT_MANAGER));

            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new GridLayout(1, 5, 5, 5));
            menuPanel.setBackground(Color.DARK_GRAY);
            menuPanel.setPreferredSize(new Dimension(Constants.MENU_WIDTH, Constants.MENU_HEIGHT));

            leftPanel.add(menuPanel, BorderLayout.PAGE_START);
            leftPanel.add(toolsPanel, BorderLayout.LINE_END);

            // whiteboard panel
            wbPanel = new WhiteBoardPanel(Color.GRAY, Constants.WB_PANEL_WIDTH, Constants.WB_PANEL_HEIGHT,
                    whiteBoard, menuPanel, toolsPanel, colorPanel, api);
        }

        if (userType.equals(Constants.USER)){
            toolsPanel.setPreferredSize(new Dimension(Constants.TOOL_WIDTH, Constants.TOOL_HEIGHT_USER));
            colorPanel.setPreferredSize(new Dimension(Constants.COLOR_WIDTH, Constants.COLOR_HEIGHT_USER));

            leftPanel.add(toolsPanel, BorderLayout.PAGE_START);

            // whiteboard panel
            wbPanel = new WhiteBoardPanel(Color.GRAY, Constants.WB_PANEL_WIDTH, Constants.WB_PANEL_HEIGHT,
                    whiteBoard, toolsPanel, colorPanel, api);

        }

        // right panel contains group-chat and user-list
        JPanel rightPanel = new JPanel(new BorderLayout());

        // group-chat panel
        gcPanel = new GroupChatPanel(Color.DARK_GRAY, Constants.GC_PANEL_WIDTH, Constants.GC_PANEL_HEIGHT,
                groupChat, userName);
        // user-list panel
        ulPanel = new UserListPanel(Color.DARK_GRAY, Constants.UL_PANEL_WIDTH, Constants.UL_PANEL_HEIGHT,
                userList, userType);

        rightPanel.add(gcPanel, BorderLayout.PAGE_START);
        rightPanel.add(ulPanel, BorderLayout.PAGE_END);

        leftPanel.add(colorPanel, BorderLayout.LINE_END);
        leftPanel.add(wbPanel, BorderLayout.PAGE_END);

        mainPanel.add(leftPanel, BorderLayout.LINE_START);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // when user-manager closes the application
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (userType.equals(Constants.MANAGER)) {
                    frame.dispose();
                    api.disconnectAll();
                    try {
                        whiteBoard.clearBoard();
                    } catch (RemoteException ex) {
                        ErrorExceptions.remoteExceptionError(ex);
                    }
                    System.exit(0);
                } else {
                    try {
                        groupChat.userLeft(userName);
                        userList.removeUser(userName);
                    } catch (RemoteException ex) {
                        ErrorExceptions.remoteExceptionError(ex);
                    }
                }
            }
        });
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void managerShutdown(String title, String message) {
        frame.dispose();
        ErrorExceptions.managerQuit(title, message);
        System.exit(0);
    }

    public void kickout(String title, String message) {
        frame.dispose();
        ErrorExceptions.kickout(title, message);
        System.exit(0);
    }

}
