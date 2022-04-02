package MVC.Stratagies;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class minmaxTest {
    minmax ab;
    @BeforeEach
    void setUp() {
        ab = new minmax(new SpeculationBoard(new Board(), "blue"),4,"blue");
    }

    @Test
    void humanMove() {
    }

    @Test
    void chooseMove() {
        System.out.println(new Board());
        System.out.println(ab.chooseMove());
    }
}