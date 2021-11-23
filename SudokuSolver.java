public class SudokuSolver {
    protected Puzzle unsolvedPuzzle;
    protected Puzzle solvedPuzzle;

    public SudokuSolver(String pQQWingPuzzle) {
        QQWingConverter temp = new QQWingConverter(pQQWingPuzzle);
        unsolvedPuzzle = temp.convert();
    }

    public Puzzle solve() {
        return solvedPuzzle;
    }
}
