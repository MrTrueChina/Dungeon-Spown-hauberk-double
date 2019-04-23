package spown.zones;

import java.awt.Rectangle;
import map.quad.Quad;
import map.room.Room;

public class RoomZone implements Zone {
    private Room _room;
    private int _doorsNumber;

    public RoomZone(Room room, int doorsNumber) {
        if (room != null) {
            _room = room;
            _doorsNumber = doorsNumber > 0 ? doorsNumber : 1;
        } else
            throw new NullPointerException("房间不能为null");
    }

    public Room room() {
        return _room;
    }

    public int x() {
        return _room.x;
    }

    public int y() {
        return _room.y;
    }

    public int width() {
        return _room.width;
    }

    public int height() {
        return _room.height;
    }

    public int doorsNumber() {
        return _doorsNumber;
    }

    /**
     * 以一维数组的形式返回房间所有地块
     * 
     * @return
     */
    @Override
    public Quad[] getQuads() {
        Quad[] quadArray = new Quad[_room.width * _room.height];

        for (int y = 0; y < _room.height; y++)
            for (int x = 0; x < _room.width; x++)
                quadArray[y * _room.width + x] = _room.getQuad(x, y);

        return quadArray;
    }

    /**
     * 获取房间顶部一行地块，顶部的定义是 “y坐标最大”
     * 
     * @return
     */
    public Quad[] getTopQuads() {
        Quad[] quads = new Quad[_room.width];

        for (int x = 0; x < _room.width; x++)
            quads[x] = _room.getQuad(x, _room.height - 1);

        return quads;
    }

    /**
     * 获取房间右边一行地块
     * 
     * @return
     */
    public Quad[] getRightQuads() {
        return _room.getQuads()[_room.width - 1];
    }

    /**
     * 获取房间底部一行地块，底部的定义是 “y坐标最小”
     * 
     * @return
     */
    public Quad[] getBottomQuads() {
        Quad[] quads = new Quad[_room.width];

        for (int x = 0; x < _room.width; x++)
            quads[x] = room().getQuad(x, 0);

        return quads;
    }

    /**
     * 获取房间左侧一列地块
     * 
     * @return
     */
    public Quad[] getLeftQuads() {
        return _room.getQuads()[0];
    }

    /**
     * 判断该房间是否和指定 Rectangle 相邻
     * 
     * @param rectangle
     * @return
     */
    public boolean nonadjacent(Rectangle rectangle) {
        return !(new Rectangle(_room.x - 1, _room.y - 1, _room.width + 2, _room.height + 2).intersects(rectangle));
    }

    /**
     * 判断房间是否包含指定地块
     * 
     * @param quad
     * @return
     */
    @Override
    public boolean contains(Quad quad) {
        return _room.contains(quad);
    }

}
