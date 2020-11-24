package api;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    private node_data n1,n2;
    @BeforeEach
    void setUp(){
        n1=new Node(1,new Location(2,3,4),7,"",0);
        n2=new Node(2,new Location(5,6,7),7,"",0);
    }
    @Test
    void getKey() {

    }

    @Test
    void getLocation() {
        assertEquals(new Location(2,3,4),n1.getLocation());
    }

    @Test
    void setLocation() {
        n1.setLocation(new Location(5.0,6.0,7.0));
        assertEquals(n2.getLocation(),n1.getLocation());
        System.out.println(n1.toString());
    }

    @Test
    void getWeight() {
    }

    @Test
    void setWeight() {
    }

    @Test
    void getInfo() {
    }

    @Test
    void setInfo() {
    }

    @Test
    void getTag() {
    }

    @Test
    void setTag() {
    }
}