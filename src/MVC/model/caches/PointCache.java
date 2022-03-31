package MVC.model.caches;

import MVC.model.Point;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class PointCache {
    private final Map<Integer, Map<Integer, Point>> cache;

    public PointCache() {
        cache = new HashMap<>();
    }

    public boolean contains(int row, int col) {
        return cache.containsKey(row) && cache.get(row).containsKey(col);
    }

    public void put(Point point) {
        if (contains(point.getRow(), point.getCol()))
            throw new IllegalArgumentException("the point " + point + "exist already in the cache");
        if (!cache.containsKey(point.getRow()))
            cache.put(point.getRow(), new HashMap<>());
        if (!cache.get(point.getRow()).containsKey(point.getCol()))
            cache.get(point.getRow()).put(point.getCol(), point);
    }

    public Point get(int row, int col) {
        if (!contains(row, col))
            throw new IllegalArgumentException(
                    MessageFormat.format("the point {0}, {1} doesn't exist in the cache",
                            row,
                            col));
        return cache.get(row).get(col);

    }
}
