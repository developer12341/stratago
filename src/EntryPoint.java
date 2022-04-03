import MVC.controller.Controller;
import MVC.model.Board;
import MVC.view.window;
import javafx.application.Application;
import javafx.stage.Stage;

public class EntryPoint extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Board model = new Board();
        window view = new window(stage);
        Controller c = new Controller(model);

        c.registerView(view);
        view.setEventHandlers(c);

    }
}
