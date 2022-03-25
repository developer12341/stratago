package MVC.model.PointClasses;

import java.util.Objects;

/**
 * this class is meant to represent a point on the board.
 * it is an immutable object, if you need to change one of the fields a new object will
 * be created
 */
public class Point {
    private static final PointCache cache = new PointCache();


    /**
     * this function creates a new point with row and col and caches it.
     * later if the same object is requested I will just pull it from the cache and return it.
     * @param row row
     * @param col column
     * @return an object that represent a point on the board.
     */
    public static Point create(int row, int col){
        if(cache.contains(row, col))
            return cache.get(row, col);
        else{
            Point obj = new Point(row, col);
            cache.put(obj);
            return obj;
        }
    }


    private int row, col;

    private Point(int row, int col) {
        this.row = row;
        this.col = col;
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
        return "Point{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }
}
