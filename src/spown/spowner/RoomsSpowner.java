package spown.spowner;

import java.awt.Rectangle;
import java.util.ArrayList;

import map.Map;
import map.quad.QuadType;
import map.room.Room;
import random.Random;

public class RoomsSpowner {
    private MapSpownData _spownData;
    private Map _map;
    private ArrayList<Room> _rooms = new ArrayList<Room>();

    /**
     * 在地图中生成房间
     * 
     * @param map
     * @param spownData
     * @return
     */
    public ArrayList<Room> spownRooms(Map map, MapSpownData spownData) {
        try {
            setupSpowner(map, spownData);

            doSpown();

            return _rooms;
        } finally {
            clearSpowner();
        }
    }

    private void setupSpowner(Map map, MapSpownData spownData) {
        _map = map;
        _spownData = spownData;
    }

    private void clearSpowner() {
        _map = null;
        _spownData = null;
    }

    private void doSpown() {
        /*
         *  for (生成信息里的房间生成次数)
         *  {
         *      随机获取一个房间的范围（位置和大小）
         *      
         *      if (房间能放置)
         *      {
         *          把这个房间放置到地图里
         *      }
         *  }
         */
        for (int i = 0; i < _spownData.spownRoomTime; i++) {
            Rectangle roomBound = gerRandomRoomBound();
            if (roomCanPut(roomBound)) {
                putRoom(new Room(roomBound, _map));
            }
        }
    }

    private Rectangle gerRandomRoomBound() {
        int roomWidth = Random.Range(_spownData.minRoomWidth, _spownData.maxRoomWidth);
        int roomHeight = Random.Range(_spownData.minRoomHeight, _spownData.maxRoomHeight);
        int roomX = Random.Range(1, _spownData.mapWidth - roomWidth - 1); // 留出地图左右边框
        int roomY = Random.Range(1, _spownData.mapHeight - roomHeight - 1);

        return new Rectangle(roomX, roomY, roomWidth, roomHeight);
    }

    private boolean roomCanPut(Rectangle roomBound) {
        for (Room oldRoom : _rooms) {
            if (oldRoom.getExpandedRectangle().intersects(roomBound))
                return false;
        }
        return true;
    }

    private void putRoom(Room room) {
        _rooms.add(room);

        for (int y = 0; y < room.height; y++)
            for (int x = 0; x < room.width; x++)
                _map.setType(x + room.x, y + room.y, QuadType.FLOOR);
    }
}
