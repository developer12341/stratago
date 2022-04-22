package MVC.view;

import MVC.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SettingScene {
    private final CheckBox showPieces;
    private final Scene scene;
    private final Button exitToMainMenu;

    public SettingScene() {
        Text title = new Text("SettingScene");
        title.setFont(new Font(30));
        showPieces = new CheckBox("show computer's pieces?");
        showPieces.setOnAction(actionEvent -> Controller.showPiece = showPieces.isSelected());

        exitToMainMenu = new Button("exit to main menu");

        VBox root = new VBox(title, showPieces, exitToMainMenu);
        root.setAlignment(Pos.CENTER);
        scene = new Scene(root, 500, 500);
    }

    public Scene getScene() {
        return scene;
    }

    public void setEventHandler(Stage stage, Scene startScene) {
        exitToMainMenu.setOnAction(event -> stage.setScene(startScene));

    }
}
