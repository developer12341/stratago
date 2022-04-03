package MVC.model;

import java.util.BitSet;

public class MapMask {
    private final int maxRow;
    private final int maxCol;
    private BitSet array;

    public MapMask(int maxRow, int maxCol) {
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        array = new BitSet(maxRow * maxCol);
        array.set(0, maxCol * maxRow);
        setValue(4, 2, false);
        setValue(5, 2, false);
        setValue(4, 3, false);
        setValue(5, 3, false);

        setValue(4, 6, false);
        setValue(5, 6, false);
        setValue(4, 7, false);
        setValue(5, 7, false);
    }

    public boolean getValue(int row, int col) {
        if (array.size() <= row * col)
            throw new IndexOutOfBoundsException("the index (%d, %d) is out of bounds".formatted(row, col));
        return array.get(row * maxCol + col);
    }

    public void setValue(int row, int col, boolean value) {
        if (array.size() < row * col)
            throw new IndexOutOfBoundsException("the index (%d, %d) is out of bounds".formatted(row, col));
        array.set(row * maxCol + col, value);
    }
}
