public class Main {
    public static void main(String[] args) {
        Puzzle p = new Puzzle();
        p.addCell(true,5,0,0);
        p.addCell(true,5,1,0);
        System.out.println("unsolved puzzle:\n"+p.toString());
        Puzzle p2 = SudokuSolver.solve(p,true,true,true);
        System.out.println("output:");
        System.out.println("full: "+p2.checkIfComplete()[0]+"\nvalid: "+p2.checkIfComplete()[1]);
        System.out.println("isValid: "+p2.isValid());
        System.out.println(p2.toString());

    }
}
