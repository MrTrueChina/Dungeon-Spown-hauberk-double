package spown;

import java.util.ArrayList;

import map.Map;
import quad.QuadType;
import room.Room;

public class MapSpowner {
    private MapSpownData _spownData;
    private Map _map;
    private ArrayList<Room> _Rooms;

    public Map map() {
        return _map;
    }

    public Map spown(final MapSpownData data) {
        _spownData = data;

        setupMap();
        spownRooms();

        return _map;
    }

    private void setupMap() {
        _map = new Map(_spownData.mapWidth, _spownData.mapHeight);

        for (int y = 0; y < _map.height; y++)
            for (int x = 0; x < _map.width; x++)
                _map.setType(x, y, QuadType.WALL);
    }

    private void spownRooms() {
        
    }
}
