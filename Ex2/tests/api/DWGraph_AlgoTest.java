package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    private dw_graph_algorithms empty, single, massive, disconnected, complex, complexT;

    @BeforeEach
    void setUp() {
        directed_weighted_graph g_massive = new DWGraph_DS();
        directed_weighted_graph g_disconnected = new DWGraph_DS();
        directed_weighted_graph g_complex = new DWGraph_DS();
        directed_weighted_graph g_complexT = new DWGraph_DS();
        node_data n;
        geo_location location;

        for (int i = 0; i < 100001; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            g_massive.addNode(n);
        }
        for (int i = 1; i <= 100000; i++) {
            g_massive.connect(i - 1, i, i * 10);
            g_massive.connect(i, i - 1, i * 10);
        }

        for (int i = 0; i < 7; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            g_disconnected.addNode(n);
            g_complex.addNode(n);
            g_complexT.addNode(n);
        }

        g_disconnected.connect(0, 1, 10);
        g_disconnected.connect(0, 3, 10);
        g_disconnected.connect(1, 4, 2);
        g_disconnected.connect(2, 0, 1);
        g_disconnected.connect(4, 2, 1);
        g_disconnected.connect(3, 4, 3);
        g_disconnected.connect(0, 1, 10);

        g_complexT.connect(0, 1, 100);
        g_complexT.connect(1, 0, 150);
        g_complexT.connect(1, 3, 200);
        g_complexT.connect(3, 2, 250);
        g_complexT.connect(2, 1, 300);
        g_complexT.connect(3, 5, 350);
        g_complexT.connect(5, 4, 400);
        g_complexT.connect(4, 3, 450);
        g_complexT.connect(5, 6, 500);

        for (int i = 1; i < 7; i++) {
            g_complex.connect(i - 1, i, i * 10);
        }
        g_complex.connect(1, 0, 1);
        g_complex.connect(3, 1, 1);
        g_complex.connect(5, 3, 1);

        massive = new DWGraph_Algo();
        massive.init(g_massive);

        disconnected = new DWGraph_Algo();
        disconnected.init(g_disconnected);

        complex = new DWGraph_Algo();
        complex.init(g_complex);

        complexT = new DWGraph_Algo();
        complexT.init(g_complexT);

        complexT = new DWGraph_Algo();
        complexT.init(g_complexT);

        complexT = new DWGraph_Algo();
        complexT.init(g_complexT);
    }

    @Test
    void init() {
        assertNotNull(massive);
        assertNotNull(disconnected);
        assertNotNull(complex);
        assertNotNull(complexT);
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
        directed_weighted_graph c = massive.copy();
        assertEquals(c, massive);
        c.removeNode(0);
        assertNotEquals(c, massive);
        assertNotEquals(new DWGraph_Algo(), massive);
    }

    @Test
    void isConnected() {
        for (int i = 1; i < 10; i++) {
            assertTrue(massive.isConnected());
            directed_weighted_graph copy = massive.copy();
            copy.removeEdge(i - 1, i);
            dw_graph_algorithms test3 = new DWGraph_Algo();
            test3.init(copy);
            assertFalse(test3.isConnected());
        }
    }

    @Test
    void shortestPathDist() {
        int ans = 0;
        for (int i = 0; i <= 1000; i++) ans += i * 10;
        assertEquals(ans, massive.shortestPathDist(0, 1000));
        assertEquals(12, disconnected.shortestPathDist(0, 4));
        assertEquals(-1, disconnected.shortestPathDist(0, 5));
        assertEquals(11, disconnected.shortestPathDist(2, 3));
        assertThrows(RuntimeException.class, () -> disconnected.shortestPathDist(0, 9));
    }

    @Test
    void shortestPath() {
        assertEquals(10001, massive.shortestPath(0, 10000).size());
        System.out.println(disconnected.shortestPath(0, 4).toString());
        System.out.println(disconnected.shortestPath(2, 3).toString());
        assertNull(disconnected.shortestPath(2, 5));
        assertThrows(RuntimeException.class, () -> disconnected.shortestPath(0, 9));
    }

    @Test
    void save() throws FileNotFoundException {
        disconnected.save("text.json");
        dw_graph_algorithms test2 = new DWGraph_Algo();
        test2.load("text.json");
        System.out.println(test2.toString());
        assertTrue(disconnected.equals(test2));
        assertTrue(test2.equals(disconnected));
        test2.getGraph().removeNode(1);
        assertNotEquals(test2, disconnected);
    }

    @Test
    void load() throws FileNotFoundException {
        dw_graph_algorithms test2 = new DWGraph_Algo();
        test2.load("data/A0");
        System.out.println("A0: " + test2.toString());
        test2.load("data/A1");
        System.out.println("A1: " + test2.toString());
        test2.load("data/A2");
        System.out.println("A2: " + test2.toString());
        test2.load("data/A3");
        System.out.println("A3: " + test2.toString());
        test2.load("data/A4");
        System.out.println("A4: " + test2.toString());
        test2.load("data/A5");
        System.out.println("A5: " + test2.toString());
    }
}