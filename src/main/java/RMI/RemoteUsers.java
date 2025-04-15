/**
 * Vincent Kurniawan
 *
 * RMI.RemoteUsers.java: RMI for user list
 */

package RMI;
import Response.ErrorExceptions;
import Response.Responses;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import Server.*;

public class RemoteUsers extends UnicastRemoteObject implements IRemoteUsers {

    private HashMap<String, ConnectionSocket> users;
    private String managerID;
    private ConnectionSocket managerSocket;
    private int counter;

    public RemoteUsers() throws RemoteException {
        this.users = new HashMap<String, ConnectionSocket>();
        counter = 0;
    }

    @Override
    public void addUser(String userName, ConnectionSocket socket) throws RemoteException {
        users.put(userName, socket);
        counter = users.size();
    }

    @Override
    public void addManager(String userName, ConnectionSocket socket) throws RemoteException {
        managerID = userName;
        managerSocket = socket;
    }

    @Override
    public String getManagerName() throws RemoteException {
        return managerID;
    }

    @Override
    public String getUsernames() throws RemoteException {
        StringBuilder appendStrings = new StringBuilder();
        for (String message : users.keySet()) {
            appendStrings.append(message.toString()).append("\n");
        }
        return appendStrings.toString();
    }

    @Override
    public String[] getUserStrings() throws RemoteException {
        String[] keyArray = users.keySet().toArray(new String[users.size()]);
        return keyArray;
    }

    @Override
    public int getCounter() throws RemoteException {
        return counter;
    }

    @Override
    public ConnectionSocket getSocket(String userName) throws RemoteException {
        return users.get(userName);
    }

    @Override
    public HashMap<String, ConnectionSocket> getUsers() throws RemoteException {
        return users;
    }

    @Override
    public void removeUser(String userName) throws RemoteException {
        users.remove(userName);
        counter = users.size();
    }

    @Override
    public void kickoutUser(String kickUser) throws RemoteException {
        JSONObject req = new JSONObject();
        req.put(Responses.REQUEST_TYPE, Responses.KICKOUT_USER);

        ConnectionSocket kickSocket = users.get(kickUser);
        try {
            kickSocket.sendRequest(req.toJSONString());
        } catch (IOException e) {
            ErrorExceptions.remoteExceptionError(e);
        }
    }

    @Override
    public void reset() throws RemoteException {
        users = new HashMap<String, ConnectionSocket>();
        managerID = null;
        managerSocket = null;
        counter = 0;
    }
}
