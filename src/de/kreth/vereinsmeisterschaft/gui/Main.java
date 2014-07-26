package de.kreth.vereinsmeisterschaft.gui;
	
import java.util.Locale;
import java.util.ResourceBundle;

import de.kreth.vereinsmeisterschaftprog.FactoryProductive;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
   
	@Override
	public void start(Stage primaryStage) {

      @SuppressWarnings("unused")
      FactoryProductive factoryProductive = new FactoryProductive();
      
		try {
		   FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main.fxml"));
		   ResourceBundle mainBundle = ResourceBundle.getBundle("main", Locale.getDefault(), MainController.class.getClassLoader());
		   loader.setResources(mainBundle);
		   BorderPane root = loader.load();
		   Scene scene = new Scene(root,900,600);
		   root.autosize();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			MainController controller = loader.getController();
			controller.setPrimaryStage(primaryStage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
