package de.kreth.vereinsmeisterschaft.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.controlsfx.dialog.Dialogs;

import de.kreth.vereinsmeisterschaftprog.business.CompetitionBusiness;
import de.kreth.vereinsmeisterschaftprog.business.InputConverter;
import de.kreth.vereinsmeisterschaftprog.business.MainBusiness;
import de.kreth.vereinsmeisterschaftprog.data.*;
import de.kreth.vereinsmeisterschaftprog.views.CompetitionView;
import de.kreth.vereinsmeisterschaftprog.views.MainView;

public class MainController extends BorderPane implements MainView, CompetitionView {

   private enum Column {
      STARTER,
      PFLICHT,
      KUER,
      RESULT,
      PLATZ,
      BUTTON
   }
   
   private MainBusiness business;
   
   @FXML Button newGroup;
   @FXML Button export;

   @FXML Button newStarter;
   @FXML ChoiceBox<Durchgang> cbDurchgang;
   @FXML ListView<CompetitionGroup> gruppenList;
   @FXML ChoiceBox<Sortierung> cbSorting;
   @FXML TableView<Result> tblErgebnisse;
   @FXML TableColumn<Result, String> starterCol;
   @FXML TableColumn<Result, String> pflichtCol;
   @FXML TableColumn<Result, String> kuerCol;
   @FXML TableColumn<Result, String> resultCol;
   @FXML TableColumn<Result, String> placeCol;
   @FXML TableColumn<Result, Button> wertungButtonCol;
   
   private Stage primaryStage;

   private Stage dialogStage;

   private CompetitionBusiness competitionBusiness;

   private Competition currentCompetition = null;
   private Map<Result, SimpleObjectProperty<Button>> resultButtonMapper;
   
   @FXML
   public void initialize() {

      if(business == null) {
         final InputConverter converter = new InputConverter();
         
         business = new MainBusiness(this);
         resultButtonMapper = new HashMap<>();

         final ResourceBundle mainBundle = ResourceBundle.getBundle("main", Locale.getDefault(), MainController.class.getClassLoader());
         
         cbDurchgang.getItems().addAll(Durchgang.values());
         cbDurchgang.getSelectionModel().selectFirst();
         cbSorting.getItems().addAll(Sortierung.values());
         cbSorting.getSelectionModel().selectFirst();
         
         List<CompetitionGroup> gruppen = business.getCompetitionGroups();
         gruppenList.getItems().addAll(gruppen);
         gruppenList.getSelectionModel().selectFirst();
         gruppenList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CompetitionGroup>() {

            @Override
            public void changed(ObservableValue<? extends CompetitionGroup> observable, CompetitionGroup oldValue, CompetitionGroup newValue) {
               business.pflichtChange(newValue);
            }
         });
         
         starterCol.setCellValueFactory(createCellValueFactory(converter, Column.STARTER));
         pflichtCol.setCellValueFactory(createCellValueFactory(converter, Column.PFLICHT));
         kuerCol.setCellValueFactory(createCellValueFactory(converter, Column.KUER));
         resultCol.setCellValueFactory(createCellValueFactory(converter, Column.RESULT));
         placeCol.setCellValueFactory(createCellValueFactory(converter, Column.PLATZ));
         
         
         wertungButtonCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Result,Button>, ObservableValue<Button>>() {
            
            @Override
            public ObservableValue<Button> call(final CellDataFeatures<Result, Button> param) {

               Result result = param.getValue();
               
               SimpleObjectProperty<Button> buttonProperty = resultButtonMapper.get(result);
               
               if(buttonProperty == null) {
                  Button btn;
                  buttonProperty = new SimpleObjectProperty<Button>();
                  btn = new Button(mainBundle.getString("main.score"));
                  btn.setOnAction(new ButtonEventHandler(result));
                  buttonProperty.set(btn);
                  resultButtonMapper.put(result, buttonProperty);
               }
               
               return buttonProperty;
            }
            
         });

