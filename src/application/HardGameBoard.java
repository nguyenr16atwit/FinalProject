package application;

public class HardGameBoard extends GameBoard {
    public HardGameBoard(int N) {
        super(N);
    }

    @Override
    public void resetPlayer() {
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                player[row][col] = initial[row][col];
            }
        }
    }

    @Override
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
}

// Implement MediumGameBoard and HardGameBoard similarly...
