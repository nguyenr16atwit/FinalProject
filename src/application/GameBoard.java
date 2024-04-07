package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
