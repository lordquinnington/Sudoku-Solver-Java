public class Puzzle {
    protected Constraint[][] constraints = new Constraint[3][9];

    public Puzzle() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                constraints[i][j] = new Constraint();       // initialises the grid with empty constraint objects
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                addCell(false, 0, i, j);        // adds a cell to each constraint using addCell method
            }
        }
    }

    public String toString() {
        String puzzle = "-------------\n";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                puzzle = puzzle.concat(constraints[0][(3*i)+j].toString()+"\n");        // adds the constraint to the String (via the Constraint's toString method)
            }
            puzzle = puzzle.concat("\n-------------\n");        // inserts a line every 3 rows just as a normal sudoku grid would be
        }

        /* returns the puzzle in a form like this to help with debugging:
        -------------
        |123|456|789|
        |234|567|891|
        |345|678|912|
        -------------
        |456|789|123|
        |567|891|234|
        |678|912|345|
        -------------
        |789|123|456|
        |891|234|567|
        |912|345|678|
        ------------- */

        return puzzle;
    }

    // method to add a cell to the grid given a 2D array index
    public void addCell(boolean pGiven, int pValue, int pColIndex, int pRowIndex) {
        int squareConstraintIndex = (pColIndex/3)+(3*(pRowIndex/3));        // works out which index for the correct square
        int squareCellIndex = (pColIndex-(3*(pColIndex/3)))+(3*(pRowIndex-(3*(pRowIndex/3))));
        Cell cell = new Cell(pGiven, pValue, pRowIndex, pColIndex, squareConstraintIndex);      // cell is passed the correct indexes

        constraints[0][pRowIndex].addCell(cell, pColIndex);     // the cell is added to each of the constraints
        constraints[1][pColIndex].addCell(cell, pRowIndex);
        constraints[2][squareConstraintIndex].addCell(cell, squareCellIndex);
    }

    public void updateCell(Cell pCell, int pColIndex, int pRowIndex) {
        int squareConstraintIndex = (pColIndex/3)+(3*(pRowIndex/3));        // works out which index for the correct square
        int squareCellIndex = (pColIndex-(3*(pColIndex/3)))+(3*(pRowIndex-(3*(pRowIndex/3))));

        constraints[0][pRowIndex].addCell(pCell, pColIndex);     // the cell is added to each of the constraints
        constraints[1][pColIndex].addCell(pCell, pRowIndex);
        constraints[2][squareConstraintIndex].addCell(pCell, squareCellIndex);
    }

    // method to return constraints in the form [rows, columns, squares]
    public Constraint[][] getConstraints() {
        return new Constraint[][]{constraints[0], constraints[1], constraints[2]};
    }

    // method to check if grid is full and valid. Returns boolean array in form [full, valid]
    public boolean[] checkIfComplete() {
        boolean full = true;

        for (int i = 0; i < 9; i++) {
            if (constraints[0][i].numOfCells() != 9 || constraints[1][i].numOfCells() != 9 || constraints[2][i].numOfCells() != 9) {
                full = false;       // sets full to false if any one of the constraints doesn't have 9 filled in cells
                break;      // breaks immediately for efficiency
            }
        }
        if (full) {
            for (int i = 0; i < 9; i++) {
                if (constraints[0][i].sumOfCells() != 45 || constraints[1][i].sumOfCells() != 45 || constraints[2][i].sumOfCells() != 45) {
                    return new boolean[]{true, false};        // returns valid (index 1) as false if any one of the constraints does not sum to 45
                }
            }
            return new boolean[]{true, true};       // only when full is true and all squares sum to 45 is valid also true
        }

        return new boolean[]{false, false};      // if full is false then valid must also be false
    }
}

