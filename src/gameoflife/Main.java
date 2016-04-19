package gameoflife;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class to run the Game of Life program.
 * @author Fredrik
 */
public class Main extends Application {
   
  
  /**
   * Start method to define the basics of the window that opens when run.
   * @param stage The initial stage
   * @throws IOException Exception for faulty FXML loading
   */
  @Override
  public void start(Stage stage) throws IOException {
      
    Parent root = FXMLLoader.load(getClass().getResource("FXMLgol.fxml"));
      
    Scene scene = new Scene(root, 740, 540);
      
    stage.setTitle("Game of Life");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
   
}
