package MVC;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.Stratagies.Strategy;
import MVC.Stratagies.mcts.MCTS;
import MVC.controller.Controller;
import MVC.model.Attack;
import MVC.model.Move;
import MVC.view.GUIManager;

public class ComputerPlayer {
    private final SpeculationBoard speculationBoard;
    private String color;
    private GUIManager window;
    private Controller c;
    private Strategy strategy;

    public ComputerPlayer(GUIManager window, Controller c, String color) {
//        strategy = new RandomizeStrategy();
        this.window = window;
        this.c = c;
        this.color = color;
        this.speculationBoard = new SpeculationBoard(c.getBoard(), color);
        strategy = new MCTS(speculationBoard, color);
    }


    public Attack movePiece(Attack attack) {
        speculationBoard.otherPlayerMove(attack.getMove().getP1(), attack.getMove().getP2(), attack.getAttackingPiece());
        Move m = strategy.chooseMove();
        return c.movePiece(m.getP1(), m.getP2());
    }

    public void setPieces() {

    }

    public void moveComputerPiece(Attack attack) {
        speculationBoard.thisPlayerMove(attack);
    }
}
