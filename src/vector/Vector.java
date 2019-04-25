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

    public Vector(Point point) {
        super(point);
    }
    
    public Vector(int x, int y) {
        super(x, y);
    }

    /**
     * 返回一个新的 Vector，它是此 Vector 和指定 Vector 相加的结果
     * 
     * @param vector
     * @return
     */
    public Vector add(Vector vector) {
        return new Vector(x + vector.x, y + vector.y);
    }

    /**
     * 返回一个新的 Vector，它是此 Vector 和指定 Vector 相减的结果
     * 
     * @param vector
     * @return
     */
    public Vector subtract(Vector vector) {
        return new Vector(x - vector.x, y - vector.y);
    }

    /**
     * 返回一个新的 Vector，他是此 Vector 和指定的数值相乘的结果
     * 
     * @param d
     * @return
     */
    public Vector multiply(double d) {
        return new Vector((int) (x * d), (int) (y * d));
    }

    /**
     * 返回一个新的 Vector，它是此 Vector 到 指定 Vector 的方向
     * 
     * @param target
     * @return
     */
    public Vector directionTo(Vector target) {
        return target.subtract(this);
    }
}
