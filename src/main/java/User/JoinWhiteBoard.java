/**
 * Vincent Kurniawan
 *
 * User.JoinWhiteBoard.java: Application to be created by User
 */

package User;
import RMI.*;
import Server.ConnectionSocket;
import Response.ErrorExceptions;
import WhiteBoard.Constants;
import WhiteBoard.WhiteBoardApp;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinWhiteBoard {

    private static int serverPort;
    private static String serverAddress;
    private static String userName;
    private static IRemoteWhiteBoard whiteBoard;
    private static IRemoteUsers userList;
    private static IRemoteChat groupChat;
    private final static int DEFAULT_RMI_PORT = 1099;

    public static void main(String args[]) throws IOException {
        // checks input arguments (server address and port)
        verifyInput(args);

        // lookup RMI registry
        lookupRMI();

        ConnectionSocket socket = null;
        try {
            socket = new ConnectionSocket(serverPort, serverAddress);
            Middleware api = new Middleware(socket);

            // creates app for User, attempts to join whiteboard
            WhiteBoardApp app = new WhiteBoardApp(Constants.USER, userName, api, whiteBoard, groupChat, userList);
            api.clientJoin(app, userName, Constants.USER);

            // user thread to check for kick-out request by manager
            String request = null;
            while (true) {
                request = socket.readResponse();
                UserThread userThread = new UserThread(app, socket, request);
                userThread.start();
            }
        } catch (ConnectException e) {
            ErrorExceptions.connectRefusedError(e);
        } catch (BindException e) {
            ErrorExceptions.bindError(e);
        } catch (UnknownHostException e) {
            ErrorExceptions.hostError(e);
        } catch (IOException e) {
            ErrorExceptions.serverDisconnected(e);
        } finally {
            socket.close();
        }
    }

    private static void verifyInput(String[] args) {
        if (args.length != 3) {
            ErrorExceptions.InvalidArgumentsUser();
        }
        try {
            serverAddress = args[0];
            serverPort = Integer.parseInt(args[1]);
            userName = args[2];
        } catch (NumberFormatException e) {
            ErrorExceptions.serverPortError(e);
        }
    }
    private static void lookupRMI() {
        try {
            // looks for RMI objects on the registry
            Registry registry = LocateRegistry.getRegistry(serverAddress, DEFAULT_RMI_PORT);
            whiteBoard = (IRemoteWhiteBoard) registry.lookup(RemoteWhiteBoard.class.toString());
            groupChat = (IRemoteChat) registry.lookup(RemoteChat.class.toString());
            userList = (IRemoteUsers) registry.lookup(RemoteUsers.class.toString());

        } catch (java.rmi.UnknownHostException e) {
            ErrorExceptions.rmiHostError(e);
        } catch (NotBoundException e) {
            ErrorExceptions.rmiBindError(e);
        } catch (RemoteException e) {
            ErrorExceptions.remoteExceptionError(e);
        }
    }
}
