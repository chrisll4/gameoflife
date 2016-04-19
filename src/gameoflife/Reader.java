package gameoflife;

import java.io.BufferedReader;
import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser.ExtensionFilter;


/**
 * Class containing methods to read pattern information from RLE files,
 * either locally or from the internet via URL.
 * @author Fredrik
 */
public class Reader {
   
  public Reader() {
  }

  String tx2 = "";
  public String[][] tab;
  
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
    tx2 = "";
    tab = new String[x][y];
    
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {
        tab[i][j] = "";
      }
    }
    
    int a = 0;
    int b = 0;
    

    // Loops to check all unicode values in pattern String array
    for (int i = 0; i < sTab.length; i++) {
      if (sTab[i] >= 48 && sTab[i] <= 57) {
        int c = Integer.parseInt(String.valueOf((char)sTab[i]));
        
        /*Under brukes tallet unicode-verdien representerer, til å
        bestemme hvor mange ganger neste tegn (b/o) skal repeteres, slik
        som flere sammenhengende celler representeres i RLE-formatet
        */
        for (int j = 0; j < c; j++) {
          if (sTab[i+1] == 98) { //hvis b (unicode)
            tab[a][b] = " ";     //død celle på denne plassen i tabellen
              if (a < tab.length-1)
                a++;
          } else if (sTab[i+1] == 111) { //hvis o (unicode)
            tab[a][b] = "X";             //levende celle her i tabellen
              if (a < tab.length-1)
                a++;
          }
        }
        i++;
        //under sjekkes det for døde/levende celler som ikke er repeterende
      } else if (sTab[i] == 98) { // hvis b (unicode)
        tab[a][b] = " ";          // død celle her i tabellen
        if (a < tab.length-1)
          a++;
      } else if (sTab[i] == 111) { // hvis o (unicode)
        tab[a][b] = "X";           // levende celler her i tabellen
        if (a < tab.length-1)
          a++;
      } else if (sTab[i] == 36) { // hvis $ (unicode)[end of line]
        if (a == tab.length-1) {  
          a = 0;                  // a settes tilbake til 0,
          b++;                    // og b økes med 1, for ny linje
        }
      } else if (sTab[i] == 33) { // hvis ! (unicode), end of file
        break;
      }
    }
    
    for (int k = 0; k < x; k++) {
      for (int l = 0; l <y; l++) {
        tx2 += tab[l][k];
      }
      tx2 += "\n";
    }
    
  };
  
 
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
          int x = 0;
          int y = 0;
          
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
          
          /*Går gjennom linjene i filen for å finne linjen som begynner
          på "x", ettersom den definerer størrelsen på mønsteret, og ser så
          etter tallverdier med opptil 4 sifre for x-verdien. Når den finner
          har funnet siste siffer i x-verdien, hopper den videre i linjen og
          ser etter tallverdier (opptil 4 sifre) for y-verdien til mønsteret.
          */
          for (int i = 0; i < nLines; i++) {
            if (strTab.get(i).startsWith("x")) {
              nLast = i;
              byte[] bTab = strTab.get(i).getBytes();
              for (int j = 0; j < bTab.length; j++) {
                if (bTab[j] >= 48 && bTab[j] <= 57) {
                  numX += (char)bTab[j];
                  lastX = j;
                  break;
                }
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
              }
              /*Begynner å lete etter y-verdier etter siste siffer i x-verdien
              (lastX), som ble økt med 1 når løkken fant siste siffer.
              */
              for (int j = lastX; j < bTab.length; j++) {
                if (bTab[j] >= 48 && bTab[j] <= 57) {
                  numY += (char)bTab[j];
                  lastY = j;
                  break;
                }
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
              }
            }
          }
          
          x = Integer.parseInt(numX);
          y = Integer.parseInt(numY);
          
          /* Finner så linjen etter linjen som definerer størrelsen på
          mønsteret, for å finne informasjon om oppbygging av mønsteret. 
          Denne mønsterlinjen deles så opp i individuelle tegn, som sendes
          til en byte-tabell.
          */
          byte[] cTab = strTab.get(nLast+1).getBytes();
          
          //Skriver ut tegnene i mønsterlinjen for å sjekke korrekt tolking
          for (int i = 0; i < cTab.length; i++) {
            System.out.print((char)cTab[i]);
          }
          
          System.out.println();
          
          
          //lagTabell(x,y,cTab);
          
          //text.setText(tx2);
          
        } catch (IOException ex) {
          System.out.println("bajs");
        }
      } else { //Feilmelding hvis man forsøker å lese et annet filformat
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("File error");
        String s = "Incorrect file format.";
        alert.setContentText(s);
        alert.show();
      }   
    }
    
    /**
     * Method to load RLE file from the input URL, 
     * interpret the file's pattern, and returning the pattern as an array 
     * for use in other methods.
     */
    public void loadUrl() {
        //Åpner dialog for å skrive inn URL
        TextInputDialog dialog = new TextInputDialog("http://");
        dialog.setTitle("Load pattern from URL");
        dialog.setHeaderText("Enter URL:");
         
        Optional<String> res = dialog.showAndWait();
         
        ArrayList<String> strTab = new ArrayList<>();
          
        int nLines = 0;
        int x = 0;
        int y = 0;
        
        /*Hvis url slutter på ".rle" (i.e. korrekt filformat), deles filen opp
        på samme måte som når man laster fra lokal fil.
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
              
              for (int i = 0; i < nLines; i++) {
                if (strTab.get(i).startsWith("x")) {
                  nLast = i;
                  byte[] bTab = strTab.get(i).getBytes();
                  for (int j = 0; j < bTab.length; j++) {
                  if (bTab[j] >= 48 && bTab[j] <= 57) {
                    numX += (char)bTab[j];
                    lastX = j;
                    break;
                  }
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
                }
                for (int j = lastX; j < bTab.length; j++) {
                  if (bTab[j] >= 48 && bTab[j] <= 57) {
                   numY += (char)bTab[j];
                    lastY = j;
                    break;
                  }
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
                }
              }
            }
          
            x = Integer.parseInt(numX);
            y = Integer.parseInt(numY);
          
            byte[] cTab = strTab.get(nLast+1).getBytes();
          
            for (int i = 0; i < cTab.length; i++) {
              System.out.print((char)cTab[i]);
            }
            System.out.println();
          
          
            /*lagTabell(x,y,cTab);
          
            text.setText(tx2);*/
            }
          } catch (MalformedURLException ex) {
              Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
          } catch (IOException ex) {
              Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //Feilmelding hvis feil filformat (URL slutter ikke på ".rle"
          Alert alert = new Alert(AlertType.ERROR);
          alert.setTitle("Error!");
          alert.setHeaderText("File error");
          String s = "Invalid URL or incorrect file format.";
          alert.setContentText(s);
          alert.show();
        }
      };
   
}
