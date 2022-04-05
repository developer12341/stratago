package MVC.view;

import MVC.ComputerPlayer;
import MVC.controller.Controller;
import MVC.model.Attack;
import MVC.model.Board;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * this is an action listener that listens for clicks on all buttons in the game scene.
 * it is the gateway the user has to interact with the program.
 */
public class HumanPlayer implements EventHandler<ActionEvent> {

    private GUIManager view;
    private Controller controller;
    private String playerColor;
    private ComputerPlayer otherPlayer;

    /**
     * @param view        the GUI class
     * @param controller  the controller
     * @param playerColor the color of the human player
     * @param otherPlayer the computer player.
     */
    public HumanPlayer(GUIManager view, Controller controller,
                       String playerColor, ComputerPlayer otherPlayer) {
        this.view = view;
        this.controller = controller;
        this.playerColor = playerColor;
        this.otherPlayer = otherPlayer;

    }

    /**
     * this function is the function that is called when the player clicks on a button.
     * it is synchronized because if the player clicks two buttons on the same time it could mess up the
     * entire game.
     *
     * @param actionEvent the click event
     */
    @Override
    public synchronized void handle(ActionEvent actionEvent) {
        if (view.isSetup()) {
            //if the game is in setup mode.
            if (view.isStartGameButton(actionEvent)) {
                //and the button the user clicked is the start button
                //start the game
                onGameStart();
            } else {
                //if the user selected other button
                if (view.isSelected()) {
                    //if there is a selected button.
                    controller.switchWithSelected(actionEvent); // switch buttons
                    controller.clearSelected();//clear selected
                } else
                    //if there is no selected button
                    controller.setSelected(actionEvent, playerColor);// select the current button
            }
        } else {
            //if the game has started
            if (view.isSelected()) {
                //if the user already selected a button
                if (view.isSelected(actionEvent)) {
                    //if the user selected the same button
                    //the program will clear their selection
                    controller.clearSelected();
                    return;
                }
                //if the piece in the selected button can't move to current button then do nothing
                if (!controller.isPossibleMove(actionEvent))
                    return;
                //move the piece in the selected place to the current button
                Attack attack = controller.moveSelectedTo(actionEvent);

                //if the user attacked a piece
                if (attack.getDefendingPiece() != null)
                    //render the image if It hasn't lost
                    controller.renderImage(attack.getMove().getP2(), attack.getDefendingPiece(), Board.getOppositeColor(playerColor));


                if (controller.isGameOver()) {
                    //if the game ended then end the game
                    controller.gameOver(playerColor);
                    return;
                }

                //after the user made it move the computer's turn arrived
                attack = otherPlayer.movePiece(attack);
                if (attack.getDefendingPiece() != null) {
                    //if the computer attacked a piece
                    otherPlayer.moveComputerPiece(attack); // give the computer player this information
                    controller.renderImage(attack.getMove().getP2(),
                            attack.getAttackingPiece(),
                            Board.getOppositeColor(playerColor));//render the defending piece.
                }

                if (controller.isGameOver()) {
                    controller.gameOver(playerColor);
                }
            } else {
                //if the user didn't select a button yet
                if (!controller.doesHaveMoves(actionEvent, playerColor))
                    return;
                //and the button is a movable card.
                // set the current button to be the selected button and highlight the possible moves
                //from the position of the button
                controller.setSelected(actionEvent, playerColor);
            }
        }
    }

    private void onGameStart() {
        controller.clearSelected();
        controller.startGame();

    }

}
