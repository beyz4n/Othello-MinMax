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


            if (isValidMove(row, col)) {
                makeMove(row, col);
                printBoard();
                if (isGameOver()) {
                    break;
                }
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


}
