package com.nz.radar.SudokuSolver;

import java.util.HashSet;
import java.util.Set;

public class SudokuSolver {
    /**
     * From:
     * http://dannylesnik.blogspot.com/2011/01/hibernate-collection-mapping.html
     */

    private static final int UNASSIGNED = 0;


    // Function to check if a given row is valid. It will return:
    // -1 if the row contains an invalid value
    // 0 if thr row contains repeated values
    // 1 is the row is valid.
    public static int valid_row(int row, Integer [][] grid){
        Integer temp[] = grid[row];
        Set<Integer> set = new HashSet<Integer>();
        for (int value : temp) {
            // Checking for values outside 0 and 9;
            // 0 is considered valid because it
            // denotes an empty cell.
            // Removing zeros and the checking for values and
            // outside 1 and 9 is another way of doing
            // the same thing.
            if (value < 0 || value > 9){
                System.out.println( "Invalid value" );
                return -1;
            }
            //Checking for repeated values.
            else if (value != 0){
                if (set.add(value) == false) {
                    return 0;
                }
            }
        }
        return 1;
    }
    // Function to check if a given column is valid. It will return:
    // -1 if the column contains an invalid value
    // 0 if the column contains repeated values
    // 1 is the column is valid.
    public static int valid_col(int col, Integer [][] grid){
        Set<Integer>set = new HashSet<Integer>();
        for (int i =0 ; i< 9; i++) {
            // Checking for values outside 0 and 9;
            // 0 is considered valid because it
            // denotes an empty cell.
            // Removing zeros and the checking for values and
            // outside 1 and 9 is another way of doing
            // the same thing.
            if (grid[i][col] < 0 || grid[i][col] > 9){
                System.out.println( "Invalid value" );
                return -1;
            }
            // Checking for repeated values.
            else if (grid[i][col] != 0){
                if (set.add(grid[i][col]) == false) {
                    return 0;
                }
            }
        }
        return 1;
    }

    public int getInvalidRowInBox() {
        return InvalidRowInBox;
    }

    public void setInvalidRowInBox(int InvalidRowInBox) {
        this.InvalidRowInBox = InvalidRowInBox;
    }

    public int InvalidRowInBox;

    public int getInvalidColInBox() {
        return InvalidColInBox;
    }

    public void setInvalidColInBox(int InvalidColInBox) {
        this.InvalidColInBox = InvalidColInBox;
    }

    public int InvalidColInBox;
    // Function to check if all the subsquares are valid. It will return:
    // -1 if a subsquare contains an invalid value
    // 0 if a subsquare contains repeated values
    // 1 if the subsquares are valid.
    public int valid_subsquares(Integer [][] grid){
        for (int row = 0 ; row < 9; row = row + 3) {
            for (int col = 0; col < 9; col = col + 3) {
                Set<Integer>set = new HashSet<Integer>();
                for(int r = row; r < row+3; r++) {
                    for(int c= col; c < col+3; c++){
                        // Checking for values outside 0 and 9;
                        // 0 is considered valid because it
                        // denotes an empty cell.
                        // Removing zeros and the checking for values and
                        // outside 1 and 9 is another way of doing
                        // the same thing.
                        if (grid[r][c] < 0 || grid[r][c] > 9){
                            System.out.println( "Invalid value" );
                            return -1;
                        }
                        // Checking for repeated values.
                        else if (grid[r][c] != 0){
                            if (set.add(grid[r][c]) == false) {
                                setInvalidColInBox(c);
                                setInvalidRowInBox(r);
                                return 0;
                            }
                        }
                    }
                }
            }
        }
        return 1;
    }
    //Function to check if the board invalid.
    public void valid_board(Integer [][] grid){
        // Checking the rows and columns.
        for (int i =0 ; i< 9; i++) {
            int res1 = valid_row(i, grid);
            if(res1<1) {
                setInvalidLine(i);
                setRowOrCol("row");
                setValid(false);
                return;
            }
            int res2 = valid_col(i, grid);
            if(res2<1) {
                setInvalidLine(i);
                setRowOrCol("col");
                setValid(false);
                return;
            }

        }
        int res3 = valid_subsquares(grid);
        // if any one the subsquares is invalid, then the board is invalid.
        if (res3 < 1) {
            System.out.println( "The board is invalid." );
            setValid(false);

        }
        else {
            System.out.println( "The board is valid." );
            setValid(true);

        }
    }


    public String getRowOrCol() {
        return rowOrCol;
    }

    public void setRowOrCol(String rowOrCol) {
        this.rowOrCol = rowOrCol;
    }

    String rowOrCol;


    public int getInvalidLine() {
        return invalidLine;
    }

    public void setInvalidLine(int invalidCell) {
        this.invalidLine = invalidCell;
    }

    int invalidLine;

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    boolean valid;



    public boolean solve(int row, int col, Integer[][] cells) {

        if (row == 9) {
            row = 0;
            if (++col == 9) {
                return true;
            }
        }
        if (cells[row][col] != UNASSIGNED) // skip filled cells
            return solve(row + 1, col, cells);
        for (int val = 1; val <= 9; ++val) {
            if (isOk(row, col, val, cells)) {
                // number ok. it respects sudoku constraints
                cells[row][col] = val;
                if (solve(row + 1, col, cells))// we start backtracking recursively
                    return true;
            }
        }
        cells[row][col] = UNASSIGNED; // reset on backtrack

        return false;
    }

    // we check if a possible number is already in a row
    private boolean isInRow(int row, int number,Integer[][] cells) {
        for (int i = 0; i < 9; i++)
            if (cells[row][i] == number)
                return true;

        return false;
    }

    // we check if a possible number is already in a column
    private boolean isInCol(int col, int number,Integer[][] cells) {
        for (int i = 0; i < 9; i++)
            if (cells[i][col] == number)
                return true;

        return false;
    }

    // we check if a possible number is in its 3x3 box
    private boolean isInBox(int row, int col, int number,Integer[][] cells) {
        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (cells[i][j] == number)
                    return true;

        return false;
    }

    // combined method to check if a number possible to a row,col position is ok
    public boolean isOk(int row, int col, int number,Integer[][] cells) {
        boolean isInsideRow=isInRow(row,number,cells);
        boolean isInsideCol=isInCol(col,number,cells);
        boolean isInsideBox=isInBox(row,col,number,cells);
        return !isInsideRow  &&  !isInsideCol  &&  !isInsideBox;
    }


}

