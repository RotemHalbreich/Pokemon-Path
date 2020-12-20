package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
    private edge_data e1, e2;

    @BeforeEach
    void setUp() {
        e1 = new Edge(1, 2, 1.1);
        e2 = new Edge(2, 4, 3.1);
    }

    @Test
    void getSrc() {
        assertAll(
                () -> assertEquals(1, e1.getSrc()),
                () -> assertEquals(2, e2.getSrc()),
                () -> assertNotEquals(1, e2.getSrc())
        );
    }

    @Test
    void getDest() {
        assertAll(
                () -> assertEquals(2, e1.getDest()),
                () -> assertEquals(4, e2.getDest()),
                () -> assertNotEquals(1, e2.getDest())
        );
    }

    @Test
    void getWeight() {
        assertAll(
                () -> assertEquals(1.1, e1.getWeight()),
                () -> assertEquals(3.1, e2.getWeight()),
                () -> assertNotEquals(1, e2.getWeight())
        );
    }

    @Test
    void getInfo() {
        assertEquals("", e1.getInfo());
    }

    @Test
    void setInfo() {
        e1.setInfo("white");
        assertEquals("white", e1.getInfo());
    }

    @Test
    void getTag() {
        assertEquals(0, e2.getTag());
    }

    @Test
    void setTag() {
        e1.setTag(1);
        assertEquals(1, e1.getTag());
    }

    @Test
    void testToString() {
        String s = "(1,2|1.1)";
        assertEquals(s, e1.toString());
    }

    @Test
    void TestEquals() {
        assertAll(
                () -> assertEquals(new Edge(1, 2, 3), new Edge(1, 2, 3)),
                () -> assertEquals(new Edge(1, 2, 1.1), e1),
                () -> assertNotEquals(e2, e1)
        );
    }
}