package MVC.view;

import MVC.model.Point;
import javafx.scene.control.Button;

/**
 * this is a button that retains its coordinates.
 * when the program needs to know where is a certain button is it just checks the object
 */
public class ViewPiece extends Button {
    private final int row;
    private final int col;

    public ViewPiece(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Point getPoint() {
        return Point.create(row, col);
    }
}
