package spown.zones;

import map.maze.Maze;
import map.quad.Quad;

public class MazeZone {
    private Maze _maze;

    public MazeZone(Maze maze) {
        if (maze != null)
            _maze = maze;
        else
            throw new NullPointerException("迷宫不能为null");
    }

    public Maze maze() {
        return _maze;
    }

    public boolean addQuad(Quad quad) {
        return _maze.addQuad(quad);
    }

    public Quad[] getQuads() {
        return _maze.getQuads();
    }

    public boolean contains(Quad quad) {
        return _maze.contains(quad);
    }
}
