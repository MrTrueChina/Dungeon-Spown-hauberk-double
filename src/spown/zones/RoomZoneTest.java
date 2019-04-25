package spown.zones;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import map.Map;
import map.quad.Quad;
import map.quad.QuadType;
import map.room.Room;
import mytest.Assertions;

class RoomZoneTest {
    private final int MAP_WIDTH = 16;
    private final int MAP_HEIGHT = 12;
    private final int ROOM_X = 4;
    private final int ROOM_Y = 2;
    private final int ROOM_WIDTH = 8;
    private final int ROOM_HEIGHT = 6;
    private final int DOORS_NUMBER = 1;

    Map _map;
    Room _room;
    RoomZone _roomZone;

    @BeforeEach
    void setUp() throws Exception {
        _map = new Map(MAP_WIDTH, MAP_HEIGHT);
        _room = new Room(ROOM_X, ROOM_Y, ROOM_WIDTH, ROOM_HEIGHT, _map);
        _roomZone = new RoomZone(_room, DOORS_NUMBER);
    }

    @AfterEach
    void tearDown() throws Exception {
        _map = null;
        _room = null;
        _roomZone = null;
    }

    @Test
    final void roomZone_Normal() {
        assertEquals(_room, _roomZone.room());
    }

    @Test
    final void roomZone_Null() {
        try {
            new RoomZone(null, 1);
            fail();
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    final void roomZone_Zero() {
        RoomZone room = new RoomZone(_room, 0);
        assertEquals(1, room.doorsNumber());
    }

    @Test
    final void roomZone_Negative() {
        RoomZone room = new RoomZone(_room, -1);
        assertEquals(1, room.doorsNumber());
    }

    @Test
    final void room_Normal() {
        assertEquals(_room, _roomZone.room());
    }

    @Test
    final void contains_Contains() {
        assertEquals(true, _roomZone.contains(_room.getQuad(1, 1)));
    }

    @Test
    final void contains_NoContains() {
        assertEquals(false, _roomZone.contains(new Quad(QuadType.FLOOR)));
    }

    @Test
    final void getQuads_Normal() {
        ArrayList<Quad> roomQuads = new ArrayList<Quad>();
        for (int x = 0; x < _room.width; x++)
            for (int y = 0; y < _room.height; y++)
                roomQuads.add(_room.getQuad(x, y));

        ArrayList<Quad> zoneQuads = new ArrayList<Quad>(Arrays.asList(_roomZone.getQuads()));

        Assertions.assertArrayListEquals(roomQuads, zoneQuads);
    }

    @Test
    final void getTopQuads_Normal() {
        ArrayList<Quad> roomTopQuads = new ArrayList<Quad>();
        for (int x = 0; x < _room.width; x++)
            roomTopQuads.add(_room.getQuad(x, _room.height - 1));

        ArrayList<Quad> zoneTopQuads = new ArrayList<Quad>(Arrays.asList(_roomZone.getTopQuads()));

        Assertions.assertArrayListEquals(roomTopQuads, zoneTopQuads);
    }

    @Test
    final void getRightQuads_Normal() {
        ArrayList<Quad> roomRightQuads = new ArrayList<Quad>();
        for (int y = 0; y < _room.height; y++)
            roomRightQuads.add(_room.getQuad(_room.width - 1, y));

        ArrayList<Quad> zoneRightQuads = new ArrayList<Quad>(Arrays.asList(_roomZone.getRightQuads()));

        Assertions.assertArrayListEquals(roomRightQuads, zoneRightQuads);
    }

    @Test
    final void getBottomQuads_Normal() {
        ArrayList<Quad> roomBottomQuads = new ArrayList<Quad>();
        for (int x = 0; x < _room.width; x++)
            roomBottomQuads.add(_room.getQuad(x, 0));

        ArrayList<Quad> zoneBottomQuads = new ArrayList<Quad>(Arrays.asList(_roomZone.getBottomQuads()));

        Assertions.assertArrayListEquals(roomBottomQuads, zoneBottomQuads);
    }

    @Test
    final void getLeftQuads_Normal() {
        ArrayList<Quad> roomLeftQuads = new ArrayList<Quad>();
        for (int y = 0; y < _room.height; y++)
            roomLeftQuads.add(_room.getQuad(0, y));

        ArrayList<Quad> zoneLeftQuads = new ArrayList<Quad>(Arrays.asList(_roomZone.getLeftQuads()));

        Assertions.assertArrayListEquals(roomLeftQuads, zoneLeftQuads);
    }

    @Test
    final void getMarginalQuads_Normal() {
        ArrayList<Quad> marginalQuads = new ArrayList<Quad>();
        marginalQuads.addAll(Arrays.asList(_room.getQuads()[0])); // 左侧一行
        marginalQuads.addAll(Arrays.asList(_room.getQuads()[_room.width - 1])); // 右侧一行
        for (int x = 1; x < _room.width - 1; x++)
            marginalQuads.add(_room.getQuad(x, 0)); // 底下一行去掉两角
        for (int x = 1; x < _room.width - 1; x++)
            marginalQuads.add(_room.getQuad(x, _room.height - 1)); // 顶部一行去掉两角

        Assertions.assertArrayListEquals(marginalQuads,
                new ArrayList<Quad>(Arrays.asList(_roomZone.getMarginalQuads())));
    }

    @Test
    final void nonadjacent_Nonadjacent() {
        Rectangle rectangle = new Rectangle(ROOM_X + ROOM_WIDTH + 1, ROOM_Y, ROOM_WIDTH, ROOM_HEIGHT);

        assertEquals(true, _roomZone.nonadjacent(rectangle));
    }

    @Test
    final void nonadjacent_Adjacent() {
        Rectangle rectangle = new Rectangle(ROOM_X + ROOM_WIDTH, ROOM_Y, ROOM_WIDTH, ROOM_HEIGHT);

        assertEquals(false, _roomZone.nonadjacent(rectangle));
    }

    @Test
    final void nonadjacent_Overlap() {
        Rectangle rectangle = new Rectangle(ROOM_X + ROOM_WIDTH / 2, ROOM_Y, ROOM_WIDTH, ROOM_HEIGHT);

        assertEquals(false, _roomZone.nonadjacent(rectangle));
    }

    @Test
    final void nonadjacent_Null() {
        try {
            _roomZone.nonadjacent(null);
            fail();
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail();
        }
    }
}
