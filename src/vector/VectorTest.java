package vector;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VectorTest {
    final static int X = 7;
    final static int Y = 5;

    Vector _vector;

    @BeforeEach
    void setUp() throws Exception {
        _vector = new Vector(X, Y);
    }

    @AfterEach
    void tearDown() throws Exception {
        _vector = null;
    }

    @Test
    final void add_Normal() {
        Vector vector = new Vector(1, 1);
        Vector expected = new Vector(X + 1, Y + 1);
        assertEquals(expected, _vector.add(vector));
    }

    @Test
    final void add_Null() {
        try {
            _vector.add(null);
            fail();
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    final void subtract_Normal() {
        Vector vector = new Vector(X - 1, Y - 1);
        Vector expected = new Vector(1, 1);
        assertEquals(expected, _vector.subtract(vector));
    }

    @Test
    final void subtract_Null() {
        try {
            _vector.subtract(null);
            fail();
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    final void multiply_Normal() {
        Vector expected = new Vector(X * 2, Y * 2);
        assertEquals(expected, _vector.multiply(2));
    }

    @Test
    final void multiply_Zero() {
        Vector expected = new Vector(0, 0);
        assertEquals(expected, _vector.multiply(0));
    }

    @Test
    final void equals_Same() {
        assertEquals(true, _vector.equals(_vector));
    }

    @Test
    final void equals_Equal() {
        Vector vector = new Vector(X, Y);
        assertEquals(true, _vector.equals(vector));
        assertEquals(true, vector.equals(_vector));
    }

    @Test
    final void equals_Inequal() {
        Vector vector = new Vector(X + 1, Y);
        assertEquals(false, _vector.equals(vector));
        assertEquals(false, vector.equals(_vector));
    }

    @Test
    final void equals_OtherClass() {
        assertEquals(false, _vector.equals(new Object()));
        assertEquals(false, new Object().equals(_vector));
    }

    @Test
    final void equals_Null() {
        assertEquals(false, _vector.equals(null));
    }
}
