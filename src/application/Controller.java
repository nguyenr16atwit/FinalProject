package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Controller implements Initializable {

    @FXML Button button_one;
    @FXML Button button_two;
    @FXML Button button_three;
    @FXML Button button_four;
    @FXML Button button_five;
    @FXML Button button_six;
    @FXML Button button_seven;
    @FXML Button button_eight;
    @FXML Button button_nine;
    @FXML Canvas canvas;
    @FXML ToggleButton button_normal;
    @FXML ToggleButton button_medium;
    @FXML ToggleButton button_hard;
    @FXML ToggleGroup difficultyToggleGroup;
    GameBoard gameboard;
    

    private int player_selected_row = -1;
    private int player_selected_col = -1;
    private Color line_color = Color.WHITE;
    
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("Start");
        gameboard = new GameBoard(9, 30);
        GraphicsContext context = canvas.getGraphicsContext2D();
        drawOnCanvas(context);
        
        difficultyToggleGroup = new ToggleGroup();
        button_normal.setToggleGroup(difficultyToggleGroup);
        button_medium.setToggleGroup(difficultyToggleGroup);
        button_hard.setToggleGroup(difficultyToggleGroup);
    }
    
    
    @FXML
    public void newGame() {
        System.out.println("New Game");

        int numberOfClues = 0;

        // Get the selected toggle from the toggle group
        Toggle selectedToggle = difficultyToggleGroup.getSelectedToggle();

        if (selectedToggle != null) {
            if (selectedToggle == button_normal) {
                numberOfClues = 25; // Normal difficulty
            } else if (selectedToggle == button_medium) {
                numberOfClues = 40; // Medium difficulty
            } else if (selectedToggle == button_hard) {
                numberOfClues = 55; // Hard difficulty
            }
        } else {
            numberOfClues = 25;
        }

        gameboard.newValues(numberOfClues);
        player_selected_row = player_selected_col = -1;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    public void check() {
        System.out.println("Check");
        boolean isCorrect = gameboard.check();

        if (isCorrect) {
            showAlert("", "Congratulations! You solved the puzzle correctly.");
        } else {
            showAlert("", "Your solution is not correct. Please try again.");
        }
    }
    
    @FXML
    public void reset() {
        System.out.println("Reset");
        gameboard.resetPlayer();
        player_selected_row = player_selected_col = -1;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    public void selectNormalDifficulty() {
        System.out.println("Selected Normal Difficulty");
        initializeGame(9, 17); // Customize based desired difficulty settings
    }

    @FXML
    public void selectMediumDifficulty() {
        System.out.println("Selected Medium Difficulty");
        initializeGame(9, 30); // Customize based desired difficulty settings
    }

    @FXML
    public void selectHardDifficulty() {
        System.out.println("Selected Hard Difficulty");
        initializeGame(9, 50); // Customize based desired difficulty settings
    }

    private void initializeGame(int boardSize, int numberOfClues) {
        gameboard = new GameBoard(boardSize, numberOfClues);
        GraphicsContext context = canvas.getGraphicsContext2D();
        drawOnCanvas(context);
    }
   
    
    private void drawOnCanvas(GraphicsContext context) {

        int initial[][] = gameboard.getInitial();
        int[][] player = gameboard.getPlayer();
        context.clearRect(0, 0, 450, 450);
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int position_y = row * 50 + 2;
                int position_x = col * 50 + 2;
                int width = 46;
                if (initial[row][col] != 0) {
                    context.setFill(Color.rgb(175, 175, 175));
                } else if (player[row][col] != 0) {
                    context.setFill(Color.rgb(225, 225, 125));
                } else {
                    context.setFill(Color.rgb(225, 225, 225));
                }
                context.fillRoundRect(position_x, position_y, width, width, 10, 10);
            }
        }

        context.setStroke(Color.rgb(193, 87, 87));
        context.setLineWidth(3);
        if (player_selected_col > -1) {
            context.strokeRoundRect(player_selected_col * 50 + 2, player_selected_row * 50 + 2, 46, 46, 10, 10);
        }

        for(int row = 0; row<9; row++) {
            for(int col = 0; col<9; col++) {

                int position_y = row * 50 + 35;
                int position_x = col * 50 + 15;

                context.setFill(Color.BLACK);
                context.setFont(new Font(30));

                if(player[row][col]!=0) {
                    String txt = Integer.toString(player[row][col]);
                    context.fillText(txt, position_x, position_y);
                }
            }
        }
        context.setStroke(line_color);
        for (int i = 1; i <= 2; i++) {
            context.strokeLine(i * 150, 0, i * 150, 450);
            context.strokeLine(0, i * 150, 450, i * 150);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public void canvasMouseClicked() {
        canvas.setOnMouseClicked(e->{
            int mouse_x = (int) e.getX();
            int mouse_y = (int) e.getY();
            player_selected_row = (int) (mouse_y / 50); // update player selected row
            player_selected_col = (int) (mouse_x / 50); // update player selected column
            drawOnCanvas(canvas.getGraphicsContext2D());
        });
    }

    public void buttonOnePressed() {
        System.out.println(1);
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        gameboard.modifyPlayer(1, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonTwoPressed() {
        System.out.println(2);
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        gameboard.modifyPlayer(2, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonThreePressed() {
        System.out.println(3);
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        gameboard.modifyPlayer(3, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonFourPressed() {
        System.out.println(4);
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        gameboard.modifyPlayer(4, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonFivePressed() {
        System.out.println(5);
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        gameboard.modifyPlayer(5, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonSixPressed() {
        System.out.println(6);
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        gameboard.modifyPlayer(6, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonSevenPressed() {
        System.out.println(7);
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        gameboard.modifyPlayer(7, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonEightPressed() {
        System.out.println(8);
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        gameboard.modifyPlayer(8, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonNinePressed() {
        System.out.println(9);
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        gameboard.modifyPlayer(9, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public class GameBoard {
        int N;
        int SRN;
        int K;
        private int[][] solution;
        private int[][] initial;
        private int[][] player;

        public GameBoard(int N, int K) {
            this.N = N;
            this.K = K;
            this.SRN = (int) Math.sqrt(N);
            solution = new int[N][N];
            initial = new int[N][N];
            player = new int[N][N];
            resetPlayer();
        }

        public void resetPlayer() {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    player[row][col] = initial[row][col];
                }
            }
        }

        public int[][] getPlayer() {
            return player;
        }
        public int[][] getInitial() {
            return initial;
        }
        public void modifyPlayer(int val, int row, int col) {
            if (val >= 0 && val <= 9)
                player[row][col] = val;
            else
                System.out.println("Value passed to player falls out of range");
        }

        Boolean check() {
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < N; col++) {
                    if (player[row][col] != solution[row][col]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public void newValues(int numberOfClues) {
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < N; col++) {
                    solution[row][col] = 0;
                    initial[row][col] = 0;
                    player[row][col] = 0;
                }
            }
            fillDiagonal();
            fillRemaining(0, SRN);
            removeKDigits(numberOfClues);
            resetPlayer();
        }

        // Fill the diagonal SRN number of SRN x SRN matrices
        void fillDiagonal() {
            for (int i = 0; i < N; i = i + SRN) {
                fillBox(i, i);
            }
        }

        // Returns false if given 3 x 3 block contains num.
        boolean unUsedInBox(int rowStart, int colStart, int num) {
            for (int i = 0; i < SRN; i++)
                for (int j = 0; j < SRN; j++)
                    if (solution[rowStart + i][colStart + j] == num)
                        return false;

            return true;
        }

        // Fill a 3 x 3 matrix.
        void fillBox(int row, int col) {
            int num;
            for (int i = 0; i < SRN; i++) {
                for (int j = 0; j < SRN; j++) {
                    do {
                        num = randomGenerator(N);
                    }
                    while (!unUsedInBox(row, col, num));
                    solution[row + i][col + j] = num;
                }
            }
        }

        // Random generator
        int randomGenerator(int num) {
            return (int) Math.floor((Math.random() * num + 1));
        }

        // Check if safe to put in cell
        boolean CheckIfSafe(int i, int j, int num) {
            return (unUsedInRow(i, num) &&
                    unUsedInCol(j, num) &&
                    unUsedInBox(i - i % SRN, j - j % SRN, num));
        }

        // check in the row for existence
        boolean unUsedInRow(int i, int num) {
            for (int j = 0; j < N; j++)
                if (solution[i][j] == num)
                    return false;
            return true;
        }

        // check in the row for existence
        boolean unUsedInCol(int j, int num) {
            for (int i = 0; i < N; i++)
                if (solution[i][j] == num)
                    return false;
            return true;
        }

        // A recursive function to fill remaining
        // matrix
        boolean fillRemaining(int i, int j) {
            if (j >= N && i < N - 1) {
                i = i + 1;
                j = 0;
            }
            if (i >= N && j >= N)
                return true;

            if (i < SRN) {
                if (j < SRN)
                    j = SRN;
            } else if (i < N - SRN) {
                if (j == (int) (i / SRN) * SRN)
                    j = j + SRN;
            } else {
                if (j == N - SRN) {
                    i = i + 1;
                    j = 0;
                    if (i >= N)
                        return true;
                }
            }

            for (int num = 1; num <= N; num++) {
                if (CheckIfSafe(i, j, num)) {
                    solution[i][j] = num;
                    if (fillRemaining(i, j + 1))
                        return true;

                    solution[i][j] = 0;
                }
            }
            return false;
        }

        // Remove the K no. of digits to
        // complete game
        public void removeKDigits(int numberOfClues) {
            List<Integer> positions = new ArrayList<>();
            for (int i = 0; i < N * N; i++) {
                positions.add(i);
            }
            Collections.shuffle(positions);

            int cellsToKeep = N * N - numberOfClues;
            for (int i = 0; i < cellsToKeep; i++) {
                int pos = positions.get(i);
                int row = pos / N;
                int col = pos % N;
                initial[row][col] = solution[row][col];
            }

            // Clear remaining cells
            for (int i = cellsToKeep; i < N * N; i++) {
                int pos = positions.get(i);
                int row = pos / N;
                int col = pos % N;
                initial[row][col] = 0;
            }
        }
    

    }
}