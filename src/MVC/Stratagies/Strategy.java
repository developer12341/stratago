package MVC.Stratagies;

import MVC.model.Move;
import MVC.model.Piece;
import MVC.model.PossibleMoves;

public interface Strategy {

    Move chooseMove(PossibleMoves possibleMoves, Piece[][] myPieces, Piece[][] otherPieces);

}
