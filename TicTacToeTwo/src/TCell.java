import javax.swing.*;

/**
 * Represents one cell of the matrix
 */
public class TCell extends JLabel {
    private static ImageIcon cross = createImageIcon("cross.png");
    private static ImageIcon circle = createImageIcon("circle.png");
    private static ImageIcon empty = createImageIcon("empty.png");

    /**
     * Creates a TCell with the 'empty' icon
     */
    public TCell() {
        setOpaque(true);
        setIcon(empty);
        invalidate();
    }

    /**
     * Sets the icon of the cell to cross, circle or empty
     * @param fill the fill to change to.
     * @pre fill != null
     */
    public void setFill(TModel.FILL fill) {
        switch (fill) {
            case CROSS:
                setIcon(cross);
                break;
            case CIRCLE:
                setIcon(circle);
                break;
            default:
                setIcon(empty);
        }
        invalidate();
    }

    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TCell.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
