package save;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import map.Map;
import quad.QuadType;

public class SaveMap {
    public static BufferedImage mapToImage(Map map) {
        BufferedImage bufferedImage = new BufferedImage(map.width, map.height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        
        for(int y = 0;y < map.height;y++)
            for(int x = 0;x < map.width;x++)
            {
                graphics.setColor(map.getType(x, y)==QuadType.FLOOR? Color.WHITE:Color.BLACK);
                graphics.fillRect(x, y, 1, 1);
            }
        
        return bufferedImage;
    }
}
