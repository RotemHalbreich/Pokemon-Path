package api;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    private geo_location l1, l2;

    @BeforeEach
    void setUp() {
        l1 = new Location(2, 3, 4);
        l2 = new Location(5, 6, 7);
    }

    @Test
    void testX() {
        assertAll(
                () -> assertEquals(2, l1.x()),
                () -> assertEquals(5, l2.x()),
                () -> assertNotEquals(2, l2.x())
        );
    }

    @Test
    void testY() {
        assertAll(
                () -> assertEquals(3, l1.y()),
                () -> assertEquals(6, l2.y()),
                () -> assertNotEquals(2, l2.y())
        );
    }

    @Test
    void testZ() {
        assertAll(
                () -> assertEquals(4, l1.z()),
                () -> assertEquals(7, l2.z()),
                () -> assertNotEquals(2, l2.z())
        );
    }

    @Test
    void testDistance() {
        assertEquals(Math.sqrt(27), l1.distance(l2));
        assertEquals(l2.distance(l1), l1.distance(l2));
    }
}