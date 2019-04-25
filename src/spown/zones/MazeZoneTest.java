package spown.zones;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import map.maze.Maze;
import map.quad.Quad;
import map.quad.QuadType;

class MazeZoneTest {
    private Maze _maze;
    private MazeZone _mazeZone;
    @BeforeEach
    void setUp() throws Exception {
        _maze = new Maze();
        _mazeZone = new MazeZone(_maze);
    }

    @AfterEach
    void tearDown() throws Exception {
        _maze = null;
        _mazeZone = null;
    }

    @Test
    final void mazeZone_Normal() {
        assertEquals(_maze, _mazeZone.maze());
    }

    @Test
    final void mazeZone_Null() {
        try {
            new MazeZone(null);
            fail();
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    final void maze() {
        assertEquals(_maze, _mazeZone.maze());
    }

    @Test
    final void addQuad_NoContain() {
        Quad quad = new Quad(QuadType.FLOOR);
        boolean result = _mazeZone.addQuad(quad);

        assertEquals(true, _maze.contains(quad));
        assertEquals(true, result);
    }

    @Test
    final void addQuad_Contain() {
        Quad quad = new Quad(QuadType.FLOOR);
        _mazeZone.addQuad(quad);
        boolean result = _mazeZone.addQuad(quad);

        assertEquals(true, _maze.contains(quad));
        assertEquals(false, result);
    }

    @Test
    final void getQuads() {
        Quad quad = new Quad(QuadType.FLOOR);
        _mazeZone.addQuad(quad);

        assertEquals(_maze.getQuads()[0], quad);
    }

    @Test
    final void contains_Contain() {
        Quad quad = new Quad(QuadType.FLOOR);
        _mazeZone.addQuad(quad);

        assertEquals(true, _mazeZone.contains(quad));
    }

    @Test
    final void contains_NoContain() {
        Quad quadA = new Quad(QuadType.FLOOR);
        Quad quadB = new Quad(QuadType.FLOOR);
        _mazeZone.addQuad(quadA);

        assertEquals(false, _mazeZone.contains(quadB));
    }
}
