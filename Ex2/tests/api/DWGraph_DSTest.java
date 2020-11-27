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
        assertEquals(0,g1.getNode(0).getKey());
    }

    @Test
    void getEdge() {
        assertEquals(10,g1.getEdge(0,1).getWeight());
    }

    @Test
    void addNode() {
        assertThrows(RuntimeException.class,()->g1.addNode(new Node(0, new Location(0,0,0), ( 5) * 10, "", 0)));
        g1.addNode(new Node(110, new Location(0,0,0), ( 5) * 10, "", 0));
        assertNotNull(g1.getNode(110));
    }

    @Test
    void connect() {
        g1.addNode(new Node(110, new Location(0,0,0), ( 5) * 10, "", 0));
        g1.connect(0,110,50);
        assertEquals(50,g1.getEdge(0,110).getWeight());
        g1.connect(0,110,55);
        assertEquals(55,g1.getEdge(0,110).getWeight());
    }

    @Test
    void getV() {
        assertEquals(5,g1.getV().size());
    }

    @Test
    void getE() {
        assertEquals(1,g1.getE(0).size());
        assertTrue(g1.getE(1000).isEmpty());
    }

    @Test
    void removeNode() {
        g1.removeNode(0);
        assertNull(g1.getNode(0));
        assertNull(g1.getEdge(0,1));
    }

    @Test
    void removeEdge() {
        g1.removeEdge(0,1);
        assertNull(g1.getEdge(0,1));

    }

    @Test
    void nodeSize() {
        int n=g1.nodeSize();
        g1.removeNode(0);
        assertEquals(n-1,g1.nodeSize());

    }

    @Test
    void edgeSize() {
        int e=g1.edgeSize();
        g1.removeEdge(0,1);
        assertEquals(e-1,g1.edgeSize());
        e=g1.edgeSize();
        System.out.println(g1.toString());
        g1.removeNode(2);
        System.out.println(g1.toString());

        assertEquals(e-4,g1.edgeSize());
    }

    @Test
    void getMC() {

    }

    @Test
    void TestToString() {
        System.out.println(g1.toString());
    }
    @Test
    void TestEquals(){
        assertEquals(new DWGraph_DS(),new DWGraph_DS());
        node_data n;
        geo_location location;
       directed_weighted_graph gg1 = new DWGraph_DS();


        //g1 graph:
        for (int i = 0; i < 5; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            gg1.addNode(n);
        }
        for (int i = 1; i < 5; i++) {
            gg1.connect(i - 1, i, i * 10);
            gg1.connect(i, i - 1, i * 10);
        }
        assertEquals(gg1,g1);
        assertNotEquals(g2,g1);
        gg1.removeNode(0);
        assertNotEquals(gg1,g1);
    }
    @Test
    void TestReverseString(){
        System.out.println("g2 :"+g2.toString());
        DWGraph_DS gg=(DWGraph_DS)g2;
        System.out.println("R :"+gg.reverseString());
    }
    @Test
    void TestReverse(){
//        DWGraph_DS TT2 = (DWGraph_DS) g2;
//
//        directed_weighted_graph r=((DWGraph_DS) g2).reverse();
//        r.removeNode(1);
//        assertNotEquals(r.toString(),((DWGraph_DS) g2).toStringReverse());
    }
}