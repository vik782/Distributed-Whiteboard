/**
 * Vincent Kurniawan
 *
 * RMI.RemoteChat.java: RMI for group chat
 */

package RMI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RemoteChat extends UnicastRemoteObject implements IRemoteChat {

    private ArrayList<String> allChat;

    public RemoteChat() throws RemoteException {
        allChat = new ArrayList<String>();
    }

    @Override
    public void sendChat(String dialogue) throws RemoteException {
        allChat.add(dialogue);
    }

    @Override
    public int getSize() {
        return allChat.size();
    }

    @Override
    public String getChatString() {
        StringBuilder appendStrings = new StringBuilder();
        for (String message : allChat) {
            appendStrings.append(message.toString()).append("\n");
        }
        return appendStrings.toString();
    }

    @Override
    public void userLeft(String userName) throws RemoteException {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = dateTime.format(formatter);

        allChat.add(userName + " just left!" + " [" + time + "]");
    }

    @Override
    public void justEntered(String userName, String userType) throws RemoteException {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = dateTime.format(formatter);
        allChat.add(userName + " just arrived!" + " [" + time + "]");
    }
    @Override
    public void reset() throws RemoteException {
        allChat = new ArrayList<String>();
    }
}
