package MVC.Stratagies;

import MVC.model.Move;
import MVC.model.PointClasses.Point;
import MVC.model.PossibleMoves;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * this is a class for debugging purposes it is here to create a "dumb" bot in order to test the logic
 * of the game.
 */
public class RandomizeStrategy implements Strategy{


    @Override
    public Move chooseMove(PossibleMoves possibleMoves) {
        Random generator = new Random();
        int value = generator.nextInt(possibleMoves.getMoves().size());
        for (Map.Entry<Point, ArrayList<Point>> move : possibleMoves.getMoves().entrySet()) {
            if(value == 0) {
                ArrayList<Point> points = move.getValue();
                return new Move(move.getKey(), points.get(generator.nextInt(points.size())));
            }
            value--;
        }
        return null;
    }
}
