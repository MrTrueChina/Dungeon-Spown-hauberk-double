package spown;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import map.Map;
import maze.Maze;
import quad.QuadType;

public class MazeSpowner {
    private LinkedList<Point> _readyNodes; // 开表，用于存储准备雕刻的节点
    private ArrayList<Point> _carvedNodes; // 闭表，用于存储雕刻完毕的节点
    private Map _map;
    private ArrayList<Maze> _mazes;

    /**
     * 在传入的地图上填充迷宫
     * 
     * @param map
     * @return
     */
    public ArrayList<Maze> fillMaze(final Map map) {
        try {
            setupSpowner(map);
            doSpown();
            return _mazes;
        } finally {
            cleanSpowner();
        }
    }

    private void setupSpowner(final Map map) {
        _map = map;
        _readyNodes = new LinkedList<Point>();
        _carvedNodes = new ArrayList<Point>();
        _mazes = new ArrayList<Maze>();
    }

    private void cleanSpowner() {
        _map = null;
        _readyNodes = null;
        _carvedNodes = null;
        _mazes = null;
    }

    private void doSpown() {
        /**
         * 1.需要先定位到迷宫的左上角，这个左上角的标准应该是“周围八个格都是墙”
         * 2.以左上角为基准进行雕刻，这个雕刻没有预先打孔，每次雕刻需要雕刻两个地块，并且将周围的节点加入开表时需要先判断能否雕刻
         * 3.雕刻完后，寻找下一个可以作为左上角的点，可以考虑找得到返回 Point，找不到返回 null，便于判断 
         * 4.直到找不到可以作为左上角的点为止，填充结束
         */
        while (carveStartNode()) // 如果能成功雕刻起点就说明还有填充的空间，如果雕刻起点失败则说明已经没有空间填充迷宫了，循环也就可以结束了
            while (_readyNodes.size() > 0) {
                //long startCarveRandomNodeTime = System.currentTimeMillis();
                carveRandomNode();
                //System.out.println("随机雕刻一个节点耗时 " + (System.currentTimeMillis() - startCarveRandomNodeTime) + " 毫秒");
            }
    }

    /**
     * 寻找并雕刻迷宫起点
     * 
     * @return 找到了起点返回true，否则返回false
     */
    private boolean carveStartNode() {
        Point startNode = getLeftTopNode(); // 以左上角的洞作为起点
        if (startNode != null) {
            carve(startNode, startNode);
            return true;
        }
        return false;
    }

    private Point getLeftTopNode() {
        for (int y = 0; y < _map.height; y++)
            for (int x = 0; x < _map.width; x++)
                if (isCanCarveNode(new Point(x, y)))
                    return new Point(x, y);
        return null;
    }

    private boolean isCanCarveNode(Point node) {
        return isCanCarveNode(node.x, node.y);
    }

    private boolean isCanCarveNode(int x, int y) {
        for (int xOffset = -1; xOffset <= 1; xOffset++)
            for (int yOffset = -1; yOffset <= 1; yOffset++)
                if (!isWall(new Point(x + xOffset, y + yOffset)))
                    return false;
        return true;
    }

    private boolean isWall(Point node) {
        return (_map.contains(node) && _map.getType(node) == QuadType.WALL);
    }

    private void carveRandomNode() {
        /*
         * 雕刻一个节点
         * 
         * 0、根据普里姆算法，每次在开表里找一个到最近的闭表点距离最近的点，而这里所有点是等距的，所以是随机找一个开表的点再连接到随机一个相邻点
         * 1、在开表里随机找出一个点
         * 2、在这个点的周围找出所有在闭表里的点
         * 3、在这些点里随机找一个打通
         * 4、这个点加入闭表
         * 5、这个点周围的点加入开表
         */
        Point readyNode = getRandomReadyNode();
        Point carvedNode = getRandomContiguousCarvedNode(readyNode);

        carve(carvedNode, readyNode);
    }

    private Point getRandomReadyNode() {
        return getRandomNode(_readyNodes);
    }

    /**
     * 随机获取一个指定节点相邻的已经雕刻的节点
     * 
     * @param centerPoint 要获取相邻节点的节点
     * @return 如果存在相邻的已雕刻节点，则返回一个已雕刻节点，否则返回null
     */
    private Point getRandomContiguousCarvedNode(final Point centerPoint) {
        LinkedList<Point> nodes = getContiguousNodes(centerPoint);

        Iterator<Point> nodesIterator = nodes.iterator();
        while (nodesIterator.hasNext())
            if (!_carvedNodes.contains(nodesIterator.next()))
                nodesIterator.remove(); // 通过迭代器移除元素，这个方法必须在 next() 之后调用，并且只能移除刚刚通过 next() 获取的元素

        if (nodes.size() > 0)
            return getRandomNode(nodes);
        return null;
    }

    /**
     * 从节点列表里随机获取一个节点
     * 
     * @param nodes 要获取节点的列表
     * @return
     */
    private Point getRandomNode(List<Point> nodes) {
        if (nodes.size() != 0)
            return nodes.get(random(nodes.size()));
        return null;
    }

    private int random(int bound) {
        return (int) (Math.random() * bound);
    }

    /**
     * 获取一个节点相邻的节点
     * 
     * @param centerPoint 中央的节点
     * @return
     */
    private LinkedList<Point> getContiguousNodes(final Point centerPoint) {
        LinkedList<Point> nodes = new LinkedList<Point>();

        if (_map.contains(centerPoint.x, centerPoint.y + 2))
            nodes.add(new Point(centerPoint.x, centerPoint.y + 2));
        if (_map.contains(centerPoint.x + 2, centerPoint.y))
            nodes.add(new Point(centerPoint.x + 2, centerPoint.y));
        if (_map.contains(centerPoint.x, centerPoint.y - 2))
            nodes.add(new Point(centerPoint.x, centerPoint.y - 2));
        if (_map.contains(centerPoint.x - 2, centerPoint.y))
            nodes.add(new Point(centerPoint.x - 2, centerPoint.y));

        return nodes;
    }

    private void carve(Point startNode, Point targetNode) {
        breakWall(startNode, targetNode);
        setContiguousDeactiveNodeToReadyList(targetNode);
        moveNodeToCarved(targetNode);
    }

    private void breakWall(Point startNode, Point targetNode) {
        _map.setType((startNode.x + targetNode.x) / 2, (startNode.y + targetNode.y) / 2, QuadType.FLOOR); // 这是中间的墙
        _map.setType(targetNode, QuadType.FLOOR);
    }

    private void setContiguousDeactiveNodeToReadyList(Point centerNode) {
        LinkedList<Point> contiguousNode = getContiguousNodes(centerNode);

        for (Point node : contiguousNode)
            if (isCanCarveNode(node) && !_readyNodes.contains(node) && !_carvedNodes.contains(node))
                _readyNodes.add(node);
    }

    private void moveNodeToCarved(Point node) {
        _readyNodes.remove(node);
        _carvedNodes.add(node);
    }
}
