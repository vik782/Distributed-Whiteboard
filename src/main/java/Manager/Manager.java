/**
 * Vincent Kurniawan
 *
 * Manager.java: Application for Manager operations
 */

package Manager;
import RMI.IRemoteChat;
import RMI.IRemoteUsers;
import Server.ConnectionSocket;
import Response.ErrorExceptions;
import Response.Responses;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class Manager {

    private static int totalUsers=0;
    private IRemoteUsers userList;
    private IRemoteChat groupChat;
    private String managerID;
    private ConnectionSocket managerSocket;
    private HashMap<String, ConnectionSocket> waitingUsers;

    public Manager(IRemoteUsers userList, IRemoteChat groupChat) {
        this.userList = userList;
        this.groupChat = groupChat;
        this.waitingUsers = new HashMap<String, ConnectionSocket>();
        managerID = null;
        managerSocket = null;
    }

    public synchronized boolean alreadyExists() {
        return managerID != null;
    }

    public synchronized String addUser(String userName, ConnectionSocket socket) {
        // unique naming convention
        userName = userName + " (" + totalUsers + ")";
        totalUsers++;
        try {
            if (managerID == null) {
                managerID = userName;
                managerSocket = socket;
                userList.addManager(userName, socket);
            }
            else {
                userList.addUser(userName, socket);
            }

        } catch (RemoteException e) {
            ErrorExceptions.remoteExceptionError(e);
        }
        return userName;
    }

    public synchronized void addWaitingUser(String userName, ConnectionSocket socket) {
        waitingUsers.put(userName, socket);
    }

    public synchronized String acceptUser(String userName) throws RemoteException {
        // unique naming convention
        String uniqueName = userName + " (" + totalUsers + ")";
        totalUsers++;
        userList.addUser(uniqueName, waitingUsers.get(userName));
        waitingUsers.remove(userName);

        return uniqueName;
    }

    public synchronized void rejectUser(String userName) {
        waitingUsers.remove(userName);
    }

    public synchronized ConnectionSocket getSocket(String userName) throws RemoteException {
        return userList.getSocket(userName);
    }

    public synchronized ConnectionSocket getWaitingSocket(String userName) {
        return waitingUsers.get(userName);
    }

    public synchronized ConnectionSocket getManagerSocket() {
        return managerSocket;
    }

    public synchronized void closeAllUsers() throws RemoteException {
        for (Map.Entry<String, ConnectionSocket> user : userList.getUsers().entrySet()) {
            if (!user.getKey().equals(managerID)) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put(Responses.REQUEST_TYPE, Responses.CLOSED_BY_MANAGER);
                    user.getValue().sendRequest(obj.toJSONString());
                } catch (IOException e) {

                }
            }
        }
    }

    public synchronized void reset() throws RemoteException {
        managerID = null;
        managerSocket = null;
        waitingUsers = new HashMap<String, ConnectionSocket>();
        userList.reset();
        groupChat.reset();
    }
}
