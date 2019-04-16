package spown;

import java.util.ArrayList;

import map.Map;
import quad.QuadType;
import room.Room;

public class MapSpowner {
    private MapSpownData _spownData;
    private Map _map;
    private ArrayList<Room> _rooms;

    public Map map() {
        return _map;
    }

    public Map spown(final MapSpownData data) {
        _spownData = data;

        setupMap();
        
        _rooms = new RoomsSpowner().spownRooms(_map, _spownData);
        System.out.println("生成了 " + _rooms.size() + " 个房间");
        
        new MazeSpowner().fillMaze(_map);

        return _map;
    }

    private void setupMap() {
        _map = new Map(_spownData.mapWidth, _spownData.mapHeight);

        for (int y = 0; y < _map.height; y++)
            for (int x = 0; x < _map.width; x++)
                _map.setType(x, y, QuadType.WALL);
    }
}
