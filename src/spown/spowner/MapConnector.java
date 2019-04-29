package spown.spowner;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import map.Map;
import map.quad.Quad;
import map.quad.QuadType;
import random.Random;
import spown.zones.MazeZone;
import spown.zones.RoomZone;
import spown.zones.Zone;
import vector.Vector;

public class MapConnector {
    /**
     * 检测连接点的最大长度，算法中墙的最大厚度是2，为了查到墙对面是什么地块最远需要查3步，则边界就是4
     */
    private final static int CHECK_CONNECT_POINT_BOUNDARY = 4;

    private Map _map;
    private ArrayList<RoomZone> _rooms;
    private ArrayList<Zone> _zones;
    private HashSet<Quad> _mainZoneQuads; // 利用 HashSet 的无序性来进行随机查找与主区域相邻的连接点
    private boolean[][] _connectPoints;

    /**
     * 连接房间和迷宫
     * 
     * @param map
     * @param rooms
     */
    public Map ConnectRoomsAndMaze(Map map, ArrayList<RoomZone> rooms, ArrayList<MazeZone> mazes,
            MapSpownData spownData) {
        try {
            setupConnector(map, rooms, mazes, spownData);
            spownConnectPoints();
            StartConnect();
            return _map;
        } finally {
            clearConnector();
        }
    }

    /**debug记录
    private void printConnectPoints() {
        StringBuilder string = new StringBuilder();
    
        for (int y = 0; y < _map.height; y++) {
            for (int x = 0; x < _map.width; x++) {
                string.append(getQuadString(_map.getQuad(x, y)));
            }
            string.append("\n");
        }
    
        System.out.println("↓生成点↓");
        System.out.println(string.toString());
        System.out.println("↑生成点↑");
    }
    
    private String getQuadString(Quad quad) {
        //生成点应该只在墙上生成
        //主区域应该全部是空地
    
        if (isConnectPoint(quad))
            if (quad.getType() == QuadType.WALL)
                return "生"; // 生成点、是墙，说明正确，用“生”表示
            else
                return "聲"; // 生成点，不是墙，说明生成点生成错误
    
        if (quad.getType() == QuadType.WALL)
            if (!_mainZoneQuads.contains(quad))
                return "墙";
            else
                return "繦"; // 墙，是主区域，说明加入主区域步骤错误
    
        if (quad.getType() == QuadType.FLOOR)
            if (_mainZoneQuads.contains(quad))
                return "十";
            else
                return "一";
    
        return "错";
    }
    
    //测试结果显示极有可能是判断是否移除连接点的方法出错了
    
    //1.房间连迷宫、墙为纵向、厚度为2、左主区域右非主区域：：靠近主区域的（左侧的）厚度1的墙被移除了连接点
    //2.房间连迷宫、墙为横向、厚度为1、上下都是主区域、检测区（迷宫）在上方：：墙的连接点没有被移除
    //3.房间连迷宫、墙为横向、厚度为2、上下都是主区域、监测区（迷宫）在下方：：靠近选择区（靠下）的生成点被移除，远离（靠上）的仍然存在
    //4.房间连迷宫、墙为纵向、厚度为2、左右都是主区域、检测区（迷宫）在右侧：：墙的连接点没有移除
    //5.房间连迷宫、墙为纵向、厚度为1、左右都是主区域、检测区（迷宫）在左侧：：墙的连接点没有移除。但同时，其他情况相同的墙的连接点被移除【没有获取到全部的连接点？】
    //6.【疑似】房间与迷宫相连、墙为横向，厚度为1、上下都是主区域，检测区无法确认：：之前没有正确清理的连接点被正确清理了
    //7.房间连迷宫、墙为纵向、厚度为1、上下都是主区域、检测区（迷宫）在左侧：：墙的连接点没有移除
    //8.房间连迷宫、墙为横向、厚度为2、下主区域上非主区域：：靠近主区域的（下方的）厚度1的墙被移除了连接点
    //9.房间连迷宫、墙为横向、厚度为2、上主区域下非主区域：：靠近主区域的（上方的）厚度1的墙被移除了连接点
    
    //房间连房间暂未发现错误
    
    //迷宫连房间暂未发现错误
    
    /*
     *  可能的错误位置：
    墙墙墙墙墙墙墙墙墙墙墙墙墙墙墙墙
    墙一生十十十十墙十十十十生一墙墙
    墙一生十十十十十十十十十生一墙墙
    墙一生十十十十生十十十十生一墙墙
    墙一生十十十十生十十十十生一墙墙
    墙一墙生墙生墙墙十十十十生一墙墙
    墙一墙生墙一墙墙生生墙生墙一墙墙
    墙一一一墙生墙墙生生墙一一一墙墙
    墙一墙墙墙一一一一一生一墙一墙墙
    墙一一一生一一一一一生一墙一墙墙
    墙一墙一生一一一一一墙墙墙一墙墙
    墙一墙一生一一一一一生一墙一墙墙
    墙一墙墙墙一一一一一生一墙一墙墙
    墙一一一墙生生生生生墙一一一墙墙
    墙墙墙墙墙一一一一一墙墙墙墙墙墙
    墙墙墙墙墙墙墙墙墙墙墙墙墙墙墙墙
     *  在上中部，两个生成点的三个方向都是主区域，此时如果上方的生成点向下移除生成点，将会导致走出三步也找不到主区域，达不到清除条件，生成点无法正常清除
     *  房间清除生成点是从四面向外清理，因此不会发生这个错误，但迷宫清除生成点是随机选择方向，这就导致了清理错误
     *  解决方式是在清除迷宫生成点时遍历所有主区域，并从主区域向所有方向发出清理
     */

