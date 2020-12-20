package api;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    private node_data n1, n2;
    private double EPS = 0.0001;

    @BeforeEach
    void setUp() {
        n1 = new Node(1, new Location(2, 3, 4), 7, "", 0);
        n2 = new Node(2, new Location(5, 6, 7), 7, "", 0);
    }

    @Test
    void getKey() {
        assertEquals(1, n1.getKey());
        assertNotEquals(2, n1.getKey());
    }

    @Test
    void getLocation() {
        assertEquals(new Location(2, 3, 4), n1.getLocation());
        geo_location loc = new Location(5, 6, 7);
        assertEquals(loc.distance(n1.getLocation()), n2.getLocation().distance(n1.getLocation()));
    }

    @Test
    void setLocation() {
        n1.setLocation(new Location(5.0, 6.0, 7.0));
        assertEquals(n2.getLocation(), n1.getLocation());
        assertEquals(n2.getLocation().distance(new Location(1, 1, 1)), n1.getLocation().distance(new Location(1, 1, 1)));
    }

    @Test
    void getWeight() {
        assertEquals(7, n2.getWeight(), EPS);
        n2.setWeight(101.23);
        assertEquals(101.23, n2.getWeight(), EPS);
    }

    @Test
    void setWeight() {
        n2.setWeight(100);
        assertEquals(100, n2.getWeight(), EPS);
    }

    @Test
    void getInfo() {
        assertEquals("", n1.getInfo());
    }

    @Test
    void setInfo() {
        n1.setInfo("green");
        assertEquals("green", n1.getInfo());
    }

    @Test
    void getTag() {
        assertEquals(0, n2.getTag());
    }

    @Test
    void setTag() {
        n2.setTag(1);
        assertEquals(1, n2.getTag());
    }

    @Test
    void TestToString() {
        String s = "Node{location=2.0,3.0,4.0, key=1, weight=7.0, info='', tag=0}";
        assertEquals(s, n1.toString());
    }

    @Test
    void TestEquals() {
        assertAll(
                () -> assertEquals(new Node(1, new Location(2, 3, 4), 7, "", 0), n1),
                () -> assertNotEquals(n2, n1),
                () -> {
                    node_data n3 = new Node(n1);
                    n1.setLocation(new Location(1, 1, 1));
                    assertNotEquals(n1, n3);
                }
        );
    }
}