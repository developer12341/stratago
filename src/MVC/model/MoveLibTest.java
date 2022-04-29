package MVC.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveLibTest {

    int move;

    @BeforeEach
    void setUp() {
        move = MoveLib.pack(5,4,3,6);
        System.out.println(Integer.toBinaryString(move));
    }

    @Test
    void pack() {
    }

    @Test
    void unpackRowFrom() {
        assertEquals(5, MoveLib.unpackRowFrom(move));
    }

    @Test
    void unpackColFrom() {
        assertEquals(4, MoveLib.unpackColFrom(move));
    }

    @Test
    void unpackRowTo() {
        assertEquals(3, MoveLib.unpackRowTo(move));
    }

    @Test
    void unpackColTo() {
        assertEquals(6, MoveLib.unpackColTo(move));
    }

    @Test
    void getFrom() {
        assertEquals("(5, 4)", MoveLib.getFrom(move));
    }

    @Test
    void convertToMove() {
        assertEquals(Move.create(5,4,3,6), MoveLib.convertToMove(move));
    }
}