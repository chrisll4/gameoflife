package gameoflife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;


/**
 * Class containing methods to read pattern information from RLE files,
 * either locally or from the internet via URL.
 * @author Fredrik
 */
public class Reader {
  
  /**
   * Byte array that will contain the pattern "decoded" from RLE notation
   */
  public byte[][] tab;
  
  /**
   * Method to interpret the pattern string from an RLE file (each character
   * placed in a separate element of an array), loaded with either
   * loadFile() or loadUrl(), as well as creating an array representation of
   * the pattern, to be sent to Gameboard's setPattern() method as a parameter.
   * @param x  Number of columns in the pattern
   * @param y  Number of rows in the pattern
   * @param sTab  Pattern string from the RLE file, as array of characters
   */
  public void lagTabell(int x, int y, byte[] sTab) {
    tab = new byte[x][y];
    
    int count = 0;
    int c = 0, r = 0;
    
    for (int i=0; i<sTab.length; i++) {
      count = 0;
      if (sTab[i] >= 48 && sTab[i] <= 57) {
        String num = "";
        num += String.valueOf((char)sTab[i]);
        if (sTab[i+1] >= 48 && sTab[i+1] <= 57) {
          num += String.valueOf((char)sTab[i+1]);
          i++;
          if (sTab[i+2] >= 48 && sTab[i+1] <= 57) {
            num += String.valueOf((char)sTab[i+2]);
            i++;
            if (sTab[i+3] >= 48 && sTab[i+1] <= 57) {
              num += String.valueOf((char)sTab[i+3]);
              i++;
            }
          }
        }
        i++;
        
        count = Integer.valueOf(num);
        if (sTab[i] == 98) {
          for (int j=0; j<count; j++) {
            tab[c][r] = 0;
            c++;
          }
        } else if (sTab[i] == 111) {
          for (int j=0; j<count; j++) {
            tab[c][r] = 1;
            c++;
          }
        }
        
        
        //break;
      } else if (sTab[i] == 98) { // hvis b (unicode)
        tab[c][r] = 0;
        c++;
      } else if (sTab[i] == 111) { // hvis o (unicode)
        tab[c][r] = 1;
        c++;
      } else if (sTab[i] == 36) { // hvis $ (unicode), end of line
        c = 0;
        r++;
      } else if (sTab[i] == 33) { // hvis ! (unicode), end of file
        break;
      }
    }    
  }
  
 
  /**
   * Method to load RLE file chosen in a Filechooser, interpret the file's
   * pattern, and return the pattern code as a byte-array of the individual
   * characters' unicode values.
   * @param path  Local path for the RLE file
   */
    public void loadFile(Path path) {      
      /*Sjekker om det er en ikke-null path, og om den leder til en RLE-fil,
      og leser filen hvis betingelsene er møtt.
      */
      if (path != null && path.toString().endsWith(".rle")) {
        try (InputStream in = Files.newInputStream(path);
          BufferedReader reader = 
              new BufferedReader(new InputStreamReader(in))) {
          String line = null;

          ArrayList<String> strTab = new ArrayList<>();
          
          int nLines = 0;
          int x, y;
          
          //Legger hver linje i filen inn i array
          while ((line = reader.readLine()) != null) {
            strTab.add(line);
            nLines ++;
          }
          
          String numX = "";
          String numY = "";
          int lastX = 0;
          int lastY = 0;
          int nLast = 0;
          
          /*
          Go through lines in the file to find the line that starts with "x",
          as this line defines the dimensions of the pattern, then looks for
          1-4 digit numbers for the x-value. Once the x-value has been found,
          the loops jump ahead and look for the y-value in the same way.
          */
          for (int i = 0; i < nLines; i++) {
            if (strTab.get(i).startsWith("x")) {
              nLast = i;
              byte[] bTab = strTab.get(i).getBytes();
              for (int j = 0; j < bTab.length; j++) {
                if (bTab[j] >= 48 && bTab[j] <= 57) {
                  numX += (char)bTab[j];
                  lastX = j;
                  if (bTab[lastX+1] >= 48 && bTab[lastX+1] <= 57) {
                    numX += (char)bTab[lastX+1];
                    lastX += 1;
                      if (bTab[lastX+1] >= 48 && bTab[lastX+1] <= 57) {
                      numX += (char)bTab[lastX+1];
                      lastX += 1;
                      if (bTab[lastX+1] >= 48 && bTab[lastX+1] <= 57) {
                        numX += (char)bTab[lastX+1];
                        lastX += 1;
                      }
                    }
                  }
                  break;
                }
              }
              /* Start looking for Y value after the last digit in the X value
              */
              for (int j = lastX+1; j < bTab.length; j++) {
                if (bTab[j] >= 48 && bTab[j] <= 57) {
                  numY += (char)bTab[j];
                  lastY = j;
                  if (bTab[lastY+1] >= 48 && bTab[lastY+1] <= 57) {
                    numY += (char)bTab[lastY+1];
                    lastY += 1;
                    if (bTab[lastY+1] >= 48 && bTab[lastY+1] <= 57) {
                      numY += (char)bTab[lastY+1];
                      lastY += 1;
                      if (bTab[lastY+1] >= 48 && bTab[lastY+1] <= 57) {
                        numY += (char)bTab[lastY+1];
                      }
                    }
                  }
                  break;
                }
              }
            }
          }
          
          x = Integer.parseInt(numX);
          y = Integer.parseInt(numY);
          
          System.out.println("x: "+x);
          System.out.println("y: "+y);
          
          /*
          After finding the dimensions of the pattern, the method then looks
          at the next line(s), which contains the RLE encoding of the pattern
          itself, and splits it into single characters, and their unicode
          values are stored in a byte array.
          */
          int patStart = nLast+1;
          byte[] cTab;
          String patRLE = "";
          
          for (int i=patStart; i<strTab.size(); i++) {
            patRLE += strTab.get(i);
          }
          
          cTab = patRLE.getBytes();
          System.out.println(patRLE);
          
          //Print pattern string to console to check that the file has been read
          for (int i = 0; i < cTab.length; i++) {
            System.out.print((char)cTab[i]);
          }
          System.out.println(); //Newline character after pattern string
          
          lagTabell(x,y,cTab);

        } catch (IOException ex) {
          System.out.println("Input error");
        }
      } else { //Error alert if an attempt is made to open non-RLE file
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("File error");
        String s = "Incorrect file format.";
        alert.setContentText(s);
        alert.show();
      }
    }
    
