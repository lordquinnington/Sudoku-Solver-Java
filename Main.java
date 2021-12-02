public class Main {
    public static void main(String[] args) {
        Puzzle solvedPuzzle = SudokuSolver.solve(QQWingConverter.convert("...4..17.........2.4..76..5....3..2...........2.1..69.1....45...6.52...73...67..9"));
        //System.out.println(solvedPuzzle);
    }
}
