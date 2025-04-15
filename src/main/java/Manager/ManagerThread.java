/**
 * Vincent Kurniawan
 *
 * ManagerThread.java: Thread executed by manager to handle user requests
 */
package Manager;
import Response.ErrorExceptions;
import Response.Responses;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import Server.*;
import javax.swing.*;

public class ManagerThread extends Thread {

    private ConnectionSocket socket;
    private final String request;
    private JSONParser parser;

    public ManagerThread(ConnectionSocket socket, String request) {
        this.request = request;
        this.socket = socket;
        parser = new JSONParser();
    }

    @Override
    public void run() {
        super.run();
        try {
            String requestType = parseReqType(request);
            switch (requestType) {
                case Responses.USER_REQUEST_JOIN:
                    // user requesting manager to join
                    requestResult();
                    break;
                default:
            }
        } catch (IOException e) {
            try {
                this.interrupt();
                socket.close();
            } catch (IOException ex) {
                // socket closes
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

    public String parseUserName(String request) {
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e) {
            ErrorExceptions.parseRequestError(e);
        }
        String code = req.get(Responses.USERNAME).toString();
        return code;
    }

    public void requestResult() throws IOException {

        JSONObject obj = new JSONObject();
        String userName = parseUserName(request);

        Object[] options = { "Accept", "Reject" };
        int result = JOptionPane.showOptionDialog(
                null,
                "A user (" + userName + ") wants to share your whiteboard...",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,   options, options[0]);
        boolean userResponse = (result == JOptionPane.YES_OPTION);

        obj.put(Responses.REQUEST_TYPE, Responses.JOIN_WHITEBOARD_OUTPUT);
        obj.put(Responses.RESULT, userResponse);
        obj.put(Responses.USERNAME, parseUserName(request));
        socket.sendRequest(obj.toJSONString());
    }

}
