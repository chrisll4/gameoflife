package gameoflife;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class to load FXML and run the Game of Life program.
 * @author Fredrik, Christine (pair programming)
 */
public class Main extends Application {
   
  
  /**
   * Start method to define the basics of the window that opens when run.
   * @param stage The initial (default) stage
   * @throws IOException Exception for faulty FXML loading
   * @author Fredrik, Christine (pair programming)
   */
  @Override
  public void start(Stage stage) throws IOException {
      
    Parent root = FXMLLoader.load(getClass().getResource("FXMLgol.fxml"));
      
    Scene scene = new Scene(root, 740, 580);
      
    stage.setTitle("Game of Life");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
   
}
