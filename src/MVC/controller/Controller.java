package MVC.controller;

import MVC.model.*;
import MVC.view.window;
import javafx.event.ActionEvent;

import java.util.Arrays;
import java.util.Objects;

public class Controller {
    private Board model;
    private window view;


    public Controller(Board model) {
        this.model = model;
    }

    public void registerView(window view) {
        this.view = view;
        Piece[][] visiblePieces = model.getPieces("red");
        Piece[][] oppositePieces = model.getPieces("blue");
        for (int row = 0; row < visiblePieces.length; row++) {
            for (int col = 0; col < visiblePieces[row].length; col++) {
                if (visiblePieces[row][col] != null) {
                    view.renderImage(visiblePieces[row][col], "red", row, col);
                } else if (oppositePieces[row][col] != null) {
                    view.renderImage(oppositePieces[row][col], "blue", row, col);
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
        Piece attackingPiece = movePiece(SelectedLocation, PieceLocation);
        return new Attack(SelectedLocation, PieceLocation, attackingPiece);
    }

    public Piece movePiece(Point p1, Point p2) {
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
        return attackingPiece;
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

    public PossibleMoves getMoves(String color) {
        return model.getMoves(color);
    }


    private Piece[][] getPieces(String color) {
        return model.getPieces(color);
    }

    public void printBoard(String playerColor) {
        System.out.println(Arrays.deepToString(model.getPieces(playerColor)));
    }

    public Board getBoard() {
        return model;
    }
}
