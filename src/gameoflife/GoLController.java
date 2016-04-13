/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Fredrik
 */
public class GoLController implements Initializable {
   
  @FXML private Canvas canvas;
  private GraphicsContext gc;
  private Timeline timeline;
  private Button start;
  
   
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
  
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    Duration dura = Duration.millis(500);
    KeyFrame keyf = new KeyFrame(dura, (ActionEvent e) -> {draw();});
    timeline = new Timeline();
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.getKeyFrames().add(keyf);
    Gameboard.populateBoard();
    draw();
  }
   
  private void draw() {
    float cellsize = (float) canvas.getHeight()/Gameboard.boardRows;
    gc = canvas.getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    
    gc.setFill(Color.DARKSLATEGREY);
    
    for (int i=0; i<Gameboard.board.length;i++) {
      for (int j=0; j<Gameboard.board[0].length;j++) {
        if (Gameboard.board[i][j]==1) {
          gc.fillRect(i*cellsize,j*cellsize,cellsize,cellsize);
        }
      }
    }
    
    Gameboard.nextGeneration();
  }
   
}
