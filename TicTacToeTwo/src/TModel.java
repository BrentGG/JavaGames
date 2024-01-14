import javax.swing.*;
import java.awt.*;
import java.util.Observable;

/**
 * This is the model of the game
 */
public class TModel extends Observable {
    private TMatrix $matrix;
    public static enum FILL { CROSS, CIRCLE, EMPTY };
    public static int SIZE = 3;
    private FILL $turn;
    private FILL $winner;
    private TStrategyInterface $strategy = null; // This is for the strategy pattern which I wanted to use for the AI players, however I decided to not do this since there isn't much strategy to TicTacToe so an AI player wouldn't be very interesting.

    public TModel() {
        $matrix = new TMatrix();
        $turn = FILL.CROSS; //cross always starts
        $winner = null;
    }

    /**
     * Performs a player move and a computer move (if necessary)
     * @param row the row that was chosen by the player
     * @param col the column that was chosen by the player
     */
    public void doMove(int row, int col) {
        try {
            placeItem(row, col);
            if ($strategy != null) {
                Point pos = $strategy.getMove();
                placeItem(pos.x, pos.y);
            }
        } catch (NoSuchCellException nscex) {
            System.err.println("Unexpected Error");
        } catch (CellFullException cfex) {
            //let user choose other cell
        }
    }

    /**
     * Places a cross or circle in a requested position.
     * @param row the row
     * @param col the column
     * @throws NoSuchCellException when the chosen position doesnt exist
     * @throws CellFullException when the chosen position is already taken
     */
    private void placeItem(int row, int col) throws NoSuchCellException, CellFullException {
        $matrix.placeItem(row, col, $turn);
        if ($matrix.threeAdjacent($turn)) {
            $winner = $turn;
        }
        else
            switchTurn();
        setChanged();
        notifyObservers(new Point(row, col));
        if ($winner != null)
            reset();
    }

    /**
     * Switch the turn between cross and circle
     */
    private void switchTurn() {
        if ($turn == FILL.CROSS)
            $turn = FILL.CIRCLE;
        else
            $turn = FILL.CROSS;
    }

    /**
     * Return the FILL on a specific position
     * @param row the row
     * @param col the column
     * @return the fill on the requested position
     * @pre the row and column exist
     */
    public TModel.FILL getFill(int row, int col) {
        return $matrix.getFill(row, col);
    }

    public TModel.FILL getTurn() {
        return $turn;
    }

    public TModel.FILL getWinner() {
        return $winner;
    }

    public void reset() {
        $matrix = new TMatrix();
        $turn = FILL.CROSS; //cross always starts, cross is always the player
        $winner = null;
    }
}
