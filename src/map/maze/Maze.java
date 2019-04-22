package map.maze;

import java.util.HashSet;

import map.quad.Quad;

public class Maze {
    HashSet<Quad> _quads = new HashSet<Quad>();

    public boolean addQuad(Quad quad) {
        return _quads.add(quad);
    }
    
    public Quad[] getQuads() {
        return _quads.toArray(new Quad[0]);
    }
    
    public boolean contains(Quad quad) {
        return _quads.contains(quad);
    }
}