    private void setupConnector(Map map, ArrayList<RoomZone> rooms, ArrayList<MazeZone> mazes, MapSpownData spownData) {
        _map = map;
        _rooms = rooms;
        setupZones(rooms, mazes);
        _mainZoneQuads = new HashSet<Quad>();
        setupConnects(map);
    }

    private void setupZones(ArrayList<RoomZone> rooms, ArrayList<MazeZone> mazes) {
        _zones = new ArrayList<Zone>();
        _zones.addAll(rooms);
        _zones.addAll(mazes);
        //        System.out.println("总区域数 = " + _zones.size());
    }

    private void setupConnects(Map map) {
        _connectPoints = new boolean[map.width][map.height];

        for (int y = 0; y < map.height; y++)
            for (int x = 0; x < map.width; x++)
                removeConnectPoint(x, y);
    }

    private void clearConnector() {
        _map = null;
        _rooms = null;
        _zones = null;
        _mainZoneQuads = null;
        _connectPoints = null;
    }

    /**
     * 生成连接点
     */
    private void spownConnectPoints() {
        /*
         *  遍历房间
         *  {
         *      生成这个房间的连接点
         *  }
         */
        //System.out.println("开始生成连接点");
        for (RoomZone room : _rooms)
            spownARoomConnectPoints(room);
    }

    /**
     * 生成一个房间的连接点
     * 
     * @param room
     */
    private void spownARoomConnectPoints(RoomZone room) {
        /**
         *  遍历房间上边每个地块
         *  {
         *      生成这个地块的向上的连接点
         *  }
         *  其他三边类似
         */
        //System.out.println("生成房间 " + room + " 的连接点");
        for (Quad quad : room.getTopQuads())
            spownAQuadConnectPoints(quad, Vector.UP);
        for (Quad quad : room.getRightQuads())
            spownAQuadConnectPoints(quad, Vector.RIGHT);
        for (Quad quad : room.getBottomQuads())
            spownAQuadConnectPoints(quad, Vector.DOWN);
        for (Quad quad : room.getLeftQuads())
            spownAQuadConnectPoints(quad, Vector.LEFT);
    }

    /**
     * 从指定地块开始，向指定方向生成连接点
     * 
     * @param startPosition
     * @param direction
     */
    private void spownAQuadConnectPoints(Point startPosition, Vector direction) {
        /*
         *  if(从起点向指定方向走能走到空地())
         *      生成连接点到空地()
         */
        //System.out.println("在 " + quadPosition + " 位置向 " + direction + " 方向生成连接点");
        
        if (canConnectToFloor(startPosition, direction))
            spownALineConnectPoints(startPosition, direction);

        //printConnectPointsNumber();
    }

    //检测从指定位置向指定方向能不能连接到空地
    private boolean canConnectToFloor(Point startPoint, Vector direction) {
        //TODO:检测从指定位置向指定方向能不能连接到空地
        /*
         *  for(步数限制)
         *      if(在地图里 && 这一步所在地块不是墙)
         *          return true
         *  return false
         */
        for (int step = 1; step < CHECK_CONNECT_POINT_BOUNDARY; step++) {
            Point currentPosition = direction.multiply(step).add(startPoint);
            
            if (!_map.contains(currentPosition))
                return false;
            
            if (_map.getType(currentPosition) != QuadType.WALL) // Point没有运算方法，所以用 Vector 的乘法先得到 Vector 之后用加法
                return true;
        }
        return false;
    }

