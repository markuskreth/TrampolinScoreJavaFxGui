package de.kreth.trampolinscore.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class GroupDialogController {

   @FXML TextField newGroupDlgName;
   @FXML TextField newGroupDlgDescription;
   
   private MainController mainController;
   private Stage stage;
   
   public void setMainController(MainController mainController) {
      this.mainController = mainController;      
   }
   
   public void createNewGroupFromDialog() {
      mainController.createNewGroupFromDialog(newGroupDlgName.getText(), newGroupDlgDescription.getText());
      stage.close();
   }

   public void setStage(Stage dialogStage) {
      this.stage = dialogStage;
   }

   public void closeDialog() {
      stage.close();
   }
}
