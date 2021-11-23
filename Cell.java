import java.util.Arrays;

public class Cell {
    protected int solution = 0;         // a solution of 0 means unsolved
    protected int[] notes = {1,2,3,4,5,6,7,8,9};        // initialises the notes as 1 to 9
    protected boolean given;        // marks whether the cell is provided (thereby influencing if it's editable or not)
    protected boolean solved = false;

    public Cell(boolean pGiven, int pSolution) {
        given = pGiven;

        if (pGiven) {
            solution = pSolution;       // if the cell is given to the user, the solution is filled in immediately
            solved = true;          // solved is also marked as true
        }
    }

    public boolean setSolution(int pSolution) {
        int x = Arrays.binarySearch(notes, pSolution);          // uses a binary search to check if the solution is in the notes thereby meaning it is a valid solution

        if (x >= 0) {
            solution = pSolution;
            solved = true;
            return true;
        } else {
            return false;
        }
    }

    public int getSolution() {
        return solution;
    }

    public boolean isSolved() {
        return solved;
    }

    public boolean isEditable() {
        return !given;
    }
}
