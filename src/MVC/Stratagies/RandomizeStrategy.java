package MVC.Stratagies;

import MVC.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    private Move chooseMove(PossibleMoves possibleMoves) {
        Random generator = new Random();
        int value = generator.nextInt(possibleMoves.size());

        for(Move move: possibleMoves){
            if(value == 0)
                return move;
            value--;
        }

        return null;
    }


    @Override
    public Move chooseMove() {
        return chooseMove(board.getMoves(color));
    }

    @Override
    public void HumanMove(Move humanPlayerMove, Piece attackingPiece) {

    }
}
