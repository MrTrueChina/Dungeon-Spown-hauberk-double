package spown.spowner;

import java.awt.Rectangle;
import java.util.ArrayList;

import map.Map;
import map.quad.QuadType;
import map.room.Room;
import random.Random;
import spown.zones.RoomZone;

public class RoomsSpowner {
    private Map _map;
    private MapSpownData _spownData;
    private ArrayList<RoomZone> _rooms;

    /**
     * 在地图中生成房间
     * 
     * @param map
     * @param spownData
     * @return
     */
    public ArrayList<RoomZone> spownRooms(Map map, MapSpownData spownData) {
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
        _rooms = new ArrayList<RoomZone>();
    }

    private void clearSpowner() {
        _map = null;
        _spownData = null;
        _rooms = null;
    }

    private void doSpown() {
        /*
         *  for (生成信息里的房间生成次数)
         *  {
         *      随机获取一个房间的范围（位置和大小）
         *      
         *      if (房间能放置)
         *      {
         *          按这个范围在地图里放置房间
         *      }
         *  }
         */
        for (int i = 0; i < _spownData.spownRoomTime; i++) {
            Rectangle roomBound = gerRandomRoomBound();
            if (roomCanPut(roomBound))
                putRoom(roomBound);
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
        for (RoomZone oldRoom : _rooms)
            if (!oldRoom.nonadjacent(roomBound))
                return false;

        return true;
    }

    private void putRoom(Rectangle roomBound) {
        Room room = new Room(roomBound, _map);
        RoomZone roomZone = new RoomZone(room, getRandomDoorsNumber());

        putRoomToMap(room);
        _rooms.add(roomZone);
    }

    private int getRandomDoorsNumber() {
        return Random.weightedRandom(_spownData.roomDoorsProbability) + 1;
    }

    private void putRoomToMap(Room room) {
        for (int y = 0; y < room.height; y++)
            for (int x = 0; x < room.width; x++)
                _map.setType(x + room.x, y + room.y, QuadType.FLOOR);
    }
}
