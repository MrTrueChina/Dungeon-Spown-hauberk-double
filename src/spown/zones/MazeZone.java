package spown.zones;

import map.maze.Maze;
import map.quad.Quad;

public class MazeZone implements Zone {
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

    /**
     * 获取迷宫中所有地块
     */
    @Override
    public Quad[] getQuads() {
        return _maze.getQuads();
    }

    /**
     * 检测这个迷宫是否包含指定地块
     */
    @Override
    public boolean contains(Quad quad) {
        return _maze.contains(quad);
    }
}
