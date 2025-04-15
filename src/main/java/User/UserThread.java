
/**
 * Vincent Kurniawan
 *
 * User.UserThread.java: Thread to handle kick-out requests from manager to user
 */

package User;
import Server.ConnectionSocket;
import Response.ErrorExceptions;
import Response.Responses;
import WhiteBoard.WhiteBoardApp;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class UserThread extends Thread {

    private final WhiteBoardApp app;
    private ConnectionSocket socket;
    private final String request;
    private JSONParser parser;

    public UserThread(WhiteBoardApp app, ConnectionSocket socket, String request) {
        this.app = app;
        this.request = request;
        this.socket = socket;
        parser = new JSONParser();
    }

    @Override
    public void run() {
        super.run();
        // send responses
        try {
            String requestType = parseReqType(request);
            switch (requestType) {
                case Responses.CLOSED_BY_MANAGER:
                    // manager closed application, kicking-out all users
                    closeUser();
                    break;
                    // kicking-out a user
                case Responses.KICKOUT_USER:
                    kickUser();
                default:
            }
        } catch (IOException e) {
            try {
                this.interrupt();
                socket.close();
            } catch (IOException ex) {
            }
        }
    }

    public String parseReqType(String request) {
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e) {
            ErrorExceptions.parseRequestError(e);
        }
        String code = req.get(Responses.REQUEST_TYPE).toString();
        return code;
    }

    public void kickUser() throws IOException {
        socket.close();
        app.kickShutDown();
    }

    public void closeUser() throws IOException {
        socket.close();
        app.managerShutdown();
    }
}
