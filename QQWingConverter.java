// temporary class for testing solving using QQWing Sudoku (https://qqwing.com/generate.html)

public class QQWingConverter {
    protected String unconvertedPuzzle;
    protected Puzzle convertedPuzzle;

    public QQWingConverter(String pUnconvertedPuzzle) {
        unconvertedPuzzle = pUnconvertedPuzzle;
    }

    public Puzzle convert() {
        char[] puzzleChars = unconvertedPuzzle.toCharArray();

        for (int i = 0; i < 81; i++) {
            if (puzzleChars[i] == '.') {

            }
        }

        return convertedPuzzle;
    }
}
