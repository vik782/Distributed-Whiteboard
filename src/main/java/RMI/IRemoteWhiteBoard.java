/**
 * Vincent Kurniawan
 *
 * RMI.IRemoteWhiteBoard.java: RMI interface of the whiteboard
 */

package RMI;
import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import WhiteBoard.*;

public interface IRemoteWhiteBoard extends Remote {

    void sketchText(int fpx, int fpy, String inputText, Color color) throws RemoteException;

    void sketchRectangle(int fpx, int fpy, int width, int height, Color color) throws RemoteException;

    void sketchCircle(int fpx, int fpy, int width, int height, Color color) throws RemoteException;

    void sketchOval(int fpx, int fpy, int width, int height, Color color) throws RemoteException;

    void sketchLine(int fpx, int fpy, int lpx, int lpy, Color color) throws RemoteException;

    void sketchPen(int fpx, int fpy, int lpx, int lpy, Color color) throws RemoteException;

    void clearBoard() throws RemoteException;

    void setBoard(SerializableBuffer board) throws RemoteException;

    SerializableBuffer getBoard() throws RemoteException;
}
