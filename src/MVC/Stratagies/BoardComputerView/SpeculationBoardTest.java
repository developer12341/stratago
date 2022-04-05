package MVC.Stratagies.BoardComputerView;

import MVC.model.Attack;
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
    void testSetup(){
        System.out.println(Arrays.deepToString(b.getOtherPieces()));
    }

    @Test
    void otherPlayerMove() {
        PossiblePiece p1 = b.getOtherPieces()[6][3];
        b.otherPlayerMove(Point.create(6,3),Point.create(5,3), null);

        assertEquals(p1, b.getOtherPieces()[5][3]);
        assertNull(b.getOtherPieces()[6][3]);
    }

    @Test
    void thisPlayerMove() {
        b.thisPlayerMove(new Attack(Point.create(4,4),Point.create(5,4),Piece.MINER,Piece.LIEUTENANT));
        System.out.println(b.getBoard());
    }
    @Test
    void getBoard() {
        Board b1 = b.getBoard();
        assertFalse(b1.isGameOver());
        System.out.println(b1);

    }
}