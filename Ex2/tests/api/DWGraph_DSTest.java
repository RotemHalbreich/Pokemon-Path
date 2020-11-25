package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
    private directed_weighted_graph g1,g2;

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
            g1.connect(i,i-1,i*10);
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
        g2.connect(1,0,1);
        g2.connect(3,1,1);
        g2.connect(5,3,1);
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
    }

    @Test
    void getE() {
        Collection<edge_data> g = g1.getE(4);
        g1.removeEdge(4,3);
        assertTrue(g.isEmpty());
        g = g1.getE(0);
        assertEquals(1, g.size());


    }

    @Test
    void removeNode() {
        DWGraph_DS TT = (DWGraph_DS) g1;
        System.out.println(g1.toString());
        System.out.println("reverse: \n" + TT.toStringReverse());
        g1.removeNode(2);
        System.out.println("g1:\n" + g1.toString());
        System.out.println("reverse: \n" + TT.toStringReverse());

    }

    @Test
    void removeEdge() {
        DWGraph_DS TT = (DWGraph_DS) g1;
        System.out.println("g1:\n" + g1.toString());
        System.out.println("reverse: \n" + TT.toStringReverse());
        assertNotNull(g1.getEdge(0, 1));
        assertEquals(new Edge(0, 1, 10), g1.removeEdge(0, 1));
        assertNull(g1.getEdge(0, 1));

        System.out.println("g1:\n" + g1.toString());
        System.out.println("reverse: \n" + TT.toStringReverse());
    }

    @Test
    void nodeSize() {

    }

    @Test
    void edgeSize() {
    }

    @Test
    void getMC() {
    }

    @Test
    void TestToString() {
        System.out.println(g1.toString());
    }
}