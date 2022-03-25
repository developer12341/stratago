package MVC.Stratagies;

import MVC.model.Move;
import MVC.model.PossibleMoves;

public interface Strategy {

    Move chooseMove(PossibleMoves possibleMoves);

}
