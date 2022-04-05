package MVC.controller;

import MVC.model.Attack;
import MVC.model.Board;
import MVC.model.Piece;
import MVC.model.Point;
import MVC.view.GUIManager;
import javafx.event.ActionEvent;

import java.util.Objects;

public class Controller {
    private Board model;
    private GUIManager view;


    public Controller(Board model) {
        this.model = model;
    }

    public void registerView(GUIManager view) {
        this.view = view;
        Piece[][] visiblePieces = model.getPieces("red");
        Piece[][] oppositePieces = model.getPieces("blue");
        for (int row = 0; row < visiblePieces.length; row++) {
            for (int col = 0; col < visiblePieces[row].length; col++) {
                if (!model.isValid(row, col))
                    continue;
                if (visiblePieces[row][col] != null) {
                    view.renderImage(visiblePieces[row][col], "red", row, col);
                } else if (oppositePieces[row][col] != null) {
                    view.renderImage(Piece.PLACEHOLDER, "blue", row, col);
                }
            }
        }
    }

    public void switchWithSelected(ActionEvent actionEvent) {
        Point PieceLocation = view.getLocation(actionEvent);
        Point SelectedLocation = view.getLocation(view.getSelected());
        if (!model.switchPieces(PieceLocation, SelectedLocation))
            return;
        view.swapImages(PieceLocation, SelectedLocation);
    }

    public Attack moveSelectedTo(ActionEvent actionEvent) {
        Point PieceLocation = view.getLocation(actionEvent);
        Point SelectedLocation = view.getLocation(view.getSelected());

        clearSelected();

        return movePiece(SelectedLocation, PieceLocation);
    }

    public Attack movePiece(Point p1, Point p2) {
        Piece attackingPiece = model.getPiece(p1);
        Piece defendingPiece = model.getPiece(p2);
        Piece winner = model.moveTo(p1, p2);

        if (winner == null) {
            view.clearImage(p2);
            view.clearImage(p1);
        } else if (winner.equals(defendingPiece))
            view.clearImage(p1);
        else
            view.moveImageTo(p1, p2);

        model.updateMoves(p2);
        model.updateMoves(p1);
        if (defendingPiece != null)
            return new Attack(p1, p2, attackingPiece, defendingPiece);
        else
            return new Attack(p1, p2, null, null);
    }

    public void setSelected(ActionEvent actionEvent, String playerColor) {
        Point PieceLocation = view.getLocation(actionEvent);
        String PieceColor = model.getColor(PieceLocation);
        if (!Objects.equals(PieceColor, playerColor))
            return;

        view.setSelected(PieceLocation, model.getMoves(PieceLocation));
    }

    public boolean isPossibleMove(ActionEvent actionEvent) {

        Point PieceLocation = view.getLocation(actionEvent);
        Point SelectedLocation = view.getLocation(view.getSelected());
        if (SelectedLocation.equals(PieceLocation))
            return false;

        String PieceColor = model.getColor(PieceLocation);
        String SelectedColor = model.getColor(SelectedLocation);
        if (Objects.equals(PieceColor, SelectedColor))
            return false;

        return model.isPossibleMove(SelectedLocation, PieceLocation);
    }

    public boolean doesHaveMoves(ActionEvent actionEvent, String playerColor) {
        Point PieceLocation = view.getLocation(actionEvent);
        String PieceColor = model.getColor(PieceLocation);
        if (PieceColor == null || playerColor == null)
            return false;
        if (!Objects.equals(PieceColor, playerColor))
            return false;
        return model.doesHaveMoves(PieceLocation);

    }

    public void clearSelected() {
        if (view.isSetup()) {
            view.clearSelected();
        } else {
            Point PieceLocation = view.getLocation(view.getSelected());
            view.clearSelected(PieceLocation, model.getMoves(PieceLocation));
        }
    }

    public void startGame() {
        model.initPossibleMoves();
        view.setSetup(false);
    }

    public Board getBoard() {
        return model;
    }

    public boolean isGameOver() {
        return model.isGameOver();
    }

    public void gameOver(String playerColor) {
        view.gameOver(model.getWinner(), playerColor);
    }

    public void renderImage(Point p, Piece piece, String color) {
        if (model.isFree(p))
            return;
        if (!Objects.equals(color, model.getColor(p)))
            return;
        view.renderImage(piece, color, p.getRow(), p.getCol());
    }
}
