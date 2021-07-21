package com.virgindroid.tictac;

public class Helper {
    public static final char[] board = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
    static int[][] winningStates = {
            {0, 1, 2},
            {0, 3, 6},
            {0, 4, 8},
            {1, 4, 7},
            {2, 5, 8},
            {3, 4, 5},
            {6, 7, 8},
            {2, 4, 6}
    };

    public static void showBoard() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            System.out.print(board[i] + " ");
            count++;
            if (count == 3) {
                count = 0;
                System.out.println();
            }
        }
    }

    public static int getNextSmartMove(char player) {
        //x =>2 => ai
        //0 =>1 => human
        char opposition = player == 'x' ? '0' : 'x';
        int maxScore = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int i = 0; i < 9; i++) {
            if (board[i] == '#') {
                board[i] = player;
                int score = minimax(board, 0, opposition, player, opposition);
                board[i] = '#';
                if (score > maxScore) {
                    maxScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    private static int minimax(char[] board, int depth, char turn, char player, char opp) {
        if (checkTerminalState(player, opp) != -2) {
            if (checkTerminalState(player, opp) == 1) {
                return 10 - depth;
            } else if (checkTerminalState(player, opp) == -1) {
                return -10 + depth;
            } else {
                return 0;
            }
        }
        int bestScore;
        if (turn == player) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == '#') {
                    board[i] = player;
                    int score = minimax(board, depth + 1, opp, player, opp);
                    board[i] = '#';
                    bestScore = Math.max(score, bestScore);
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == '#') {
                    board[i] = opp;
                    int score = minimax(board, depth + 1, player, player, opp);
                    board[i] = '#';
                    bestScore = Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }

    public static int checkTerminalState(char player, char opp) {
        //0  => tie
        //1  => win
        //-1 =>loose
        //-2 => game is in progress

        for (int[] state : winningStates) {
            if ((board[state[0]] == board[state[1]]) && (board[state[1]] == board[state[2]]) && board[state[0]] == player) {
                return 1;
            }
            if ((board[state[0]] == board[state[1]]) && (board[state[1]] == board[state[2]]) && board[state[0]] == opp) {
                return -1;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (board[i] == '#') {
                return -2;
            }
        }
        return 0;
    }

    public static void updateBoard(char[] board, int pos, char crossOrZero) {
        board[pos] = crossOrZero;
    }


}
