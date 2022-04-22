import MVC.controller.Controller;
import MVC.model.Board;
import MVC.view.GameScene;
import MVC.view.SettingScene;
import MVC.view.StartScene;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * this is the main class and entry point. it creates the mvc and starts the GUI.
 */
public class gameApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Board model = new Board();
        GameScene view = new GameScene(stage);
        StartScene startScene = new StartScene();
        SettingScene settingScene = new SettingScene();
        Controller c = new Controller(model);

        c.registerView(view);
        view.setEventHandlers(c);
        startScene.setEventHandler(stage, view.getScene(),settingScene.getScene(), c);
        settingScene.setEventHandler(stage,startScene.getScene());

        stage.setScene(startScene.getScene());
        stage.show();

    }
}
