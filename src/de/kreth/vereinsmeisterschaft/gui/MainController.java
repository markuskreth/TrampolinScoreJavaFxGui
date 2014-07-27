package de.kreth.vereinsmeisterschaft.gui;

import java.io.IOException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import de.kreth.vereinsmeisterschaftprog.business.CompetitionBusiness;
import de.kreth.vereinsmeisterschaftprog.business.MainBusiness;
import de.kreth.vereinsmeisterschaftprog.data.*;
import de.kreth.vereinsmeisterschaftprog.views.CompetitionView;
import de.kreth.vereinsmeisterschaftprog.views.MainView;

public class MainController extends BorderPane implements MainView, CompetitionView {

   private MainBusiness business;
   
   @FXML Button newGroup;
   @FXML Button export;

   @FXML Button newStarter;
   @FXML ChoiceBox<Durchgang> cbDurchgang;
   @FXML ListView<CompetitionGroup> gruppenList;
   @FXML ChoiceBox<Sortierung> cbSorting;
   @FXML TableView<Ergebnis> tblErgebnisse;
   @FXML TableColumn<Ergebnis, String> starterCol;
   @FXML TableColumn pflichtCol;
   @FXML TableColumn kuerCol;
   @FXML TableColumn resultCol;
   @FXML TableColumn placeCol;
   

   private Stage primaryStage;

   private Stage dialogStage;

   private CompetitionBusiness currentCompetition;
   
   @FXML
   public void initialize() {

      if(business == null) {
         business = new MainBusiness(this);
         cbDurchgang.getItems().addAll(Durchgang.values());
         cbDurchgang.getSelectionModel().selectFirst();
         cbSorting.getItems().addAll(Sortierung.values());
         cbSorting.getSelectionModel().selectFirst();
         List<CompetitionGroup> gruppen = business.getGruppen();
         gruppenList.getItems().addAll(gruppen);
         gruppenList.getSelectionModel().selectFirst();
         currentCompetition = business.getCompetitionBusiness();
         currentCompetition.setView(this);
         starterCol.setCellValueFactory(new PropertyValueFactory<Ergebnis, String>("starterName"));
         pflichtCol.setCellValueFactory(new PropertyValueFactory<Ergebnis, String>("pflicht"));
      }
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
      business.getCompetitionBusiness().newStarter(starterName);
   }
   
   public void export() {
      business.doExportGroup();
   }

   @Override
   public void groupsChanged() {

      List<CompetitionGroup> gruppen = business.getGruppen();
      gruppenList.getItems().clear();
      gruppenList.getItems().addAll(gruppen);      
   }

   public void setPrimaryStage(Stage primaryStage) {
      this.primaryStage = primaryStage;
   }

   @Override
   public void setCompetition(Competition wettkampf) {
      this.tblErgebnisse.getItems().addAll(wettkampf.getErgebnisse());
   }

   @Override
   public void showWertung(String starterName, Wertung wertung) {
      
   }
   
}
