/**
 * Vincent Kurniawan
 *
 * Server.WhiteBoardServer.java: The server for the application
 */

package Server;
import Manager.Manager;
import RMI.*;
import Response.ErrorExceptions;

import java.io.IOException;
import java.net.*;
import javax.net.*;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteBoardServer {
    private final static int DEFAULT_RMI_PORT = 1099;
    private static int serverPort;
    private static String serverAddress;
    private static Socket socket;
    private static Manager manager;

    private static void verifyInput(String[] args) {
        if (args.length != 2) {
            ErrorExceptions.InvalidArguments();
        }
        try {
            serverAddress = args[0];
            serverPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            ErrorExceptions.serverPortError(e);
        }
        System.out.println("Arguments Verified!");
    }

    private static void generateBindRMI() {

        try {
            // instantiate RMI objects
            IRemoteWhiteBoard whiteBoard = new RemoteWhiteBoard();
            IRemoteChat groupChat = new RemoteChat();
            IRemoteUsers userList = new RemoteUsers();

            // bind RMI objects to registry
            Registry registry = LocateRegistry.createRegistry(DEFAULT_RMI_PORT);
            registry.bind(whiteBoard.getClass().toString(), whiteBoard);
            registry.bind(groupChat.getClass().toString(), groupChat);
            registry.bind(userList.getClass().toString(), userList);

            // instantiate manager of the server
            manager = new Manager(userList, groupChat);

        } catch (AlreadyBoundException e) {
            ErrorExceptions.alreadyBoundError(e);
        } catch (AccessException e) {
            ErrorExceptions.accessError(e);
        } catch (RemoteException e) {
            ErrorExceptions.remoteExceptionError(e);
        }
        System.out.println("RMI Objects Registered!");
    }

    public static void main(String[] args) throws IOException {

        // checks input arguments (server address and port)
        verifyInput(args);

        // generate RMI registry and bind remote objects
        generateBindRMI();

        try {
            // generate socket for server
            ServerSocketFactory serverFactory = ServerSocketFactory.getDefault();
            ServerSocket serverSocket = serverFactory.createServerSocket(serverPort);
            serverSocket.setReuseAddress(true);

            System.out.println("Server Ready!");
            while (true) {
                // a client is connected to the server
                socket = serverSocket.accept();
                ConnectionSocket connectionSocket = new ConnectionSocket(socket);

                // task from client request
                ServerThread serverThread = new ServerThread(connectionSocket, manager);
                serverThread.start();
            }
        } catch (BindException e) {
            ErrorExceptions.bindError(e);
        } catch (IOException e) {

        } finally {
            // closes server socket
            socket.close();
        }
    }
}
