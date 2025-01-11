import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OthelloGame {
    private static final int SIZE = 8;
    private static int heuristic = 1;
    private static int heuristic_AI1 = 1;
    private static int heuristic_AI2 = 1;
    private static int numberOfPlies = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please select the game method you want to use");
            System.out.println("1 for Human vs Human");
            System.out.println("2 for Human vs AI");
            System.out.println("3 for AI vs AI");
            int gameMethod;
            try {
                gameMethod = Integer.parseInt(scanner.nextLine());
                if (gameMethod < 1 || gameMethod > 3) {
                    System.out.println("Invalid input");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                continue;
            }

            if(gameMethod == 2){
                System.out.println("Please select the heuristic you want to use");
                System.out.println("1 for h1");
                System.out.println("2 for h2");
                System.out.println("3 for h3");
                try {
                    heuristic = Integer.parseInt(scanner.nextLine());
                    if (heuristic < 1 || heuristic > 3) {
                        System.out.println("Invalid input");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                    continue;
                }
            }

            if(gameMethod == 3){
                System.out.println("Please select the heuristic for AI 1");
                System.out.println("1 for h1");
                System.out.println("2 for h2");
                System.out.println("3 for h3");
                try {
                    heuristic_AI1 = Integer.parseInt(scanner.nextLine());
                    if (heuristic < 1 || heuristic > 3) {
                        System.out.println("Invalid input");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                    continue;
                }

                System.out.println("Please select the heuristic for AI 2");
                System.out.println("1 for h1");
                System.out.println("2 for h2");
                System.out.println("3 for h3");
                try {
                    heuristic_AI2 = Integer.parseInt(scanner.nextLine());
                    if (heuristic < 1 || heuristic > 3) {
                        System.out.println("Invalid input");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                    continue;
                }

            }

            if(gameMethod != 1) {

                System.out.println("Please select the number of plies you want to use");
                try {
                    numberOfPlies = Integer.parseInt(scanner.nextLine());
                    if (numberOfPlies < 1) {
                        System.out.println("Invalid input");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                    continue;
                }
            }
            System.out.println("Game Method: " + gameMethod);
            System.out.println("Heuristic: " + heuristic);
            System.out.println("Number of Plies: " + numberOfPlies);

            if(gameMethod == 1) {
                OthelloGame game = new OthelloGame();
                game.playHumanVsHuman();
            } else if(gameMethod == 2) {
                OthelloGame game = new OthelloGame();
                game.playHumanVsAI();
            } else if(gameMethod == 3) {
                OthelloGame game = new OthelloGame();
                game.playAIVsAI();
            }
        }

    }

    public void playHumanVsHuman() {
        char[][] board = initializeBoard();
        printBoard(board);

        char player = 'X';

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Player " + player + "'s turn.");
            System.out.println("Enter column (a-h) and row (1-8):");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            char colChar = parts[0].charAt(0);

            int row = Integer.parseInt(parts[1]) - 1;
            int col = colChar - 'a';

            System.out.println("Row: " + row + ", Column: " + col);

            List<int[]> moves = getValidMoves(board, player);
            for (int[] move : moves) System.out.println("Valid move: " + (char) ('a' + move[1]) + " " + (move[0] + 1));
            if(isValidMove(board, player, row, col)) {

                board = makeMove(board, player, row, col);
                printBoard(board);

                if(isGameOver(board, player)) {
                    break;
                }

                player = player == 'X' ? 'O' : 'X';

            }


        }

        // Game over, determine the winner
        int countofX = countStones(board, 'X');
        int countofO = countStones(board, 'O');

        System.out.println("Game Over!");
        System.out.println("Final Score: ");
        System.out.println("Player X: " + countofX);
        System.out.println("Player O: " + countofO);

        if (countofX > countofO) {
            System.out.println("Player 1 (X) wins!");
        } else if (countofO > countofX) {
            System.out.println("Player 2 (O) wins!");
        } else {
            System.out.println("It's a draw!");
        }


    }

    public static boolean isValidMove(char[][] board, char player, int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != '.') {
            return false;
        }

        for (int rowStep = -1; rowStep <= 1; rowStep++) {
            for (int colStep = -1; colStep <= 1; colStep++) {
                if (rowStep == 0 && colStep == 0)
                    continue;
                if (isValidDirection(board, row, col, player, rowStep, colStep)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isValidDirection(char[][] board, int row, int col, char player, int rowStep, int colStep) {
        int newRow = row + rowStep;
        int newCol = col + colStep;
        boolean hasOpponent = false;

        while (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
            if (board[newRow][newCol] == '.') {
                return false;
            } else if (board[newRow][newCol] == (player == 'X' ? 'O' : 'X')) {
                hasOpponent = true;
            } else if (hasOpponent && board[newRow][newCol] == player) {
                return true;
            } else {
                return false;
            }
            newRow += rowStep;
            newCol += colStep;
        }
        return false;
    }


    public static char[][] makeMove(char[][] board, char player, int row, int col) {
        board[row][col] = player;
        for (int rowStep = -1; rowStep <= 1; rowStep++) {
            for (int colStep = -1; colStep <= 1; colStep++) {
                if (rowStep == 0 && colStep == 0)
                    continue;
                if (isValidDirection(board, row, col, player, rowStep, colStep)) {
                    flipPieces(board, player, row, col, rowStep, colStep);
                }
            }
        }
        return board;
    }

    public static void flipPieces(char[][] board, char player, int row, int col, int rowStep, int colStep) {
        int nextRow = row + rowStep;
        int nextCol = col + colStep;

        while (board[nextRow][nextCol] != player) {
            board[nextRow][nextCol] = player;
            nextRow += rowStep;
            nextCol += colStep;
        }
    }

    public static boolean isGameOver(char[][] board, char player) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(board, player, i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int countStones(char[][] board, char player) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    public static List<int[]> getValidMoves(char[][] board, char player) {
        List<int[]> validMoves = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(board, player, i, j)) {
                    validMoves.add(new int[]{i, j});
                }
            }
        }

        return validMoves;
    }

    public  void playHumanVsAI() {
        char[][] board = initializeBoard();
        printBoard(board);
    
        char player = 'X';
        // Human player X and AI player O
    
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (player == 'X') {
                System.out.println("Player (Human) " + player + "'s turn.");
                System.out.println("Enter column (a-h) and row (1-8):");
                String input = scanner.nextLine();
                String[] parts = input.split(" ");
                char colChar = parts[0].charAt(0);
    
                int row = Integer.parseInt(parts[1]) - 1;
                int col = colChar - 'a';
    
                System.out.println("Row: " + row + ", Column: " + col);
    
                List<int[]> moves = getValidMoves(board, player);
                for (int[] move : moves) {
                    System.out.println("Valid move: " + (char) ('a' + move[1]) + " " + (move[0] + 1));
                }
    
                if (isValidMove(board, player, row, col)) {
                    board = makeMove(board, player, row, col);
                    printBoard(board);
    
                    if (isGameOver(board, player)) {
                        break;
                    }
                } else {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }
            } else {
                System.out.println("AI (Player " + player + ")'s turn.");
                List<int[]> aiMoves = getValidMoves(board, player);
                for (int[] move : aiMoves) System.out.println("Valid move: " + (char) ('a' + move[1]) + " " + (move[0] + 1));
                if (!aiMoves.isEmpty()) {
                    
                    int[] bestMove = findBestMove(board, player);
                    board = makeMove(board, player, bestMove[0], bestMove[1]);
                    System.out.println("AI moves to: " + (char) ('a' + bestMove[1]) + " " + (bestMove[0] + 1));
                    printBoard(board);
    
                    if (isGameOver(board, player)) {
                        break;
                    }
                } else {
                    System.out.println("AI has no valid moves.");
                }
            }
    
            // Switch players
            player = player == 'X' ? 'O' : 'X';
        }

        
    
        // Game over, determine the winner
        int countOfX = countStones(board, 'X');
        int countOfO = countStones(board, 'O');
    
        System.out.println("Game Over!");
        System.out.println("Final Score: ");
        System.out.println("Player (Human) X: " + countOfX);
        System.out.println("AI Player O: " + countOfO);
    
        if (countOfX > countOfO) {
            System.out.println("Player 1 (Human X) wins!");
        } else if (countOfO > countOfX) {
            System.out.println("AI (O) wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }
    
    public int[] findBestMove(char[][] board, char player1) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

    
        for (int[] move : getValidMoves(board, player1)) {
            char[][] tempBoard = copyBoard(board);
            tempBoard = makeMove(tempBoard, player1, move[0], move[1]);
            System.out.println("board in find best move");
            printBoard(tempBoard);
            int score = minimax(tempBoard, 0, false, player1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
    
        return bestMove;
    }



public int minimax(char[][] board, int depth, boolean isMaximizing, char player1, int alpha, int beta) {
    if (isGameOver(board, player1) || depth == numberOfPlies) {
        if (heuristic == 1) {
            return h1(board, player1);
        } else if (heuristic == 2) {
           printBoard(board);
            return h2(board, isMaximizing, player1);
        } else if (heuristic == 3) {
            printBoard(board);
            return h3(board, isMaximizing,player1);
        }
    }
    char player2 = player1 == 'X' ? 'O' : 'X';

    if (isMaximizing) { //player1
        int maxEval = Integer.MIN_VALUE;
        for (int[] move : getValidMoves(board, player1)) {
            char[][] tempBoard = copyBoard(board);
            tempBoard = makeMove(tempBoard, player1, move[0], move[1]);
            System.out.println("board in minimax player 1 -- player " + player1);
            printBoard(tempBoard);
            int eval = minimax(tempBoard, depth + 1, false, player1, alpha, beta);
            System.out.println("Evaluations are in max:" + eval);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);
            if (beta <= alpha) {
                break;
            }
        }
        System.out.println("Heloooo max evaluetion is:" + maxEval);
        return maxEval;
    } else { // player2
        int minEval = Integer.MAX_VALUE;
        for (int[] move : getValidMoves(board, player2)) {
            char[][] tempBoard = copyBoard(board);
            tempBoard = makeMove(tempBoard, player2, move[0], move[1]);
            System.out.println("board in minimax player 2 -- player " + player2);
            int eval = minimax(tempBoard, depth + 1, true, player1,  alpha, beta);
            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);
            if (beta <= alpha) {
                break;
            }
        }
        return minEval;
    }
}


    public static int h2(char[][] state, boolean isMaximizingPlayer, char currentPlayer) {

        System.out.println("Current player in h2" + currentPlayer);
        int playedMoves = playedMovesCount(state);
        System.out.println("Played moves count:" + playedMoves);
        int playerCount = 0, opponentCount = 0;
        int opponentCountInSides = 0;
        int currentCountInSides = 0;
        char opponent = (currentPlayer == 'X') ? 'O' : 'X';

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (state[i][j] == currentPlayer) {
                    playerCount++;
                } else if (state[i][j] == opponent) {
                    opponentCount++;
                }

                if (i == 0 || i == SIZE - 1 || j == 0 || j == SIZE - 1 || i == 1 || i == SIZE - 2 || j == 1
                        || j == SIZE - 2) {

                    if (playedMoves < 16) {

                        if (state[i][j] == opponent) {
                            opponentCountInSides += (isCorner(i, j) ? 3 : 2);
                        } else if (state[i][j] == currentPlayer) {
                            currentCountInSides += (isCorner(i, j) ? 4 : 2);
                        }
                    } else {
                        if (state[i][j] == opponent) {
                            opponentCountInSides += (isCorner(i, j) ? 4 : 2);
                        } else if (state[i][j] == currentPlayer) {
                            currentCountInSides += (isCorner(i, j) ? 10 : 2);
                        }
                    }

                }
            }
        }

        int baseValue = playerCount - opponentCount;
        int h2Value = currentCountInSides - opponentCountInSides;

        if (isMaximizingPlayer) {
            System.out.println("Calculated value in h2 for maximizing player is:" + (baseValue+h2Value));
            return baseValue + h2Value;
        } else {
            System.out.println("Calculated value in h2 for minimazing player is:" + (-baseValue-h2Value));
            return -(baseValue + h2Value);
        }
    }


    public static int playedMovesCount(char[][] state) {

        int playedMoves = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (state[i][j] != '.') {
                    playedMoves++;
                }
            }
        }
        return playedMoves;
    }

    public static boolean isCorner(int i, int j) {
        return (i == 0 && j == 0) || (i == 0 && j == SIZE - 1) || (i == SIZE - 1 && j == 0)
                || (i == SIZE - 1 && j == SIZE - 1);
    }


    public static int h3(char[][] state, boolean isMaximizingPlayer, char currentPlayer) {

        System.out.println("Current player in h3 " + currentPlayer);
        int playedMoves = playedMovesCount(state);
        int playerCount = 0, opponentCount = 0;
        int h3Value = 0;
        int opponentCountInSides = 0, currentCountInSides = 0;
        char opponent = (currentPlayer == 'X') ? 'O' : 'X';
        System.out.println("Opponent player in h3 " + opponent);
        int safeStones = 0;
        // Iterate through the board to calculate player stones and positional values
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (state[i][j] == currentPlayer) {
                    playerCount++; // Count current player's stones
                } else if (state[i][j] == opponent) {
                    opponentCount++; // Count opponent's stones
                }

                // Evaluate edge and corner positions
                if (isEdgeOrCorner(i, j)) {
                    if (playedMoves < 16) { // Early game
                        if (state[i][j] == opponent) {
                            opponentCountInSides += (isCorner(i, j) ? 3 : 2);
                        } else if (state[i][j] == currentPlayer) {
                            currentCountInSides += (isCorner(i, j) ? 4 : 2);
                        }
                    } else { // Late game
                        if (state[i][j] == opponent) {
                            opponentCountInSides += (isCorner(i, j) ? 4 : 2);
                        } else if (state[i][j] == currentPlayer) {
                            currentCountInSides += (isCorner(i, j) ? 10 : 2);
                        }
                    }
                }

                // Find safe stones and add to h3 value
                int numberOfSafeStones = findSafeFlippableStones(state, currentPlayer, opponent, i,j);

                if(numberOfSafeStones > safeStones) {
                    safeStones = numberOfSafeStones;
                }

            }
        }

        h3Value += safeStones * 2; // Add 8 points for each safe stone
        System.out.println("NUMBER OF SAFE STONES ARE:  " + safeStones);
        System.out.println("h3 value is: " + h3Value);

        // Combine all the calculated values
        int baseValue = playerCount - opponentCount; // Difference in the number of stones
        int positionalValue = currentCountInSides - opponentCountInSides; // Positional value
        int totalValue = baseValue + positionalValue + h3Value;

        // Return the value based on whether the player is maximizing or minimizing
        return isMaximizingPlayer ? totalValue : -totalValue;
    }


    public static int  findSafeFlippableStones(char[][] board, char myStone, char opponentStone, int i, int j) {
        int rows = board.length;
        int cols = board[0].length;
        int maxFlippedStones = 0;

        int[] rowDir = {0, 0, 1, -1, 1, -1, 1, -1};
        int[] colDir = {1, -1, 0, 0, 1, 1, -1, -1};


        if (board[i][j] == myStone) {

            int flippedStones = 0;

            for (int d = 0; d < 8; d++) {
                int currentFlips = 0;
                int r = i + rowDir[d];
                int c = j + colDir[d];

                while (r >= 0 && r < rows && c >= 0 && c < cols && board[r][c] == opponentStone) {
                    currentFlips++;
                    r += rowDir[d];
                    c += colDir[d];
                }


                    if (r >= 0 && r < rows && c >= 0 && c < cols && board[r][c] == '.' && currentFlips != 0) {
                        int rnew = r + rowDir[d];
                        int cnew = c + colDir[d];
                        int rnew2 = i - rowDir[d];
                        int cnew2 = j - colDir[d];

                        if ((rnew >= 0 && rnew < rows && cnew >= 0 && cnew < cols) && (board[rnew][cnew] != opponentStone || isEdgeOrCorner2ForH3(r,c))
                        || notInTheTable(rnew,cnew)) {
                            if ((rnew2 >= 0 && rnew2 < rows && cnew2 >= 0 && cnew2 < cols) && (board[rnew2][cnew2] != opponentStone || isEdgeOrCorner2ForH3(r,c))
                                    || notInTheTable(rnew2,cnew2)    )
                            {
                                        flippedStones = currentFlips;
                                    }
                                }

                        if (flippedStones > maxFlippedStones) {
                            maxFlippedStones = flippedStones;
                        }
                    }

            }

        }
        return maxFlippedStones;
    }



    public static boolean notInTheTable(int row, int col) {
        return row < 0 || row >= SIZE || col < 0 || col >= SIZE;
    }

    public static boolean isEdgeOrCorner(int i, int j) {
        return i == 0 || i == SIZE - 1 || j == 0 || j == SIZE - 1 ||
                i == 1 || i == SIZE - 2 || j == 1 || j == SIZE - 2;
    }



    public static boolean isEdgeOrCorner2ForH3(int i, int j) {
        return i == 0 || i == SIZE - 1 || j == 0 || j == SIZE - 1 ;
    }







