/**
 * Vincent Kurniawan
 *
 * Response.ErrorExceptions.java: Information of error codes, description and message for a particular exception thrown
 *
 */

package Response;
import javax.swing.*;

public class ErrorExceptions {

    public static final String ARGUMENTS_LENGTH_ERROR = "400";
    public static final String SERVER_PORT_ERROR = "401";
    public static final String ALREADY_BOUND_ERROR = "402";
    public static final String ACCESS_ERROR = "403";
    public static final String BIND_PORT_ERROR = "404";
    public static final String REMOTE_EXCEPTION_ERROR = "405";
    public static final String PARSE_REQUEST_ERROR = "406";
    public static final String CONNECT_REFUSED_ERROR = "407";
    public static final String UNKNOWN_HOST_ERROR = "408";
    public static final String RMI_UNKNOWN_HOST_ERROR = "409";
    public static final String SERVER_DISCONNECTED = "410";
    public static final String EMPTY_INPUT = "411";
    public static final String RMI_NOT_BOUND = "412";
    public static final String ARGUMENTS_LENGTH_ERROR_USER = "413";
    public static final String NO_MANAGER = "414";
    public static final String REJECT_USER = "415";
    public static final String DUPLICATE_MANAGER = "416";
    public static final String KICKOUT = "417";
    public static final String NO_KICK = "418";


    public static void InvalidArguments () {
        String description = "Invalid Arguments";
        String message = "Invalid number of arguments, needs to exactly be 2: <server address> <server port>";
        showError(ARGUMENTS_LENGTH_ERROR, description, message);
    }

    public static void InvalidArgumentsUser () {
        String description = "Invalid Arguments";
        String message = "Invalid number of arguments, needs to exactly be 3: <server address> <server port> <username>";
        showError(ARGUMENTS_LENGTH_ERROR_USER, description, message);
    }
    public static void serverPortError (Exception e) {
        String description = "Invalid Server Port";
        showError(SERVER_PORT_ERROR, description, e.getMessage());
    }
    public static void alreadyBoundError(Exception e){
        String description = "RMI Bound Error";
        showError(ALREADY_BOUND_ERROR, description, e.getMessage());
    }
    public static void accessError (Exception e){
        String description = "RMI Access Error";
        showError(ACCESS_ERROR, description, e.getMessage());
    }
    public static void remoteExceptionError (Exception e){
        String description = "Remote Exception Error";
        showError(REMOTE_EXCEPTION_ERROR, description, e.getMessage());
    }

    public static void bindError (Exception e){
        String description = "Port Bind Error";
        showError(BIND_PORT_ERROR, description, e.getMessage());
    }

    public static void parseRequestError (Exception e){
        String description = "Parsing of request error by Client ";
        notifyGUI(PARSE_REQUEST_ERROR, description, e.getMessage());
    }

    public static void hostError (Exception e){
        String description = "Unable to connect to specified host/domain name";
        showError(UNKNOWN_HOST_ERROR, description, e.getMessage());
    }

    public static void rmiHostError (Exception e){
        String description = "RMI unable to connect to specified host/domain name";
        showError(RMI_UNKNOWN_HOST_ERROR, description, e.getMessage());
    }

    public static void rmiBindError (Exception e){
        String description = "RMI unable to bind objects";
        showError(RMI_NOT_BOUND, description, e.getMessage());
    }

    public static void connectRefusedError (Exception e){
        String description = "Connection to server is refused";
        showError(CONNECT_REFUSED_ERROR, description, e.getMessage());
    }

    public static void serverDisconnected (Exception e){
        String description = "Connection to server is disrupted";
        showError(SERVER_DISCONNECTED, description, e.getMessage());
    }

    public static void emptyInput (){
        String description = "Input is Empty";
        notifyGUI(EMPTY_INPUT, description, "Text cannot be empty!");
    }

    public static void noUserKick (){
        String description = "No User Selected";
        notifyGUI(NO_KICK, description, "Can't kickout imaginary user!");
    }

    public static void rejectUser (){
        String description = "Manager - Join Declined";
        showError(REJECT_USER, description, "Manager has declined you joining, goodbye!");
    }

    public static void kickout (String title, String message){
        showError(KICKOUT, title, message);
    }

    public static void managerQuit (String title, String message){
        showError(KICKOUT, title, message);
    }

    public static void noManager (){
        String description = "Manager - Non-Existent";
        showError(NO_MANAGER, description, "Manager doesn't exist yet, goodbye!");
    }

    public static void managerExists (){
        String description = "Manager - Already Exists";
        showError(DUPLICATE_MANAGER, description, "No more than 2 managers at a time!");
    }

    public static String getError(String errorCode, String description, String errorContent) {
        return "Error Code -> " + errorCode + " (" + description + ")" +
                "\n" + "Message -> "+ errorContent;
    }
    private static void showError(String code, String description, String message) {
        JOptionPane.showConfirmDialog(
                null,
                getError(code,description, message),
                "Error",
                JOptionPane.DEFAULT_OPTION
        );
        System.exit(1);
    }

    private static void notifyGUI(String code, String description, String message) {
        JOptionPane.showConfirmDialog(
                null,
                getError(code,description, message),
                "Notification",
                JOptionPane.DEFAULT_OPTION
        );
    }

}
