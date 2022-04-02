package MVC.view;

import MVC.ComputerPlayer;
import MVC.controller.Controller;
import MVC.model.Attack;
import MVC.model.Move;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HumanPlayer implements EventHandler<ActionEvent>{

    private window view;
    private Controller controller;
    private String playerColor;
    private ComputerPlayer OtherPlayer;

    public HumanPlayer(window view, Controller controller,
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
            if(view.isSelected()){
                if (view.isSelected(actionEvent)) {
                    controller.clearSelected();
                    return;
                }
                if(!controller.isPossibleMove(actionEvent))
                    return;
                Attack attack = movePiece(actionEvent);
                OtherPlayer.movePiece(attack.getMove(), attack.getAttackingPiece());
            }
            else {
                if(!controller.doesHaveMoves(actionEvent, playerColor))
                    return;
                controller.setSelected(actionEvent, playerColor);
            }
        }
    }

    private Attack movePiece(ActionEvent actionEvent) {
        return controller.moveSelectedTo(actionEvent);

    }


    private void onGameStart() {
        controller.clearSelected();
        controller.startGame();
//        controller.startGame();

    }

}
