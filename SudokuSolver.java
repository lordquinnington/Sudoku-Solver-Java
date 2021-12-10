public class SudokuSolver {
    public static Puzzle solve(Puzzle pUnsolvedPuzzle) {
        Puzzle puzzle = pUnsolvedPuzzle;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!(puzzle.getConstraints())[1][i].getCells()[j].isSolved()) {
                    puzzle = eliminateNotes(puzzle, (puzzle.getConstraints())[1][i].getCells()[j], i, j);     // passes each cell to be solved
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!(puzzle.getConstraints())[1][i].getCells()[j].isSolved()) {
                    puzzle = openSingles(puzzle, (puzzle.getConstraints())[1][i].getCells()[j]);     // passes each cell to be solved
                }
            }
        }

        return puzzle;
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
    protected static Puzzle openSingles(Puzzle pCurrentPuzzle, Cell pCurrentCell) {
        Constraint[][] constraints = pCurrentPuzzle.getConstraints();       // retrieves all constraints
        int[] cellConstraints = pCurrentCell.getConstraints();      // retrieves the indexes of the cell's constraints

        boolean changed = true;     // marks if any updates to the grid have been made (potentially meaning more solving can be done)

        while (changed) {
            changed = false;

            for (int i = 0; i < 3; i++) {
                if (constraints[i][cellConstraints[i]].numOfCells() == 8) {
                    Cell[] cells = constraints[i][cellConstraints[i]].getCells();       // gets cells if constraint has 8

                    for (int j = 0; j < 9; j++) {
                        if (cells[j].getSolution() == 0) {      // locates blank index
                            int[] index = indexToUpdate(i, cellConstraints[i], j);      // works out index to add cell into grid
                            pCurrentPuzzle.addCell(false, (45-constraints[i][cellConstraints[i]].sumOfCells()), index[0], index[1]);
                            constraints = pCurrentPuzzle.getConstraints();      // gets updated constraints
                            changed = true;     // a change has been made so change is marked back to true
                            break;      // breaks immediately as there will be no more empty cells
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
    public static Puzzle eliminateNotes(Puzzle pCurrentPuzzle, Cell pCurrentCell, int pCol, int pRow) {
        Constraint[][] constraints = pCurrentPuzzle.getConstraints();       // retrieves all constraints
        int[] cellConstraints = pCurrentCell.getConstraints();      // retrieves the indexes of the cell's constraints

        for (int i = 0; i < 3; i++) {
            int[] cellValues = constraints[i][cellConstraints[i]].getSolvedCells();     // gets array of solved values
            pCurrentCell = removeNotesFromCell(pCurrentCell, cellValues);       // removes all solved values from cell's notes

            pCurrentPuzzle.updateCell(pCurrentCell, pCol, pRow);        // updates puzzle with the cell
            constraints = pCurrentPuzzle.getConstraints();      // retrieves updated constraints
        }

        return pCurrentPuzzle;
    }

    // method to remove notes from a given cell
    protected static Cell removeNotesFromCell(Cell pCell, int[] pOtherCells) {
        for (int pOtherCell : pOtherCells) {      // don't need to check if value is in the cell's
            pCell.removeNote(pOtherCell);               // notes as it will just be ignored if not
        }

        if (pCell.numOfNotes() == 1) {
            pCell.setSolution(pCell.notesRemaining()[0]);     // sets cell value to note if only one note left
        }

        return pCell;
    }

    /* #######################################################
                          HIDDEN PAIRS
    ####################################################### */

    // method to check for hidden pairs
}

