package MVC.model;

import MVC.model.PointClasses.Point;
import MVC.view.window;
import javafx.event.ActionEvent;

import java.util.Objects;

public class Model {
    private Board board;
    private window view;


    public Model() {
        board = new Board();
    }

    public void registerView(window view) {
        this.view = view;
        view.setImages(board.getPieces("red"),
                board.getPieces("blue"),
                "red",
                "blue");
    }

    public void switchWithSelected(ActionEvent actionEvent) {
        Point PieceLocation = view.getLocation(actionEvent);
        Point SelectedLocation = view.getLocation(view.getSelected());
        if (!board.switchPieces(PieceLocation, SelectedLocation))
            return;
        view.swapImages(PieceLocation, SelectedLocation);
    }

    public void moveSelectedTo(ActionEvent actionEvent) {
        Point PieceLocation = view.getLocation(actionEvent);
        Point SelectedLocation = view.getLocation(view.getSelected());

        clearSelected();
        movePiece(SelectedLocation, PieceLocation);
    }

    public void movePiece(Point p1, Point p2) {

        Point winner = board.moveTo(p1, p2);

        if (winner == null) {
            view.clearImage(p2);
            view.clearImage(p1);
        } else if (winner.equals(p2))
            view.clearImage(p1);
        else {
            view.moveImageTo(p1, p2);
        }

        board.updateMoves(p2);
        board.updateMoves(p1);
    }

    public void setSelected(ActionEvent actionEvent, String playerColor) {
        Point PieceLocation = view.getLocation(actionEvent);
        String PieceColor = board.getColor(PieceLocation);
        if (!Objects.equals(PieceColor, playerColor))
            return;

        view.setSelected(PieceLocation, board.getMoves(PieceLocation));
    }

    public boolean isPossibleMove(ActionEvent actionEvent) {

        Point PieceLocation = view.getLocation(actionEvent);
        Point SelectedLocation = view.getLocation(view.getSelected());
        if (SelectedLocation.equals(PieceLocation))
            return false;

        String PieceColor = board.getColor(PieceLocation);
        String SelectedColor = board.getColor(SelectedLocation);
        if (Objects.equals(PieceColor, SelectedColor))
            return false;

        return board.isPossibleMove(SelectedLocation, PieceLocation);
    }

    public boolean doesHaveMoves(ActionEvent actionEvent, String playerColor) {
        Point PieceLocation = view.getLocation(actionEvent);
        String PieceColor = board.getColor(PieceLocation);
        if (PieceColor == null || playerColor == null)
            return false;
        if (!Objects.equals(PieceColor, playerColor))
            return false;
        return board.doesHaveMoves(PieceLocation);

    }

    public void clearSelected() {
        if (view.isSetup()) {
            view.clearSelected();
        } else {
            Point PieceLocation = view.getLocation(view.getSelected());
            view.clearSelected(PieceLocation, board.getMoves(PieceLocation));
        }
    }

    public void startGame() {
        board.initPossibleMoves();
        view.setSetup(false);
    }

    public PossibleMoves getMoves(String color) {
        return board.getMoves(color);
    }

}
