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
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.sound.sampled.LineUnavailableException;

/**
 * Controller class that imports FXML data to produce GUI and controls 
 * animation and board data. Also controls GUI events.
 * @author Fredrik, Chrinstine (where noted)
 */
public class GoLController implements Initializable {
   
  @FXML private Canvas canvas;
  @FXML private Label gen;
  @FXML private Label dim;
  private GraphicsContext gc;
  private Timeline timeline;
  private double frameRate = 30;
  private int gens = 0;
  @FXML private ChoiceBox gridSize;
  @FXML private ChoiceBox patternList;
  @FXML private Slider gridSlider;

  
  /**
   * Method to start animation if not running, or pause animation if running
   * @param e  Button pressed
   * @author Fredrik
   */
  @FXML
  private void startStop(ActionEvent e) {
    if (timeline.getStatus() == Status.RUNNING) {
      timeline.pause();
    } else {
      timeline.play();
    }
  }
  
  /**
   * Method to reset board to initial configuration (when pattern was first loaded)
   * @param e  Button pressed
   * @author Fredrik
   */
  @FXML
  private void reset(ActionEvent e) {
    timeline.stop();
    gb.setPattern(gb.patOrigin);
    gb.populateBoard();
    draw();
    resetGens();
  }
  
  /**
   * Method to generate a random pattern, place it in the gameboard,
   * and draw the pattern on the canvas
   * @author Fredrik
   */
  @FXML
  private void generateRandom() {
    timeline.stop();
    Patterns.generateRandomPattern(gb.boardCols, gb.boardRows);
    gb.setPattern(Patterns.randomPattern);
    gb.populateBoard();
    draw();
    resetGens();
  }
  
  /**
   * Method to update generation counter in lower right corner
   * @author Fredrik
   */
  private void updateGens() {
    gens++;
    gen.setText("Generations: "+gens);
  }
  
  /**
   * Method to reset generation counter to 0
   * @author Fredrik
   */
  private void resetGens() {
    gens = 0;
    gen.setText("Generations: "+gens);
  }
  
  private void updateDim() {
    dim.setText("Dimensions: " + gb.boardCols + "x" + gb.boardRows);
  }
  
  /**
   * Method to change framerate of animation to the parameter value
   * @param fps  Value to change framerate to
   * @author Fredrik
   */
  @FXML
  private void setFrameRate(int fps) {
    frameRate = fps;
  }
  
  /**
   * Method to import patterns from RLE files. Opens a small window with 
   * buttons to open either a file found locally on the computer, or a file
   * on the internet, through a URL.
   * @throws IOException Input exception if no file is loaded
   * @author Fredrik
   */
  @FXML
  private void reader() throws IOException {
    timeline.pause();
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
        gb.setPattern(read.tab);
        gb.populateBoard();
        draw();
      } catch (NoSuchElementException nse) {
        System.out.println("Window closed, no file specified");
        try {
          Sound.makeSound();
        } catch (LineUnavailableException ex) {
          Logger.getLogger(GoLController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        Reader read = new Reader();
        read.loadFile(path);
        gb.setPattern(read.tab);
        gb.populateBoard();
        draw();
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
   * @param url Default URL object
   * @param rb  Default ResourceBundle object
   * @author Fredrik, Christine (pair programming): basic UI setup
   * @author Fredrik: listeners, animation, board population
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
    //Create Gameboard object, set initial pattern, and populate board with cells
    gb = new Gameboard();
    gb.setPattern(Patterns.glider);
    gb.populateBoard();
    
    resetGens();
    updateDim();
    gridSlider.setShowTickMarks(false);
    gridSlider.setMin(30);
    gridSlider.setMax(480);
    gridSlider.setValue(30);
    
    gridSlider.valueProperty().addListener((
               ObservableValue<? extends Number> ov,
               Number oval, Number nval) -> {
                  gb.setBoardDim((int)(1.5*nval.intValue()), nval.intValue());
                  gb.setPattern(gb.board);
                  gb.populateBoard();
                  
                  updateDim();
                  draw();
                  // HIE HEI HEI
                  
         }
      );
    
    
    /*
    //Set items in the gridSize ChoiceBox
    gridSize.setItems(FXCollections.observableArrayList(
          "45x30","90x60","120x80")
    );*/
    
    
    
    /*
    Add listener to gridSize ChoiceBox, to determine which item on the
    list has been chosen, and then define what to do based on the index of 
    the item chosen.
    */
    /*
    gridSize.getSelectionModel().selectedIndexProperty().addListener(new
          ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number val, Number nval) {
              switch (nval.intValue()) {
                case 0:
                  timeline.pause();
                  gb.setBoardDim(45, 30);
                  gb.populateBoard();
                  draw();
                  resetGens();
                  break;
                case 1:
                  timeline.pause();
                  gb.setBoardDim(90, 60);
                  gb.populateBoard();
                  draw();
                  resetGens();
                  break;
                case 2:
                  timeline.pause();
                  gb.setBoardDim(120, 80);
                  gb.populateBoard();
                  draw();
                  resetGens();
                  break;
                default:
                  break;
              }
            }
          });
    */
    
    //Set items in the patternList ChoiceBox
    patternList.setItems(FXCollections.observableArrayList(
          "Glider","Gosper glider gun","Pulsar","Random","Load RLE file")
    );
    
    /*
    Add listener to patternList ChoiceBox, to determine which item on the
    list has been chosen, and then define what to do based on the index of 
    the item chosen.
    */
    patternList.getSelectionModel().selectedIndexProperty().addListener(new
          ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number val, Number nval) {
              switch (nval.intValue()) {
                case 0:
                  timeline.pause();
                  gb.setPattern(Patterns.glider);
                  gb.populateBoard();
                  resetGens();
                  draw();
                  break;
                case 1:
                  timeline.pause();
                  gb.setPattern(Patterns.gosper);
                  gb.populateBoard();
                  resetGens();
                  draw();
                  break;
                case 2:
                  timeline.pause();
                  gb.setPattern(Patterns.pulsar);
                  gb.populateBoard();
                  resetGens();
                  draw();
                  break;
                case 3:
                  timeline.pause();
                  generateRandom();
                  break;
                case 4:
                  timeline.pause();
                  try {
                    reader();
                  } catch (IOException io) {
                    System.err.println("Input error");
                  }
                default:
                  break;
              }
            }
          });
    
    
    Duration dura = Duration.millis(1000/frameRate);
    
    //Make each frame run the draw() method on init
    KeyFrame keyf = new KeyFrame(dura, (ActionEvent e) -> {gb.nextGeneration(); draw(); updateGens();});
    timeline = new Timeline();
    
    //Set animation to run indefinitely (or until paused)
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.getKeyFrames().add(keyf);
    
    //Draw initial cell configuration
    draw();
  }
  
  
  /**
   * Method containing instructions for drawing cells to canvas object,
   * as well as calling Gameboard's nextGeneration() method to calculate the
   * next configuration of living and non-living cells.
   * @author Fredrik
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
  }
  
   
}
