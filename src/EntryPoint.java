import MVC.controller.Controller;
import MVC.model.Model;
import MVC.view.window;
import javafx.application.Application;
import javafx.stage.Stage;

public class EntryPoint extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Controller c = new Controller();
        window view = new window(stage);
        Model m = new Model();

        m.registerView(view);
        c.setModel(m);
        view.setEventHandlers(c);

    }
}
