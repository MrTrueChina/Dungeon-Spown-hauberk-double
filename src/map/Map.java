package map;

import java.awt.Point;
import java.awt.Rectangle;

import map.quad.Quad;
import map.quad.QuadType;

@SuppressWarnings("serial")
public class Map extends Rectangle {
    private Quad[][] _quads;

    public Map(int width, int height) {
        super(width, height);

        _quads = new Quad[width][height];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                _quads[x][y] = new Quad(x, y, QuadType.FLOOR);
    }

    public void setType(Point point, QuadType type) {
        setType(point.x, point.y, type);
    }

    public void setType(int x, int y, QuadType type) {
        _quads[x][y].setType(type);
    }

    public QuadType getType(Point point) {
        return getType(point.x, point.y);
    }

    public QuadType getType(int x, int y) {
        return _quads[x][y].getType();
    }

    public Quad getQuad(Point point) {
        return getQuad(point.x, point.y);
    }

    public Quad getQuad(int x, int y) {
        return _quads[x][y];
    }
}
