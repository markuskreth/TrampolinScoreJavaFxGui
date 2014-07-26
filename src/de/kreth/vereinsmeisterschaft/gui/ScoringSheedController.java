package de.kreth.vereinsmeisterschaft.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


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
}
