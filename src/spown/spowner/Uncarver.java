package spown.spowner;

import java.awt.Point;

import map.Map;
import map.quad.QuadType;

public class Uncarver {
    Map _map;

    public Map uncarve(final Map map) {
        try {
            setupUncarver(map);
            doUncarver();
            return _map;
        } finally {
            clearUncarver();
        }
    }

    private void setupUncarver(final Map map) {
        _map = map;
    }

    private void clearUncarver() {
        _map = null;
    }

    private void doUncarver() {
        /*
         *  while(清理死路());
         */
        while (clearDeadEnd())
            ;
    }

    private boolean clearDeadEnd() {
        /*
         *  遍历地图上所有地块
         *  {
         *      if(这个地块是空地 && 这个地块上下左右有至少三个是墙)
         *      {
         *          从这个地块开始清理死胡同
         *          return true
         *      }
         *  }
         *  
         *  执行到这说明没有死胡同了 return false
         */
        for (int x = 0; x < _map.width; x++)
            for (int y = 0; y < _map.height; y++)
                if (_map.getType(x, y) == QuadType.FLOOR && isDeadEnd(x, y)) {
                    clearADeadEnd(new Point(x, y));
                    return true;
                }
        return false;
    }

    private boolean isDeadEnd(final Point point) {
        return isDeadEnd(point.x, point.y);
    }

    private boolean isDeadEnd(final int x, final int y) {
        int contiguousWallNumber = 0;

        if (_map.getType(x, y + 1) == QuadType.WALL)
            contiguousWallNumber++;
        if (_map.getType(x + 1, y) == QuadType.WALL)
            contiguousWallNumber++;
        if (_map.getType(x, y - 1) == QuadType.WALL)
            contiguousWallNumber++;
        if (_map.getType(x - 1, y) == QuadType.WALL)
            contiguousWallNumber++;

        return contiguousWallNumber >= 3; // 一个格子总共相邻四个格子，至少三个是墙，很明显是死路
    }

    private void clearADeadEnd(final Point deadEnd) {
        /*
         * 要一次清理一串死胡同，必须要解决的问题是清理一个死胡同后怎么样将清理点移动到下一个位置
         * 首先当然要有一个 Point 存储清理点
         * 之后需要一个循环，一直循环到清理点不是死胡同
         */
        /*
         *  Point 清理点 = 参数死胡同
         *  
         *  while(清理点是死胡同)
         *  {
         *      填上清理点
         *      找到清理点旁边的那个空地，把清理点移过去
         *  }
         */
        Point clearPoint = deadEnd;

        while (clearPoint != null && isDeadEnd(clearPoint)) {
            _map.setType(clearPoint, QuadType.WALL);
            clearPoint = getContiguousFloorPoint(clearPoint);
        }
    }

    private Point getContiguousFloorPoint(Point center) {
        if (_map.getType(center.x, center.y + 1) == QuadType.FLOOR)
            return new Point(center.x, center.y + 1);
        if (_map.getType(center.x + 1, center.y) == QuadType.FLOOR)
            return new Point(center.x + 1, center.y);
        if (_map.getType(center.x, center.y - 1) == QuadType.FLOOR)
            return new Point(center.x, center.y - 1);
        if (_map.getType(center.x - 1, center.y) == QuadType.FLOOR)
            return new Point(center.x - 1, center.y);
        return null;
    }
}
