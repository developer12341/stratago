import MVC.controller.Controller;
import MVC.model.Board;
import MVC.view.GUIManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class gameApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Board model = new Board();
        GUIManager view = new GUIManager(stage);
        Controller c = new Controller(model);

        c.registerView(view);
        view.setEventHandlers(c);

    }
}