    /**
     * Method to load RLE file from URL specified in TextInputDialog,
     * interpret the file's pattern, and return the pattern as an array 
     * for use in other methods.
     */
    public void loadUrl() {
        TextInputDialog dialog = new TextInputDialog("http://");
        dialog.setTitle("Load pattern from URL");
        dialog.setHeaderText("Enter URL:");
         
        Optional<String> res = dialog.showAndWait();
         
        ArrayList<String> strTab = new ArrayList<>();
          
        int nLines = 0;
        int x = 0;
        int y = 0;
        
        /*
        If the specified URL ends with ".rle" (i.e. leads to an RLE file),
        the file is processed much like if it were a local file loaded in the 
        loadFile() method.
        */
        if (res.get().endsWith(".rle")) {
          try {
              URL url = new URL(res.get());
            try (BufferedReader in = new BufferedReader(
                      new InputStreamReader(url.openStream()))) {
              String line;
              String txt = "";
              while ((line = in.readLine()) != null) {
                txt = txt + line + "\n";
                strTab.add(line);
                nLines ++;
              }
              in.close();
              
              String numX = "";
              String numY = "";
              int lastX = 0;
              int lastY = 0;
              int nLast = 0;
              
              
              /*
              Starts looking for pattern dimensions (x = ..., y = ...)
              */
              for (int i = 0; i < nLines; i++) {
                if (strTab.get(i).startsWith("x")) {
                  nLast = i;
                  byte[] bTab = strTab.get(i).getBytes();
                  for (int j = 0; j < bTab.length; j++) {
                    if (bTab[j] >= 48 && bTab[j] <= 57) {
                      numX += (char)bTab[j];
                      lastX = j;
                      if (bTab[lastX+1] >= 48 && bTab[lastX+1] <= 57) {
                        numX += (char)bTab[lastX+1];
                        lastX += 1;
                        if (bTab[lastX+1] >= 48 && bTab[lastX+1] <= 57) {
                          numX += (char)bTab[lastX+1];
                          lastX += 1;
                          if (bTab[lastX+1] >= 48 && bTab[lastX+1] <= 57) {
                            numX += (char)bTab[lastX+1];
                            lastX += 1;
                          }
                        }
                      }
                      break;
                    }
                  }
                  /* Start looking for Y value after the last digit in the X value
                  */
                  for (int j = lastX+1; j < bTab.length; j++) {
                    if (bTab[j] >= 48 && bTab[j] <= 57) {
                      numY += (char)bTab[j];
                      lastY = j;
                      if (bTab[lastY+1] >= 48 && bTab[lastY+1] <= 57) {
                        numY += (char)bTab[lastY+1];
                        lastY += 1;
                        if (bTab[lastY+1] >= 48 && bTab[lastY+1] <= 57) {
                          numY += (char)bTab[lastY+1];
                          lastY += 1;
                          if (bTab[lastY+1] >= 48 && bTab[lastY+1] <= 57) {
                            numY += (char)bTab[lastY+1];
                          }
                        }
                      }
                      break;
                    }
                  }
                }
              }
          
              x = Integer.parseInt(numX);
              y = Integer.parseInt(numY);
              
              System.out.println("x: "+x);
              System.out.println("y: "+y);
          
              /*
              Splits pattern string into single characters, sends their unicode
              values to a byte array.
              */
            
              int patStart = nLast+1;
              byte[] cTab;
              String patRLE = "";          
          
              for (int i=patStart; i<strTab.size(); i++) {
                patRLE += strTab.get(i);
              }
          
              cTab = patRLE.getBytes();
              System.out.println(patRLE);
          
              for (int i = 0; i < cTab.length; i++) {
                System.out.print((char)cTab[i]);
              }
              System.out.println();
          
          
              lagTabell(x,y,cTab);
            }
          } catch (MalformedURLException ex) {
              Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
          } catch (IOException ex) {
              Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //Error alert if specified URL does not lead to RLE file
          Alert alert = new Alert(AlertType.ERROR);
          alert.setTitle("Error!");
          alert.setHeaderText("File error");
          String s = "Invalid URL or incorrect file format.";
          alert.setContentText(s);
          alert.show();
        }
      };
   
}
