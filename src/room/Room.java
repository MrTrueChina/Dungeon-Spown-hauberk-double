package room;

import java.awt.Rectangle;

import quad.Quad;

@SuppressWarnings("serial")
public class Room extends Rectangle {
    private Quad[][] _quads; // 【潜在bug】：房间的地块可以和地图不一样，这样会导致同一个坐标有两个地块，进而导致难以寻找的bug

    public Room(int width, int height) {
        this(0, 0, width, height);
    }

    public Room(int x, int y, int width, int height) {
        super(x, y, width, height);
        _quads = new Quad[width][height];
    }

    /**
     * 获取边缘增加后的Rectangle，用于判断与其他房间是否相交
     * 
     * @return
     */
    public Rectangle getExpandedRectangle() {
        return new Rectangle(x - 1, y - 1, width + 2, height + 2); // Rectangle 的相交判断似乎不包括长宽方向的边缘，因此+2
    }
}
