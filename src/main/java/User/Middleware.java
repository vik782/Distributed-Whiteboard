/**
 * Vincent Kurniawan
 *
 * Middleware.java: The middleware program that acts as a communication API between client and server
 *
 */

package User;
import Response.ErrorExceptions;
import Response.Responses;
import Server.*;
import WhiteBoard.Constants;
import WhiteBoard.WhiteBoardApp;
import org.json.simple.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.*;

public class Middleware {

    private ConnectionSocket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private JSONParser parser;
    private JDialog dialog;

    public Middleware(ConnectionSocket socket){
        this.socket = socket;
        this.in = new DataInputStream(in);
        this.out = new DataOutputStream(out);
        this.parser = new JSONParser();
    }

    // user-manager requesting the whiteboard
    public void clientJoin(WhiteBoardApp app, String userName, String userType){
        JSONObject obj = new JSONObject();
        makeRequest(obj, userName, userType);

        String response = null;
        try {
            socket.sendRequest(obj.toJSONString());
            response = socket.readResponse();
            String requestType = getResponseType(response);

            switch (requestType) {
                case Responses.MANAGER_CREATE_SUCCESS:
                    // manager starts app
                    System.out.println("Create Whiteboard Success");
                    app.openApp(getUniqueName(response));
                    break;
                case Responses.MANAGER_CREATE_FAIL:
                    // manager already exists, can't start app
                    System.out.println("Create Whiteboard Fail");
                    app.duplicateManager();
                    break;
                case Responses.USER_JOIN_ACCEPT:
                    // manager accepts, user joins
                    System.out.println("Join Whiteboard Success");
                    dialog.dispose();
                    app.openApp(getUniqueName(response));
                    break;
                case Responses.USER_JOIN_REJECT:
                    // manager rejects, user doesn't join
                    System.out.println("Join Whiteboard Fail");
                    dialog.dispose();
                    app.closeApp();
                    break;
                case Responses.USER_JOIN_NO_MANAGER:
                    // user tries to join, but manager hasn't existed yet
                    System.out.println("Join Whiteboard Fail - No Manager");
                    app.noManager();
                    break;
                default:
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    // manager response in closing application to each connected users
    public void disconnectAll(){
        JSONObject obj = new JSONObject();
        obj.put(Responses.REQUEST_TYPE, Responses.CLOSED_BY_MANAGER);
        try {
            socket.sendRequest(obj.toJSONString());
            socket.close();
        } catch (IOException e){
            // e.printStackTrace();
        }
    }
    private void makeRequest(JSONObject obj, String userName, String userType){
        obj.put(Responses.USERNAME, userName);
        obj.put(Responses.USER_TYPE, userType);
        if (userType.equals(Constants.MANAGER)){
            obj.put(Responses.REQUEST_TYPE, Responses.CREATE_WHITEBOARD);
        } else {
            SwingUtilities.invokeLater(() -> {
                JLabel messageLabel = new JLabel("Waiting for manager's confirmation...");
                JOptionPane optionPane = new JOptionPane(messageLabel, JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                dialog = optionPane.createDialog(null, "Join Request");
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            });
            obj.put(Responses.REQUEST_TYPE, Responses.JOIN_WHITEBOARD);
        }
    }
    public String getResponseType(String response) {
        JSONObject res = new JSONObject();
        try {
            res = (JSONObject) parser.parse(response);
        } catch (ParseException e){
            ErrorExceptions.parseRequestError(e);
        }
        String code = res.get(Responses.REQUEST_TYPE).toString();
        return code;
    }

    public String getUniqueName(String response) {
        JSONObject res = new JSONObject();
        try {
            res = (JSONObject) parser.parse(response);
        } catch (ParseException e){
            ErrorExceptions.parseRequestError(e);
        }
        String name = res.get(Responses.UNIQUE_USERNAME).toString();
        return name;
    }
}
