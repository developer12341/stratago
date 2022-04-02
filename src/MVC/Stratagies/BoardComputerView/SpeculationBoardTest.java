package MVC.Stratagies.BoardComputerView;

import MVC.model.Board;
import MVC.model.Piece;
import MVC.model.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SpeculationBoardTest {

    private SpeculationBoard b;

    @BeforeEach
    void setUp() {
        b =new SpeculationBoard(new Board(),"blue");
    }

    @Test
    void otherPlayerMove() {
        PossiblePiece p1 = b.getOtherPieces()[6][3];
        b.otherPlayerMove(Point.create(6,3),Point.create(5,3), null);

        assertEquals(p1, b.getOtherPieces()[5][3]);
        assertNull(b.getOtherPieces()[6][3]);
    }

    @Test
    void getBoard() {
        Board b1 = b.getBoard();
        assertFalse(b1.isGameOver());
        System.out.println(Arrays.deepToString(b1.getPieces("red")));
        System.out.println(Arrays.deepToString(b1.getPieces("blue")));
        assertFalse(b1.isGameOver());
        assertEquals(Piece.FLAG, b1.getPiece(Point.create(3,6)));
        b1.moveTo(Point.create(6,6),Point.create(3,6));
        assertNotEquals(Piece.FLAG, b1.getPiece(Point.create(3,6)));
        System.out.println(b1.getPiece(Point.create(3,6)));
        assertTrue(b1.isGameOver());

    }
}