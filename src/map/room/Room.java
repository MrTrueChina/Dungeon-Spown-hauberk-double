package map.room;

import java.awt.Rectangle;

import map.Map;
import map.quad.Quad;

@SuppressWarnings("serial")
public class Room extends Rectangle {
    private Quad[][] _quads;

    public Room(Rectangle rectangle, Map map) {
        this(rectangle.x, rectangle.y, rectangle.width, rectangle.height, map);
    }

    public Room(int width, int height, Map map) {
        this(0, 0, width, height, map);
    }

    public Room(int x, int y, int width, int height, Map map) {
        super(x, y, width, height);
        setupQuads(map);
    }

    private void setupQuads(Map map) {
        _quads = new Quad[width][height];

        for (int yOffset = 0; yOffset < height; yOffset++)
            for (int xOffset = 0; xOffset < width; xOffset++)
                _quads[xOffset][yOffset] = map.getQuad(x + xOffset, y + yOffset);
    }

    public Quad[][] getQuads() {
        return _quads;
    }

    public Quad getQuad(int x,int y) {
        return _quads[x][y];
    }
    
    /**
     * 检测房间中是否包含指定地块
     * 
     * @param quad
     * @return
     */
    public boolean contains(Quad quad) {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if (_quads[x][y] == quad)
                    return true;
        return false;
    }
}
