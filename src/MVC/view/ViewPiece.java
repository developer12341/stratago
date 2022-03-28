package MVC.view;

import MVC.model.PointClasses.Point;
import javafx.scene.control.Button;

public class ViewPiece extends Button {
    private Point point;

    public ViewPiece(int row, int col) {
        point = Point.create(row, col);
    }

    public ViewPiece(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }
}
