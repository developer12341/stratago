package MVC.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void isEmpty() {
    }

    @Test
    void iterator() {
        p.addMove(Point.create(0,0), Point.create(0,1));
        for(Move move : p){
            System.out.println(move);
        }

    }
}