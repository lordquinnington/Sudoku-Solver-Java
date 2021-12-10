public class Cell {
    protected int solution = 0;         // a solution of 0 means unsolved
    protected int[] notes = {0,0,0,0,0,0,0,0,0};           // initialises the notes to blank
    protected boolean given;        // marks whether the cell is provided (thereby influencing if it's editable or not)
    protected boolean solved = false;           // marks whether the cell has been solved (ie has a solution)
    protected int row;      // stores the row (from 0 to 8)
    protected int column;       // stores the column (from 0 to 8)
    protected int square;       // stores the square (from 0 to 8)

    public Cell(boolean pGiven, int pSolution, int pRow, int pColumn, int pSquare) {
        given = pGiven;

        row = pRow;
        column = pColumn;
        square = pSquare;

        if (pGiven || pSolution != 0) {
            solution = pSolution;       // if the cell is given to the user or worked out, the solution is filled in immediately
            solved = true;          // solved is also marked as true
        }
        else {
            for (int i = 0; i < 9; i++) {
                notes[i] = i+1;         // initialises the notes as 1 to 9 if blank cell
            }
        }
    }

    public String toString() {
        return Integer.toString(solution);
    }

    // method to set the solution of the cell
    public boolean setSolution(int pSolution) {
        boolean found = false;

        for (int i = 0; i < 9; i++) {
            if (notes[i] == pSolution) {        // uses a linear search to check if the value is in notes
                found = true;       // marks found as true if present
                break;
            }
        }

        // sets the solution if the suggested solution is a valid option
        if (found) {
            solution = pSolution;
            solved = true;
            return true;
        }
        else {
            return false;
        }
    }

    // method to return the solution to the square
    public int getSolution() {
        return solution;
    }

    // method to check the square has been solved (used before getting the solution)
    public boolean isSolved() {
        return solved;
    }

    // method to check if the square can be changed
    public boolean isEditable() {
        return !given;
    }

    // method to return the notes
    public int[] getNotes() {
        return notes;
    }

    // method to remove a specified note
    public void removeNote(int pNoteToRemove) {
        for (int i = 0; i < notes.length; i++) {
            if (notes[i] == pNoteToRemove) {
                notes[i] = 0;           // sets the note to 0 (blank) if found
                return;         // returns immediately to improve efficiency
            }
        }
    }

    // method to return the constraints the Cell is in, in the form [row, column, square]
    public int[] getConstraints() {
        return new int[]{row, column, square};
    }

    // method to return all the current notes
    public int[] notesRemaining() {
        int[] notesLeft = new int[numOfNotes()];        // uses numOfNotes to initialise an array of correct length
        int nextIndex = 0;      // marks the next available index in notesLeft

        for (int i = 0; i < 9; i++) {
            if (notes[i] != 0) {
                notesLeft[nextIndex] = notes[i];        // adds to notesLeft if value is not blank
                nextIndex++;        // next index is incremented
            }
        }

        return notesLeft;
    }

    // method to find how many notes are left
    public int numOfNotes() {
        int notesLeft = 0;      // counter for notes left

        for (int i = 0; i < 9; i++) {
            if (notes[i] != 0) {
                notesLeft++;        // if a non-blank note is found, notes left is incremented
            }
        }

        return notesLeft;
    }
}

