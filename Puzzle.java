public class Puzzle {
    protected Constraint[] rows = new Constraint[9];            // creates 3 lots of constraints to represent rows, columns and squares
    protected Constraint[] columns = new Constraint[9];
    protected Constraint[] squares = new Constraint[9];

    public Puzzle() {
        for (int i = 0; i < 9; i++) {
            rows[i] = new Constraint();         // all initialised to blank constraints
            columns[i] = new Constraint();
            squares[i] = new Constraint();
        }
    }

    public String toString() {
        String puzzle = "-------------\n";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                puzzle = puzzle.concat(rows[(3*i)+j].toString()+"\n");        // adds the constraint to the String (via the Constraint's toString method)
            }
            puzzle = puzzle.concat("\n-------------\n");
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
    public void addCell(Cell pCell, int pColIndex, int pRowIndex) {
        rows[pRowIndex].addCell(pCell, pColIndex);
        columns[pColIndex].addCell(pCell, pRowIndex);

        int squareConstraintIndex = (pColIndex/3)+(3*(pRowIndex/3));        // works out which index for the correct square
        int squareCellIndex = (pColIndex-(3*(pColIndex/3)))+(3*(pRowIndex-(3*(pRowIndex/3))));
        squares[squareConstraintIndex].addCell(pCell, squareCellIndex);
    }

    // method to return constraints in the form [rows, columns, squares]
    public Constraint[][] getConstraints() {
        return new Constraint[][]{rows, columns, squares};
    }

    // method to check if grid is full and valid. Returns boolean array in form [full, valid]
    public boolean[] checkIfComplete() {
        boolean full = true;

        for (int i = 0; i < 9; i++) {
            if (rows[i].numOfCells() != 9 || columns[i].numOfCells() != 9 || squares[i].numOfCells() != 9) {
                full = false;       // sets full to false if any one of the constraints doesn't have 9 filled in cells
                break;      // breaks immediately for efficiency
            }
        }
        if (full) {
            for (int i = 0; i < 9; i++) {
                if (rows[i].sumOfCells() != 45 || columns[i].sumOfCells() != 45 || squares[i].sumOfCells() != 45) {
                    return new boolean[]{true, false};        // returns valid (index 1) as false if any one of the constraints does not sum to 45
                }
            }
            return new boolean[]{true, true};       // only when full is true and all squares sum to 45 is valid also true
        }

        return new boolean[]{false, false};      // if full is false then valid must also be false
    }
}

