package maze;

import java.util.HashSet;

import quad.Quad;

public class Maze {
    HashSet<Quad> _quads = new HashSet<Quad>();

    public boolean addQuad(Quad quad) {
        return _quads.add(quad);
    }
    
    public boolean contains(Quad quad) {
        return _quads.contains(quad);
    }
}
