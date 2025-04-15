/**
 * Vincent Kurniawan
 *
 * RemoteWhiteBoard.java: RMI for whiteboard
 */

package RMI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import WhiteBoard.*;

public class RemoteWhiteBoard extends UnicastRemoteObject implements IRemoteWhiteBoard {

    private SerializableBuffer board;

    public RemoteWhiteBoard() throws RemoteException {
        board = new SerializableBuffer(new BufferedImage(Constants.WHITEBOARD_WIDTH, Constants.WHITEBOARD_HEIGHT, BufferedImage.TYPE_INT_RGB), Color.WHITE);
    }

    @Override
    public void sketchText(int fpx, int fpy, String inputText, Color color) throws RemoteException {
        Graphics2D boardGraphic = (Graphics2D) board.getImage().getGraphics();
        boardGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boardGraphic.setColor(color);
        boardGraphic.setStroke(new BasicStroke(Constants.LINE_STROKE));
        boardGraphic.setFont(new Font(Constants.FONT_TYPE, Font.PLAIN, Constants.FONT_TEXT_SIZE));
        boardGraphic.drawString(inputText, fpx, fpy);
    }

    @Override
    public void sketchRectangle(int fpx, int fpy, int width, int height, Color color) throws RemoteException {
        Graphics2D boardGraphic = (Graphics2D) board.getImage().getGraphics();
        boardGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boardGraphic.setColor(color);
        boardGraphic.fillRect(fpx, fpy, width, height);
    }

    @Override
    public void sketchCircle(int fpx, int fpy, int width, int height, Color color) throws RemoteException {
        Graphics2D boardGraphic = (Graphics2D) board.getImage().getGraphics();
        boardGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boardGraphic.setColor(color);
        boardGraphic.fillOval(fpx, fpy, width, height);
    }

    @Override
    public void sketchOval(int fpx, int fpy, int width, int height, Color color) throws RemoteException {
        Graphics2D boardGraphic = (Graphics2D) board.getImage().getGraphics();
        boardGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boardGraphic.setColor(color);
        boardGraphic.fillOval(fpx, fpy, width, height);
    }

    @Override
    public void sketchLine(int fpx, int fpy, int lpx, int lpy, Color color) throws RemoteException {
        Graphics2D boardGraphic = (Graphics2D) board.getImage().getGraphics();
        boardGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boardGraphic.setColor(color);
        boardGraphic.setStroke(new BasicStroke(Constants.LINE_STROKE));
        boardGraphic.drawLine(fpx, fpy, lpx, lpy);
    }

    @Override
    public void sketchPen(int fpx, int fpy, int lpx, int lpy, Color color) throws RemoteException {
        Graphics2D boardGraphic = (Graphics2D) board.getImage().getGraphics();
        boardGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boardGraphic.setColor(color);
        boardGraphic.setStroke(new BasicStroke(Constants.PEN_STROKE));
        boardGraphic.drawLine(fpx, fpy, lpx, lpy);
    }
    @Override
    public void clearBoard() throws RemoteException{
        board.setImage(new BufferedImage(Constants.WHITEBOARD_WIDTH, Constants.WHITEBOARD_HEIGHT, BufferedImage.TYPE_INT_RGB), Color.WHITE);
    }

    @Override
    public void setBoard(SerializableBuffer board) throws RemoteException{
        this.board = board;
    }

    @Override
    public SerializableBuffer getBoard() throws RemoteException{
        return board;
    }
}
