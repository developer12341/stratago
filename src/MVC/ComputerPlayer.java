package MVC;

import MVC.Stratagies.ABOptimized;
import MVC.Stratagies.AlphaBeta;
import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.Stratagies.Strategy;
import MVC.controller.Controller;
import MVC.model.Attack;
import MVC.model.Move;

/**
 * this is the computer player. in here we manage the pieces the computer thinks the player
 * has
 */
public class ComputerPlayer {

    private final SpeculationBoard speculationBoard;
    private String color;
    private Controller c;
    private Strategy strategy;

    /**
     * @param c the controller
     * @param color the color of the computer player.
     */
    public ComputerPlayer(Controller c, String color) {
        this.c = c;
        this.color = color;
        this.speculationBoard = new SpeculationBoard(c.getBoard(), color);
        strategy = new ABOptimized(speculationBoard, color);
    }


    /**
     * @param attack the attack the player made (S/he always runs first so there will always be an
     *               attack)
     * @return the attack the computer made on the human player
     */
    public Attack movePiece(Attack attack) {
        speculationBoard.otherPlayerMove(attack.getMove().getP1(), attack.getMove().getP2(), attack.getAttackingPiece());
        Move m = strategy.chooseMove();
        return c.movePiece(m.getP1(), m.getP2());
    }

    /**
     * when the human moves the human player receives more information, the defending piece.
     * the computer is the same in that regard, it receives new information on some moves it makes
     * @param attack the attack the computer made on the human.
     */
    public void moveComputerPiece(Attack attack) {
        speculationBoard.thisPlayerMove(attack);
    }
}
