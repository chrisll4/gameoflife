package gameoflife;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.*;
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
