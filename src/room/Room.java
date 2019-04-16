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
}
