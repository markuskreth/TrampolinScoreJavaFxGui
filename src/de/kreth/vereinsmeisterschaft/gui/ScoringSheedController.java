package de.kreth.vereinsmeisterschaft.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import de.kreth.vereinsmeisterschaftprog.business.InputConverter;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;


public class ScoringSheedController extends BorderPane {

   @FXML Label startername;
   @FXML Button btnChangeName;
   @FXML Button btnOk;
   @FXML Button btnCancel;

   @FXML TextField kari1;
   @FXML TextField kari2;
   @FXML TextField kari3;
   @FXML TextField kari4;
   @FXML TextField kari5;

   @FXML TextField kariDiff;

   @FXML Label result;

   private Wertung wertung;
   private Wertung original;
   private ScoringErgebnisPropertyChangeListener scoringErgebnisListener = new ScoringErgebnisPropertyChangeListener();
   private InputConverter converter = new InputConverter();
   
   @FXML
   public void initialize() {
      kari1.focusedProperty().addListener(new ChangeListener<Boolean>() {

         @Override
         public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue == Boolean.FALSE){
               if(wertung != null) {
                  try {
                     double kari1Value = converter.convert(kari1.getText());
                     wertung.setKari1(kari1Value);
                     kari1.setText(converter.format(kari1Value));
                  } catch (ParseException e) {
                     e.printStackTrace();
                  }
               }
               
            }
         }
      });
   }
   
   public void resetInput(){
      wertung.setKari1(original.getKari1());
      wertung.setKari2(original.getKari1());
      wertung.setKari3(original.getKari1());
      wertung.setKari4(original.getKari1());
      wertung.setKari5(original.getKari1());
      wertung.setSchwierigkeit(original.getSchwierigkeit());
   }
   
   public void setErgebnis(Wertung wertung) {
      if(this.wertung != null) {
         this.wertung.removePropertyChangeListener(scoringErgebnisListener);
      }
      this.wertung = wertung;
      this.original = wertung.clone();
   }
   
   private class ScoringErgebnisPropertyChangeListener implements PropertyChangeListener {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         if(evt.getPropertyName().matches(Wertung.ERGEBNIS_CHANGE_PROPERTY)) {
            result.setText(converter.format(wertung.getErgebnis()));
         }
      }
      
   }
}
