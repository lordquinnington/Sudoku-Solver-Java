public class Puzzle {
    protected Constraint[][] constraints = new Constraint[3][9];
    protected boolean valid = true;

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

        if (!allConstraintsValid()) {
            markAsInvalid();
        }

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

    // method to find the index with the least number of notes
    public int[] indexWithLeastNotes() {
        int numOfNotes = 10;        // starts off as 10 just in case all cells have 9 notes, then [0,0] will be returned
        int[] index = new int[2];       // array to store the 2D index of the cell with the least notes as [col,row]

        for (int i = 0; i < 9; i++) {       // loops through the grid
            for (int j = 0; j < 9; j++) {
                if (!constraints[1][i].getCells()[j].isSolved()) {      // checks if the cell is blank
                    if (constraints[1][i].getCells()[j].numOfNotes() < numOfNotes) {        // checks if the cell has less notes
                        numOfNotes = constraints[1][i].getCells()[j].numOfNotes();      // sets to new least value
                        index = new int[]{i,j};     // marks the index of current least value
                    }
                }
            }
        }

        return index;
    }

    // as Java passes objects by reference, this is needed to make a new Puzzle which is separate but the same in value
    public Puzzle makeCopy() {
        Puzzle p2 = new Puzzle();       // creates new separate instance of a Puzzle

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int sol = constraints[1][i].getCells()[j].getSolution();        // retrieves the cell's solution
                boolean given = !constraints[1][i].getCells()[j].isEditable();      // retrieves if the cell was given
                int[] notes = constraints[1][i].getCells()[j].getNotes();       // retrieves the notes
                int[] newCellNotes = new int[notes.length];
                System.arraycopy(notes, 0, newCellNotes, 0, notes.length);
                int squareConstraintIndex = (i/3)+(3*(j/3));        // works out which index for the correct square

                Cell c = new Cell(given,sol,j,i,squareConstraintIndex);     // creates a new cell to add
                c.setNotes(newCellNotes);      // sets the notes to match the original cell
                p2.updateCell(c,i,j);       // updates the second puzzle
            }
        }

        return p2;
    }

    // method to set if the grid is valid or not
    public void markAsInvalid() {
        valid = false;
    }

    // method to get if the grid is valid or not
    public boolean isValid() {
        return valid;
    }

    // method to check if the grid is the same as another grid
    public boolean isEqual(Puzzle pOtherPuzzle) {
        Constraint[][] otherConstraints = pOtherPuzzle.getConstraints();        // gets the constraints from the other puzzle

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (otherConstraints[1][i].getCells()[j].getSolution() != constraints[1][i].getCells()[j].getSolution()) {
                    return false;       // returns false if the solutions do not match
                }
                if (!notesEqual(otherConstraints[1][i].getCells()[j].getNotes(),constraints[1][i].getCells()[j].getNotes())) {
                    return false;       // returns false if the notes do not match
                }
            }
        }

        return true;        // returns true if both notes and solutions match
    }

    // method to check if two int arrays are equal (for checking notes)
    protected boolean notesEqual(int[] pOtherNotes, int[] notes) {
        for (int i = 0; i < 9; i++) {
            if (pOtherNotes[i] != notes[i]) {
                return false;       // returns false if any of the notes do not match
            }
        }

        return true;        // returns true if all of the notes match
    }

    // method to check if all constraints are valid (ie don't contain any duplicates)
    public boolean allConstraintsValid() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {       // loops through all the constraints
                if (constraints[i][j].containsDuplicates()) {
                    return false;       // returns false immediately if any constraints contain duplicates
                }
            }
        }

        return true;
    }
}

