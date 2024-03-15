package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * @author Raymond Nguyen
 */

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		
		primaryStage.setTitle("Sudoku Puzzle Game");
		
		primaryStage.setScene(new Scene(root, 750, 500));
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
