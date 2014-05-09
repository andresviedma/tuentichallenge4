package com.andresviedma.tuenticontest2014.challenge5;

import java.util.Arrays;


class GameOfLifeBoard {
    public static final char DEAD_STATE = '-';
    public static final char ALIVE_STATE = 'X';
    
    private static final String RULES_STAY_ALIVE = "23";
    private static final String RULES_BIRTH = "3";
    
    private boolean[][] board;
    
    public GameOfLifeBoard(String[] vals) {
        int rowCount = vals.length;
        int colCount = vals[0].length();
        this.board = new boolean[rowCount][colCount];
        for (int i=0; i<rowCount; i++) {
            for (int j=0; j<rowCount; j++) {
                this.board[i][j] = (vals[i].charAt(j) == ALIVE_STATE);
            }
        }
    }
    
    public GameOfLifeBoard(boolean[][] vals) {
        this.board = vals;
    }
    
    public int getRowCount() {
        return board.length;
    }
    public int getColumnCount() {
        return board[0].length;
    }
    
    public GameOfLifeBoard nextGeneration() {
        int rows = this.getRowCount();
        int cols = this.getColumnCount();
        boolean next[][] = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
           for (int j = 0; j < cols; j++) {
              next[i][j] = this.nextCellState(i, j);
           }
        }
        return new GameOfLifeBoard(next);
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (boolean row[] : board) {
            for (boolean cell : row) {
               buf.append(cell ? ALIVE_STATE : DEAD_STATE);
            }
            buf.append('\n');
         }
        return buf.toString();
    }
    
    public String sorroundingToString() {
        int rows = this.getRowCount();
        int cols = this.getColumnCount();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < rows; i++) {
           for (int j = 0; j < cols; j++) {
               buf.append(this.countSurrounding(i, j));
           }
            buf.append('\n');
         }
        return buf.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof GameOfLifeBoard))  return false;
        GameOfLifeBoard b = (GameOfLifeBoard) o;
        return Arrays.deepEquals(b.board, board);
    }
    
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    private boolean nextCellState(int x, int y) {
        int count = countSurrounding(x, y);
        String rules = (board[x][y]? RULES_STAY_ALIVE : RULES_BIRTH);
        return rules.contains(Integer.toString(count));
    }
    
    private int countSurrounding(int x, int y) {
        int iMin = (x == 0 ? 0 : x - 1);
        int iMax = (x == getRowCount()-1 ? x : x + 1);
        int jMin = (y == 0 ? 0 : y - 1);
        int jMax = (y == getColumnCount()-1 ? y : y + 1);
        
        int count = 0;
        for (int i = iMin; i <= iMax; i++) {
            for (int j = jMin; j <= jMax; j++) {
                if (board[i][j] && ((x != i) || (y != j))) {
                    count++;
                }
            }
         }
        return count;
    }

    
    public static void main(String[] args) {
        String[] board = {
                "X------X",
                "--------",
                "---X----",
                "---X----",
                "---X----",
                "--------",
                "--------",
                "X------X"
            };
        GameOfLifeBoard b = new GameOfLifeBoard(board);
        System.out.println("* Start:");
        System.out.println(b);
        System.out.println(b.sorroundingToString());
//if(true)  return;
//        b = b.nextGeneration();
        System.out.println("* I1:");
        System.out.println(b);
        b = b.nextGeneration();
        System.out.println("* I2:");
        System.out.println(b);
        b = b.nextGeneration();
        System.out.println("* I3:");
        System.out.println(b);
        b = b.nextGeneration();
        System.out.println("* I4:");
        System.out.println(b);
    }
}