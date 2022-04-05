package MVC.view;

import MVC.ComputerPlayer;
import MVC.controller.Controller;
import MVC.model.Board;
import MVC.model.Piece;
import MVC.model.Point;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

/**
 * this is the manager for all things GUI related.
 * when the program wants to change the window it goes throw here.
 */
public class GUIManager {
    /**
     * the buttons
     */
    private ViewPiece[][] board;
    /**
     * this boolean represent rather the game is in setup mode or play mode
     */
    private boolean setup;

    private Button startGame;
    private Button selected;
    private Scene scene;

    private BorderPane root;

    public GUIManager(Stage stage) {
        board = new ViewPiece[Board.size][Board.size];
        selected = null;
        setup = true;
        initGUIObjects();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * create the players and add the human player to listen on every click.
     * @param c the controller
     */
    public void setEventHandlers(Controller c) {
        ComputerPlayer otherPlayer = new ComputerPlayer(c, "blue");
        EventHandler<ActionEvent> onButtonClick = new HumanPlayer(this, c, "red", otherPlayer);
        setEventHandlers(onButtonClick);
    }

    /**
     * @param onButtonClick the action listener for all the buttons
     */
    public void setEventHandlers(EventHandler<ActionEvent> onButtonClick) {
        startGame.setOnAction(onButtonClick);
        for (Button[] row : board) {
            for (Button b : row) {
                if(b != null){
                    b.setOnAction(onButtonClick);
                }
            }
        }
    }

    /**
     * init the GUI objects and add them to the Scene.
     */
    private void initGUIObjects() {
        root = new BorderPane();

        GridPane grid = new GridPane();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if(Board.mapMask.getValue(row, col)){
                    board[row][col] = new ViewPiece(row, col);
                    board[row][col].setMinWidth(50);
                    board[row][col].setMinHeight(40);
                    grid.add(board[row][col], col, row);

                }
            }
        }
        root.setCenter(grid);

        startGame = new Button("Start game");
        root.setBottom(startGame);

        scene = new Scene(root, 500, 500);
    }

    public void renderImage(Piece piece, String VisibleColor, int row, int col) {

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

    /**
     * this function starts the setup or stops it.
     * if the setup is stopped it hides the start game button.
     * @param setup start the setup or stop it
     */
    public void setSetup(boolean setup) {
        if (!setup)
            root.getChildren().remove(startGame);
        else
            root.getChildren().add(startGame);
        this.setup = setup;
    }

    /**
     * computes if the button the user clicked is the setup button.
     */
    public boolean isStartGameButton(ActionEvent actionEvent) {
        return actionEvent.getSource() == startGame;
    }

    public Button getSelected() {
        return selected;
    }

    /**
     * this function set the selected button and, if there are moves
     * it highlight them
     * @param p the point of the selected button
     * @param points the other points the selected Piece can go to.
     */
    public void setSelected(Point p, List<Point> points) {

        selected = board[p.getRow()][p.getCol()];
        if (points == null)
            return;
        highlightMoves(p, points);
    }

    /**
     * if the user would like to highlight the selected button
     * they should call this function
     * @param p the point of the selected button
     * @param points the other points the selected Piece can go to.
     */
    private void highlightMoves(Point p, List<Point> points) {
        board[p.getRow()][p.getCol()].setStyle("-fx-background-color: #CCFF99;");
        if (points == null)
            return;
        for (Point point : points) {
            board[point.getRow()][point.getCol()].setStyle("-fx-background-color: #CCFF99;");
        }
    }

    /**
     * @return if some button is selected
     */
    public boolean isSelected() {
        return selected != null;
    }

    /**
     * overloading the getLocation for beauty purposes
     * @param actionEvent the event clicked
     * @return the location of the click
     */
    public Point getLocation(ActionEvent actionEvent) {
        if (!(actionEvent.getSource() instanceof ViewPiece findVal))
            throw new IllegalArgumentException("the button selected was not in the board");
        return getLocation(findVal);
    }


    /**
     * get the location of a button in the board.
     * it grabs the stored location from the ViewPiece object.
     * @return the location of the button
     */
    public Point getLocation(Button button) {
        if (!(button instanceof ViewPiece))
            throw new IllegalArgumentException("the button selected was not in the board");
        return ((ViewPiece) button).getPoint();
    }

    /**
     * swap the images at p1 with p2
     * @param p1 point 1
     * @param p2 point 2
     */
    public void swapImages(Point p1, Point p2) {
        ImageView temp = (ImageView) board[p1.getRow()][p1.getCol()].getGraphic();
        board[p1.getRow()][p1.getCol()].setGraphic(
                board[p2.getRow()][p2.getCol()].getGraphic());
        board[p2.getRow()][p2.getCol()].setGraphic(temp);
    }

    /**
     * @param actionEvent the button clicked
     * @return rather the button clicked is already selected
     */
    public boolean isSelected(ActionEvent actionEvent) {
        return actionEvent.getSource() == selected;
    }

    /**
     * this function clears the image in p
     * @param p a point in the board
     */
    public void clearImage(Point p) {
        board[p.getRow()][p.getCol()].setGraphic(null);
    }

    /**
     * this function moves the image at p1 to p2
     * @param p1 the point to move from
     * @param p2 the point to move to
     */
    public void moveImageTo(Point p1, Point p2) {
        board[p2.getRow()][p2.getCol()].setGraphic(
                board[p1.getRow()][p1.getCol()].getGraphic());
        clearImage(p1);
    }

    /**
     * clear the selected button and unhighlight the moves
     */
    public void clearSelected(Point p, List<Point> points) {
        selected = null;
        unhighlightMoves(p, points);
    }

    /**
     * if the user would like to unhighlight the selected button
     * they should call this function
     * @param p the point of the selected button
     * @param points the other points the selected Piece can go to.
     */
    private void unhighlightMoves(Point p, List<Point> points) {

        board[p.getRow()][p.getCol()].setStyle(null);
        if (points == null)
            return;
        for (Point point : points) {
            board[point.getRow()][point.getCol()].setStyle(null);
        }
    }

    /**
     * clear the selected button
     */
    public void clearSelected() {
        selected = null;
    }

    /**
     * when the game ended this function is called
     * @param winner the winner of the game
     * @param playerColor the color of the human player
     */
    public void gameOver(String winner, String playerColor) {
        if(winner.equals(playerColor)){
            System.out.println(playerColor + " you won!");
        }else{
            System.out.println(playerColor + " you lost:(");
        }
        for (ViewPiece[] row: board){
            for (ViewPiece button: row){
                if(button != null)
                    button.setDisable(true);
            }
        }
    }
}
