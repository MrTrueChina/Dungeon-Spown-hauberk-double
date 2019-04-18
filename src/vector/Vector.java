package vector;

import java.awt.Point;

@SuppressWarnings("serial")
public class Vector extends Point {
    public static final Vector UP = new Vector(0, 1);
    public static final Vector RIGHT = new Vector(1, 0);
    public static final Vector DOWN = new Vector(0, -1);
    public static final Vector LEFT = new Vector(-1, 0);

    public Vector() {
        super();
    }

    public Vector(int x, int y) {
        super(x, y);
    }
}
