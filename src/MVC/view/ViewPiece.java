package MVC.view;

import MVC.model.Point;
import javafx.scene.control.Button;

public class ViewPiece extends Button {
    private int row, col;

    public ViewPiece(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Point getPoint() {
        return Point.create(row, col);
    }
}
