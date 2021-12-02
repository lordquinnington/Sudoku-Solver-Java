public class Constraint {
    protected Cell[] cells = new Cell[9];

    public Constraint() {
        for (int i = 0; i < 9; i++) {
            cells[i] = new Cell(false, 0);      // all cells are initialised as blank -> 0
        }
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

        for (int i = 0; i < numOfCells(); i++) {
            if (cells[i].isSolved()) {
                cellValues[cellValueIndex] = cells[i].getSolution();        // if cell is solved, the value will be added
                cellValueIndex++;
            }
        }

        return cellValues;
    }
}

