package room.test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import map.Map;
import quad.Quad;
import quad.QuadType;
import room.Room;

class RoomTest {
    private Map _map;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        _map = new Map(10, 10);

        for (int y = 0; y < _map.height; y++)
            for (int x = 0; x < _map.width; x++)
                _map.setType(x, y, QuadType.FLOOR);
    }

    @AfterEach
    void tearDown() throws Exception {
        _map = null;
    }

    /**
     * Room (int width, int height, Map map)
     * 应当获取从 (0,0) 到 (x,y) 的所有地块
     */
    @Test
    final void roomIntIntMap_Normal() {
        Room room = new Room(6, 6, _map);

        for (int x = 0; x < room.width; x++)
            for (int y = 0; y < room.height; y++)
                assertEquals(_map.getQuad(x, y), room.getQuads()[x][y]);
    }

    /**
     * Room (int width, int height, Map map)
     * 超出地图，应当抛出越界异常
     */
    @Test
    final void roomIntIntMap_OutOfBounds() {
        try {
            Room room = new Room(12, 12, _map);
            fail();
            System.out.println(room); // 这行用来消去警告
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    final void roomIntIntIntIntMap_Normal() {
        Room room = new Room(2, 2, 6, 6, _map);

        for (int x = 0; x < room.width; x++)
            for (int y = 0; y < room.height; y++)
                assertEquals(_map.getQuad(room.x + x, room.y + y), room.getQuads()[x][y]);
    }

    @Test
    final void roomIntIntIntIntMap_OutOfBounds() {
        try {
            Room room = new Room(5, 5, 6, 6, _map);
            fail();
            System.out.println(room); // 这行用来消去警告
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    final void getQuads_Normal() {
        Room room = new Room(6, 6, _map);

        for (int x = 0; x < room.width; x++)
            for (int y = 0; y < room.height; y++)
                assertEquals(_map.getQuad(x, y), room.getQuads()[x][y]);
    }

    @Test
    final void getQuadArray_Normal() {
        Room room = new Room(6, 6, _map);

        ArrayList<Quad> roomQuads = new ArrayList<Quad>(Arrays.asList(room.getQuadArray()));
        ArrayList<Quad> mapToRoomQuads = new ArrayList<Quad>();
        for (int x = 0; x < room.width; x++)
            for (int y = 0; y < room.height; y++)
                mapToRoomQuads.add(_map.getQuad(x, y));

        for (Quad quad : mapToRoomQuads)
            if (!roomQuads.contains(quad))
                fail();

        for (Quad quad : roomQuads)
            if (!mapToRoomQuads.contains(quad))
                fail();
    }

    /**
     * 获取顶部一行的地块，顶部的定义是 “y坐标最大”
     */
    @Test
    final void getTopQuads_Normal() {
        Room room = new Room(2, 2, 4, 6, _map);

        ArrayList<Quad> mapQuads = new ArrayList<Quad>();

        for (int x = room.x; x < room.x + room.width; x++)
            mapQuads.add(_map.getQuad(x, room.y + room.height - 1));

        ArrayList<Quad> roomTopQuads = new ArrayList<Quad>(Arrays.asList(room.getTopQuads()));

        for (Quad quad : mapQuads)
            if (!roomTopQuads.contains(quad))
                fail();

        for (Quad quad : roomTopQuads)
            if (!mapQuads.contains(quad))
                fail();
    }

    /**
     * 获取底部一行的地块，底部的定义是 “y坐标最小”
     */
    @Test
    final void getBottomQuads_Normal() {
        Room room = new Room(2, 2, 4, 6, _map);

        ArrayList<Quad> mapQuads = new ArrayList<Quad>();

        for (int x = room.x; x < room.x + room.width; x++)
            mapQuads.add(_map.getQuad(x, room.y));

        ArrayList<Quad> roomTopQuads = new ArrayList<Quad>(Arrays.asList(room.getBottomQuads()));

        for (Quad quad : mapQuads)
            if (!roomTopQuads.contains(quad))
                fail();

        for (Quad quad : roomTopQuads)
            if (!mapQuads.contains(quad))
                fail();
    }

    @Test
    final void getLeftQuads_Normal() {
        Room room = new Room(2, 2, 4, 6, _map);

        ArrayList<Quad> mapQuads = new ArrayList<Quad>();

        for (int y = room.y; y < room.y + room.height; y++)
            mapQuads.add(_map.getQuad(room.x, y));

        ArrayList<Quad> roomTopQuads = new ArrayList<Quad>(Arrays.asList(room.getLeftQuads()));

        for (Quad quad : mapQuads)
            if (!roomTopQuads.contains(quad))
                fail();

        for (Quad quad : roomTopQuads)
            if (!mapQuads.contains(quad))
                fail();
    }

    @Test
    final void getRightQuads_Normal() {
        Room room = new Room(2, 2, 4, 6, _map);

        ArrayList<Quad> mapQuads = new ArrayList<Quad>();

        for (int y = room.y; y < room.y + room.height; y++)
            mapQuads.add(_map.getQuad(room.x + room.width - 1, y));

        ArrayList<Quad> roomTopQuads = new ArrayList<Quad>(Arrays.asList(room.getRightQuads()));

        for (Quad quad : mapQuads)
            if (!roomTopQuads.contains(quad))
                fail();

        for (Quad quad : roomTopQuads)
            if (!mapQuads.contains(quad))
                fail();
    }

    /**
     * 获取用于检测重叠的扩展后的 Rectangle，这个 Rectangel 需要对原房间边缘 +1 的范围检测为包含
     */
    @Test
    final void getExpandedRectangle_Normal() {
        Room room = new Room(2, 2, 6, 4, _map);

        Rectangle expandedRectangle = room.getExpandedRectangle();

        assertEquals(true, expandedRectangle.contains(new Point(1, 1)));
        assertEquals(true, expandedRectangle.contains(new Point(1, 6)));
        assertEquals(true, expandedRectangle.contains(new Point(8, 1)));
        assertEquals(true, expandedRectangle.contains(new Point(8, 6)));

        assertEquals(false, expandedRectangle.contains(new Point(0, 0)));
        assertEquals(false, expandedRectangle.contains(new Point(0, 7)));
        assertEquals(false, expandedRectangle.contains(new Point(9, 0)));
        assertEquals(false, expandedRectangle.contains(new Point(9, 7)));
    }
}
