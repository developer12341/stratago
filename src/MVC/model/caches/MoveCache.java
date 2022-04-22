package MVC.model.caches;

import MVC.model.Move;
import MVC.model.Point;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * this is a cache for the Move object. the reason I need a cache in here is that it is a
 * very used object. if I won't have a caching system there will be a lot of garbage collection that slows
 * the program down
 */
public class MoveCache {

    public final Map<Point, Map<Point, Move>> cache;

    public MoveCache() {
        cache = new HashMap<>();
    }


    public void put(Move move) {
        if (contains(move.getP1(), move.getP2()))
            throw new IllegalArgumentException("the move " + move + " exist already in the cache");
        if (!cache.containsKey(move.getP1()))
            cache.put(move.getP1(), new HashMap<>());
        if (!cache.get(move.getP1()).containsKey(move.getP2()))
            cache.get(move.getP1()).put(move.getP2(), move);
    }


    public Move get(Point point1, Point point2){
        if (!contains(point1, point2))
            throw new IllegalArgumentException(
                    MessageFormat.format("the points {0}, {1} doesn't exist in the cache",
                            point1,
                            point2));
        return cache.get(point1).get(point2);
    }

    public boolean contains(Point point1, Point point2) {
        return cache.containsKey(point1) && cache.get(point1).containsKey(point2);
    }
}
