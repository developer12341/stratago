package MVC;

import MVC.Stratagies.RandomizeStrategy;
import MVC.Stratagies.Strategy;
import MVC.controller.Controller;
import MVC.view.window;

public class ComputerPlayer {
    private String color;
    private MVC.view.window window;
    private Controller c;
    private Strategy strategy;
    public ComputerPlayer(window window, Controller c, String color) {
        strategy = new RandomizeStrategy();
        this.window = window;
        this.c = c;
        this.color = color;
    }


    public void movePiece() {
        c.movePiece(strategy.chooseMove(c.getMoves(color),
                c.getPieces(color),
                c.getOppositePieces(color)));

    }

    public void setPieces() {

    }
}
