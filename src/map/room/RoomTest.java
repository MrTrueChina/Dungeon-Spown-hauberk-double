package map.room;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import map.Map;
import map.quad.QuadType;

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

    @Test
    final void roomIntIntMap_Normal() {
        Room room = new Room(6, 6, _map);

        for (int x = 0; x < room.width; x++)
            for (int y = 0; y < room.height; y++)
                assertEquals(_map.getQuad(x, y), room.getQuads()[x][y]);
    }

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
}
