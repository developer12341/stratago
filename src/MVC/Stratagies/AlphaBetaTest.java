package MVC.Stratagies;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlphaBetaTest {
    AlphaBeta ab;
    @BeforeEach
    void setUp() {
        ab = new AlphaBeta(new SpeculationBoard(new Board(), "blue"),"blue");
    }

    @Test
    void humanMove() {
    }

    @Test
    void chooseMove() {
        System.out.println(ab.chooseMove());
    }
}