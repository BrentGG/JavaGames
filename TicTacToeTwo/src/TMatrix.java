/**
 * This class represents the matrix in which the crosses and circles are placed
 */
public class TMatrix {
    private TModel.FILL[][] $matrix;

    public TMatrix() {
        $matrix = new TModel.FILL[TModel.SIZE][TModel.SIZE];
        initMatrix();
    }

    /**
     * Sets all the cells in the matrix to FILL.EMPTY
     * @pre $matrix has been initialized (new)
     * @post every position is $matrix equals FILL.EMPTY
     */
    private void initMatrix() {
        for (int i = 0; i < TModel.SIZE; ++i)
            for (int j = 0; j < TModel.SIZE; ++j)
                $matrix[i][j] = TModel.FILL.EMPTY;
    }

    /**
     * Places an item in a specific position
     * @param row the row
     * @param col the col
     * @param item the item
     * @throws NoSuchCellException when the position doesn't exist
     * @throws CellFullException when the position is not empty
     */
    public void placeItem(int row, int col, TModel.FILL item) throws NoSuchCellException, CellFullException{
        try {
            if ($matrix[row][col] != TModel.FILL.EMPTY)
                throw new CellFullException();
            $matrix[row][col] = item;
        } catch (IndexOutOfBoundsException ioobex) {
            throw new NoSuchCellException();
        }
    }

    /**
     * Checks for three adjacent cells with a specific FILL
     * @param turn the FILL
     * @return true if three adjacent detected
     */
    public boolean threeAdjacent(TModel.FILL turn) {
        if (!threeHorizontalAdjacent(turn))
            if (!threeVerticalAdjacent(turn))
                if (!threeDiagonalAdjacent(turn))
                    return false;
        return true;
    }

    /**
     * Checks for three adjacent FILLS of type turn horizontally
     * @param turn the turn
     * @return true if three adjacent
     */
    private boolean threeHorizontalAdjacent(TModel.FILL turn) {
        for (int i = 0; i < TModel.SIZE; ++i) {
            if ($matrix[i][0] == turn &&
                $matrix[i][1] == turn &&
                $matrix[i][2] == turn)
                return true;
        }
        return false;
    }

    /**
     * Checks for three adjacent FILLS of type turn vertically
     * @param turn the turn
     * @return true if three adjacent
     */
    private boolean threeVerticalAdjacent(TModel.FILL turn) {
        for (int i = 0; i < TModel.SIZE; ++i) {
            if ($matrix[0][i] == turn &&
                $matrix[1][i] == turn &&
                $matrix[2][i] == turn)
                return true;
        }
        return false;
    }

    /**
     * Checks for three adjacent FILLS of type turn diagonally
     * @param turn the turn
     * @return true if three adjacent
     */
    private boolean threeDiagonalAdjacent(TModel.FILL turn) {
        if ($matrix[0][0] == turn && $matrix[1][1] == turn && $matrix[2][2] == turn)
            return true;
        if ($matrix[2][0] == turn && $matrix[1][1] == turn && $matrix[0][2] == turn)
            return true;
        return false;
    }

    /**
     * Return the FILL on a specific position
     * @param row the row
     * @param col the column
     * @return the fill on the requested position
     * @pre the row and column exist
     */
    public TModel.FILL getFill(int row, int col) {
        return $matrix[row][col];
    }
}
