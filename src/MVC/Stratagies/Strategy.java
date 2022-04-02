package MVC.Stratagies;

import MVC.model.Move;
import MVC.model.Piece;

public interface Strategy {

    Move chooseMove();

    void HumanMove(Move humanPlayerMove, Piece attackingPiece);
}
