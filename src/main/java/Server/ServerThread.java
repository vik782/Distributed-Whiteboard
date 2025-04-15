/**
 * Vincent Kurniawan
 *
 * Server.ServerThread.java: Thread to handle user-manager requests
 */

package Server;
import Manager.Manager;
import Response.ErrorExceptions;
import Response.Responses;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.RemoteException;

public class ServerThread extends Thread {

    private ConnectionSocket socket;
    private Manager manager;
    private JSONParser parser;

    public ServerThread(ConnectionSocket socket, Manager manager) {
        this.socket = socket;
        this.manager = manager;
        this.parser = new JSONParser();
    }

    // starts server thread
    @Override
    public void run() {
        super.run();
        try {
            // waits for user requests
            String userName = null;
            while (true) {
                String clientMsg = socket.readResponse();

                // user or manager has left the application
                if (clientMsg == null) {
                    break;
                }
                // process request
                String request = getRequestType(clientMsg);
                switch (request) {
                    case Responses.CREATE_WHITEBOARD:
                        // manager attempts to create whiteboard
                        createWhiteboard(clientMsg);
                        break;
                    case Responses.JOIN_WHITEBOARD:
                        // user attempts to join whiteboard
                        joinWhiteboard(clientMsg);
                        break;
                    case Responses.JOIN_WHITEBOARD_OUTPUT:
                        // manager's decision on user
                        acceptReject(clientMsg);
                        break;
                    case Responses.CLOSED_BY_MANAGER:
                        // manager closes the whiteboard
                        closeAll();
                    default:
                }
            }
        } catch (IOException e) {
        }
    }

    public void closeAll() throws RemoteException {
        manager.closeAllUsers();
        manager.reset();
    }

    public String getRequestType(String request) {
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e) {
            ErrorExceptions.parseRequestError(e);
        }
        String code = req.get("REQUEST_TYPE").toString();
        return code;
    }

    public String getUserName(String request) {
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e) {
            ErrorExceptions.parseRequestError(e);
        }
        String code = req.get(Responses.USERNAME).toString();
        return code;
    }

    public String getOutput(String request) {
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e) {
            ErrorExceptions.parseRequestError(e);
        }
        String code = req.get(Responses.RESULT).toString();
        return code;
    }

    public void createWhiteboard(String clientMsg) throws IOException {
        JSONObject res = new JSONObject();
        String userName = getUserName(clientMsg);

        if (!manager.alreadyExists()) {
            String uniqueName = manager.addUser(userName, socket);
            res.put(Responses.UNIQUE_USERNAME, uniqueName);
            res.put(Responses.REQUEST_TYPE, Responses.MANAGER_CREATE_SUCCESS);
            socket.sendRequest(res.toJSONString());
        } else {
            res.put(Responses.REQUEST_TYPE, Responses.MANAGER_CREATE_FAIL);
            socket.sendRequest(res.toJSONString());
        }
    }

    public void joinWhiteboard(String clientMsg) throws IOException {
        JSONObject res = new JSONObject();
        String userName = getUserName(clientMsg);
        if (!manager.alreadyExists()) {
            res.put(Responses.REQUEST_TYPE, Responses.USER_JOIN_NO_MANAGER);
            socket.sendRequest(res.toJSONString());
        } else {
            ConnectionSocket socketManager = manager.getManagerSocket();
            manager.addWaitingUser(userName, socket);
            res.put(Responses.REQUEST_TYPE, Responses.USER_REQUEST_JOIN);
            res.put(Responses.USERNAME, userName);
            socketManager.sendRequest(res.toJSONString());
        }
    }

    public void acceptReject(String clientMsg) throws IOException {
        JSONObject res = new JSONObject();
        boolean output = Boolean.parseBoolean(getOutput(clientMsg));

        String userName = getUserName(clientMsg);
        ConnectionSocket userSocket;
        res.put(Responses.USERNAME, userName);

        if (output) {
            String uniqueName = manager.acceptUser(userName);
            res.put(Responses.REQUEST_TYPE, Responses.USER_JOIN_ACCEPT);
            res.put(Responses.UNIQUE_USERNAME, uniqueName);
            userSocket = manager.getSocket(uniqueName);
            userSocket.sendRequest(res.toJSONString());
        } else {
            userSocket = manager.getWaitingSocket(userName);
            manager.rejectUser(userName);
            res.put(Responses.REQUEST_TYPE, Responses.USER_JOIN_REJECT);
            userSocket.sendRequest(res.toJSONString());
        }
    }
}