         competitionBusiness = business.getCompetitionBusiness();
         competitionBusiness.setView(this);
      }
   }
   
   private class ButtonEventHandler implements EventHandler<ActionEvent> {

      
      private Result ergebnis;

      public ButtonEventHandler(Result ergebnis) {
         this.ergebnis = ergebnis;
      }
      
      @Override
      public void handle(ActionEvent event) {
         Durchgang selectedDurchgang = cbDurchgang.getSelectionModel().getSelectedItem();
         competitionBusiness.werteErgebnis(ergebnis, selectedDurchgang);
      }
      
   }
   
   private Callback<TableColumn.CellDataFeatures<Result,String>, ObservableValue<String>> createCellValueFactory(final InputConverter converter,final Column col) {
      
      return new Callback<TableColumn.CellDataFeatures<Result,String>, ObservableValue<String>>() {

         @Override
         public ObservableValue<String> call(CellDataFeatures<Result, String> param) {
            SimpleStringProperty property = new SimpleStringProperty();
            switch (col) {
               case KUER:
                  property.setValue(converter.format(param.getValue().getKuer().getErgebnis()));
                  break;
               case PFLICHT:
                  property.setValue(converter.format(param.getValue().getPflicht().getErgebnis()));
                  break;
               case RESULT:
                  property.setValue(converter.format(param.getValue().getErgebnis()));
                  break;
               case STARTER:
                  property.setValue(param.getValue().getStarterName());
                  break;
               case PLATZ:
                  property.setValue(param.getValue().getPlatz() + ".");
                  break;
               default:
                  break;
            }
            return property;
         }
         
      };
      
   }
   
   @Override
   public void showNewGruppeDialog() {

      ResourceBundle mainBundle = ResourceBundle.getBundle("new_group_dialog", Locale.getDefault(), getClass().getClassLoader());
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("NewGroupDialog.fxml"), mainBundle);
         BorderPane root = loader.load();
         Scene scene = new Scene(root,400,200);
         dialogStage = new Stage();
         dialogStage.initOwner(primaryStage);
         dialogStage.initModality(Modality.APPLICATION_MODAL);
         GroupDialogController contr = loader.getController();
         contr.setMainController(this);
         contr.setStage(dialogStage);
         dialogStage.setScene(scene);
         dialogStage.showAndWait();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   public void showNewStarterDialog () {
      
      ResourceBundle starterDialogBundle = ResourceBundle.getBundle("new_starter_dialog", Locale.getDefault(), getClass().getClassLoader());
      Optional<String> showTextInput = Dialogs.create()
                                             .owner(null)
                                             .title(starterDialogBundle.getString("title"))
                                             .message(starterDialogBundle.getString("starterName"))
                                             .showTextInput();
      if(showTextInput.isPresent())
         createNewStarter(showTextInput.get());
   }
   
   public void createNewGroupFromDialog(String groupName, String groupDescription) {
      business.createGroup(groupName, groupDescription);
   }
   
   public void createNewStarter(String starterName){
      competitionBusiness.newStarter(starterName);
   }
   
   public void export() {
      business.doExportGroup();
   }

   @Override
   public void groupsChanged() {

      List<CompetitionGroup> gruppen = business.getCompetitionGroups();
      gruppenList.getItems().clear();
      gruppenList.getItems().addAll(gruppen);      
   }

   public void setPrimaryStage(Stage primaryStage) {
      this.primaryStage = primaryStage;
   }

   @Override
   public void setCompetition(Competition wettkampf) {
      
      if(currentCompetition!= null)
         currentCompetition.removePropertyChangeListener(listener);
      
      currentCompetition = wettkampf;
      List<Result> ergebnisse = wettkampf.getErgebnisse();
      
      refreshErgebnisTable(ergebnisse);
      
      wettkampf.addPropertyChangeListener(listener);
   }

   private CompetitionChangeListener listener = new CompetitionChangeListener();
   
   private void refreshErgebnisTable(List<Result> ergebnisse) {
      this.tblErgebnisse.getItems().clear();
      this.tblErgebnisse.getItems().addAll(ergebnisse);
   }

   private class CompetitionChangeListener implements PropertyChangeListener {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         refreshErgebnisTable(currentCompetition.getErgebnisse());
      }
      
   }
   
   @Override
   public void showWertung(String starterName, Wertung wertung) {

      ResourceBundle mainBundle = ResourceBundle.getBundle("scoringsheet", Locale.getDefault(), getClass().getClassLoader());
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("ScoringSheed.fxml"), mainBundle);
         BorderPane root = loader.load();
         Scene scene = new Scene(root,600,300);
         dialogStage = new Stage();
         dialogStage.initOwner(primaryStage);
         dialogStage.initModality(Modality.APPLICATION_MODAL);
         ScoringSheedController contr = loader.getController();
         contr.setErgebnis(starterName, wertung);
         contr.setStage(dialogStage);
         dialogStage.setScene(scene);
         dialogStage.showAndWait();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
}
