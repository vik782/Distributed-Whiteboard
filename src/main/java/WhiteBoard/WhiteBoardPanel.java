/**
 * Vincent Kurniawan
 *
 * WhiteBoard.WhiteBoardPanel.java: Swing Panel for whiteboard
 */

package WhiteBoard;
import RMI.*;
import Response.ErrorExceptions;
import User.Middleware;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.SwingUtilities.isLeftMouseButton;

public class WhiteBoardPanel extends JPanel implements MouseListener, MouseMotionListener {

    private Point fp = new Point();
    private Point lp = new Point();
    private Point current = new Point();
    private ArrayList<JButton> toolGroup = new ArrayList<JButton>();
    private ArrayList<JButton> colorGroup = new ArrayList<JButton>();
    private String currentTool;
    private Color currentColor;
    private IRemoteWhiteBoard whiteBoard;
    private JPanel toolsPanel;
    private JPanel colorPanel;
    private JPanel menuPanel;
    private JMenu menu;
    private Middleware api;

    public WhiteBoardPanel(Color color, int width, int height, IRemoteWhiteBoard whiteBoard, JPanel toolsPanel,
                           JPanel colorPanel, Middleware api) {
        this.whiteBoard = whiteBoard;
        this.toolsPanel = toolsPanel;
        this.colorPanel = colorPanel;
        this.api = api;

        setBackground(color);
        setPreferredSize(new Dimension(width, height));
        repaint();

        createToolButtons();
        createColorButtons();

        currentTool = Constants.RECTANGLE;
        currentColor = Color.RED;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public WhiteBoardPanel(Color color, int width, int height, IRemoteWhiteBoard whiteBoard, JPanel menuPanel, JPanel toolsPanel, JPanel colorPanel, Middleware api) {
        this(color, width, height, whiteBoard, toolsPanel, colorPanel, api);
        this.menuPanel = menuPanel;
        createFileMenu();
    }

    private void createToolButtons() {
        JButton rectangleButton = toolButton(Constants.RECTANGLE);
        rectangleButton.setBackground(Color.ORANGE);
        JButton ovalButton = toolButton(Constants.OVAL);
        JButton lineButton = toolButton(Constants.LINE);
        JButton circleButton = toolButton(Constants.CIRCLE);
        JButton textButton = toolButton(Constants.TEXT);
        JButton penButton = toolButton(Constants.PEN);
        toolGroup.add(rectangleButton);
        toolGroup.add(ovalButton);
        toolGroup.add(lineButton);
        toolGroup.add(circleButton);
        toolGroup.add(textButton);
        toolGroup.add(penButton);
    }

    private void createColorButtons() {
        Color[] colors = {
                Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN,
                Color.CYAN, Color.ORANGE, Color.MAGENTA, Color.PINK,
                Color.GRAY, Color.LIGHT_GRAY, Color.WHITE,
                Color.decode("#6A2E2A"), // fired-brick
                Color.decode("#D0A384"), // light french beige
                Color.decode("#AA875C"), // corduroy
                Color.decode("#93C572"), // pistachio
                Color.decode("#FFFDD0") // cream
        };

        for (Color chosenColor : colors) {
            JButton button = new JButton();
            button.setBackground(chosenColor);
            if (chosenColor == Color.RED) {
                button.setBorder(new LineBorder(Color.BLACK, Constants.HIGHLIGHT_BORDER_SIZE));
            }
            button.addActionListener(new ColorButtonListener());
            colorPanel.add(button);
            colorGroup.add(button);
        }
    }

    private class ColorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton sourceButton = (JButton) e.getSource();
            sourceButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, Constants.HIGHLIGHT_BORDER_SIZE));

            for (JButton button : colorGroup) {
                if (button != sourceButton) {
                    button.setBorder(new LineBorder(Color.BLACK, 0));
                }
            }

            Color buttonColor = sourceButton.getBackground();
            currentColor = buttonColor;
        }
    }

    private class ToolHighlighter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton sourceButton = (JButton) e.getSource();
            currentTool = sourceButton.getText();
            sourceButton.setBackground(Color.ORANGE);

            for (JButton button : toolGroup) {
                if (button != sourceButton) {
                    button.setBackground(Color.LIGHT_GRAY);
                }
            }
        }
    }

    private JButton toolButton(String text) {
        JButton button = new JButton(text);
        button.setSize(Constants.TOOL_BUTTON_WIDTH, Constants.TOOL_BUTTON_HEIGHT);
        button.setFont(new Font(Constants.FONT_TYPE, Font.PLAIN, Constants.FONT_TOOL_SIZE));
        button.setBackground(Color.LIGHT_GRAY);
        button.setOpaque(true);
        button.addActionListener(new ToolHighlighter());
        button.setFocusPainted(false);
        toolsPanel.add(button);
        return button;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Graphics2D g2D = (Graphics2D) g;
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.drawImage(whiteBoard.getBoard().getImage(), 0, 0, null);
            this.repaint();

        } catch (RemoteException e) {
            ErrorExceptions.remoteExceptionError(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isLeftMouseButton(e)) {
            fp.x = e.getX();
            fp.y = e.getY();
            current.x = fp.x;
            current.y = fp.y;

            if (currentTool.equals(Constants.TEXT)) {
                String inputText = showInputDialog("Type your text!");
                if (inputText == null || inputText.isEmpty()) {
                    ErrorExceptions.emptyInput();
                } else {
                    try {
                        whiteBoard.sketchText(fp.x, fp.y, inputText, currentColor);
                        repaint();
                    } catch (RemoteException ex) {
                       ErrorExceptions.remoteExceptionError(ex);
                    }
                }
            }
        } else {
            newPoints();
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (isLeftMouseButton(e)) {
            current.x = e.getX();
            current.y = e.getY();
            lp.x = current.x;
            lp.y = current.y;

            switch (currentTool) {
                case Constants.PEN:
                    try {
                        whiteBoard.sketchPen(lp.x, lp.y, current.x, current.y, currentColor);
                        repaint();
                    } catch (RemoteException ex) {
                        ErrorExceptions.remoteExceptionError(ex);
                    }
                    break;
                default:
            }
        } else {
            newPoints();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (isLeftMouseButton(e)) {
            int x = Math.min(fp.x, current.x);
            int y = Math.min(fp.y, current.y);
            int width = Math.abs(current.x - fp.x);
            int height = Math.abs(current.y - fp.y);

            try {
                switch (currentTool) {
                    case Constants.RECTANGLE:
                        whiteBoard.sketchRectangle(x, y, width, height, currentColor);
                        repaint();
                        break;
                    case Constants.OVAL:
                        whiteBoard.sketchOval(x, y, width, height, currentColor);
                        repaint();
                        break;
                    case Constants.CIRCLE:
                        whiteBoard.sketchCircle(x, y, width, width, currentColor);
                        repaint();
                        break;
                    case Constants.LINE:
                        whiteBoard.sketchLine(fp.x, fp.y, current.x, current.y, currentColor);
                        repaint();
                        break;
                }
            } catch (RemoteException ex) {
                ErrorExceptions.remoteExceptionError(ex);
            }

        } else {
            newPoints();
        }
    }

    private void newPoints() {
        current = new Point();
        fp = new Point();
        lp = new Point();
    }

    private ImageIcon scaleImage(ImageIcon icon) {
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(Constants.MENU_ICON_WIDTH, Constants.MENU_ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        return icon;
    }

    private JMenuItem createItem(String type) {

        JMenuItem item = new JMenuItem(type);
        item.setSize(Constants.MENU_BUTTON_WIDTH, Constants.MENU_BUTTON_HEIGHT);
        menuPanel.add(item);
        return item;
    }

    private void createFileMenu() {
        menu = new JMenu();
        menuPanel.setLayout(new FlowLayout());

        // New file, clears whiteboard
        JMenuItem newBoard = createItem("New");
        ImageIcon newIcon = new ImageIcon("src/main/resources/new.png");
        newIcon = scaleImage(newIcon);
        newBoard.setIcon(newIcon);
        newBoard.setFont(new Font(Constants.FONT_TYPE, Font.BOLD, Constants.FONT_MENU_SIZE));
        newBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    whiteBoard.clearBoard();
                    repaint();
                    Constants.notifyGUI("File successfully cleared!");
                } catch (RemoteException ex) {
                    ErrorExceptions.remoteExceptionError(ex);
                }
            }
        });

        // Open file
        JMenuItem openBoard = createItem("Open");
        ImageIcon openIcon = new ImageIcon("src/main/resources/open.png");
        openIcon = scaleImage(openIcon);
        openBoard.setIcon(openIcon);
        openBoard.setFont(new Font(Constants.FONT_TYPE, Font.BOLD, Constants.FONT_MENU_SIZE));
        openBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // file extensions
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter extensions = new FileNameExtensionFilter("Image Files",
                        "png", "jpeg", "jpg");
                fileChooser.setFileFilter(extensions);

                // file panel to choose files from
                int choice = fileChooser.showOpenDialog(toolsPanel);
                if (choice == JFileChooser.APPROVE_OPTION) {

                    File file = fileChooser.getSelectedFile();
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(file);
                        SerializableBuffer boardOpened = new SerializableBuffer(img);
                        whiteBoard.setBoard(boardOpened);
                        repaint();
                        Constants.notifyGUI("File successfully opened!");

                    } catch (IOException ex) {

                    }
                }
            }
        });

        // Save current whiteboard to an image file
        JMenuItem saveBoard = createItem("Save");
        ImageIcon saveIcon = new ImageIcon("src/main/resources/save.png");
        saveIcon = scaleImage(saveIcon);
        saveBoard.setIcon(saveIcon);
        saveBoard.setFont(new Font(Constants.FONT_TYPE, Font.BOLD, Constants.FONT_MENU_SIZE));

        // path to src: "src/main/resources/default"
        saveBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileSave("default", "png");
            }
        });

        // Save as to allow user choose what format image and storage location
        JMenuItem saveAsBoard = createItem("Save As");
        ImageIcon saveAsIcon = new ImageIcon("src/main/resources/saveAs.png");
        saveAsIcon = scaleImage(saveAsIcon);
        saveAsBoard.setIcon(saveAsIcon);
        saveAsBoard.setFont(new Font(Constants.FONT_TYPE, Font.BOLD, Constants.FONT_MENU_SIZE));
        saveAsBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileSaveAs();
            }
        });

        // Close whiteboard
        JMenuItem closeBoard = createItem("Close");
        ImageIcon closeIcon = new ImageIcon("src/main/resources/close.png");
        closeIcon = scaleImage(closeIcon);
        closeBoard.setIcon(closeIcon);
        closeBoard.setFont(new Font(Constants.FONT_TYPE, Font.BOLD, Constants.FONT_MENU_SIZE));
        closeBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Object[] options = {"Save First", "No, Just Close"};
                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Are you sure closing?",
                        "Manager - Close",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);

                if (choice == JOptionPane.YES_OPTION) {
                    fileSaveAs();
                    api.disconnectAll();
                    try {
                        whiteBoard.clearBoard();
                    } catch (RemoteException ex) {
                    }
                    System.exit(0);

                } else if (choice == JOptionPane.NO_OPTION) {
                    api.disconnectAll();
                    try {
                        whiteBoard.clearBoard();
                    } catch (RemoteException ex) {
                    }
                    System.exit(0);
                } else {
                    // dialog closed, cancel close
                }
            }
        });
    }

    private void fileSave(String fileName, String type) {
        // default save in png format
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("-yyyy-MM-dd-HH-mm-ss");
        String time = dateTime.format(formatter);
        File file = new File(fileName + time + "." + type);
        try {
            ImageIO.write(whiteBoard.getBoard().getImage(), type, file);
        } catch (IOException e) {
        }
        Constants.notifyGUI("File successfully saved!");
    }

    public void fileSaveAs(){
        // file extensions
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter extensions = new FileNameExtensionFilter("Image Files",
                "png", "jpeg", "jpg");
        fileChooser.setFileFilter(extensions);

        int choice = fileChooser.showSaveDialog(menuPanel);
        if (choice == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            String fileName = file.getPath();
            String type = null;

            // gets extension of the inputted filename
            // if not equal to approved extensions, saved as png by default
            int lastDot = fileName.lastIndexOf(".");
            if (lastDot != -1) {
                type = fileName.substring(lastDot + 1);
                System.out.println(type);
                if ((!type.equals("png")) && (!type.equals("jpg")) && (!type.equals("jpeg"))) {
                    type = "png";
                }
                else{
                    fileName = fileName.substring(0, lastDot);
                }
            } else {
                type = "png";
            }
            fileSave(fileName, type);
        }
    }

    // Not used mouse events
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
