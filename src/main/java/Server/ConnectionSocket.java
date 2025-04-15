/**
 * Vincent Kurniawan
 *
 * Server.ConnectionSocket.java: Custom socket implementation
 */

package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ConnectionSocket {
    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;

    // Server socket
    public ConnectionSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.is = new DataInputStream(socket.getInputStream());
        this.os = new DataOutputStream(socket.getOutputStream());

    }

    // Client socket
    public ConnectionSocket(int serverPort, String serverAddress) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.is = new DataInputStream(socket.getInputStream());
        this.os = new DataOutputStream(socket.getOutputStream());

    }

    public void sendRequest(String request) throws IOException {
        System.out.println("Request sent: " + request);
        os.writeUTF(request);
        os.flush();
    }

    public String readResponse() throws IOException {
        String receive = is.readUTF();
        System.out.println("Response received: " + receive);
        return receive;
    }

    public void close() throws IOException {
        is.close();
        os.close();
        socket.close();
    }
}
