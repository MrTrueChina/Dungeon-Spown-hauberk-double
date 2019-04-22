package spown.zones;

import map.quad.Quad;

public interface Zone {
    public Quad[] getQuads();
    public boolean contains(Quad quad);
}
