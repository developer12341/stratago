package MVC.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PossibleMovesTest {
    private PossibleMoves p;

    @BeforeEach
    void setUp() {
        p = new PossibleMoves();
    }

    @Test
    void addMove() {
    }

    @Test
    void getMoves() {
    }

    @Test
    void testGetMoves() {
    }

    @Test
    void contains() {
    }

    @Test
    void testContains() {
    }

    @Test
    void clear() {
    }

    @Test
    void testClear() {
    }

    @Test
    void testToString() {
        p.addMove(Point.create(0,0), Point.create(0,1));
        p.addMove(Point.create(0,0), Point.create(0,2));
        p.addMove(Point.create(0,0), Point.create(0,9));
        p.addMove(Point.create(0,1), Point.create(0,2));

        System.out.println(p);
    }

    @Test
    void isEmpty() {
        assertTrue(p.isEmpty());
        p.addMove(Point.create(0,0), Point.create(0,1));
        assertFalse(p.isEmpty());
    }

    @Test
    void iterator() {
        p.addMove(Point.create(0,0), Point.create(0,1));
        int i = 1;
        for(Move move : p){
            i++;
            p.addMove(Point.create(0,0), Point.create(0,i));
            assertEquals(Move.create(0,0,0,1),move);
        }

    }

    @Test
    void size() {
        assertEquals(0,p.size());
        p.addMove(Point.create(0,0), Point.create(0,1));
        assertEquals(1,p.size());
        p.addMove(Point.create(0,0), Point.create(0,2));
        assertEquals(2,p.size());
        p.addMove(Point.create(2,2), Point.create(0,2));
        assertEquals(3,p.size());

    }

    @Test
    void testClone() {
        p.addMove(Point.create(2,2), Point.create(0,2));
        PossibleMoves p2 = p.clone();
        p.addMove(Point.create(0,0), Point.create(0,2));
        p.addMove(Point.create(2,2), Point.create(0,3));

        assertEquals(new ArrayList<>(Collections.singleton(Point.create(0, 2))),p2.getMoves(Point.create(2,2)));
    }
}