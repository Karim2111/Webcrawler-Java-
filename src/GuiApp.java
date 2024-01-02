
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class GuiApp extends Application {
    public void start(Stage primaryStage) {
        Pane aPane = new Pane();

        ProjectTesterImp m = new ProjectTesterImp();
        ProjectTesterView view = new ProjectTesterView(m);
        aPane.getChildren().add(view);

        primaryStage.setTitle("Search Engine");
        primaryStage.setResizable(true);

        primaryStage.setScene(new Scene(aPane,350,300));
        primaryStage.show();
        view.update();


        view.getSearchButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                view.update();
            }
        });
    }


}