    //从指定位置向指定方向生成一行连接点
    private void spownALineConnectPoints(Point startPoint, Vector direction) {
        //TODO:从指定位置向指定方向生成一行连接点
        /*
         *  for(步数)
         *  {
         *      if(这一步所在的地块是墙)
         *          生成连接点()
         *      else
         *          return
         *  }
         */
        for (int step = 1;; step++) {
            Point currentPosition = direction.multiply(step).add(startPoint);
            if (_map.getType(currentPosition) == QuadType.WALL)
                addConnectPoint(currentPosition);
            else
                return;
        }
    }

    /** 用于测试连接点生成的测试输出
    private void printConnectPointsNumber() {
        int num = 0;
        for (int y = 0; y < _connectPoints[0].length; y++)
            for (int x = 0; x < _connectPoints.length; x++)
                if (_connectPoints[x][y])
                    num++;
        System.out.println("连接点数量 = " + num);
    }
    */

    /**
     * 添加连接点
     * 
     * @param point
     */
    private void addConnectPoint(Point point) {
        addConnectPoint(point.x, point.y);
    }

    /**
     * 添加连接点
     * 
     * @param x
     * @param y
     */
    private void addConnectPoint(int x, int y) {
        _connectPoints[x][y] = true;
    }

    /**
     * 移除连接点
     * 
     * @param point
     */
    private void removeConnectPoint(Point point) {
        removeConnectPoint(point.x, point.y);
    }

    /**
     * 移除连接点
     * 
     * @param x
     * @param y
     */
    private void removeConnectPoint(int x, int y) {
        _connectPoints[x][y] = false;
    }

    /**
     * 判断一个位置是不是连接点
     * 
     * @param point
     * @return
     */
    private boolean isConnectPoint(Point point) {
        return isConnectPoint(point.x, point.y);
    }

    /**
     * 判断一个位置是不是连接点
     * 
     * @param x
     * @param y
     * @return
     */
    private boolean isConnectPoint(int x, int y) {
        return _connectPoints[x][y];
    }

    /**
     * 开始连接
     */
    private void StartConnect() {
        /*
         *  随机选一个房间，所有地块加入主区域表
         *  
         *  Point 连接点
         *  while((随机获取一个主区域相邻的连接点) != null)
         *  {
         *      连接连接点
         *  }
         */
        connectStartRoom(); // 随机选一个房间，所有地块加入主区域表

        Point connectPoint = null;
        while ((connectPoint = getRandomConnectPointaAjacentMainZone()) != null)
            connectAConnectPoint(connectPoint);
    }

    /**
     * 连接第一个房间
     */
    private void connectStartRoom() {
        /*
         *  Room 房间 =  随机出一个房间
         *  房间中的地块加入到主区域
         *  
         *  还需要其他操作吗？似乎不需要，似乎没有清理连接点的必要性
         */
        RoomZone room = getRandomRoom();
        addARoomQuadsToMainZone(room);
        //System.out.println("第一个房间 = " + room);
    }

    private RoomZone getRandomRoom() {
        return _rooms.get(Random.Range(0, _rooms.size()));
    }

    /**
     * 将一个房间的地块加入到主区域
     * 
     * @param room
     */
    private void addARoomQuadsToMainZone(final RoomZone room) {
        _mainZoneQuads.addAll(Arrays.asList(room.getQuads()));
    }

    /**
     * 获取随机与主区域相连的连接点
     * 
     * @return
     */
    private Point getRandomConnectPointaAjacentMainZone() {
        /*
         *  遍历所有主区域地块（利用 HashSet 的无序性实现随机）
         *  {
         *      获取当前地块相邻的连接点
         *      if(连接点 != null)
         *      {
         *          return 连接点
         *      }
         *  }
         *  
         *  遍历完了也没找到，return null
         */
        Point connectPoint = null;
        for (Quad quad : _mainZoneQuads) {
            connectPoint = getContiguousConnectPoint(quad);
            if (connectPoint != null)
                return connectPoint;
        }
        return null;
    }

