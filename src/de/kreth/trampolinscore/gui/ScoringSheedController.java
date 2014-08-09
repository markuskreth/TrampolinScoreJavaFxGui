package de.kreth.trampolinscore.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.ParseException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import de.kreth.trampolinscore.business.InputConverter;
import de.kreth.trampolinscore.data.RoutineType;
import de.kreth.trampolinscore.data.Routine;


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

   private Routine wertung;
   private Routine original;
   
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
                  BigDecimal kariValue = BigDecimal.valueOf(converter.convert(field.getText()));
                  field.setText(converter.format(kariValue.doubleValue()));
                  switch (kari) {
                     case DIFF:
                        wertung.setTariff(kariValue);
                        break;
                     case JUDGE1:
                        wertung.setJudge1(kariValue);
                        break;
                     case JUDGE2:
                        wertung.setJudge2(kariValue);
                        break;
                     case JUDGE3:
                        wertung.setJudge3(kariValue);
                        break;
                     case JUDGE4:
                        wertung.setJudge4(kariValue);
                        break;
                     case JUDGE5:
                        wertung.setJudge5(kariValue);
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
      wertung.setJudge1(original.getJudge1());
      wertung.setJudge2(original.getJudge1());
      wertung.setJudge3(original.getJudge1());
      wertung.setJudge4(original.getJudge1());
      wertung.setJudge5(original.getJudge1());
      wertung.setTariff(original.getTariff());
   }
   
   public void setErgebnis(String starterName, Routine round) {
      if(this.wertung != null) {
         this.wertung.removePropertyChangeListener(scoringErgebnisListener);
      }
      this.startername.setText(starterName);
      this.wertung = round;
      this.original = round.clone();
      kari1.setText(converter.format(round.getJudge1().doubleValue()));
      kari2.setText(converter.format(round.getJudge2().doubleValue()));
      kari3.setText(converter.format(round.getJudge3().doubleValue()));
      kari4.setText(converter.format(round.getJudge4().doubleValue()));
      kari5.setText(converter.format(round.getJudge5().doubleValue()));
      
      if(round.getRoutine()==RoutineType.COMPULSORY) {
         kariDiff.setDisable(true);
         kariDiff.setText("");
         kariDiff.setVisible(false);
      } else {
         kariDiff.setText(converter.format(round.getTariff().doubleValue()));
      }

      result.setText(converter.format(round.getResult().doubleValue()));
      
      wertung.addPropertyChangeListener(scoringErgebnisListener);
   }
   
   private class ScoringErgebnisPropertyChangeListener implements PropertyChangeListener {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         if(evt.getPropertyName().matches(Routine.RESULT_CHANGE_PROPERTY)) {
            result.setText(converter.format(wertung.getResult().doubleValue()));
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
