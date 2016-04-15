package gameoflife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GoLFX extends Application {
   
   @Override
   public void start(Stage stage) throws Exception {
      
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
