package MVC.view;

import MVC.ComputerPlayer;
import MVC.controller.Controller;
import MVC.model.Piece;
import MVC.model.PointClasses.Point;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class window {
    private ViewPiece[][] board;
    private boolean setup;
    private Button startGame;
    private Button selected;
    private Scene scene;
    private BorderPane root;

    public window(Stage stage) {
        board = new ViewPiece[10][10];
        selected = null;
        setup = true;
        initGUIObjects();
        stage.setScene(scene);
        stage.show();
    }

    public void setEventHandlers(Controller c) {
        ComputerPlayer otherPlayer = new ComputerPlayer(this, c, "blue");
        EventHandler<ActionEvent> onButtonClick = new HumanPlayer(this, c, "red", otherPlayer);
        startGame.setOnAction(onButtonClick);
        for (Button[] row : board) {
            for (Button b : row) {
                b.setOnAction(onButtonClick);
            }
        }
    }

    private void initGUIObjects() {
        root = new BorderPane();

        GridPane grid = new GridPane();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = new ViewPiece(row, col);
                board[row][col].setMinWidth(50);
                board[row][col].setMinHeight(30);
                grid.add(board[row][col], col, row);
            }
        }
        root.setCenter(grid);

        startGame = new Button("Start game");
        root.setBottom(startGame);

        scene = new Scene(root, 500, 500);
    }

    public void setImages(Piece[][] visiblePieces, Piece[][] BackPieces, String VisibleColor, String oppositeColor) {
        for (int row = 0; row < visiblePieces.length; row++) {
            for (int col = 0; col < visiblePieces[row].length; col++) {
                if (visiblePieces[row][col] != null) {
                    renderImage(visiblePieces[row][col], VisibleColor, row, col);
                } else if (BackPieces[row][col] != null) {
                    renderImage(BackPieces[row][col], oppositeColor, row, col);
                }
            }
        }
    }

    private void renderImage(Piece piece, String VisibleColor, int row, int col) {

        ImageView imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setFitHeight(30);
        if (piece != null) {
            if (VisibleColor.equals("red"))
                imageView.setImage(piece.redPieceImage);
            else if (VisibleColor.equals("blue"))
                imageView.setImage(piece.bluePieceImage);
        }
        board[row][col].setGraphic(imageView);
    }

    public boolean isSetup() {
        return setup;
    }

    public void setSetup(boolean setup) {
        if (!setup)
            root.getChildren().remove(startGame);
        else
            root.getChildren().add(startGame);
        this.setup = setup;
    }

    public boolean isStartGameButton(ActionEvent actionEvent) {
        return actionEvent.getSource() == startGame;
    }

    public Button getSelected() {
        return selected;
    }

    public void setSelected(Point p, ArrayList<Point> points) {

        selected = board[p.getRow()][p.getCol()];
        if (points == null)
            return;
        highlightMoves(p, points);
    }

    private void highlightMoves(Point p, ArrayList<Point> points) {
        board[p.getRow()][p.getCol()].setStyle("-fx-background-color: #CCFF99;");
        if (points == null)
            return;
        for (Point point : points) {
            board[point.getRow()][point.getCol()].setStyle("-fx-background-color: #CCFF99;");
        }
    }

    public boolean isSelected() {
        return selected != null;
    }

    public Point getLocation(ActionEvent actionEvent) {
        if(!(actionEvent.getSource() instanceof ViewPiece findVal))
            throw new IllegalArgumentException("the button selected was not in the board");
        return getLocation(findVal);
    }


    public Point getLocation(Button button) {
        if(!(button instanceof ViewPiece))
            throw new IllegalArgumentException("the button selected was not in the board");
        return ((ViewPiece) button).getPoint();
    }


    public void swapImages(Point pieceLocation, Point SelectedLocation) {
        ImageView temp = (ImageView) board[pieceLocation.getRow()][pieceLocation.getCol()].getGraphic();
        board[pieceLocation.getRow()][pieceLocation.getCol()].setGraphic(
                board[SelectedLocation.getRow()][SelectedLocation.getCol()].getGraphic());
        board[SelectedLocation.getRow()][SelectedLocation.getCol()].setGraphic(temp);
    }

    public boolean isSelected(ActionEvent actionEvent) {
        return actionEvent.getSource() == selected;
    }

    public void clearImage(Point p) {
        board[p.getRow()][p.getCol()].setGraphic(null);
    }

    public void moveImageTo(Point p1, Point p2) {
        board[p2.getRow()][p2.getCol()].setGraphic(
                board[p1.getRow()][p1.getCol()].getGraphic());
        clearImage(p1);
    }

    public void clearSelected(Point p, ArrayList<Point> moves) {
        selected = null;
        unhighlightMoves(p, moves);
    }

    private void unhighlightMoves(Point p, ArrayList<Point> moves) {

        board[p.getRow()][p.getCol()].setStyle(null);
        if (moves == null)
            return;
        for (Point point : moves) {
            board[point.getRow()][point.getCol()].setStyle(null);
        }
    }

    public void clearSelected() {
        selected = null;
    }
}