    /**
     * 获取一个地块上下左右四个相邻位置中第一个是连接点的那个，如果四个都不是连接点则返回 null
     * 
     * @param quad
     * @return
     */
    private Vector getContiguousConnectPoint(final Quad quad) {
        /*
         *  遍历当前地块的上下左右四个Point
         *  {
         *      if (找到连接点了)
         *          return 找到的连接点
         *  }
         *  
         *  没找到 return null
         */
        if (isConnectPoint(quad.x, quad.y + 1))
            return new Vector(quad.x, quad.y + 1);
        if (isConnectPoint(quad.x + 1, quad.y))
            return new Vector(quad.x + 1, quad.y);
        if (isConnectPoint(quad.x, quad.y - 1))
            return new Vector(quad.x, quad.y - 1);
        if (isConnectPoint(quad.x - 1, quad.y))
            return new Vector(quad.x - 1, quad.y);
        return null;
    }

    /**
     * 从一个连接点出发，连接连接点连接到的区域
     * 
     * @param connectPoint
     */
    private void connectAConnectPoint(final Point connectPoint) {
        /*
         *  Point 起点 = 获取相邻的主区域点(连接点)
         *  Vector 方向 = 连接点 - 主区域点
         *  
         *  for(step = 1;;step++)
         *  {
         *      Point 当前点 = 根据起点、方向、步数计算当前点
         *      
         *      if(当前点不是空地)
         *      {
         *          连接这个地块（应该是墙）
         *      }
         *      else
         *      {
         *          根据地块获取区域()
         *          连接这个区域()
         *          return
         *      }
         *  }
         */
        Point startPoint = getContiguousMainQuad(connectPoint);
        Vector direction = new Vector(connectPoint.x - startPoint.x, connectPoint.y - startPoint.y);

        for (int step = 1;; step++) {
            Quad currentQuad = _map.getQuad(startPoint.x + direction.x * step, startPoint.y + direction.y * step);

            if (_map.getType(currentQuad) != QuadType.FLOOR) {
                connectAQuad(currentQuad);
            } else {
                connectAZone(getZoneByQuad(currentQuad));
                return;
            }
        }
    }

    /**
     * 获取一个地块相邻的一个主区域地块
     * 
     * @param center
     * @return
     */
    private Point getContiguousMainQuad(Point center) {
        if (_mainZoneQuads.contains(_map.getQuad(center.x, center.y + 1)))
            return _map.getQuad(center.x, center.y + 1);
        if (_mainZoneQuads.contains(_map.getQuad(center.x + 1, center.y)))
            return _map.getQuad(center.x + 1, center.y);
        if (_mainZoneQuads.contains(_map.getQuad(center.x, center.y - 1)))
            return _map.getQuad(center.x, center.y - 1);
        if (_mainZoneQuads.contains(_map.getQuad(center.x - 1, center.y)))
            return _map.getQuad(center.x - 1, center.y);
        return null;
    }

    /**
     * 获取地块所属的区域
     * 
     * @param quad
     * @return
     */
    private Zone getZoneByQuad(Quad quad) {
        /*
         *  遍历区域
         *      if (区域包含这个地块)
         *          return 这个区域
         *  return null
         */
        for (Zone zone : _zones)
            if (zone.contains(quad))
                return zone;
        return null;
    }

    /**
     * 连接一个区域
     * 
     * @param zone
     */
    private void connectAZone(Zone zone) {
        /*
         *  将这个区域所有地块加入主区域
         *  
         *  if (这是个房间)
         *      开多个门
         *  
         *  清理这个区域的连接点
         */
        addZoneQuadsToMainZone(zone);

        if (zone instanceof RoomZone)
            addDoorToRoom((RoomZone) zone);

        clearAZoneConnectPoints(zone);
    }

    /**
     * 根据房间的生成信息给房间增加门
     * 
     * @param room
     */
    private void addDoorToRoom(RoomZone room) {
        /*
         *  以随机顺序遍历四个边的地块
         *  {
         *      if(门的数量达到了房间的门数量
         *          return
         *  
         *      if(这个地块相邻的是连接点 && 连接点连接到主区域)
         *      {
         *          连接到主区域去()
         *          门数量++
         *      }
         *  }
         */
        HashSet<Quad> marginalQuads = new HashSet<Quad>(Arrays.asList(room.getMarginalQuads()));
        int doorsNumber = 1;

        for (Quad quad : marginalQuads) {
            if (doorsNumber >= room.doorsNumber())
                return;

            Point connectPoint = getContiguousConnectPoint(quad);
            if (connectPoint != null && isConnectToMainZone(quad, connectPoint)) {
                connectLine(quad, connectPoint);
                doorsNumber++;
            }
        }
    }

    /**
     * 判断从指定地块向指定地块的方向能不能走到主区域
     * 
     * @param quad
     * @param connectPoint
     * @return
     */
    private boolean isConnectToMainZone(final Point startQuad, final Point connectPoint) {
        return isConnectToMainZone(startQuad, Vector.getDirection(startQuad, connectPoint));
    }

