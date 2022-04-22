package MVC.model.caches;

import MVC.model.Point;

/**
 * this is a cache for the Point object. the reason I need a cache in here is that it is a
 * very used object. if I won't have a caching system there will be a lot of garbage collection that slows
 * the program down
 */
public class PointCache {

    /**
     * the reason an array is used and not a hashmap is that in this game there could be only so much points.
     * it is wasteful to create a hashmap when the row and column can be the hash.
     */
    private final Point[][] cache;
    private int maxRow;
    private int maxCol;

    public PointCache(int maxRow, int maxCol) {
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        cache = new Point[maxRow][maxCol];
    }

    public boolean contains(int row, int col) {
        if (!isValidPosition(row, col))
            return false;
        return cache[row][col] != null;
    }

    private boolean isValidPosition(int row, int col) {
        return maxRow > row && row >= 0 && maxCol > col && col >= 0;
    }

    public void put(Point point) {
        if (!isValidPosition(point.getRow(), point.getCol()))
            throw new IndexOutOfBoundsException("the point " + point + " cannot be added to the cache");
        if (contains(point.getRow(), point.getCol()))
            throw new IllegalArgumentException("the point " + point + "exist already in the cache");
        cache[point.getRow()][point.getCol()] = point;
    }

    public Point get(int row, int col) {
        if (!contains(row, col))
            throw new IllegalArgumentException("the point (%d, %d) doesn't exist in the cache".formatted(row, col));
        return cache[row][col];

    }
}
