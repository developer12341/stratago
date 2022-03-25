package MVC.model.PointClasses;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    Point p1, p2, p3;

    @BeforeEach
    void setUp() {
        p1 = Point.create(1,2);
        p2 = Point.create(2,5);
        p3 = Point.create(1,2);
    }

    @org.junit.jupiter.api.Test
    void getRow() {
        assertEquals(p1.getRow(), 1);
        assertEquals(p2.getRow(), 2);
        assertEquals(p3.getRow(), 1);
    }

    @org.junit.jupiter.api.Test
    void getCol() {
        assertEquals(p1.getCol(), 2);
        assertEquals(p2.getCol(), 5);
        assertEquals(p3.getCol(), 2);
    }

    @org.junit.jupiter.api.Test
    void equalsTest() {
        System.out.println(p1 == p3);
        assertEquals(p1,p3);
    }

}