// temporary class for testing solving using QQWing Sudoku (https://qqwing.com/generate.html)

public class QQWingConverter {

    public static Puzzle convert(String pUnconvertedPuzzle) {
        char[] puzzleChars = pUnconvertedPuzzle.toCharArray();

        Puzzle convertedPuzzle = new Puzzle();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++){
                if (puzzleChars[(9*i)+j] == '.') {
                    convertedPuzzle.addCell(false,0, j, i);     // a '.' represents a blank cell and therefore a 0
                }
                else {
                    try {
                        int value = Integer.parseInt(String.valueOf(puzzleChars[((9*i)+j)]));
                        convertedPuzzle.addCell(true, value, j, i);
                    }
                    catch(NumberFormatException e) {
                        convertedPuzzle.addCell(false, 0, j, i);          // sets to empty if not valid value for some reason
                    }
                }
            }
        }

        return convertedPuzzle;
    }
}

