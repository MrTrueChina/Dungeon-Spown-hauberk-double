package display;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import map.Map;
import save.SaveMap;
import spown.MapSpownData;
import spown.MapSpowner;

public class MainWindow {
    JLabel _imageLabel;
    JButton _spownButton;

    public MainWindow() {
        JFrame mainWindow = setupMainWindow();

        mainWindow.add(setupImageLabel());
        displayDefaultMap();
        
        mainWindow.add(setupSpownButton());

        mainWindow.setVisible(true);
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

        spownData.mapWidth = 64;
        spownData.mapHeight = 64;
        spownData.minRoomWidth = 4;
        spownData.minRoomHeight = 4;
        spownData.maxRoomWidth = 6;
        spownData.maxRoomHeight = 6;
        spownData.spownRoomTime = 1000;
        spownData.roomDoorsProbability = new int[] { 2, 5 };

        Map map = new MapSpowner().spown(spownData);
        Image scaledMazeImage;
        if (map.getWidth() > map.getHeight())
            scaledMazeImage = SaveMap.mapToImage(map).getScaledInstance(550, -1, Image.SCALE_FAST);
        else
            scaledMazeImage = SaveMap.mapToImage(map).getScaledInstance(-1, 550, Image.SCALE_FAST);

        _imageLabel.setIcon(new ImageIcon(scaledMazeImage));
    }
}
