package MVC.Stratagies;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.Board;
import MVC.model.Move;
import MVC.model.Piece;

public class MCTS implements Strategy {

    private String color;
    private SpeculationBoard board;
    public MCTS(Board board, String color) {
        this.color = color;
        this.board = new SpeculationBoard(board, color);
    }

    //todo: implement.

    @Override
    public Move chooseMove() {
        //choose a move from board.getPossibleMoves() with monte carlo tree search




        return null;
    }

    @Override
    public void HumanMove(Move humanPlayerMove, Piece attackingPiece) {

    }
}
