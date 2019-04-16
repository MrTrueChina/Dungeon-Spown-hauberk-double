package display;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import map.Map;
import save.SaveMap;
import spown.MapSpownData;
import spown.MapSpowner;

public class MainWindow {
    JLabel _imageLabel;
    
    public MainWindow() {
        JFrame mainWindow = setupMainWindow();
        
        mainWindow.add(setupImageLabel());
        displayDefaultMap();
        
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
    
    private void displayDefaultMap() {
        MapSpownData spownData = new MapSpownData();
        
        spownData.mapWidth = 550;
        spownData.mapHeight = 550;
        spownData.minRoomWidth = 3;
        spownData.minRoomHeight = 3;
        spownData.maxRoomWidth = 24;
        spownData.maxRoomHeight = 24;
        spownData.spownRoomTime = 100;
        
        Map map = new MapSpowner().spown(spownData);
        BufferedImage mapImage = SaveMap.mapToImage(map);
        _imageLabel.setIcon(new ImageIcon(mapImage));
    }
}
