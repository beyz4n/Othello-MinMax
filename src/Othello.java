import java.util.Scanner;

public class Othello {
    static final int SIZE = 8;
    static char[][] board = new char[SIZE][SIZE];
    static char currentPlayer = 'X';



    public static void main(String[] args) {
        initializeBoard();
        printBoard();


        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Player " + currentPlayer + "'s turn.");
            System.out.print("Enter column (a-h) and row (1-8): ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            char colChar = parts[0].charAt(0);
            int row = Integer.parseInt(parts[1]) - 1;
            int col = colChar - 'a';

            // Check if the move is valid
            if (isValidMove(row, col)) {
                // Make the move and print the board
                makeMove(row, col);
                printBoard();
                // Check if the game is over
                if (isGameOver()) {
                    break;
                }

                // Switch player
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Switch player
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }

        System.out.println("Game Over!");
        scanner.close();
    }



    public static void initializeBoard() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = '.';

        // Initialize the middle of the board
        board[3][3] = 'O';  // (4, 4) O
        board[4][4] = 'O';  // (5, 5) O
        board[3][4] = 'X';  // (4, 5) X
        board[4][3] = 'X';  // (5, 4) X

    }

    public static void printBoard() {
        System.out.print("  a b c d e f g h\n");
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static boolean isValidMove(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != '.') {
            return false;
        }

        // Check in all directions
        for (int rowStep = -1; rowStep <= 1; rowStep++) {
            for (int colStep = -1; colStep <= 1; colStep++) {
                // Skip the current cell
                if (rowStep == 0 && colStep == 0)
                    continue;
                // Check if the direction is valid
                if (isValidDirection(row, col, rowStep, colStep)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isValidDirection(int row, int col, int rowStep, int colStep) {
        int newRow = row + rowStep;
        int newCol = col + colStep;
        boolean hasOpponent = false;

        // Check if the direction is valid
        while (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
            // If the current cell is empty, that direction is not valid. exit with false
            if (board[newRow][newCol] == '.') {
                return false;
                // If the current cell is the opponent's piece continue to check the next cell
            } else if (board[newRow][newCol] == (currentPlayer == 'X' ? 'O' : 'X')) {
                hasOpponent = true;
                // If the current cell is the current player's piece and there is at least one opponent's piece then return true
            } else if (hasOpponent && board[newRow][newCol] == currentPlayer) {
                return true;
                // Otherwise, return false
            } else {
                return false;
            }
            // Move to the next cell
            newRow += rowStep;
            newCol += colStep;
        }
        return false;
    }

    // Make a move
    public static void makeMove(int row, int col) {
        // Place the current player's piece
        board[row][col] = currentPlayer;
        // Flip opponent pieces in all directions
        // Check in all directions
        for (int rowStep = -1; rowStep <= 1; rowStep++) {
            for (int colStep = -1; colStep <= 1; colStep++) {
                // Skip the current cell
                if (rowStep == 0 && colStep == 0)
                    continue;
                // Check if the direction is valid
                if (isValidDirection(row, col, rowStep, colStep)) {
                    // Flip opponent's pieces in that direction
                    flipPieces(row, col, rowStep, colStep);
                }
            }
        }
    }

    // Flip opponent's pieces in a given direction
    public static void flipPieces(int row, int col, int rowStep, int colStep) {
        int nextRow = row + rowStep;
        int nextCol = col + colStep;
        // If it is not current player's piece, flip it
        while (board[nextRow][nextCol] != currentPlayer) {
            // Flip the opponent's piece
            board[nextRow][nextCol] = currentPlayer;
            // Move to the next cell
            nextRow += rowStep;
            nextCol += colStep;
        }
    }

    // Check if the game is over
    public static boolean isGameOver() {
        // Check if the board is not capable of making a move
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }



}
