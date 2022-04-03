package MVC.Stratagies.mcts;

import MVC.Stratagies.AlphaBeta;
import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MCTSTest {

    private MCTS ab;
    Board b;

    @BeforeEach
    void setUp() {
        b = new Board();
        b.initPossibleMoves();
        ab = new MCTS(new SpeculationBoard(b, "blue"),"blue");
    }

    @Test
    void chooseMove() {
        System.out.println(b);
        System.out.println(ab.chooseMove());
    }
}