import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Othello {
    static final int SIZE = 8;
    static char[][] board = new char[SIZE][SIZE];
    static char currentPlayer = 'X';
    static String heuristic = "h1";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("For human player vs human player enter 1,\n" +
                "For human player vs AI player enter 2,\n" +
                "For AI player vs AI player enter 3 :");
        int gameMethod = scanner.nextInt();

        if (gameMethod == 1) {
            humanVsHuman();
        } else if (gameMethod == 2) {
            humanVsAI();
        } else {
            System.out.println("Invalid input. Try again.");
        }
    }

    public static void humanVsHuman() {
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
                if (isGameOver()) break;
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }

        System.out.println("Game Over!");
    }

    public static void humanVsAI() {
        initializeBoard();
        printBoard();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select heuristic for AI (enter h1, h2 or h3): ");
        String heuristic = scanner.nextLine();

        if (heuristic.equals("h1")) {
            heuristic = "h1";            
        } else if (heuristic.equals("h2")) {
            heuristic = "h2";
        } else if (heuristic.equals("h3")) {
            heuristic = "h3";
        }
        else {
            System.out.println("Invalid input. Try again.");
        }


        while (true) {
            if (currentPlayer == 'X') {
                // Human player
                System.out.println("Player X's turn.");
                System.out.print("Enter column (a-h) and row (1-8): ");
                String input = scanner.nextLine();
                String[] parts = input.split(" ");
                char colChar = parts[0].charAt(0);
                int row = Integer.parseInt(parts[1]) - 1;
                int col = colChar - 'a';

                if (isValidMove(row, col)) {
                    makeMove(row, col);
                    printBoard();
                    if (isGameOver()) break;
                    currentPlayer = 'O';
                } else {
                    System.out.println("Invalid move. Try again.");
                }
            } else {
                // AI player
                System.out.println("AI's turn.");
                int[] bestAction = alphaBetaSearch(board);
                makeMove(bestAction[0], bestAction[1]);
                printBoard();
                if (isGameOver()) break;
                currentPlayer = 'X';
            }
        }

        System.out.println("Game Over!");
    }

    public static void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '.';
            }
        }
        board[3][3] = 'O';
        board[4][4] = 'O';
        board[3][4] = 'X';
        board[4][3] = 'X';
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

        for (int rowStep = -1; rowStep <= 1; rowStep++) {
            for (int colStep = -1; colStep <= 1; colStep++) {
                if (rowStep == 0 && colStep == 0) continue;
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

        while (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
            if (board[newRow][newCol] == '.') {
                return false;
            } else if (board[newRow][newCol] == (currentPlayer == 'X' ? 'O' : 'X')) {
                hasOpponent = true;
            } else if (hasOpponent && board[newRow][newCol] == currentPlayer) {
                return true;
            } else {
                return false;
            }
            newRow += rowStep;
            newCol += colStep;
        }
        return false;
    }

    public static void makeMove(int row, int col) {
        board[row][col] = currentPlayer;
        for (int rowStep = -1; rowStep <= 1; rowStep++) {
            for (int colStep = -1; colStep <= 1; colStep++) {
                if (rowStep == 0 && colStep == 0) continue;
                if (isValidDirection(row, col, rowStep, colStep)) {
                    flipPieces(row, col, rowStep, colStep);
                }
            }
        }
    }

    public static void flipPieces(int row, int col, int rowStep, int colStep) {
        int nextRow = row + rowStep;
        int nextCol = col + colStep;

        while (board[nextRow][nextCol] != currentPlayer) {
            board[nextRow][nextCol] = currentPlayer;
            nextRow += rowStep;
            nextCol += colStep;
        }
    }

    public static boolean isGameOver() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j)) {
                    return false; 
                }
            }
        }
        return true; 
    }
    

    public static int[] alphaBetaSearch(char[][] state) {
        int bestValue = Integer.MIN_VALUE;
        int[] bestAction = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int depth = 5; // Maximum depth to search
    
        for (int[] action : actions(state)) {
            char[][] newState = result(state, action);
            int value = minValue(newState, alpha, beta, depth - 1);
            if (value > bestValue) {
                bestValue = value;
                bestAction = action;
            }
        }
        return bestAction;
    }
    
    public static int maxValue(char[][] state, int alpha, int beta, int depth) {
        if (isTerminal(state) || depth == 0) {
            if(heuristic.equals("h1")) {
                return h1(state);
            }
            else if(heuristic.equals("h2")) {
               // return h2(state);
            }
            else if (heuristic.equals("h3")) {
              //  return h3(state);
            }
           
        }
        int v = Integer.MIN_VALUE;
        for (int[] action : actions(state)) {
            char[][] newState = result(state, action);
            v = Math.max(v, minValue(newState, alpha, beta, depth - 1));
            if (v >= beta) {
                return v; 
            }
            alpha = Math.max(alpha, v);
        }
        return v;
    }
    
    public static int minValue(char[][] state, int alpha, int beta, int depth) {
        if (isTerminal(state) || depth == 0) {
            if(heuristic.equals("h1")) {
                return h1(state);
            }
            else if(heuristic.equals("h2")) {
               // return h2(state);
            }
            else if (heuristic.equals("h3")) {
              //  return h3(state);
            }
        }
        int v = Integer.MAX_VALUE;
        for (int[] action : actions(state)) {
            char[][] newState = result(state, action);
            v = Math.min(v, maxValue(newState, alpha, beta, depth - 1));
            if (v <= alpha) {
                return v; 
            }
            beta = Math.min(beta, v);
        }
        return v;
    }
    

    public static boolean isTerminal(char[][] state) {
        return isGameOver() || actions(state).isEmpty();
    }

    public static int h1(char[][] state) {
        int playerCount = 0, opponentCount = 0;
        char opponent = (currentPlayer == 'X') ? 'O' : 'X';

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (state[i][j] == currentPlayer) {
                    playerCount++;
                } else if (state[i][j] == opponent) {
                    opponentCount++;
                }
            }
        }
        return playerCount - opponentCount;
    }

    public static List<int[]> actions(char[][] state) {
        List<int[]> validActions = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (state[i][j] == '.' && isValidMove(i, j)) {
                    validActions.add(new int[]{i, j});
                }
            }
        }
        return validActions;
    }
    

    public static char[][] result(char[][] state, int[] action) {
        char[][] newState = copyBoard(state);
        int row = action[0];
        int col = action[1];
        newState[row][col] = currentPlayer;

        for (int rowStep = -1; rowStep <= 1; rowStep++) {
            for (int colStep = -1; colStep <= 1; colStep++) {
                if (rowStep == 0 && colStep == 0) continue;
                if (isValidDirectionOnState(newState, row, col, rowStep, colStep)) {
                    flipPiecesOnState(newState, row, col, rowStep, colStep);
                }
            }
        }
        return newState;
    }

    public static boolean isValidDirectionOnState(char[][] state, int row, int col, int rowStep, int colStep) {
        int newRow = row + rowStep;
        int newCol = col + colStep;
        boolean hasOpponent = false;

        while (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
            if (state[newRow][newCol] == '.') {
                return false;
            } else if (state[newRow][newCol] == (currentPlayer == 'X' ? 'O' : 'X')) {
                hasOpponent = true;
            } else if (hasOpponent && state[newRow][newCol] == currentPlayer) {
                return true;
            } else {
                return false;
            }
            newRow += rowStep;
            newCol += colStep;
        }
        return false;
    }

    public static void flipPiecesOnState(char[][] state, int row, int col, int rowStep, int colStep) {
        int nextRow = row + rowStep;
        int nextCol = col + colStep;

        while (state[nextRow][nextCol] != currentPlayer) {
            state[nextRow][nextCol] = currentPlayer;
            nextRow += rowStep;
            nextCol += colStep;
        }
    }

    public static char[][] copyBoard(char[][] state) {
        char[][] copy = new char[state.length][state[0].length];
        for (int i = 0; i < state.length; i++) {
            System.arraycopy(state[i], 0, copy[i], 0, state[i].length);
        }
        return copy;
    }
}
