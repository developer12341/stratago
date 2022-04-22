package MVC.view;

import MVC.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartScene {
    private final Button startGame;
    private final Scene scene;
    private final Button settings;

    public StartScene() {

        Text startMsg = new Text("welcome to stratego");
        startMsg.setFont(new Font(30));

        startGame = new Button("start game");
        settings = new Button("settings");

        VBox root = new VBox(startMsg, startGame, settings);
        root.setAlignment(Pos.CENTER);
        scene = new Scene(root, 500, 500);

    }
    public void setEventHandler(Stage stage, Scene gameScene, Scene Settings, Controller c){
        startGame.setOnAction(actionEvent -> {
            c.renderPieces();
            stage.setScene(gameScene);
        });
        settings.setOnAction(actionEvent -> stage.setScene(Settings));

    }
    public Scene getScene(){
        return scene;
    }
}
