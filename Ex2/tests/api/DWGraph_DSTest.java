package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
    private directed_weighted_graph g1, g2, g3;

    @BeforeEach
    void setUp() {
        node_data n;
        geo_location location;
        g1 = new DWGraph_DS();
        g2 = new DWGraph_DS();
        g3 = new DWGraph_DS();

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

        //g3 graph:
        for (int i = 0; i < 7; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            g3.addNode(n);
        }
        g3.connect(0, 1, 100);
        g3.connect(1, 0, 150);
        g3.connect(1, 3, 200);
        g3.connect(3, 2, 250);
        g3.connect(2, 1, 300);
        g3.connect(3, 5, 350);
        g3.connect(5, 4, 400);
        g3.connect(4, 3, 450);
        g3.connect(5, 6, 500);
    }

    @Test
    void getNode() {


        assertAll("Simple testing for method getNode() for DWGraph_DS class: ",

                () -> {
                    String s = "shaked";
                    g2.removeNode(0);
                    assertNull(g2.getNode(0));
                    s = s.substring(0, s.length() - 2);
                    System.out.println();

                },
                () -> assertNotNull(g1.getNode(0)),
                () -> assertEquals(50, g1.getNode(0).getWeight()),
                () -> assertEquals(0, g1.getNode(0).getKey())
        );

    }

    @Test
    void getEdge() {
        assertEquals(10, g1.getEdge(0, 1).getWeight());
    }

    @Test
    void addNode() {
        assertThrows(RuntimeException.class, () -> g1.addNode(new Node(0, new Location(0, 0, 0), (5) * 10, "", 0)));
        g1.addNode(new Node(110, new Location(0, 0, 0), (5) * 10, "", 0));
        assertNotNull(g1.getNode(110));

        DWGraph_DS TT1 = (DWGraph_DS) g1;
        System.out.println("g1:\n" + g1.toString());
        System.out.println("g1_reversed: \n" + TT1.reverseString());

        DWGraph_DS TT2 = (DWGraph_DS) g2;
        System.out.println("g2:\n" + g2.toString());
        System.out.println("g2_reversed: \n" + TT2.reverseString());
    }

    @Test
    void connect() {
        g1.addNode(new Node(110, new Location(0, 0, 0), (5) * 10, "", 0));
        g1.connect(0, 110, 50);
        assertEquals(50, g1.getEdge(0, 110).getWeight());
        g1.connect(0, 110, 55);
        assertEquals(55, g1.getEdge(0, 110).getWeight());

        g3.addNode(new Node(10, new Location(4, 5, 6), (5) * 10, "", 0));
        g3.connect(10, 2, 123);
        g3.connect(10, 4, 321);
        assertEquals(321, g3.getEdge(10, 4).getWeight());
    }

    @Test
    void getV() {
        assertEquals(5, g1.getV().size());
        assertEquals(7, g2.getV().size());
    }

    @Test
    void getE() {
        assertEquals(1, g1.getE(0).size());
        assertTrue(g1.getE(1000).isEmpty());

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
        g1.removeNode(0);
        assertNull(g1.getNode(0));
        assertNull(g1.getEdge(0, 1));

        DWGraph_DS TT1 = (DWGraph_DS) g1;
        assertAll("removeNode() on graph g1 tests: ",
                () -> {
                    System.out.println("g1:\n" + g1.toString());
                    System.out.println("g1 reversed: \n" + TT1.reverseString());
                    g1.removeNode(2);
                    System.out.println("g1:\n" + g1.toString());
                    System.out.println("g1 reversed: \n" + TT1.reverseString());
                });

        DWGraph_DS TT2 = (DWGraph_DS) g2;
        assertAll("removeNode() on graph g2 tests: ",
                () -> {
                    System.out.println("g2:\n" + g2.toString());
                    System.out.println("g2 reversed: \n" + TT2.reverseString());
                    g2.removeNode(3);
                    System.out.println("g2:\n" + g2.toString());
                    System.out.println("g2 reversed: \n" + TT2.reverseString());
                });
    }

    @Test
    void removeEdge() {

        DWGraph_DS TT1 = (DWGraph_DS) g1;
        assertAll("removeEdge in graph g1 testing for DWGraph_DS: ",
                () -> {
                    System.out.println("g1:\n" + g1.toString());
                    System.out.println("g1 reversed: \n" + TT1.reverseString());
                },
                () -> assertNotNull(g1.getEdge(0, 1)),
                () -> assertEquals(new Edge(0, 1, 10), g1.removeEdge(0, 1)),
                () -> assertNull(g1.getEdge(0, 1)),
                () -> {
                    System.out.println("g1:\n" + g1.toString());
                    System.out.println("g1 reversed: \n" + TT1.reverseString());
                });

        DWGraph_DS TT2 = (DWGraph_DS) g2;
        assertAll("removeEdge in graph g1 testing for DWGraph_DS: ",
                () -> {
                    System.out.println("g2:\n" + g2.toString());
                    System.out.println("g2 reversed: \n" + TT2.reverseString());
                },
                () -> assertNotNull(g2.getEdge(5, 6)),
                () -> assertEquals(new Edge(3, 4, 40.0), g2.removeEdge(3, 4)),
                () -> assertNull(g2.getEdge(3, 4)),
                () -> {
                    System.out.println("g2:\n" + g2.toString());
                    System.out.println("g2 reversed: \n" + TT2.reverseString());
                }
        );


    }

    @Test
    void nodeSize() {
        int n = g1.nodeSize();
        assertAll("nodeSize() test for all graphs: ",
                //g1:
                () -> assertEquals(5, n),
                () -> g1.removeNode(0),
                () -> assertEquals(n - 1, g1.nodeSize()),
                //g2:
                () -> assertEquals(7, g2.nodeSize()),
                () -> g2.removeNode(3),
                () -> assertEquals(6, g2.nodeSize()),
                //g3:
                () -> assertEquals(7, g3.nodeSize()),
                () -> g3.removeNode(3),
                () -> assertEquals(6, g3.nodeSize()));

    }

    @Test
    void edgeSize() {
        assertAll("edgeSize() test on graph g1: ",
                () -> assertEquals(9, g2.edgeSize()),
                () -> {
                    System.out.println("g2: \n" + g2.toString());
                    g2.removeNode(3);
                    System.out.println("g2: \n" + g2.toString());
                },
                () -> assertEquals(5, g2.edgeSize()),
                () -> {
                    g2.removeNode(1);
                    System.out.println("g2: \n" + g2.toString());
                },
                () -> assertEquals(2, g2.edgeSize()));

        assertAll("edgeSize() test on graph g2: ",
                () -> assertEquals(8, g1.edgeSize()),
                () -> {
                    g1.removeNode(3);
                    System.out.println("g1: \n" + g1.toString());
                },
                () -> assertEquals(4, g1.edgeSize()),
                () -> {
                    g1.removeNode(1);
                    System.out.println("g1: \n" + g1.toString());
                },
                () -> assertEquals(0, g1.edgeSize()));
    }