    //TODO: 这个方法正常工作是有前提的，可能和生成点是否存在有关
    /**
     * 判断从指定地块向指定方向能不能走到主区域
     * 
     * @param quad
     * @param connectPoint
     * @return
     */
    private boolean isConnectToMainZone(final Point startQuad, final Vector direction) {
        /*
         *  生成点没有通向地图边界的可能，判断超出地图没有意义
         * 
         *  for(小于最大长度)
         *  {
         *      if(这个位置不是生成点())
         *          return 这个位置是不是主区域()
         *  }
         */
        Vector startVector = new Vector(startQuad);

        for (int step = 1; step <= CHECK_CONNECT_POINT_BOUNDARY; step++) {
            Vector currentPosition = startVector.add(direction.multiply(step));
            if (!isConnectPoint(currentPosition))
                return isMainZone(_map.getQuad(currentPosition));
        }
        return false;
    }

    private boolean isMainZone(Quad quad) {
        return _mainZoneQuads.contains(quad);
    }

    /**
     * 从指定地块向连接点方向开道，直到连接到不是墙的地块
     * 
     * @param startQuad
     * @param connectPoint
     */
    private void connectLine(Point startQuad, Point connectPoint) {
        connectLine(startQuad, Vector.getDirection(startQuad, connectPoint));
    }

    /**
     * 从指定地块向指定方向开道，直到连接到不是墙的地块
     * 
     * @param startQuad
     * @param connectPoint
     */
    private void connectLine(Point startQuad, Vector direction) {
        /*
         *  for(步数)
         *  {
         *      if(这一步位置是墙)
         *          连接这个地块进主区域
         *      else
         *          return
         *  }
         */
        Vector startPosition = new Vector(startQuad);

        for (int step = 1;; step++) /*设计上第零步是空地，肯定不是生成点，所以要从 1 开始*/ {
            Vector currentPosition = startPosition.add(direction.multiply(step));
            if (_map.getType(currentPosition) == QuadType.WALL)
                connectAQuad(currentPosition);
            else
                return;
        }
    }

    /**
     * 将一个区域的所有地块加入主区域
     * 
     * @param zone
     */
    private void addZoneQuadsToMainZone(Zone zone) {
        _mainZoneQuads.addAll(Arrays.asList(zone.getQuads()));
    }

    /**
     * 清理一个区域的连接点
     * 
     * @param zone
     */
    private void clearAZoneConnectPoints(Zone zone) {
        /*
         *  遍历这个区域所有的地块
         *  {
         *      清理这个地块的连接点
         *  }
         */
        for (Quad quad : zone.getQuads())
            clearAQuadConnectPoint(quad);
    }

    /**
     * 把一个地块连接到主区域
     * 
     * @param quadPoint
     */
    private void connectAQuad(Point quadPoint) {
        /*
         *  打穿墙
         *  加进主区域
         *  清除连接点
         */
        _map.setType(quadPoint, QuadType.FLOOR);
        _mainZoneQuads.add(_map.getQuad(quadPoint));
        removeConnectPoint(quadPoint);
    }

    private void clearConnectPoint(final Point startPoint, final Vector direction) {
        /*
         *  if(从起点向指定方向连接到主区域())
         *      清理从起点到指定方向的连接点（）
         */
        if (isConnectToMainZone(startPoint, direction))
            clearLineConnectPoint(startPoint, direction);
    }

    /**
     * 从起点向指定方向清理一行连接点
     *     
     * @param startPoint
     * @param direction
     */
    private void clearLineConnectPoint(Point startPoint, Vector direction) {
        /*
         *  for(步数)
         *  {
         *      if(这一步的位置是连接点)
         *          移除连接点
         *      else
         *          return
         *  }
         */
        Vector startVector = new Vector(startPoint);
        for (int step = 1;; step++) {
            Point currentPoint = startVector.add(direction.multiply(step));
            if (isConnectPoint(currentPoint))
                removeConnectPoint(currentPoint);
            else
                return;
        }
    }

    /**
     * 清除一个地块四周的连接点
     * 
     * @param quad
     */
    private void clearAQuadConnectPoint(Quad quad) {
        clearConnectPoint(quad, Vector.UP);
        clearConnectPoint(quad, Vector.RIGHT);
        clearConnectPoint(quad, Vector.DOWN);
        clearConnectPoint(quad, Vector.LEFT);
    }
}
