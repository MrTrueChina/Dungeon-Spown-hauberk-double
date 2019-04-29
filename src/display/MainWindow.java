package display;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import map.Map;
import spown.spowner.MapSpownData;
import spown.spowner.MapSpowner;

public class MainWindow {
    private JLabel _imageLabel;
    private JTextField _widthField;
    private JButton _spownButton;

    private int _width = 96;

    public MainWindow() {
        JFrame mainWindow = setupMainWindow();

        mainWindow.add(setupImageLabel());

        mainWindow.add(setupWidthField());

        mainWindow.add(setupSpownButton());

        mainWindow.setVisible(true);

        displayDefaultMap();
    }

    private JFrame setupMainWindow() {
        JFrame mainWindow = new JFrame("地牢生成");

        mainWindow.setSize(600, 800);
        mainWindow.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 300,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 400);
        mainWindow.setLayout(null);
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        return mainWindow;
    }

    private JLabel setupImageLabel() {
        _imageLabel = new JLabel();
        _imageLabel.setBounds(25, 25, 550, 550);

        return _imageLabel;
    }

    private JTextField setupWidthField() {
        _widthField = new JTextField();
        _widthField.setText(_width + "");
        _widthField.setBounds(50, 500, 100, 30);

        _widthField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int width = Integer.parseInt(_widthField.getText());
                    if (width < 1)
                        width = 1;
                } catch (NumberFormatException e1) {
                    _widthField.setText(_width + "");
                }
            }
        });

        return _widthField;
    }

    private JButton setupSpownButton() {
        _spownButton = new JButton("生成地牢");
        _spownButton.setBounds(100, 600, 150, 50);

        _spownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDefaultMap();
            }
        });

        return _spownButton;
    }

    private void displayDefaultMap() {
        MapSpownData spownData = new MapSpownData();

        spownData.mapWidth = 96;
        spownData.mapHeight = 64;
        spownData.minRoomWidth = 6;
        spownData.minRoomHeight = 6;
        spownData.maxRoomWidth = 12;
        spownData.maxRoomHeight = 12;
        spownData.spownRoomTime = 200;
        spownData.roomDoorsProbability = new int[] { 10, 7 };

        Map map = new MapSpowner().spown(spownData);
        Image scaledMazeImage;
        if (map.getWidth() > map.getHeight())
            scaledMazeImage = MapToImage.mapToImage(map).getScaledInstance(550, -1, Image.SCALE_FAST);
        else
            scaledMazeImage = MapToImage.mapToImage(map).getScaledInstance(-1, 550, Image.SCALE_FAST);

        _imageLabel.setIcon(new ImageIcon(scaledMazeImage));
    }
}
