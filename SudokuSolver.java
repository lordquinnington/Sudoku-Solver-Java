public class SudokuSolver {
    public static Puzzle solve(Puzzle pUnsolvedPuzzle) {
        Puzzle unsolvedPuzzle = eliminateNotes(pUnsolvedPuzzle);
        Puzzle solvedPuzzle = openSingles(unsolvedPuzzle);
        // do more solving
        return solvedPuzzle;
    }

    /* #######################################################
                         MISC METHODS
    ####################################################### */

    // method to return 2D grid reference of where the Cell should be added
    protected static int[] indexToUpdate(int pConstraint, int pConstraintIndex, int pCellIndex) {
        if (pConstraint == 0) {
            return new int[]{pCellIndex, pConstraintIndex};     // returns correct index if cell was in a row
        }
        else if (pConstraint == 1) {
            return new int[]{pConstraintIndex, pCellIndex};     // returns correct index if cell was in a column
        }
        else {
            return new int[]{((pCellIndex%3) + (3*(pConstraintIndex%3))), ((pCellIndex/3) + (3*(pConstraintIndex/3)))};     // returns correct index if cell was in a column
        }
    }

    /* #######################################################
                         OPEN SINGLES
    ####################################################### */

    // method to check for open singles
    protected static Puzzle openSingles(Puzzle pCurrentPuzzle) {
        Constraint[][] constraints = pCurrentPuzzle.getConstraints();       // gets the current constraints

        for (int i = 0; i < 3; i++) {       // loops through each row/column/square constraint array
            for (int j = 0; j < 9; j++) {       // loops through each row/column/square
                if (constraints[i][j].numOfCells() == 8) {
                    Cell[] cells = constraints[i][j].getCells();        // retrieves the cells if there are eight

                    for (int k = 0; k < 9; k++) {
                        if (cells[k].getSolution() == 0) {      // finds the empty cell
                            int[] index = indexToUpdate(i, j, k);       // calculates which 2D index the cell is at
                            pCurrentPuzzle.addCell(new Cell(false, (45 - constraints[i][j].sumOfCells())), index[0], index[1]);
                            constraints = pCurrentPuzzle.getConstraints();      // retrieves the updated constraints
                            break;
                        }
                    }
                }
            }
        }

        return pCurrentPuzzle;
    }

    /* #######################################################
                        ELIMINATE NOTES
    ####################################################### */

    // method to find notes which can be eliminated
    protected static Puzzle eliminateNotes(Puzzle pCurrentPuzzle) {
        Constraint[][] constraints = pCurrentPuzzle.getConstraints();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                if (constraints[i][j].numOfCells() < 9) {
                    int[] cellValues = constraints[i][j].getSolvedCells();      // retrieves all the solved cells for the constraint
                    Cell[] cells = constraints[i][j].getCells();

                    for (int k = 0; k < 9; k++) {
                        if (!cells[k].isSolved()) {     // if the cell is not solved, the program will try remove notes
                            Cell updatedCell = removeNotesFromCell(cells[k], cellValues);       // updates the cell and adds to grid
                            pCurrentPuzzle.addCell(updatedCell, indexToUpdate(i, j, k)[0], indexToUpdate(i, j, k)[1]);
                        }
                    }
                }
            }
        }

        return pCurrentPuzzle;
    }

    // method to remove notes from a given cell
    protected static Cell removeNotesFromCell(Cell pCell, int[] pOtherCells) {
        for (int i = 0; i < pOtherCells.length; i++) {      // don't need to check if value is in the cell's
            pCell.removeNote(pOtherCells[i]);               // notes as it will just be ignored if not
        }

        if (pCell.getNotes().length == 1) {
            pCell.setSolution(pCell.getNotes()[0]);     // sets cell value to note if only one note left
        }

        return pCell;
    }
}
