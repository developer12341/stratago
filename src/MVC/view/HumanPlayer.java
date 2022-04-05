package MVC.view;

import MVC.ComputerPlayer;
import MVC.controller.Controller;
import MVC.model.Attack;
import MVC.model.Board;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HumanPlayer implements EventHandler<ActionEvent> {

    private GUIManager view;
    private Controller controller;
    private String playerColor;
    private ComputerPlayer OtherPlayer;

    public HumanPlayer(GUIManager view, Controller controller,
                       String playerColor, ComputerPlayer otherPlayer) {
        this.view = view;
        this.controller = controller;
        this.playerColor = playerColor;
        OtherPlayer = otherPlayer;

    }

    @Override
    public synchronized void handle(ActionEvent actionEvent) {
        if (view.isSetup()) {
            if (view.isStartGameButton(actionEvent)) {
                onGameStart();
            } else {
                if (view.isSelected()) {
                    controller.switchWithSelected(actionEvent);
                    controller.clearSelected();
                } else
                    controller.setSelected(actionEvent, playerColor);
            }
        } else {
            if (view.isSelected()) {
                if (view.isSelected(actionEvent)) {
                    controller.clearSelected();
                    return;
                }
                if (!controller.isPossibleMove(actionEvent))
                    return;
                Attack attack = controller.moveSelectedTo(actionEvent);
                if (attack.getDefendingPiece() != null)
                    controller.renderImage(attack.getMove().getP2(), attack.getDefendingPiece(), Board.getOppositeColor(playerColor));
                if (controller.isGameOver()) {
                    controller.gameOver(playerColor);
                    return;
                }

                attack = OtherPlayer.movePiece(attack);
                if (attack.getDefendingPiece() != null) {
                    OtherPlayer.moveComputerPiece(attack);
                    controller.renderImage(attack.getMove().getP2(), attack.getDefendingPiece(), Board.getOppositeColor(playerColor));
                }

                if (controller.isGameOver()) {
                    controller.gameOver(playerColor);
                }
            } else {
                if (!controller.doesHaveMoves(actionEvent, playerColor))
                    return;
                controller.setSelected(actionEvent, playerColor);
            }
        }
    }

    private void onGameStart() {
        controller.clearSelected();
        controller.startGame();

    }

}