public void playAIVsAI() {
    char[][] board = initializeBoard();
    printBoard(board);

    char player = 'X';
    heuristic = heuristic_AI1;
    // AI player1 X and AI player2 O

    while (true) {
        System.out.println("AI (Player " + player + ")'s turn.");
        List<int[]> aiMoves = getValidMoves(board, player);
        for (int[] move : aiMoves) {
            System.out.println("Valid move: " + (char) ('a' + move[1]) + " " + (move[0] + 1));
        }

        if (!aiMoves.isEmpty()) {
            int[] bestMove = findBestMove(board, player);
            board = makeMove(board, player, bestMove[0], bestMove[1]);
            System.out.println("AI (Player " + player + ") moves to: " + (char) ('a' + bestMove[1]) + " " + (bestMove[0] + 1));
            printBoard(board);

            if (isGameOver(board, player)) {
                break;
            }
        } else {
            System.out.println("AI (Player " + player + ") has no valid moves.");
        }

        // Switch players
        player = player == 'X' ? 'O' : 'X';
        heuristic = player == 'X' ? heuristic_AI1 : heuristic_AI2;
      
    }

    // Game over, determine the winner
    int countOfX = countStones(board, 'X');
    int countOfO = countStones(board, 'O');

    System.out.println("Game Over!");
    System.out.println("Final Score: ");
    System.out.println("AI Player X: " + countOfX);
    System.out.println("AI Player O: " + countOfO);

    if (countOfX > countOfO) {
        System.out.println("AI Player X wins!");
    } else if (countOfO > countOfX) {
        System.out.println("AI Player O wins!");
    } else {
        System.out.println("It's a draw!");
    }
}




    public static int h1(char[][] state, char currentPlayer) {
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

    public static char[][] initializeBoard() {
        char[][] board = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '.';
            }
        }
        board[3][3] = 'O';
        board[4][4] = 'O';
        board[3][4] = 'X';
        board[4][3] = 'X';
        return board;
    }

    public static char[][] copyBoard(char[][] state) {
        char[][] copy = new char[state.length][state[0].length];
        for (int i = 0; i < state.length; i++) {
            System.arraycopy(state[i], 0, copy[i], 0, state[i].length);
        }
        return copy;
    }


    public static void printBoard(char[][] board) {
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