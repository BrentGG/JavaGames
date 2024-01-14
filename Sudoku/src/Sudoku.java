import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Random;

/**
 * Main game logic.
 * @author Brent Gerets
 */
public class Sudoku extends Observable {
    private int[][] $matrix;
    private int[][] $startMatrix;
    private int[][] $solutionMatrix;
    public static int SIZE = 9;

    public Sudoku() {
        $matrix = new int[SIZE][SIZE];
    }

    /**
     * Main method, program starts here.
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Sudoku sudoku = new Sudoku();
                SudokuView sudokuView = new SudokuView(new SudokuController(sudoku));
                sudoku.addObserver(sudokuView);
            }
        });
    }

    /**
     * Chooses a random puzzle from the file of sudoku puzzles
     */
    public void newGame() {
        Random rng = new Random();
        String[] game = getLine(rng.nextInt(500000));
        $startMatrix = parseLine(game[0]);
        copyMatrix($matrix, $startMatrix);
        $solutionMatrix = parseLine(game[1]);
        setChanged();
        notifyObservers();
    }

    /**
     * Gets a line from a file with sudoku puzzles
     * @return the line
     */
    private String[] getLine(int linenumber) {
        String[] result = new String[2];
        String csvFile = "E:/Programming/Java Programs/Sudoku/src/sudoku.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int i = 0;
            while (i <= linenumber) {
                line = br.readLine();
                if (i == linenumber) {
                    result[0] = line.split(cvsSplitBy)[0];
                    result[1] = line.split(cvsSplitBy)[1];
                }
                ++i;
            }

        } catch (FileNotFoundException e) {
            System.err.println("Sudoku file not found.");
        } catch (IOException e) {
            System.err.println("An unexpected exception occurred.");
        }

        return result;
    }

    /**
     * Parses a sudoku puzzle in the form of a string and puts it in a two-dimensional array
     * @param line the string to be parsed
     * @return the array
     */
    private int[][] parseLine(String line) {
        int[][] result = new int[SIZE][SIZE];
        int unitNumber = 0;
        int startIndex = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                result[unitNumber] = parseLineToUnit(startIndex, line);
                startIndex += 3;
                unitNumber++;
            }
            startIndex += 18;
        }
        return result;
    }

    /**
     *
     * @param startIndex the index where the 'unit' starts
     * @return an array containing to right numbers
     * @param line the line to be parsed
     */
    private int[] parseLineToUnit(int startIndex, String line) {
        int[] result = new int[SIZE];
        int index = 0;

        for (int i = 0; i < 3; ++i) {
            for (int j = startIndex; j < startIndex + 3; ++j) {
                result[index] = Integer.parseInt(String.valueOf(line.charAt(j)));
                ++index;
            }
            startIndex += 9;
        }

        return result;
    }

    public void resetGame() {
        copyMatrix($matrix, $startMatrix);
        setChanged();
        notifyObservers();
    }

    public void setNumber(int x, int y, int number) {
        if ($startMatrix[x][y] == 0) {
            $matrix[x][y] = number;
        }
        setChanged();
        notifyObservers();
    }

    private void copyMatrix(int[][] dest, int[][] src) {
        for (int i = 0; i < src.length; ++i) {
            System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
        }
    }

    public int[][] getMatrix() {
        return $matrix;
    }

    /**
     * Check if the user has solved the puzzle
     */
    public void checkSolution() {
        String message = "";
        if (isEqual($matrix, $solutionMatrix))
            message = "won";
        else
            message = "lost";
        setChanged();
        notifyObservers(message);
    }

    /**
     * Check if a matrix is full
     * @param matrix the matrix to be checked
     * @return true is full
     */
    private boolean isFull(int[][] matrix) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Checks if 2 matrices are equal.
     */
    private boolean isEqual(int[][] m1, int[][] m2) {
        for (int i = 0; i < m1.length; ++i) {
            for (int j = 0; j < m1[0].length; ++j) {
                if (m1[i][j] != m2[i][j])
                    return false;
            }
        }
        return true;
    }
}
