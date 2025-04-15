/**
 * Vincent Kurniawan
 *
 * RMI.IRemoteChat.java: RMI interface of the group chat
 */

package RMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteChat extends Remote {
    void sendChat(String dialogue) throws RemoteException;

    int getSize() throws RemoteException;

    String getChatString() throws RemoteException;

    void userLeft(String userName) throws RemoteException;
    void justEntered(String userName, String userType) throws RemoteException;
    void reset() throws RemoteException;
}
