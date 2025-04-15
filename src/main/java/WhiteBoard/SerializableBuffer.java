/**
 * Vincent Kurniawan
 *
 * WhiteBoard.SerializableBuffer.java: Serialization of buffer image for the whiteboard
 */

package WhiteBoard;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class SerializableBuffer implements Serializable {

    private transient BufferedImage image;
    public SerializableBuffer(BufferedImage image) {
        this.image = image;
    }

    public SerializableBuffer(BufferedImage image, Color color) {
        this.image = image;
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(color);
        g.fillRect(0,0, Constants.WHITEBOARD_WIDTH, Constants.WHITEBOARD_HEIGHT);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image, Color color){
        this.image = image;
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(color);
        g.fillRect(0,0, Constants.WHITEBOARD_WIDTH, Constants.WHITEBOARD_HEIGHT);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteStream);
        byte[] imageData = byteStream.toByteArray();
        out.writeObject(imageData);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        byte[] imageData = (byte[]) in.readObject();
        ByteArrayInputStream byteStream = new ByteArrayInputStream(imageData);
        image = ImageIO.read(byteStream);
    }

}
