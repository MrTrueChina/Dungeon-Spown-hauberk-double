package spown.spowner;

import java.util.ArrayList;

import map.Map;
import map.maze.Maze;
import map.quad.QuadType;
import map.room.Room;
import spown.zones.MazeZone;
import spown.zones.RoomZone;

public class MapSpowner {
    private MapSpownData _spownData;
    private Map _map;
    private ArrayList<Room> _rooms;
    private ArrayList<Maze> _mazes;

    public Map map() {
        return _map;
    }

    public Map spown(final MapSpownData data) {
        _spownData = data;

        setupMap();

        _rooms = new RoomsSpowner().spownRooms(_map, _spownData);
        System.out.println("生成了 " + _rooms.size() + " 个房间");

        _mazes = new MazeSpowner().fillMaze(_map);
        System.out.println("生成了 " + _mazes.size() + " 个迷宫");

        /////////////////////////////////////
        
        ArrayList<RoomZone> roomZones = new ArrayList<RoomZone>();
        for (Room room : _rooms) 
            roomZones.add(new RoomZone(room));
        
        ArrayList<MazeZone> mazeZones = new ArrayList<MazeZone>();
        for (Maze maze : _mazes)
            mazeZones.add(new MazeZone(maze));
        
        /////////////////////////////////////
        
        new MapConnector().ConnectRoomsAndMaze(_map, roomZones, mazeZones, _spownData);
        
        new Uncarver().uncarve(_map);

        return _map;
    }

    private void setupMap() {
        _map = new Map(_spownData.mapWidth, _spownData.mapHeight);

        for (int y = 0; y < _map.height; y++)
            for (int x = 0; x < _map.width; x++)
                _map.setType(x, y, QuadType.WALL);
    }
}
