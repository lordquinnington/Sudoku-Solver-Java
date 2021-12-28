public class Constraint {
    protected Cell[] cells;

    public Constraint() {
        cells = new Cell[9];        // initialises a new Cell array of length nine
    }

    public String toString() {
        String constraint = "|";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                constraint = constraint.concat(cells[(3*i)+j].toString());
            }
            constraint = constraint.concat("|");         // returns the constraint in the form of a row ie: |123|456|879|
        }
        return constraint;
    }

    // method to add a Cell to the specified index in the constraint
    public void addCell(Cell pCell, int pIndex) {
        cells[pIndex] = pCell;
    }

    // method to return the array of cells
    public Cell[] getCells() {
        return cells;
    }

    // method to return how many filled cells are in the constraint
    public int numOfCells() {
        int num = 0;

        for (int i = 0; i < 9; i++) {
            if (cells[i].getSolution() != 0) {
                num++;          // if the cell is not blank, num is incremented
            }
        }

        return num;
    }

    // method to return the sum of all the cells in the constraint
    public int sumOfCells() {
        int sum = 0;

        for (int i = 0; i < 9; i++) {
            sum = sum + cells[i].getSolution();     // if cell is blank, 0 will be added
        }

        return sum;
    }

    // method to get the values of all the solved cells
    public int[] getSolvedCells() {
        int[] cellValues = new int[numOfCells()];
        int cellValueIndex = 0;

        for (int i = 0; i < 9; i++) {
            if (cells[i].isSolved()) {
                cellValues[cellValueIndex] = cells[i].getSolution();        // if cell is solved, the value will be added
                cellValueIndex++;
            }
        }

        return cellValues;
    }

    // method to get the notes from all the cells
    public int[][] getAllNotes() {
        int[][] allNotes = new int[9][];        // the index of the notes in the array can be used to keep track of which cell they came from

        for (int i = 0; i < 9; i++) {
            if (cells[i].isSolved()) {
                allNotes[i] = new int[]{0};     // if the cell is solved, a 0 is added as it effectively has no notes
            }
            else {
                allNotes[i] = cells[i].notesRemaining();      // if cell is blank, it's notes array is added
            }
        }

        return allNotes;
    }

    // method to check if the constraint contains duplicate numbers
    public boolean containsDuplicates() {
        int[] numsInConstraint = new int[0];        // array to store all appearing numbers

        for (int i = 0; i < 9; i++) {
            if (cells[i].getSolution() != 0) {      // no point adding 0 to the array
                if (SudokuSolver.inArr(numsInConstraint,cells[i].getSolution())) {
                    return true;        // if number already appears in the array than it must be a duplicate and therefore invalid
                }
                else {
                    // adds the number to array if not already in
                    numsInConstraint = SudokuSolver.append(numsInConstraint, cells[i].getSolution());
                }
            }
        }

        return false;
    }
}

