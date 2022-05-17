package MVC.model;

import MVC.model.caches.PointCache;

import java.util.Objects;

import static java.lang.Math.abs;

/**
 * this class is meant to represent a point on the board.
 * it is an immutable object, if you need to change one of the fields a new object will
 * be created
 */
public class Point {

    private static final PointCache cache = new PointCache(10, 10);
    private final int row;
    private final int col;


    private Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * this function creates a new point with row and col and caches it.
     * later if the same object is requested I will just pull it from the cache and return it.
     *
     * @param row row
     * @param col column
     * @return an object that represent a point on the board.
     */
    public static Point create(int row, int col) {
        if (cache.contains(row, col))
            return cache.get(row, col);
        Point obj = new Point(row, col);
        cache.put(obj);
        return obj;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;
        return getRow() == point.getRow() && getCol() == point.getCol();
    }

    @Override
    public String toString() {
        return "(%d, %d)".formatted(row, col);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }

    /**
     * if in normal euclidean geometry the distance is SQRT(A*A + B*B)
     * in taxicab geometry the distance is just A + B;
     * the reason for it is that in this game you can move at a diagonal,
     * so you have to move throw X and Y
     * @param p the point of origin
     * @return the "taxicab geometry" distance from p to this point.
     */
    public int distance(Point p) {
        return distance(p.getRow(), p.getCol());
    }

    public int distance(int row, int col) {
        return abs(this.row - row) + abs(this.col - col);
    }
}
