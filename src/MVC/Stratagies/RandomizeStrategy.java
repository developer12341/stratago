package MVC.Stratagies;

import MVC.model.Board;
import MVC.model.Move;

/**
 * this is a class for debugging purposes it is here to create a "dumb" bot in order to test the logic
 * of the game.
 */
public class RandomizeStrategy implements Strategy{
    private Board board;
    private String color;

    public RandomizeStrategy(Board board, String color) {
        this.board = board;
        this.color = color;
    }


    @Override
    public Move chooseMove() {
        //choose a random move
        return board.getMoves(color).chooseRandomMove();
    }

}
