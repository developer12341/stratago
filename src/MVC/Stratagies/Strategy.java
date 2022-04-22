package MVC.Stratagies;

import MVC.model.Move;

public interface Strategy {

    /**
     * this function calculate the base move to take.
     * @return the best move.
     */
    Move chooseMove();
}
