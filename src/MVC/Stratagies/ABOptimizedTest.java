package MVC.Stratagies;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ABOptimizedTest {

    private ABOptimized abOptimized;
    @BeforeEach
    void setUp() {
        abOptimized = new ABOptimized(new SpeculationBoard(new Board(), "blue"),"blue");
    }

    @Test
    void chooseMove() {
        System.out.println(abOptimized.chooseMove());
    }
}