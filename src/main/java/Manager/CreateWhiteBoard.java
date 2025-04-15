/**
 * Vincent Kurniawan
 *
 * CreateWhiteBoard.java: Whiteboard to be created by Manager
 */

package Manager;
import RMI.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Response.ErrorExceptions;
import Server.*;
import WhiteBoard.Constants;
import User.Middleware;
import WhiteBoard.WhiteBoardApp;

public class CreateWhiteBoard {

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

            // creates app for Manager, attempts to create whiteboard
            WhiteBoardApp app = new WhiteBoardApp(Constants.MANAGER, userName, api, whiteBoard, groupChat, userList);
            api.clientJoin(app, userName, Constants.MANAGER);

            // manager handles join requests by potential users
            String request = null;
            while (true) {
                request = socket.readResponse();
                ManagerThread managerThread = new ManagerThread(socket, request);
                managerThread.start();
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
