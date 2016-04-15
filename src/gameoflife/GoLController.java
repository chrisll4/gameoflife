package gameoflife;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GoLController implements Initializable {
   
  @FXML private Canvas canvas;
  private GraphicsContext gc;
  private Timeline timeline;
  private double frameRate = 30;
  
   
  @FXML
  private ChoiceBox<?> pattern;
  
  @FXML
  private void start(ActionEvent e) {
    timeline.play();
  }
  
  @FXML
  private void stop(ActionEvent e) {
    timeline.pause();
  }
  
  @FXML
  private void reset(ActionEvent e) {
    timeline.pause();
    gb.populateBoard();
    draw();
  }
  
  @FXML
  private void setFrameRate(int fps) {
    frameRate = fps;
  }
  
  @FXML
  private void anim() throws IOException {
    Stage anim = new Stage();
    Parent root = new Group();
    Scene scene = new Scene(root, 200, 200);
    anim.setScene(scene);
    anim.setTitle("Hei");
    
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));      
    grid.setVgap(10);
    grid.setHgap(10);
    scene.setRoot(grid);
    
    Label lab = new Label("Velg RLE-fil");
    Button btn = new Button("Hei");
    
    btn.setMinWidth(100);
    btn.setMinHeight(50);
    
    btn.setOnAction((ActionEvent e) -> {
      File file;
      Path path;
      FileChooser fc = new FileChooser();
      FileChooser.ExtensionFilter filt = new FileChooser.ExtensionFilter("RLE files","*.rle");
      fc.setTitle("Choose RLE-file to load");
      fc.setSelectedExtensionFilter(filt);
      file = fc.showOpenDialog(anim);
      path = file.toPath();
      });
    
    grid.add(lab, 0, 0);
    grid.add(btn, 0, 1);
    
    anim.show();
  }
  
  Gameboard gb;
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    Duration dura = Duration.millis(1000/frameRate);
    KeyFrame keyf = new KeyFrame(dura, (ActionEvent e) -> {draw();});
    timeline = new Timeline();
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.getKeyFrames().add(keyf);
    
    gb = new Gameboard();
    gb.setPattern(GoL.gosper);
    gb.populateBoard();
    draw();
  }
   
  private void draw() {
    float cellsize = (float) canvas.getHeight()/gb.boardRows;
    gc = canvas.getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    
    for (int i=0; i<gb.boardCols;i++) {
      for (int j=0; j<gb.boardRows;j++) {
        if (gb.board[i][j].alive == true) {
          gc.setFill(Color.DARKSLATEGREY);
          gc.fillRect(i*cellsize,j*cellsize,cellsize,cellsize);
        }
      }
    }
    gb.nextGeneration();
  }
   
}
