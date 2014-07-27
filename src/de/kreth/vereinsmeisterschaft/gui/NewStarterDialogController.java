package de.kreth.vereinsmeisterschaft.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class NewStarterDialogController {

   @FXML TextField starterName;
   
   private Stage stage;
   private MainController controller;

   public void setStage(Stage dialogStage) {
      this.stage = dialogStage;
   }

   public void setMainController(MainController mainController) {
      this.controller = mainController;
   }
   
   public void onCancel() {
      stage.close();      
   }
   
   public void onOk() {
      controller.createNewStarter(starterName.getText());
      stage.close();
   }
}
