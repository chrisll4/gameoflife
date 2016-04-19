package gameoflife;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.sound.sampled.LineUnavailableException;

/**
 * Controller class that imports FXML data to produce GUI and controls 
 * animation and board data. Also controls GUI events.
 * @author Fredrik
 */
public class GoLController implements Initializable {
   
  @FXML private Canvas canvas;
  private GraphicsContext gc;
  private Timeline timeline;
  private double frameRate = 1;
  
   
  @FXML
  private ChoiceBox<?> pattern;
  
  /**
   * Method to start animation
   * @param e  Button pressed
   */
  @FXML
  private void start(ActionEvent e) {
    timeline.play();
  }
  
  /**
   * Method to pause animation
   * @param e  Button pressed
   */
  @FXML
  private void stop(ActionEvent e) {
    timeline.pause();
  }
  
  /**
   * Method to reset board to initial configuration (when pattern was first loaded)
   * @param e  Button pressed
   */
  @FXML
  private void reset(ActionEvent e) {
    timeline.pause();
    gb.populateBoard();
    draw();
  }
  
  /**
   * Method to change framerate of animation to the parameter value
   * @param fps  Value to change framerate to
   */
  @FXML
  private void setFrameRate(int fps) {
    frameRate = fps;
  }
  
  /**
   * Method to import patterns from RLE files. Opens a small window with 
   * buttons to open either a file found locally on the computer, or a file
   * on the internet, through a URL.
   * @throws IOException 
   */
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
    Button btm = new Button("Hå");
    
    btn.setMinWidth(100);
    btn.setMinHeight(50);
    
    btm.setMinWidth(100);
    btm.setMinHeight(50);
    
    btm.setOnAction((ActionEvent e) -> {
      Reader read = new Reader();
      try {
        read.loadUrl();
      } catch (NoSuchElementException nse) {
        System.out.println("Window closed, no file specified");
      }
    });
    
    btn.setOnAction((ActionEvent e) -> {
      File file;
      Path path;
      FileChooser fc = new FileChooser();
      FileChooser.ExtensionFilter filt = new FileChooser.ExtensionFilter("RLE files","*.rle");
      fc.setTitle("Choose RLE-file to load");
      fc.setSelectedExtensionFilter(filt);
      file = fc.showOpenDialog(anim);
      if (file != null) {
        path = file.toPath();
        try {
        Reader read = new Reader();
        read.loadFile(path);
        } catch (RuntimeException rt){
          System.out.println("ya dun goofed");
        }
      } else {
        System.out.println("Window closed, no file selected");
        try {
          Sound.makeSound();
        } catch (LineUnavailableException ex) {
          Logger.getLogger(GoLController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      
    });
    
    grid.add(lab, 0, 0);
    grid.add(btn, 0, 1);
    grid.add(btm, 0, 2);
    
    anim.show();
  }
  
  Gameboard gb;
  
  /**
   * Method to set up the initial settings of the program, including
   * defining timeline animation, creating a Gameboard object and filling it,
   * as well as drawing the initial configuration to the canvas object.
   * @param url
   * @param rb 
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    Duration dura = Duration.millis(1000/frameRate);
    
    //Make each frame run the draw() method on init
    KeyFrame keyf = new KeyFrame(dura, (ActionEvent e) -> {draw();});
    timeline = new Timeline();
    
    //Set animation to run indefinitely (or until paused)
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.getKeyFrames().add(keyf);
    
    //Create Gameboard object, set initial patter, and populate board with cells
    gb = new Gameboard();
    gb.setPattern(Patterns.glider);
    gb.populateBoard();
    
    //Draw initial cell configuration
    draw();
  }
  
  
  /**
   * Method containing instructions for drawing cells to canvas object,
   * as well as calling Gameboard's nextGeneration() method to calculate the
   * next configuration of living and non-living cells.
   */
  private void draw() {
    //Set size of each cell based on size of canvas
    float cellsize = (float) canvas.getHeight()/gb.boardRows;
    gc = canvas.getGraphicsContext2D();
    //Set background/dead cell colour
    gc.setFill(Color.WHITE);
    //Draw background across the whole canvas
    gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    
    //Loop through board cells, looking for living ones
    for (int i=0; i<gb.boardCols;i++) {
      for (int j=0; j<gb.boardRows;j++) {
        if (gb.board[i][j].alive == true) {
          //if cell is alive, set cell colour
          gc.setFill(Color.DARKSLATEGREY);
          //then draw the living cell
          gc.fillRect(i*cellsize,j*cellsize,cellsize,cellsize);
        }
      }
    }
    //Call nextGeneration method after current generation is fully drawn
    gb.nextGeneration();
  }
   
}
