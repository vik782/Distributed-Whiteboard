/**
 * Vincent Kurniawan
 *
 * WhiteBoard.WhiteBoardApp.java: Application to store all whiteboard elements
 */

package WhiteBoard;
import RMI.IRemoteChat;
import RMI.IRemoteUsers;
import RMI.IRemoteWhiteBoard;
import Response.ErrorExceptions;
import User.Middleware;
import java.rmi.RemoteException;

public class WhiteBoardApp {

    private String userType;
    private String userName;
    private Middleware api;
    private IRemoteWhiteBoard whiteBoard;
    private IRemoteUsers userList;
    private IRemoteChat groupChat;
    private GUIWhiteBoard GUIapp;

    public WhiteBoardApp(String userType, String userName, Middleware api,
                         IRemoteWhiteBoard whiteBoard, IRemoteChat groupChat, IRemoteUsers userList){
        this.userType = userType;
        this.whiteBoard = whiteBoard;
        this.groupChat = groupChat;
        this.userList = userList;
        this.userName = userName;
        this.api = api;
    }

    public void openApp(String userName) throws RemoteException {
        this.userName = userName;
        System.out.println("Whiteboard starting for: " + userName + " - " + userType);
        GUIWhiteBoard whiteboardGUI = new GUIWhiteBoard(api, "Vik's WhiteBoard - " + userType,
                userName, userType, whiteBoard, groupChat, userList);
        groupChat.justEntered(userName, userType);
        this.GUIapp = whiteboardGUI;
    }
    public void closeApp(){
        System.out.println("Failed to start whiteboard for: " + userName + " - " + userType);
        ErrorExceptions.rejectUser();
    }

    public void duplicateManager(){
        System.out.println("Failed to start whiteboard for: " + userName + " - " + userType);
        ErrorExceptions.managerExists();
    }

    public void noManager() {
        System.out.println("Failed to start whiteboard for: " + userName + " - " + userType);
        ErrorExceptions.noManager();
    }

    public void managerShutdown() throws RemoteException {
        String title = "Manager Left";
        String message = "Manager has left, kicking user: " + userName;
        groupChat.reset();
        whiteBoard.clearBoard();
        GUIapp.managerShutdown(title, message);
    }

    public void kickShutDown() throws RemoteException {
        String title = "Manager Kick Out ";
        String message = userName + ", you have been kicked out!";
        groupChat.userLeft(userName);
        GUIapp.kickout(title, message);
    }

}
