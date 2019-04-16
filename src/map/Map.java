package map;

import java.awt.Rectangle;

import quad.Quad;
import quad.QuadType;

@SuppressWarnings("serial")
public class Map extends Rectangle {
    private Quad[][] _quads;

    public Map(int width, int height) {
        super(width, height);

        _quads = new Quad[width][height];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                _quads[x][y] = new Quad(QuadType.FLOOR);
    }

    public void setType(int x, int y, QuadType type) {
        _quads[x][y].setType(type);
    }

    public QuadType getType(int x, int y) {
        return _quads[x][y].getType();
    }
}
