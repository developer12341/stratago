package MVC.controller;

import MVC.model.Model;
import MVC.model.Move;
import MVC.model.Piece;
import MVC.model.PossibleMoves;
import javafx.event.ActionEvent;


public class Controller {
    private Model model;

    public void setModel(Model model) {
        this.model = model;
    }

    public void switchWithSelected(ActionEvent actionEvent) {
        model.switchWithSelected(actionEvent);
    }


    public void moveSelectedTo(ActionEvent actionEvent) {
        model.moveSelectedTo(actionEvent);
    }

    public void setSelected(ActionEvent actionEvent, String PlayerColor) {
        model.setSelected(actionEvent, PlayerColor);
    }

    public boolean isPossibleMove(ActionEvent actionEvent) {
        return model.isPossibleMove(actionEvent);
    }

    public boolean doesHaveMoves(ActionEvent actionEvent, String playerColor) {
        return model.doesHaveMoves(actionEvent, playerColor);
    }

    public void clearSelected() {
        model.clearSelected();
    }

    public void startGame() {
        model.startGame();
    }

    public PossibleMoves getMoves(String color) {
        return model.getMoves(color);
    }

    public void movePiece(Move move) {
        model.movePiece(move.getP1(), move.getP2());
    }

    public Piece[][] getOppositePieces(String color) {
        return model.getOppositePieces(color);
    }

    public Piece[][] getPieces(String color) {
        return model.getPieces(color);
    }
}
