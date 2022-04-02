package MVC;

import java.util.BitSet;

public class Boolean2DArray {
    private final int maxRow;
    private final int maxCol;
    private BitSet array;
    public Boolean2DArray(int maxRow, int maxCol) {
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        array = new BitSet(maxRow * maxCol);
    }
    public boolean getValue(int row, int col){
        if(array.size() < row * col)
            throw new IndexOutOfBoundsException("the index (%d, %d) is out of bounds".formatted(row,col));
        return array.get(row * maxCol + col);
    }

    public void setValue(int row, int col, boolean value){
        if(array.size() < row * col)
            throw new IndexOutOfBoundsException("the index (%d, %d) is out of bounds".formatted(row,col));
        array.set(row * maxCol + col, value);
    }
}