//        int e = g1.edgeSize();
//        g1.removeEdge(0, 1);
//        assertEquals(e - 1, g1.edgeSize());
//        e = g1.edgeSize();
//        System.out.println(g1.toString());
//        g1.removeNode(2);
//        System.out.println(g1.toString());
//        assertEquals(e - 4, g1.edgeSize());


    @Test
    void getMC() {
        assertAll("getMC() test for graph g1: ",
                () -> assertEquals(13, g1.getMC()),  //<<<<<<<<<<<<<<< FIXED in addNode()
                () -> {
                    g1.removeNode(2);
                    System.out.println("g1: \n" + g1.toString());
                },
                () -> assertEquals(13 + 5, g1.getMC()));

        assertAll("getMC() test for graph g2: ",
                () -> assertEquals(16, g2.getMC()),
                () -> {
                    g2.removeNode(3);
                    System.out.println("g2: \n" + g2.toString());
                },
                () -> assertEquals(16 + 5, g2.getMC()));
    }

    @Test
    void TestToString() {
        System.out.println(g1.toString());
        System.out.println(g2.toString());
    }

    @Test
    void TestEquals() {
        assertEquals(new DWGraph_DS(), new DWGraph_DS());
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
        assertEquals(gg1, g1);
        assertNotEquals(g2, g1);
        gg1.removeNode(0);
        assertNotEquals(gg1, g1);
        assertNotEquals(g2, g3);
    }

    @Test
    void TestReverseString() {
        System.out.println("g2 :" + g2.toString());
        DWGraph_DS gg = (DWGraph_DS) g2;
        System.out.println("Reversed g2 :" + gg.reverseString());

        System.out.println("g3 :" + g3.toString());
        DWGraph_DS ggg = (DWGraph_DS) g3;
        System.out.println("Reversed g3 :" + ggg.reverseString());
    }

    @Test
    void TestReverse() {
//        DWGraph_DS TT2 = (DWGraph_DS) g2;
//        assertEquals(((DWGraph_DS) g2).reverseString(), ((DWGraph_DS) g2).reverse().toString());
//        directed_weighted_graph r = ((DWGraph_DS) g2).reverse();
//        r.removeNode(1);
//        assertNotEquals(r.toString(), ((DWGraph_DS) g2).reverseString());
    }
}