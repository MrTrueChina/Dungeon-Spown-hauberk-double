package spown;

import java.util.ArrayList;

import map.Map;
import quad.QuadType;
import random.Random;
import room.Room;

public class RoomsSpowner {
    MapSpownData _spownData;
    Map _map;
    ArrayList<Room> _rooms = new ArrayList<Room>();

    /**
     * 在地图中生成房间
     * 
     * @param map
     * @param spownData
     * @return
     */
    public ArrayList<Room> spownRooms(Map map, MapSpownData spownData) {
        setupSpowner(map, spownData);

        doSpown();

        return _rooms;
    }

    private void setupSpowner(Map map, MapSpownData spownData) {
        _map = map;
        _spownData = spownData;
    }

    private void doSpown() {
        for (int i = 0; i < _spownData.spownRoomTime; i++) {
            int roomWidth = Random.Range(_spownData.minRoomWidth, _spownData.maxRoomWidth);
            int roomHeight = Random.Range(_spownData.minRoomHeight, _spownData.maxRoomHeight);
            int roomX = Random.Range(1, _spownData.mapWidth - roomWidth - 1); // 留出地图左右边框
            int roomY = Random.Range(1, _spownData.mapHeight - roomHeight - 1);

            Room newRoom = new Room(roomX, roomY, roomWidth, roomHeight, _map);

            if (newRoomCanPut(newRoom))
                putRoom(newRoom);
        }
    }

    private boolean newRoomCanPut(Room newRoom) {
        for (Room oldRoom : _rooms) {
            if (oldRoom.getExpandedRectangle().intersects(newRoom))
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
