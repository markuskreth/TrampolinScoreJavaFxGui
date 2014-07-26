package de.kreth.vereinsmeisterschaft.gui;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import de.kreth.vereinsmeisterschaftprog.business.MainBusiness;
import de.kreth.vereinsmeisterschaftprog.data.*;
import de.kreth.vereinsmeisterschaftprog.views.MainView;

public class MainController extends BorderPane implements MainView {

   private MainBusiness business;
   
   @FXML Button newGroup;
   @FXML Button export;

   @FXML Button newStarter;
   @FXML ChoiceBox<Durchgang> cbDurchgang;
   @FXML ListView<Gruppe> gruppenList;
   @FXML ChoiceBox<Sortierung> cbSorting;
   @FXML TableView<Ergebnis> tblErgebnisse;

   private Stage primaryStage;

   private Stage dialogStage;
   
   @FXML
   public void initialize() {

      if(business == null) {
         business = new MainBusiness(this);
         cbDurchgang.getItems().addAll(Durchgang.values());
         cbDurchgang.getSelectionModel().selectFirst();
         cbSorting.getItems().addAll(Sortierung.values());
         cbSorting.getSelectionModel().selectFirst();
         List<Gruppe> gruppen = business.getGruppen();
         gruppenList.getItems().addAll(gruppen);
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
   
   public void createNewGroupFromDialog(String groupName, String groupDescription) {
      business.createGroup(groupName, groupDescription);
   }
   
   public void export() {
      business.doExport();
   }

   @Override
   public void groupsChanged() {

      List<Gruppe> gruppen = business.getGruppen();
      gruppenList.getItems().clear();
      gruppenList.getItems().addAll(gruppen);      
   }

   public void setPrimaryStage(Stage primaryStage) {
      this.primaryStage = primaryStage;
   }
   
}
