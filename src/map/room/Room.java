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

    /**
     * 以一维数组形式返回所有地块
     * 
     * @return
     */
    public Quad[] getQuadArray() {
        Quad[] quadArray = new Quad[width * height];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                quadArray[y * width + x] = _quads[x][y];

        return quadArray;
    }

    /**
     * 获取顶部（y坐标最大）一行地块
     * 
     * @return
     */
    public Quad[] getTopQuads() {
        Quad[] quads = new Quad[width];

        for (int i = 0; i < width; i++)
            quads[i] = _quads[i][_quads[0].length - 1];

        return quads;
    }

    /**
     * 获取底部（y坐标最小）一行地块
     * 
     * @return
     */
    public Quad[] getBottomQuads() {
        Quad[] quads = new Quad[width];

        for (int i = 0; i < width; i++)
            quads[i] = _quads[i][0];

        return quads;
    }

    /**
     * 获取左边一行地块
     * 
     * @return
     */
    public Quad[] getLeftQuads() {
        return _quads[0];
    }

    /**
     * 获取右边一行地块
     * 
     * @return
     */
    public Quad[] getRightQuads() {
        return _quads[_quads.length - 1];
    }

    /**
     * 获取边缘增加后的Rectangle，用于判断与其他房间是否相交
     * 
     * @return
     */
    public Rectangle getExpandedRectangle() {
        return new Rectangle(x - 1, y - 1, width + 2, height + 2); // Rectangle 的相交判断似乎不包括长宽方向的边缘，因此+2
    }

    public boolean contains(Quad quad) {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if (_quads[x][y] == quad)
                    return true;
        return false;
    }
}
