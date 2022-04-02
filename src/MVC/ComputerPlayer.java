package MVC;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.Stratagies.AlphaBeta;
import MVC.Stratagies.Strategy;
import MVC.controller.Controller;
import MVC.model.Move;
import MVC.model.Piece;
import MVC.view.window;

public class ComputerPlayer {
    private String color;
    private MVC.view.window window;
    private Controller c;
    private Strategy strategy;
    public ComputerPlayer(window window, Controller c, String color) {
//        strategy = new RandomizeStrategy();
        this.window = window;
        this.c = c;
        this.color = color;
        strategy = new AlphaBeta(new SpeculationBoard(c.getBoard(), color),4,color);
    }


    public void movePiece(Move humanPlayerMove, Piece attackingPiece) {
        strategy.HumanMove(humanPlayerMove, attackingPiece);
        Move m = strategy.chooseMove();
        c.movePiece(m.getP1(),m.getP2());

    }

    public void setPieces() {

    }
}
