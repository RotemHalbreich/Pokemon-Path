package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    private dw_graph_algorithms test, test1;

    @BeforeEach
    void setUp() {
        directed_weighted_graph g = new DWGraph_DS();
        directed_weighted_graph g1 = new DWGraph_DS();
        node_data n;
        geo_location location;
        for (int i = 0; i < 7; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            g1.addNode(n);
        }
        for (int i = 0; i < 100001; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            g.addNode(n);
        }
        for (int i = 1; i <= 100000; i++) {
            g.connect(i - 1, i, i * 10);
            g.connect(i, i - 1, i * 10);
        }
        g1.connect(0, 1, 10);
        g1.connect(0, 3, 10);
        g1.connect(1, 4, 2);
        g1.connect(2, 0, 1);
        g1.connect(4, 2, 1);
        g1.connect(3, 4, 3);
        g1.connect(0, 1, 10);
        test1 = new DWGraph_Algo();
        test1.init(g1);
        test = new DWGraph_Algo();
        test.init(g);

    }

    @Test
    void init() {
        assertNotNull(test);
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
        directed_weighted_graph c = test.copy();
        assertEquals(c, test);
        c.removeNode(0);
        assertNotEquals(c, test);
        assertNotEquals(new DWGraph_Algo(), test);

    }

    @Test
    void isConnected() {
        assertTrue(test.isConnected());
        test.getGraph().removeEdge(0, 1);
        assertFalse(test.isConnected());
    }

    @Test
    void shortestPathDist() {
        assertEquals(12, test1.shortestPathDist(0, 4));
        assertEquals(-1, test1.shortestPathDist(0, 5));
        assertEquals(11, test1.shortestPathDist(2, 3));
        assertThrows(RuntimeException.class, () -> test1.shortestPathDist(0, 9));
    }

    @Test
    void shortestPath() {
        System.out.println(test1.shortestPath(0, 4).toString());
        System.out.println(test1.shortestPath(2, 3).toString());
        assertNull(test1.shortestPath(2, 5));
        assertThrows(RuntimeException.class, () -> test1.shortestPath(0, 9));
    }

    @Test
    void save() throws FileNotFoundException {

        test1.save("text.json");
        dw_graph_algorithms test2 = new DWGraph_Algo();
        test2.load("text.json");
        System.out.println(test2.toString());
        assertTrue(test1.equals(test2));
        assertTrue(test2.equals(test1));
        test2.getGraph().removeNode(1);
        assertNotEquals(test2,test1);
    }

    @Test
    void load() {
    }
}