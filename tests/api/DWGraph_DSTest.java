package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
    private directed_weighted_graph line, complex, complexT;

    @BeforeEach
    void setUp() {
        node_data n;
        geo_location location;
        line = new DWGraph_DS();
        complex = new DWGraph_DS();
        complexT = new DWGraph_DS();

        for (int i = 0; i < 5; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            line.addNode(n);
        }
        for (int i = 1; i < 5; i++) {
            line.connect(i - 1, i, i * 10);
            line.connect(i, i - 1, i * 10);
        }

        for (int i = 0; i < 7; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            complex.addNode(n);
        }
        for (int i = 1; i < 7; i++) {
            complex.connect(i - 1, i, i * 10);
        }
        complex.connect(1, 0, 1);
        complex.connect(3, 1, 1);
        complex.connect(5, 3, 1);

        for (int i = 0; i < 7; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            complexT.addNode(n);
        }
        complexT.connect(0, 1, 100);
        complexT.connect(1, 0, 150);
        complexT.connect(1, 3, 200);
        complexT.connect(3, 2, 250);
        complexT.connect(2, 1, 300);
        complexT.connect(3, 5, 350);
        complexT.connect(5, 4, 400);
        complexT.connect(4, 3, 450);
        complexT.connect(5, 6, 500);
    }

    @Test
    void getNode() {
        assertAll("Simple testing for method getNode() for DWGraph_DS class: \n",

                () -> {
                    complex.removeNode(0);
                    assertNull(complex.getNode(0));
                },
                () -> assertNotNull(line.getNode(0)),
                () -> assertEquals(50, line.getNode(0).getWeight()),
                () -> assertEquals(0, line.getNode(0).getKey()),
                () -> assertNull(line.getNode(10001))
        );
    }

    @Test
    void getEdge() {
        assertEquals(new Edge(0, 1, 100), complexT.getEdge(0, 1));
        assertEquals(10, line.getEdge(0, 1).getWeight());
        assertNotEquals(complex.getEdge(0, 1), complex.getEdge(1, 0));
    }

    @Test
    void addNode() {
        assertThrows(RuntimeException.class, () -> line.addNode(new Node(0, new Location(0, 0, 0), (5) * 10, "", 0)));
        for (int i = 100; i < 150; i++) {
            line.addNode(new Node(i, new Location(0, 0, 0)));
            assertNotNull(line.getNode(i));
        }
    }

    @Test
    void connect() {
        assertAll("Test for exceptions: \n",
                () -> assertThrows(RuntimeException.class, () -> complex.connect(1, 5, -10)),
                () -> assertThrows(RuntimeException.class, () -> complex.connect(-1, 5, 10)),
                () -> assertThrows(RuntimeException.class, () -> complex.connect(1, -5, 10))
        );
        complex.connect(6, 5, 10);
        assertNotNull(complex.getEdge(6, 5));
        complex.removeEdge(6, 5);
        assertNull(complex.getEdge(6, 5));

        DWGraph_DS complex1 = (DWGraph_DS) complex;
        edge_data e = new Edge(6, 5, 10);
        complex1.connect(e);
        assertEquals(e, complex1.getEdge(6, 5));
    }

    @Test
    void getV() {
        assertEquals(5, line.getV().size());
        assertEquals(7, complex.getV().size());
        assertEquals(line.nodeSize(), line.getV().size());
    }

    @Test
    void getE() {
        assertEquals(1, line.getE(0).size());
        assertTrue(line.getE(1000).isEmpty());

        Collection<edge_data> g = line.getE(4);
        line.removeEdge(4, 3);
        assertTrue(g.isEmpty());
        g = line.getE(0);
        assertEquals(1, g.size());

        Collection<edge_data> gg = complex.getE(6);
        complex.removeEdge(5, 6);
        assertTrue(gg.isEmpty());
        gg = complex.getE(6);
        assertNotEquals(1, gg.size());
        assertEquals(0, gg.size());
    }

    @Test
    void removeNode() {
        line.removeNode(0);
        assertNull(line.getNode(0));
        assertNull(line.getEdge(0, 1));
        DWGraph_DS TT1 = (DWGraph_DS) line;
        assertNotNull(line.getNode(2));
        assertNotNull(TT1.reverse().getNode(2));
        line.removeNode(2);
        for (int i = 1; i < 3; i++) {
            assertNull(line.getEdge(i, i + 1));
            assertNull(line.getEdge(i + 1, i));
            assertNull(TT1.reverse().getEdge(i, i + 1));
            assertNull(TT1.reverse().getEdge(i + 1, i));
        }

        DWGraph_DS TT2 = (DWGraph_DS) complex;
        assertEquals(complex.nodeSize(), TT2.reverse().nodeSize());
        complex.removeNode(3);
        assertEquals(complex.nodeSize(), TT2.reverse().nodeSize());
    }

    @Test
    void removeEdge() {

        DWGraph_DS T1 = (DWGraph_DS) line;
        assertAll("removeEdge in graph line testing for DWGraph_DS: \n",
                () -> assertNotNull(line.getEdge(0, 1)),
                () -> assertEquals(new Edge(0, 1, 10), line.removeEdge(0, 1)),
                () -> assertNull(line.getEdge(0, 1))
        );

        DWGraph_DS T2 = (DWGraph_DS) complex;
        assertAll("removeEdge in graph complex testing for DWGraph_DS: \n",
                () -> assertNotNull(complex.getEdge(5, 6)),
                () -> assertEquals(new Edge(3, 4, 40.0), complex.removeEdge(3, 4)),
                () -> assertNull(complex.getEdge(3, 4))
        );

        DWGraph_DS T3 = (DWGraph_DS) complexT;
        assertAll("removeEdge in graph complexT testing for DWGraph_DS: \n",
                () -> assertNotNull(complexT.getEdge(5, 6)),
                () -> assertEquals(new Edge(4, 3, 450.0), complexT.removeEdge(4, 3)),
                () -> assertNull(complexT.getEdge(3, 4)),
                () -> assertNull(complexT.getEdge(4, 3))
        );
    }

    @Test
    void nodeSize() {
        int n = line.nodeSize();
        DWGraph_DS T1 = (DWGraph_DS) line;
        DWGraph_DS T2 = (DWGraph_DS) complex;
        DWGraph_DS T3 = (DWGraph_DS) complexT;

        assertAll("nodeSize() test for all graphs: \n",

                () -> assertEquals(5, n),
                () -> assertEquals(5, T1.reverse().nodeSize()),
                () -> line.removeNode(0),
                () -> assertEquals(n - 1, line.nodeSize()),
                () -> assertEquals(n - 1, T1.reverse().nodeSize()),

                () -> assertEquals(7, complex.nodeSize()),
                () -> assertEquals(7, T2.reverse().nodeSize()),
                () -> complex.removeNode(3),
                () -> assertEquals(6, complex.nodeSize()),
                () -> assertEquals(6, T2.reverse().nodeSize()),

                () -> assertEquals(7, complexT.nodeSize()),
                () -> assertEquals(7, T3.reverse().nodeSize()),
                () -> complexT.removeNode(3),
                () -> assertEquals(6, complexT.nodeSize()),
                () -> assertEquals(6, T3.reverse().nodeSize())
        );
    }

    @Test
    void edgeSize() {
        assertAll("edgeSize() test on graph line: \n",
                () -> assertEquals(8, line.edgeSize()),
                () -> line.removeNode(3),
                () -> assertEquals(((DWGraph_DS) line).reverse().edgeSize(), line.edgeSize()),
                () -> assertEquals(4, line.edgeSize()),
                () -> line.removeNode(1),
                () -> assertEquals(0, line.edgeSize()));

        assertAll("edgeSize() test on graph complex: \n",
                () -> assertEquals(9, complex.edgeSize()),
                () -> complex.removeNode(3),
                () -> assertEquals(5, complex.edgeSize()),
                () -> complex.removeNode(1),
                () -> assertEquals(2, complex.edgeSize()));

        assertAll("edgeSize() test on graph complexT: \n",
                () -> assertEquals(9, complexT.edgeSize()),
                () -> complexT.removeNode(3),
                () -> assertEquals(5, complexT.edgeSize()),
                () -> complexT.removeNode(1),
                () -> assertEquals(2, complexT.edgeSize()));
    }

    @Test
    void getMC() {
        assertAll("getMC() test for graph line: \n",
                () -> assertEquals(13, line.getMC()),
                () -> line.removeNode(2),
                () -> assertEquals(13 + 5, line.getMC()));

        assertAll("getMC() test for graph complex: \n",
                () -> assertEquals(16, complex.getMC()),
                () -> complex.removeNode(3),
                () -> assertEquals(16 + 5, complex.getMC()));

        assertAll("getMC() test for graph complexT: \n",
                () -> assertEquals(16, complexT.getMC()),
                () -> complexT.removeNode(3),
                () -> assertEquals(16 + 5, complexT.getMC()));
    }

    @Test
    void TestToString() {
        System.out.println("line: \n" + line.toString());
        System.out.println("complex: \n" + complex.toString());
        System.out.println("complexT: \n" + complexT.toString());
    }

    @Test
    void TestEquals() {
        assertEquals(new DWGraph_DS(), new DWGraph_DS());
        node_data n;
        geo_location location;
        directed_weighted_graph gg1 = new DWGraph_DS();

        //line graph:
        for (int i = 0; i < 5; i++) {
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            gg1.addNode(n);
        }
        for (int i = 1; i < 5; i++) {
            gg1.connect(i - 1, i, i * 10);
            gg1.connect(i, i - 1, i * 10);
        }

        assertEquals(gg1, line);

        gg1.connect(0, 1, 1999);

        assertNotEquals(gg1, line);
        assertNotEquals(complex, line);
        gg1.removeNode(0);
        assertNotEquals(gg1, line);
        assertNotEquals(complex, complexT);
    }

    @Test
    void TestReverseString() {
        System.out.println("complex: \n" + complex.toString());
        DWGraph_DS gg = (DWGraph_DS) complex;
        System.out.println("Reversed complex: \n" + gg.reverseString());

        System.out.println("complexT :" + complexT.toString());
        DWGraph_DS ggg = (DWGraph_DS) complexT;
        System.out.println("Reversed complexT: \n" + ggg.reverseString());
    }

    @Test
    void TestReverse() {
        DWGraph_DS T2 = (DWGraph_DS) complex;
        DWGraph_DS T3 = (DWGraph_DS) complexT;
        directed_weighted_graph rComplex = ((DWGraph_DS) complex).reverse();
        directed_weighted_graph rComplexT = ((DWGraph_DS) complexT).reverse();

        assertAll("Reverse test for complex graph: \n",

                () -> assertEquals(((DWGraph_DS) complex).reverseString(), ((DWGraph_DS) complex).reverse().toString()),
                () -> rComplex.removeNode(1),
                () -> assertNotEquals(rComplex.toString(), ((DWGraph_DS) complex).reverseString())
        );

        assertAll("Reverse test for complexT graph: \n",

                () -> assertEquals(((DWGraph_DS) complexT).reverseString(), ((DWGraph_DS) complexT).reverse().toString()),
                () -> rComplexT.removeNode(1),
                () -> assertNotEquals(rComplexT.toString(), ((DWGraph_DS) complexT).reverseString())
        );
    }
}