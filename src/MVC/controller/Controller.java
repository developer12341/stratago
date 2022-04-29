package MVC.controller;

import MVC.model.Attack;
import MVC.model.Board;
import MVC.model.Piece;
import MVC.model.Point;
import MVC.view.GameScene;
import javafx.event.ActionEvent;

import java.util.Objects;

/**
 * this class is the controller, it is the
 * bridge between the model and the view.
 */
public class Controller {
    public static boolean showPiece = false;


    private Board model;
    private GameScene view;


    public Controller(Board model) {
        this.model = model;
    }

    /**
     * register the view and show all the pieces.
     */
    public void registerView(GameScene view) {
        this.view = view;
        renderPieces();
    }

    public void renderPieces() {
        Piece[][] visiblePieces = model.getPieces("red");
        Piece[][] oppositePieces = model.getPieces("blue");
        for (int row = 0; row < visiblePieces.length; row++) {
            for (int col = 0; col < visiblePieces[row].length; col++) {
                if (!model.isValid(row, col))
                    continue;
                if (visiblePieces[row][col] != null) {
                    view.renderImage(visiblePieces[row][col], "red", row, col);
                } else if (oppositePieces[row][col] != null) {
                    if(!showPiece)
                        view.renderImage(Piece.PLACEHOLDER, "blue", row, col);
                    else
                        view.renderImage(oppositePieces[row][col], "blue", row, col);
                }
            }
        }
    }

    /**
     * switch the selected piece with the current piece.
     * @param actionEvent the current piece.
     */
    public void switchWithSelected(ActionEvent actionEvent) {
        Point PieceLocation = view.getLocation(actionEvent);
        Point SelectedLocation = view.getLocation(view.getSelected());
        if (!model.switchPieces(PieceLocation, SelectedLocation))
            return;
        view.swapImages(PieceLocation, SelectedLocation);
    }

    /**
     * move the selected piece to the current piece.
     * @param actionEvent the current piece
     * @return the attack made on the player.
     */
    public Attack moveSelectedTo(ActionEvent actionEvent) {
        Point PieceLocation = view.getLocation(actionEvent);
        Point SelectedLocation = view.getLocation(view.getSelected());

        clearSelected();

        return movePiece(SelectedLocation, PieceLocation);
    }

    /**
     * move the piece from p1 to p2 and update the moves and the view
     * @param p1 the starting point
     * @param p2 the destination
     * @return the attack made.
     */
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

        if (defendingPiece != null)
            return new Attack(p1, p2, attackingPiece, defendingPiece);
        else
            return new Attack(p1, p2, null, null);
    }

    /**
     * set the button to be selected if it matches the player's color.
     * @param actionEvent the current button
     * @param playerColor the color
     */
    public void setSelected(ActionEvent actionEvent, String playerColor) {
        Point PieceLocation = view.getLocation(actionEvent);
        String PieceColor = model.getColor(PieceLocation);
        if (!Objects.equals(PieceColor, playerColor))
            return;
        if(view.isSetup())
            view.setSelected(view.getLocation(actionEvent));
        view.setSelected(PieceLocation, model.getMoves(PieceLocation));
    }

    /**
     * @param actionEvent a button
     * @return true if the move from the selected button to this button is legal
     */
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

    /**
     * @param actionEvent the button clicked
     * @param playerColor the color of the player
     * @return true if the point of this button has any legal moves
     */
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

    /**
     * initialize the possible moves of both players and start the game
     */
    public void startGame() {
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

    /**
     * render a piece at a certain point.
     */
    public void renderImage(Point p, Piece piece, String color) {
        if (model.isFree(p))
            return;
        if (!Objects.equals(color, model.getColor(p)))
            return;
        view.renderImage(piece, color, p.getRow(), p.getCol());
    }
}
