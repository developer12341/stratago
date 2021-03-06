package generator;

import MVC.model.Piece;
import MVC.model.Point;
import MVC.view.GameScene;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * this is a class to help me select good starting boards. it connects directly to GameScene and
 * displays the boards to let me make changes.
 * if the user click the button start game the board they created is appended to the file.
 */
public class BoardSelector extends Application {
    GameScene view;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        view = new GameScene(stage);
        BoardGenerator boardGenerator = new BoardGenerator();
        loadBoard(boardGenerator.generate());
        EventHandler<ActionEvent> onButtonCLick = new EventHandler<>() {
            @Override
            public synchronized void handle(ActionEvent actionEvent) {
                if (view.isStartGameButton(actionEvent)) {
                    System.out.println("yey");
                    boardGenerator.saveBoard();
                    loadBoard(boardGenerator.generate());
                } else if (view.isSelected()) {
                    Point p1 = view.getLocation(view.getSelected());
                    Point p2 = view.getLocation(actionEvent);
                    boardGenerator.swapPieces(p1, p2);
                    view.swapImages(p1, p2);
                    view.clearSelected();
                } else {
                    view.setSelected(view.getLocation(actionEvent), null);
                }
            }

        };
        view.setEventHandlers(onButtonCLick);

    }

    private void loadBoard(Piece[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                view.renderImage(board[row][col], "red", row + 6, col);
            }
        }
    }
}
