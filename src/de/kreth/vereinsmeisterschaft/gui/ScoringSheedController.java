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
import javafx.stage.Stage;
import de.kreth.vereinsmeisterschaftprog.business.InputConverter;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;


public class ScoringSheedController extends BorderPane {

   private enum Kari {
      JUDGE1,
      JUDGE2,
      JUDGE3,
      JUDGE4,
      JUDGE5,
      DIFF
   }
   
   @FXML Label startername;
   @FXML Button btnChangeName;

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
   private Stage dialogStage;
   
   @FXML
   public void initialize() {
      kari1.focusedProperty().addListener(new InputChangeListener(Kari.JUDGE1));
      kari2.focusedProperty().addListener(new InputChangeListener(Kari.JUDGE2));
      kari3.focusedProperty().addListener(new InputChangeListener(Kari.JUDGE3));
      kari4.focusedProperty().addListener(new InputChangeListener(Kari.JUDGE4));
      kari5.focusedProperty().addListener(new InputChangeListener(Kari.JUDGE5));
      kariDiff.focusedProperty().addListener(new InputChangeListener(Kari.DIFF));
   }
   
   private class InputChangeListener implements ChangeListener<Boolean> {
      
      private TextField field; 
      private Kari kari;
      
      public InputChangeListener(Kari kari) {
         this.kari = kari;
         switch (kari) {
            case DIFF:
               field = kariDiff;
               break;
            case JUDGE1:
               field = kari1;
               break;
            case JUDGE2:
               field = kari2;
               break;
            case JUDGE3:
               field = kari3;
               break;
            case JUDGE4:
               field = kari4;
               break;
            case JUDGE5:
               field = kari5;
               break;
         }
      }
      
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

         if(newValue == Boolean.FALSE){
            if(wertung != null) {
               try {
                  double kariValue = converter.convert(field.getText());
                  field.setText(converter.format(kariValue));
                  switch (kari) {
                     case DIFF:
                        wertung.setSchwierigkeit(kariValue);
                        break;
                     case JUDGE1:
                        wertung.setKari1(kariValue);
                        break;
                     case JUDGE2:
                        wertung.setKari2(kariValue);
                        break;
                     case JUDGE3:
                        wertung.setKari3(kariValue);
                        break;
                     case JUDGE4:
                        wertung.setKari4(kariValue);
                        break;
                     case JUDGE5:
                        wertung.setKari5(kariValue);
                        break;
                  }
               } catch (ParseException e) {
                  e.printStackTrace();
               }
            }
            
         }
      }
      
   }
   
   private void resetInput(){
      wertung.setKari1(original.getKari1());
      wertung.setKari2(original.getKari1());
      wertung.setKari3(original.getKari1());
      wertung.setKari4(original.getKari1());
      wertung.setKari5(original.getKari1());
      wertung.setSchwierigkeit(original.getSchwierigkeit());
   }
   
   public void setErgebnis(String starterName, Wertung round) {
      if(this.wertung != null) {
         this.wertung.removePropertyChangeListener(scoringErgebnisListener);
      }
      this.startername.setText(starterName);
      this.wertung = round;
      this.original = round.clone();
      kari1.setText(converter.format(round.getKari1()));
      kari2.setText(converter.format(round.getKari2()));
      kari3.setText(converter.format(round.getKari3()));
      kari4.setText(converter.format(round.getKari4()));
      kari5.setText(converter.format(round.getKari5()));
      kariDiff.setText(converter.format(round.getSchwierigkeit()));
      result.setText(converter.format(round.getErgebnis()));
      wertung.addPropertyChangeListener(scoringErgebnisListener);
   }
   
   private class ScoringErgebnisPropertyChangeListener implements PropertyChangeListener {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         if(evt.getPropertyName().matches(Wertung.ERGEBNIS_CHANGE_PROPERTY)) {
            result.setText(converter.format(wertung.getErgebnis()));
         }
      }
      
   }
   
   public void onCancelClick() {
      resetInput();
      dialogStage.close();
   }
   
   public void onOkClick () {
      dialogStage.close();      
   }

   public void setStage(Stage dialogStage) {
      this.dialogStage = dialogStage;
   }
   
}
