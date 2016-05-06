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
  @FXML private Label fpsLab;
  private GraphicsContext gc;
  private Timeline timeline;
  private double frameRate = 10;
  private int gens = 0;
  private Duration dura;
  private KeyFrame keyf;
  @FXML private ChoiceBox gridSize;
  @FXML private ChoiceBox patternList;
  @FXML private Slider gridSlider;
  @FXML private Slider fpsSlider;
  
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
  
  /**
   * Method to update Grid label
   */
  private void updateDim() {
    dim.setText("Grid: " + gb.boardCols + "x" + gb.boardRows);
  }
  
  /**
   * Method to change framerate of animation to the parameter value
   * @param fps  Value to change framerate to
   * @author Fredrik
   */
  private void setFrameRate(int fps) {
    frameRate = fps;
    dura = Duration.millis(1000/frameRate);
    keyf = new KeyFrame(dura, (ActionEvent e) -> {
      gb.nextGeneration();
      draw();
      updateGens();
    });
    
    timeline.stop();
    timeline.getKeyFrames().setAll(keyf);
    timeline.play();
  }
  
  /**
   * Method that updates the FPS label in the GUI
   * @author Fredrik
   */
  private void updateFrameRate() {
    fpsLab.setText("FPS: " + frameRate);
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
    Stage stg = new Stage();
    Parent root = new Group();
    Scene scene = new Scene(root, 200, 200);
    stg.setScene(scene);
    stg.setTitle("Load pattern from RLE file");
    
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));      
    grid.setVgap(10);
    grid.setHgap(10);
    scene.setRoot(grid);
    
    Label lab = new Label("Load local file");
    Label labUrl = new Label("Load from URL:");
    Button btn = new Button("Choose file...");
    Button btm = new Button("Enter URL...");
    
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
        stg.hide();
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
      file = fc.showOpenDialog(stg);
      if (file != null) {
        path = file.toPath();
        Reader read = new Reader();
        read.loadFile(path);
        gb.setPattern(read.tab);
        gb.populateBoard();
        draw();
        stg.hide();
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
    grid.add(labUrl, 0, 2);
    grid.add(btm, 0, 3);
    
    stg.show();
  }
  
  GameboardDynamic gb;
  
  /**
   * Method to set up the initial settings of the program, including
   * defining timeline animation, creating a Gameboard object and filling it,
   * as well as drawing the initial configuration to the canvas object.
   * @param url Default URL object
   * @param rb  Default ResourceBundle object
   * @author Fredrik, Christine (pair programming): UI setup
   * @author Fredrik: listeners, animation, board population
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
    //Create Gameboard object, set initial pattern, and populate board with cells
    gb = new GameboardDynamic();
    gb.setPattern(Patterns.glider);
    gb.populateBoard();
    
    //Set initial text for dynamic labels
    resetGens();
    updateDim();
    updateFrameRate();
    
    //Setup for gridSlider
    gridSlider.setShowTickMarks(false);
    gridSlider.setMin(30);
    gridSlider.setMax(480);
    gridSlider.setValue(30);
    
    //Listener for grid size slider
    gridSlider.valueProperty().addListener((
            ObservableValue<? extends Number> ov,
            Number oval, Number nval) -> {
              gb.setBoardDim((int)(1.5*nval.intValue()), nval.intValue());
              gb.setPattern(gb.board);
              gb.populateBoard();
              updateDim();
              draw();      
            }
      );
    
    fpsSlider.setMin(1);
    fpsSlider.setMax(60);
    fpsSlider.setValue(10);
    fpsSlider.setMajorTickUnit(20);
    fpsSlider.setShowTickMarks(true);
    
    //Listener for FPS slider
    fpsSlider.valueProperty().addListener((
            ObservableValue<? extends Number> ov,
            Number oval, Number nval) -> {
              if (fpsSlider.isValueChanging()) {
                setFrameRate(nval.intValue());
                updateFrameRate();
              }
      }
    );
    
    //Set items in the patternList ChoiceBox
    patternList.setItems(FXCollections.observableArrayList(
          "Glider","Gosper glider gun","Pulsar")
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
                default:
                  break;
              }
            }
          });
    
    //Define initial duration of each keyframe
    dura = Duration.millis(1000/frameRate);
    
    //Set actions for each keyframe
    keyf = new KeyFrame(dura, (ActionEvent e) -> {
      gb.nextGeneration();
      draw();
      updateGens();
    });
    
    //Create timeline object
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
        if (gb.board.get(i).get(j).alive == true) {
          //if cell is alive, set cell colour
          gc.setFill(Color.DARKSLATEGREY);
          //then draw the living cell
          gc.fillRect(i*cellsize,j*cellsize,cellsize,cellsize);
        }
      }
    }
  }
  
   
}
