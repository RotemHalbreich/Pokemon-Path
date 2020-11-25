package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
    private directed_weighted_graph g1, g2;

    @BeforeEach
    void setUp() {
        node_data n;
        geo_location location;
        g1 = new DWGraph_DS();
        g2 = new DWGraph_DS();

        //g1 graph:
        for (int i = 0; i < 5; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            g1.addNode(n);
        }
        for (int i = 1; i < 5; i++) {
            g1.connect(i - 1, i, i * 10);
            g1.connect(i, i - 1, i * 10);
        }

        //g2 graph:
        for (int i = 0; i < 7; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            g2.addNode(n);
        }
        for (int i = 1; i < 7; i++) {
            g2.connect(i - 1, i, i * 10);
        }
        g2.connect(1, 0, 1);
        g2.connect(3, 1, 1);
        g2.connect(5, 3, 1);
    }

    @Test
    void getNode() {
        for (int i = 0; i < 5; i++) assertNotNull(g1.getNode(i));
        assertThrows(RuntimeException.class, () -> {
            geo_location l = new Location(1, 5, 10);
            g1.addNode(new Node(1, l, 10, "", 0));
        });

        node_data n = new Node(0, new Location(0, 5, 0), 50, "", 0);
        assertEquals(n, g1.getNode(0));
    }

    @Test
    void getEdge() {
        for (int i = 1; i < 5; i++)
            assertEquals(new Edge(i - 1, i, i * 10), g1.getEdge(i - 1, i));

    }

    @Test
    void addNode() {
        DWGraph_DS TT1 = (DWGraph_DS) g1;
        System.out.println("g1:\n" + g1.toString());
        System.out.println("g1_reversed: \n" + TT1.toStringReverse());


        DWGraph_DS TT2 = (DWGraph_DS) g2;
        System.out.println("g2:\n" + g2.toString());
        System.out.println("g2_reversed: \n" + TT2.toStringReverse());
    }

    @Test
    void connect() {

    }

    @Test
    void getV() {
        assertEquals(5, g1.getV().size());

        assertEquals(7, g2.getV().size());
    }

    @Test
    void getE() {
        //g1:
        Collection<edge_data> g = g1.getE(4);
        g1.removeEdge(4, 3);
        assertTrue(g.isEmpty());
        g = g1.getE(0);
        assertEquals(1, g.size());

        //g2:
        Collection<edge_data> gg = g2.getE(6);
        g2.removeEdge(5, 6);
        assertTrue(gg.isEmpty());
        gg = g2.getE(6);
        assertNotEquals(1, gg.size());
        assertEquals(0, gg.size());
    }

    @Test
    void removeNode() {
        //g1 test:
        DWGraph_DS TT1 = (DWGraph_DS) g1;
        System.out.println("g1:\n" + g1.toString());
        System.out.println("g1 reversed: \n" + TT1.toStringReverse());
        g1.removeNode(2);
        System.out.println("g1:\n" + g1.toString());
        System.out.println("g1 reversed: \n" + TT1.toStringReverse());

        //g2 test:
        DWGraph_DS TT2 = (DWGraph_DS) g2;
        System.out.println("g2:\n" + g2.toString());
        System.out.println("g2 reversed: \n" + TT2.toStringReverse());
        g2.removeNode(3);
        System.out.println("g2:\n" + g2.toString());
        System.out.println("g2 reversed: \n" + TT2.toStringReverse());

    }

    @Test
    void removeEdge() {
        //g1:
        DWGraph_DS TT1 = (DWGraph_DS) g1;
        System.out.println("g1:\n" + g1.toString());
        System.out.println("g1 reversed: \n" + TT1.toStringReverse());
        assertNotNull(g1.getEdge(0, 1));
        assertEquals(new Edge(0, 1, 10), g1.removeEdge(0, 1));
        assertNull(g1.getEdge(0, 1));

        System.out.println("g1:\n" + g1.toString());
        System.out.println("g1 reversed: \n" + TT1.toStringReverse());

        //g2:
        DWGraph_DS TT2 = (DWGraph_DS) g2;
        System.out.println("g2:\n" + g2.toString());
        System.out.println("g2 reversed: \n" + TT2.toStringReverse());
        assertNotNull(g2.getEdge(5, 6));
        assertEquals(new Edge(3, 4, 40.0), g2.removeEdge(3, 4));
        assertNull(g2.getEdge(3, 4));

        System.out.println("g2:\n" + g2.toString());
        System.out.println("g2 reversed: \n" + TT2.toStringReverse());
    }

    @Test
    void nodeSize() {
        assertEquals(7, g2.nodeSize());
        g2.removeNode(3);
        assertEquals(6, g2.nodeSize());

        assertEquals(5, g1.nodeSize());

    }

    @Test
    void edgeSize() {
        assertEquals(9, g2.edgeSize());
        System.out.println("g2: \n" + g2.toString());
        g2.removeNode(3);
        System.out.println("g2: \n" + g2.toString());
        assertEquals(5, g2.edgeSize());
        g2.removeNode(1);
        System.out.println("g2: \n" + g2.toString());
        assertEquals(2, g2.edgeSize());

        assertEquals(8, g1.edgeSize());
        g1.removeNode(3);
        System.out.println("g1: \n" + g1.toString());
        assertEquals(4, g1.edgeSize());
        g1.removeNode(1);
        System.out.println("g1: \n" + g1.toString());
        assertEquals(0, g1.edgeSize());
    }

    @Test
    void getMC() {
        assertEquals(13, g1.getMC());
        g1.removeNode(2);
        System.out.println("g1: \n" + g1.toString());
        assertEquals(13 + 5, g1.getMC());

        assertEquals(16, g2.getMC());
        g2.removeNode(3);
        System.out.println("g2: \n" + g2.toString());
        assertEquals(16 + 5, g2.getMC());
    }

    @Test
    void TestToString() {
        System.out.println(g1.toString());
        System.out.println(g2.toString());
    }
}