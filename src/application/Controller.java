package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Controller implements Initializable {
	
	    @FXML
	    private Button button_one, button_two, button_three, button_four, button_five, button_six, button_seven, button_eight, button_nine;
	    
	    @FXML
	    private Canvas canvas;
	
	    @FXML
	    private ToggleButton button_normal, button_medium, button_hard;
	
	    @FXML
	    private ToggleGroup difficultyToggleGroup;
	
	    private GameBoard gameboard;
	    GraphicsContext context;
	
	    private int player_selected_row = -1;
	    private int player_selected_col = -1;
	
	    @Override
	    public void initialize(URL arg0, ResourceBundle arg1) {
	        context = canvas.getGraphicsContext2D();
	        initializeGameWithDefaultDifficulty(); // Initialize game with default difficulty
	    }

	    
	
	    @FXML
	    private void newGame() {
	        setDifficulty("Normal");
	        drawOnCanvas(canvas.getGraphicsContext2D());
	    }
	
	    @FXML
	    private void check() {
	        boolean isCorrect = gameboard.check();
	        if (isCorrect) {
	            showAlert("Congratulations!", "You solved the puzzle correctly.");
	        } else {
	            showAlert("Incorrect Solution", "Your solution is not correct. Please try again.");
	        }
	    }
	
	    @FXML
	    private void reset() {
	        gameboard.resetPlayer();
	        player_selected_row = player_selected_col = -1;
	        drawOnCanvas(canvas.getGraphicsContext2D());
	    }
	
	    @FXML
	    private void canvasMouseClicked() {
	        canvas.setOnMouseClicked(e -> {
	            int mouse_x = (int) e.getX();
	            int mouse_y = (int) e.getY();
	            player_selected_row = (int) (mouse_y / 50);
	            player_selected_col = (int) (mouse_x / 50);
	            drawOnCanvas(canvas.getGraphicsContext2D());
	        });
	    }
	
	    private int getNumberOfClues(String difficulty) {
	        switch (difficulty) {
	            case "Normal":
	                return 30; // Return the number of clues for Normal difficulty
	            case "Medium":
	                return 40; // Return the number of clues for Medium difficulty
	            case "Hard":
	                return 60; // Return the number of clues for Hard difficulty
	            default:
	                return 30; // Default to Normal difficulty if unknown difficulty is provided
	        }
	    }
	    
	    @FXML
	    private void setDifficulty(String difficulty) {
	        switch (difficulty) {
	            case "Normal":
	                gameboard = new NormalGameBoard(9);
	                break;
	            case "Medium":
	                gameboard = new MediumGameBoard(9);
	                break;
	            case "Hard":
	                gameboard = new HardGameBoard(9);
	                break;
	            default:
	                break;
	        }
	        
	        int numberOfClues = getNumberOfClues(difficulty); // Get the number of clues based on difficulty
	        gameboard.newValues(numberOfClues); // Generate new game values (clues) based on the selected difficulty
	        player_selected_row = player_selected_col = -1; // Reset player selection
	        drawOnCanvas(context); // Redraw canvas with new game board
	    }

	    @FXML
	    private void setDifficultyNormal(MouseEvent event) {
	        setDifficulty("Normal");
	    }

	    @FXML
	    private void setDifficultyMedium(MouseEvent event) {
	        setDifficulty("Medium");
	    }

	    @FXML
	    private void setDifficultyHard(MouseEvent event) {
	        setDifficulty("Hard");
	    }


	    
	    private void initializeGameWithDefaultDifficulty() {
	        setDifficulty("Normal"); // Initialize with default difficulty
	        drawOnCanvas(context); // Draw initial game board
	    }




	    
	    private void drawOnCanvas(GraphicsContext context) {
	        int[][] initial = gameboard.getInitial();
	        int[][] player = gameboard.getPlayer();

	        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	        double cellSize = 50.0;

	        for (int row = 0; row < 9; row++) {
	            for (int col = 0; col < 9; col++) {
	                double x = col * cellSize;
	                double y = row * cellSize;
	                int value = player[row][col];
	                Color fillColor = initial[row][col] != 0 ? Color.LIGHTGRAY : Color.WHITE;

	                if (row == player_selected_row && col == player_selected_col) {
	                    fillColor = Color.LIGHTBLUE; // highlight color
	                }

	                context.setFill(fillColor);
	                context.fillRect(x, y, cellSize, cellSize);
	                context.setStroke(Color.BLACK);
	                context.strokeRect(x, y, cellSize, cellSize);

	                if (value != 0) {
	                    context.setFill(Color.BLACK);
	                    context.setFont(Font.font(cellSize * 0.6));
	                    context.fillText(Integer.toString(value), x + cellSize * 0.35, y + cellSize * 0.7);
	                }
	            }
	        }
	    }
	
	    private void showAlert(String title, String content) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(content);
	        alert.showAndWait();
	    }
	
	    @FXML
	    public void buttonPressed(javafx.event.ActionEvent event) {
	        Button clickedButton = (Button) event.getSource();
	        int value = Integer.parseInt(clickedButton.getText());

	        if (player_selected_row != -1 && player_selected_col != -1) {
	            gameboard.modifyPlayer(value, player_selected_row, player_selected_col);
	            drawOnCanvas(context);
	        }
	    }
	    
	    @FXML
	    private void buttonClicked(ActionEvent event) {
	        // Ensure the event source is a Button
	        if (event.getSource() instanceof Button) {
	            Button clickedButton = (Button) event.getSource();
	            String buttonText = clickedButton.getText();
	            setDifficulty(buttonText); // Set difficulty based on button text
	        }
	    }
	}
