/**
 * Vincent Kurniawan
 *
 * WhiteBoard.Constants.java: Information of constants for GUI visuals, whiteboard and user types
 *
 */

package WhiteBoard;

import javax.swing.*;

public class Constants {

    public static final int FRAME_WIDTH = 900;
    public static final int FRAME_HEIGHT = 750;

    public static final int WB_PANEL_WIDTH = 600;
    public static final int WB_PANEL_HEIGHT = 550;
    public static final int GC_PANEL_WIDTH = FRAME_WIDTH - WB_PANEL_WIDTH;
    public static final int GC_PANEL_HEIGHT = 450;
    public static final int UL_PANEL_WIDTH = FRAME_WIDTH - WB_PANEL_WIDTH;
    public static final int UL_PANEL_HEIGHT = 300;

    public static final int WHITEBOARD_WIDTH = WB_PANEL_WIDTH;
    public static final int WHITEBOARD_HEIGHT = WB_PANEL_HEIGHT;
    public static final int TOOL_WIDTH = WB_PANEL_WIDTH;
    public static final int TOOL_HEIGHT_MANAGER = 50;
    public static final int TOOL_HEIGHT_USER = 50;
    public static final int COLOR_WIDTH = WB_PANEL_WIDTH;
    public static final int COLOR_HEIGHT_MANAGER = 100;
    public static final int COLOR_HEIGHT_USER = 150;
    public static final int MENU_WIDTH = WB_PANEL_WIDTH;
    public static final int MENU_HEIGHT = 50;


    public static final int FRAME_BORDER_SIZE = 2;
    public static final int HIGHLIGHT_BORDER_SIZE = 4;

    public static final String FONT_TYPE = "Arial";
    public static final int FONT_TEXT_SIZE = 20;
    public static final int FONT_LABEL_SIZE = 15;
    public static final int FONT_LIST_SIZE = 15;
    public static final int FONT_TOOL_SIZE = 15;
    public static final int FONT_CHAT_SIZE = 14;
    public static final int FONT_MENU_SIZE = 12;
    public static final int LINE_STROKE = 3;
    public static final int PEN_STROKE = 5;


    public static final int SP_CHAT_X = 25;
    public static final int SP_CHAT_Y = 50;
    public static final int SP_CHAT_WIDTH = GC_PANEL_WIDTH - (SP_CHAT_X*2);
    public static final int SP_CHAT_HEIGHT = GC_PANEL_HEIGHT - 150;

    public static final int UT_X = SP_CHAT_X;
    public static final int UT_Y = SP_CHAT_HEIGHT + SP_CHAT_Y;
    public static final int UT_WIDTH = GC_PANEL_WIDTH - (SP_CHAT_X*2);
    public static final int UT_HEIGHT = 50;

    public static final int SC_X = SP_CHAT_X;
    public static final int SC_Y = UT_Y + UT_HEIGHT;
    public static final int SC_WIDTH = GC_PANEL_WIDTH - (SP_CHAT_X*2);
    public static final int SC_HEIGHT = 25;

    public static final int LABEL_X = 100;
    public static final int LABEL_Y = 25;
    public static final int LABEL_WIDTH = GC_PANEL_WIDTH - 50;
    public static final int LABEL_HEIGHT = 20;

    public static final int MANAGER_NAME_X = 25;
    public static final int MANAGER_NAME_Y = LABEL_Y*2;
    public static final int MANAGER_NAME_WIDTH = GC_PANEL_WIDTH - (MANAGER_NAME_X*2);
    public static final int MANAGER_NAME_HEIGHT = 20;

    public static final int SP_LIST_X = MANAGER_NAME_X;
    public static final int SP_LIST_Y = MANAGER_NAME_Y + MANAGER_NAME_HEIGHT;
    public static final int SP_LIST_WIDTH = UL_PANEL_WIDTH - (MANAGER_NAME_X*2);
    public static final int SP_LIST_HEIGHT = UL_PANEL_HEIGHT - 100;

    public static final int KICK_X = MANAGER_NAME_X;
    public static final int KICK_Y = SP_LIST_Y + SP_LIST_HEIGHT;
    public static final int KICK_WIDTH = GC_PANEL_WIDTH - (MANAGER_NAME_X*2);
    public static final int KICK_HEIGHT = 25;

    public static final int MENU_BUTTON_WIDTH = 20;
    public static final int MENU_BUTTON_HEIGHT = 20;
    public static final int MENU_ICON_WIDTH = 25;
    public static final int MENU_ICON_HEIGHT = 25;

    public static final int TOOL_BUTTON_WIDTH = 25;
    public static final int TOOL_BUTTON_HEIGHT = 25;

    public static final String MANAGER = "Manager";
    public static final String USER = "User";

    public static final String RECTANGLE = "Rectangle";
    public static final String CIRCLE = "Circle";
    public static final String OVAL = "Oval";
    public static final String LINE = "Line";
    public static final String TEXT = "Text";
    public static final String PEN = "Pen";

    public static void notifyGUI(String message){
        JOptionPane.showConfirmDialog(
                null,
                message,
                "File Notification",
                JOptionPane.DEFAULT_OPTION
        );
    }

}
