/**
 * Vincent Kurniawan
 *
 * RMI.IRemoteUsers.java: RMI interface of the user list
 */

package RMI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import Server.*;


public interface IRemoteUsers extends Remote {

    void addUser(String userName, ConnectionSocket socket) throws RemoteException;

    String getUsernames() throws RemoteException;

    int getCounter() throws RemoteException;

    ConnectionSocket getSocket(String username) throws RemoteException;

    HashMap<String, ConnectionSocket> getUsers() throws RemoteException;

    void removeUser(String userName) throws RemoteException;

    String[] getUserStrings() throws RemoteException;

    void kickoutUser(String kickUser) throws RemoteException;

    void addManager(String username, ConnectionSocket socket) throws RemoteException;
    String getManagerName() throws RemoteException;
    void reset() throws RemoteException;
}
