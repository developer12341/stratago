package MVC.Stratagies;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.Board;
import MVC.model.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ABOptimizedTest {

    private ABOptimized abOptimized;
    SpeculationBoard aaa;
    @BeforeEach
    void setUp() {
        aaa = new SpeculationBoard(new Board(), "blue");
        abOptimized = new ABOptimized(aaa,"blue");
    }

    @Test
    void chooseMove() {
        System.out.println(abOptimized.chooseMove());
    }

    void testMoves() {

    }


    //test the evaluation function
    @Test
    void evaluate() {
        Board b = new Board();
        assertEquals(0,abOptimized.func(b));

        while(!b.isGameOver()) {
            //select a random move
            List<Move> moves = b.getMoves(b.getPlayerTurn());
            Move move = moves.get((int) (Math.random() * moves.size()));
            b.moveTo(move.getP1(), move.getP2());
            System.out.println(b);
            System.out.println(abOptimized.func(b));
        }
    }

}