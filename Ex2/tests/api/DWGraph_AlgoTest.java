package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    private dw_graph_algorithms test;
    @BeforeEach
    void ssetUp(){
        directed_weighted_graph g=new DWGraph_DS();
        node_data n;
        geo_location location;
        for(int i=0;i<100001;i++){
            location = new Location(i, i + 5, i * 10);
            n = new Node(i, location, (i + 5) * 10, "", 0);
            g.addNode(n);
        }
        for (int i = 1; i <= 100000; i++) {
            g.connect(i - 1, i, i * 10);
            g.connect(i, i - 1, i * 10);
        }
        test=new DWGraph_Algo();
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
        directed_weighted_graph c=test.copy();
        assertEquals(c,test);
        c.removeNode(0);
        assertNotEquals(c,test);
        assertNotEquals(new DWGraph_Algo(),test);

    }

    @Test
    void isConnected() {
        assertTrue(test.isConnected());
        test.getGraph().removeEdge(0,1);
        assertFalse(test.isConnected());
    }

    @Test
    void shortestPathDist() {
    }

    @Test
    void shortestPath() {
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }
}