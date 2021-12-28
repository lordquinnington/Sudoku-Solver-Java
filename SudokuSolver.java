public class SudokuSolver {
    // method to call to attempt to solve any given grid
    public static Puzzle solve(Puzzle pCurrentPuzzle, boolean useOS, boolean useHS, boolean useNG) {
        if (!pCurrentPuzzle.allConstraintsValid()) {
            pCurrentPuzzle.markAsInvalid();
            return pCurrentPuzzle;
        }
        Puzzle prevPuzzle = pCurrentPuzzle.makeCopy();      // makes a copy as java passes by reference
        Puzzle p = applySolvingLogic(pCurrentPuzzle,useOS,useHS,useNG);     // attempts initial solving using all solving techniques

        if (!(p.checkIfComplete()[1])) {        // if initial solving fails, takes an even more brute force dependent approach
            return applySolvingLogic(prevPuzzle,false,false,false);
        }

        return p;       // returns the puzzle if the solving did not fail
    }

    // method to tie all the solving logic together to solve the grid
    protected static Puzzle applySolvingLogic(Puzzle pPuzzle, boolean useOS, boolean useHS, boolean useNG) {
        boolean needsBruteForcing = false;      // marks if brute forcing is needed

        while (!pPuzzle.checkIfComplete()[1]) {        // loops round whilst the grid is not complete or stuck
            Puzzle beforeSolving = pPuzzle.makeCopy();       // makes copy of grid to refer back to after to check for differences

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {       // considers each cell individually
                    if (!(pPuzzle.getConstraints())[1][i].getCells()[j].isSolved()) {        // checks if the cell is solved
                        boolean valid = eliminateNotes(pPuzzle, (pPuzzle.getConstraints())[1][i].getCells()[j], i, j);        // applies eliminateNotes

                        if (!valid) {       // if the cell is not solved and has no notes then the grid is invalid
                            pPuzzle.markAsInvalid();
                            return pPuzzle;
                        }

                        // applies other solving techniques, if required
                        if (useOS) {
                            openSingles(pPuzzle, (pPuzzle.getConstraints())[1][i].getCells()[j]);
                        }
                        if (useHS) {
                            hiddenSingles(pPuzzle, (pPuzzle.getConstraints())[1][i].getCells()[j], i, j);
                        }
                        if (useNG) {
                            noteGroups(pPuzzle, (pPuzzle.getConstraints())[1][i].getCells()[j], i, j);
                        }
                    }
                }
            }

            if (pPuzzle.checkIfComplete()[0] && !pPuzzle.checkIfComplete()[1]) {
                pPuzzle.markAsInvalid();     // marks grid as invalid if full but not valid
                return pPuzzle;
            }
            if (pPuzzle.isEqual(beforeSolving)) {       // checks if any solving progress has been made
                needsBruteForcing = true;       // if not, initiates brute forcing
                break;
            }
        }

        if (needsBruteForcing) {
            return bruteForce(pPuzzle, useOS, useHS, useNG);      // returns the grid after brute forcing, if needed
        }
        return pPuzzle;
    }

    /* #######################################################
                         MISC METHODS
    ####################################################### */

    // method to return 2D grid reference of where the Cell should be added
    protected static int[] indexToUpdate(int pConstraint, int pConstraintIndex, int pCellIndex) {
        if (pConstraint == 0) {
            return new int[]{pCellIndex, pConstraintIndex};     // returns correct index if cell was in a row
        }
        else if (pConstraint == 1) {
            return new int[]{pConstraintIndex, pCellIndex};     // returns correct index if cell was in a column
        }
        else {
            return new int[]{((pCellIndex%3) + (3*(pConstraintIndex%3))), ((pCellIndex/3) + (3*(pConstraintIndex/3)))};     // returns correct index if cell was in a column
        }
    }

    // method to append a value onto a given array
    public static int[] append(int[] arr, int newVal) {
        int[] newArr = new int[arr.length+1];       // initialises an array 1 longer than the previous

        // copies the contents of the old array to the new array
        System.arraycopy(arr, 0, newArr, 0, arr.length);

        newArr[arr.length] = newVal;      // sets the last index equal to the value to be appended
        return newArr;
    }

    // method to check if a value appears in a given array
    public static boolean inArr(int[] arr, int val) {
        for (int arrVal : arr) {      // loops through the given array
            if (arrVal == val) {
                return true;        // returns true if value is in array
            }
        }

        return false;       // returns false if value is not found
    }

    /* #######################################################
                         OPEN SINGLES
    ####################################################### */

    // method to check for open singles
    protected static void openSingles(Puzzle pCurrentPuzzle, Cell pCurrentCell) {
        Constraint[][] constraints = pCurrentPuzzle.getConstraints();       // retrieves all constraints
        int[] cellConstraints = pCurrentCell.getConstraints();      // retrieves the indexes of the cell's constraints

        boolean changed = true;     // marks if any updates to the grid have been made (potentially meaning more solving can be done)

        while (changed) {
            changed = false;

            for (int i = 0; i < 3; i++) {
                if (constraints[i][cellConstraints[i]].numOfCells() == 8) {
                    Cell[] cells = constraints[i][cellConstraints[i]].getCells();       // gets cells if constraint has 8

                    for (int j = 0; j < 9; j++) {
                        if (cells[j].getSolution() == 0) {      // locates blank index
                            int[] index = indexToUpdate(i, cellConstraints[i], j);      // works out index to add cell into grid
                            int value = 45-constraints[i][cellConstraints[i]].sumOfCells();
                            if (value >= 1 && value <= 9) {
                                pCurrentPuzzle.addCell(false, value, index[0], index[1]);
                                constraints = pCurrentPuzzle.getConstraints();      // gets updated constraints
                                changed = true;     // a change has been made so change is marked back to true
                                break;      // breaks immediately as there will be no more empty cells
                            }
                            else {
                                pCurrentPuzzle.markAsInvalid();
                            }
                        }
                    }
                }
            }
        }
    }

    /* #######################################################
                        ELIMINATE NOTES
    ####################################################### */

    // method to find notes which can be eliminated
    protected static boolean eliminateNotes(Puzzle pCurrentPuzzle, Cell pCurrentCell, int pCol, int pRow) {
        Constraint[][] constraints = pCurrentPuzzle.getConstraints();       // retrieves all constraints
        int[] cellConstraints = pCurrentCell.getConstraints();      // retrieves the indexes of the cell's constraints

        for (int i = 0; i < 3; i++) {
            int[] cellValues = constraints[i][cellConstraints[i]].getSolvedCells();     // gets array of solved values
            removeNotesFromCell(pCurrentCell, cellValues);// removes all solved values from cell's notes

            pCurrentPuzzle.updateCell(pCurrentCell, pCol, pRow);        // updates puzzle with the cell
            constraints = pCurrentPuzzle.getConstraints();      // retrieves updated constraints
        }

        // returns false if length of notes is 0 and cell is not solved -> invalid grid
        return (!(pCurrentCell.notesRemaining().length == 0) || pCurrentCell.isSolved());
    }

    // method to remove notes from a given cell
    protected static void removeNotesFromCell(Cell pCell, int[] pOtherCells) {
        for (int pOtherCell : pOtherCells) {      // don't need to check if value is in the cell's
            pCell.removeNote(pOtherCell);               // notes as it will just be ignored if not
        }

        if (pCell.numOfNotes() == 1) {
            pCell.setSolution(pCell.notesRemaining()[0]);     // sets cell value to note if only one note left
        }
    }

    /* #######################################################
                         HIDDEN SINGLES
    ####################################################### */

    // method to check for hidden singles
    protected static void hiddenSingles(Puzzle pCurrentPuzzle, Cell pCurrentCell, int pCol, int pRow) {
        Constraint[][] constraints = pCurrentPuzzle.getConstraints();       // retrieves the constraints
        int[] cellConstraints = pCurrentCell.getConstraints();      // retrieves the index the cell is at in the constraints

        for (int i = 0; i < pCurrentCell.notesRemaining().length; i++) {        // loops through all the remaining notes
            for (int j = 0; j < 3; j++) {       // loops through all the constraints
                if (numOfAppearances(pCurrentCell.notesRemaining()[i], constraints[j][cellConstraints[j]].getAllNotes()) == 1) {
                    pCurrentCell.setSolution(pCurrentCell.notesRemaining()[i]);     // if note is hidden single, the value is updated
                    pCurrentPuzzle.updateCell(pCurrentCell, pCol, pRow);        //  puzzle is updated
                    return;      // returns immediately as no point checking anymore if cell has been filled
                }
            }
        }

    }

    // method to check how many times a note appears in the constraint
    protected static int numOfAppearances(int pNote, int[][] pNotes) {
        int counter = 0;        // counter to keep track

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < pNotes[i].length; j++) {
                if (pNote == pNotes[i][j]) {
                    counter++;      // if the given note appears in the array of all notes then the counter increments
                }
            }
        }

        return counter;     // will return 1 if note is the note is a hidden single
    }

    /* #######################################################
                    PAIRS, TRIPLETS & QUADS
    ####################################################### */

    // method to check for quads, triplets and pairs for a cell in any of it's constraints
    protected static void noteGroups(Puzzle pCurrentPuzzle, Cell pCurrentCell, int pCol, int pRow) {
        Constraint[][] constraints = pCurrentPuzzle.getConstraints();       // retrieves the constraints
        int[] cellConstraints = pCurrentCell.getConstraints();      // retrieves the index the cell is at in the constraints
        int[] notes = pCurrentCell.notesRemaining();        // gets all the notes remaining in the cell

        if (notes.length == 1) {
            return;      // error catching - if there is only one note then the cell is an open single thus returns immediately
        }

        for (int i = 0; i < 3; i++) {       // loops through each of the constraints
            int[][] allNotes = constraints[i][cellConstraints[i]].getAllNotes();        // retrieves all the notes from the constraint

            for (int k = 4; k >= 2; k--) {      // k becomes the constant determining quads, triplets and pairs
                if (k <= notes.length) {        // checks that there are enough notes - no point looking for quads in a cell with only two notes
                    int[][] allAppearances = new int[notes.length][];       // creates 2D array to map the note appearances to the indexes they appear at
                    for (int l = 0; l < notes.length; l++) {        // loops through the notes and adds the other indexes to the array
                        allAppearances[l] = appearanceIndex(allNotes, notes[l], constraints[i][cellConstraints[i]], pCol, pRow);
                    }

                    int[][] x = findNoteGroups(allAppearances, notes, k);      // checks for groups

                    if (x[0].length > 0 && x[0][0] != -1 && x[1].length > 0 && x[1][0] == k-1) {
                        int[] notesToEliminate = x[0];      // gets the notes to be removed
                        int[] indexesToRemoveFrom = x[1];       // gets the indexes of the other cells which the notes need to be removed from

                        if (notesToEliminate[0] != 0) {     // if there are groups, the array will be of k length
                            removeNotesFromCell(pCurrentCell, notesToEliminate);// removes the notes from the cell
                            pCurrentPuzzle.updateCell(pCurrentCell, pCol, pRow);        // updates the puzzle

                            for (int indexToRemove : indexesToRemoveFrom) {      // loops through the indexes and removes the notes from each of the cells
                                removeNotesFromCell(constraints[i][cellConstraints[i]].getCells()[indexToRemove], notesToEliminate);
                                int[] temp = constraints[i][cellConstraints[i]].getCells()[indexToRemove].getConstraints();    // updates puzzle
                                pCurrentPuzzle.updateCell(constraints[i][cellConstraints[i]].getCells()[indexToRemove], temp[1], temp[0]);
                            }

                            constraints = pCurrentPuzzle.getConstraints();      // retrieves the updated constraints
                        }
                    }
                }
            }
        }
    }

    // method to find all the other indexes that the note appears in
    protected static int[] appearanceIndex(int[][] pAllNotes, int pNote, Constraint constraint, int pCol, int pRow) {
        int[] indexes = new int[0];     // array to store all the other appearances

        for (int i = 0; i < 9; i++) {       // loops through each of the cells in the constraint
            // checks that the cell is not the same as the current one and if the cell is still not solved
            if (!constraint.getCells()[i].isSolved()) {
                if (constraint.getCells()[i].getConstraints()[1] != pCol || constraint.getCells()[i].getConstraints()[0] != pRow) {
                    for (int j = 0; j < pAllNotes[i].length; j++) {     // loops through each of the cell's notes using the all notes array
                        if (pNote == pAllNotes[i][j]) {     // if the note is present in the other cell's notes, that cell's index in the constraint is added
                            indexes = append(indexes, i);
                        }
                    }
                }
            }
        }

        return indexes;
    }

    // method to find all the notes which can be eliminated from the cell, if any
    protected static int[][] findNoteGroups(int[][] pAppearances, int[] pNotes, int pNum) {
        int[] groups = findGroups(pAppearances,pNum);

        if (groups.length > 1) {
            int[] notesToKeep = new int[pNum];

            for (int i = 0; i < pNum; i++) {
                notesToKeep[i] = pNotes[groups[i]];     // adds the notes which are part of the group to an array
            }

            int[] notesToEliminate = notesToEliminate(notesToKeep, new int[]{1,2,3,4,5,6,7,8,9});       // gets all the notes which arent the group

            int[] differentIndexes = new int[0];        // array to store each unique index

            for (int i = 0; i < pAppearances.length; i++) {     // forms an array of the indexes to update (without duplicates)
                if (inArr(groups,i)) {
                    for (int j = 0; j < pAppearances[i].length; j++) {
                        if (!inArr(differentIndexes, pAppearances[i][j])) {        // checks if the value is in the array
                            differentIndexes = append(differentIndexes, pAppearances[i][j]);        // if not, it adds it to the array
                        }
                    }
                }
            }

            return new int[][]{notesToEliminate,differentIndexes};
        }

        return new int[][]{{-1},{-1}};
    }

    // method to effectively invert the notes to keep, as to return an array of notes to remove
    protected static int[] notesToEliminate(int[] pNotesToKeep, int[] pNotes) {
        int[] notesToEliminate = new int[0];      // initialises an array for all notes to eliminate

        for (int note : pNotes) {       // loops through all the notes
            if (!inArr(pNotesToKeep, note)) {       // checks if note is to be kept or not
                notesToEliminate = append(notesToEliminate, note);     // if not in array of notes to keep, it is added to the notes to eliminate
            }
        }

        return notesToEliminate;
    }

    // method to search through all the unique combinations  and check if they have the correct number of different indexes
    protected static int[] findGroups(int[][] pPotentialGroups, int pNum) {
        // for loop which will check each combination eg in an array of [1,2,3,4] with num as 3,
        // the loop will check 123, 124, 134, 234 thereby trying every possible combination
        for (int i = 0; i < pPotentialGroups.length; i++) {
            for (int j = i+1; j < pPotentialGroups.length; j++) {
                if (pNum > 2) {     // carries on looping if looking for a triplet or quad
                    for (int k = j+1; k < pPotentialGroups.length; k++) {
                        if (pNum > 3) {     // carries on looking if looking for a quad
                            for (int l = k+1; l < pPotentialGroups.length; l++) {
                                int[][] x = {pPotentialGroups[i], pPotentialGroups[j], pPotentialGroups[k], pPotentialGroups[l]};
                                int uniqueIndexes = numUniqueIndexes(x);        // checks the number of different indexes the 4 combinations have

                                if (uniqueIndexes <= pNum - 1) {            // if the combination meets the requirements
                                    return new int[]{i,j,k,l};      // returns array of the indexes of potential notes which form a group of quads
                                }
                            }
                        }
                        else {
                            int[][] x = {pPotentialGroups[i], pPotentialGroups[j], pPotentialGroups[k]};
                            int uniqueIndexes = numUniqueIndexes(x);        // checks the number of different indexes the 3 combinations have

                            if (uniqueIndexes <= pNum - 1) {        // if the combination meets the requirements
                                return new int[]{i,j,k};        // returns array of the indexes of potential notes which form a group of triplets
                            }
                        }
                    }
                }
                else {
                    int[][] x = {pPotentialGroups[i], pPotentialGroups[j]};
                    int uniqueIndexes = numUniqueIndexes(x);        // checks the number of different indexes the 2 combinations have

                    if (uniqueIndexes <= pNum - 1) {        // if the combination meets the requirements
                        return new int[]{i,j};        // returns array of the indexes of potential notes which form a group of pairs
                    }
                }
            }
        }

        return new int[]{-1};       // returns -1 if no groups which satisfy requirements
    }

    // method to count how many indexes are different
    protected static int numUniqueIndexes(int[][] pIndexes) {
        int[] numUniqueIndexes = new int[0];        // array to store each unique index

        for (int[] index : pIndexes) {
            for (int i : index) {
                if (!inArr(numUniqueIndexes, i)) {        // checks if the value is in the array
                    numUniqueIndexes = append(numUniqueIndexes, i);        // if not, it adds it to the array
                }
            }
        }

        return numUniqueIndexes.length;     // the length of the array is returned giving how ever many different indexes there are
    }

    /* #######################################################
                        BRUTE FORCING
    ####################################################### */

    // method to brute force/guess to find a puzzle's solution
    protected static Puzzle bruteForce(Puzzle pCurrentPuzzle, boolean useOS, boolean useHS, boolean useNG) {
        int[] indexWithLeastNotes = pCurrentPuzzle.indexWithLeastNotes();       // gets index with least notes
        int numOfNotes = pCurrentPuzzle.getConstraints()[1][indexWithLeastNotes[0]].getCells()[indexWithLeastNotes[1]].numOfNotes();

        for (int i = 0; i < numOfNotes; i++) {      // loops through each note in order to try it as a value
            Puzzle p = pCurrentPuzzle.makeCopy();       // makes identical copy to try the note as the value
            // updates the puzzle with the cell to test
            p.addCell(true, pCurrentPuzzle.getConstraints()[1][indexWithLeastNotes[0]].getCells()[indexWithLeastNotes[1]].notesRemaining()[i],indexWithLeastNotes[0],indexWithLeastNotes[1]);
            p = applySolvingLogic(p,useOS,useHS,useNG);       // uses recursion to try solve the grid

            if (p.isValid() || p.checkIfComplete()[1]) {      // if the grid comes back as valid then the answer is returned
                return p;
            }
        }

        pCurrentPuzzle.markAsInvalid();
        return pCurrentPuzzle;      // if all grids come back as unsolved then the puzzle is deemed as unsolvable
    }
}

