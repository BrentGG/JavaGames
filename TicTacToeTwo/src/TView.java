import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

/**
 * The main view of the game, contains TCells
 */
public class TView extends JFrame implements Observer {
    private TController $controller;
    private JPanel $menu;
    private JPanel $field;
    private TCell[][] $cells;
    private GridBagConstraints $layoutConstraints;

    public TView(TController controller) {
        $controller = controller;

        setLayout(new GridBagLayout());
        $layoutConstraints = new GridBagConstraints();
        $layoutConstraints.gridwidth = 1;
        $layoutConstraints.gridheight = 1;

        initMenu();
        initField();

        setTitle("TicTacToe");
        setSize(650, 700);
        setLocation(200, 100);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Initializes the menu and all the components that are linked to the menu
     */
    private void initMenu() {
        $menu = new JPanel(new GridLayout(1, 4));

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
                $controller.onReset();
            }
        });
        $menu.add(resetButton);

        $layoutConstraints.gridx = 0;
        $layoutConstraints.gridy = 0;
        $layoutConstraints.insets = new Insets(0, 0, 10, 0);
        getContentPane().add($menu, $layoutConstraints);
    }

    /**
     * Initializes the field and all the components that are linked to the field
     */
    private void initField() {
        $field = new JPanel(new GridLayout(TModel.SIZE, TModel.SIZE, 3, 3));
        $field.setPreferredSize(new Dimension(600, 600));
        $cells = new TCell[TModel.SIZE][TModel.SIZE];
        initCells();

        $layoutConstraints.gridx = 0;
        $layoutConstraints.gridy = 1;
        $layoutConstraints.insets = new Insets(0, 0, 0, 0);
        getContentPane().add($field, $layoutConstraints);
    }

    /**
     * Initializes all the cells in $cells and adds them to $cellPanel. Adds a MouseListener to every cell.
     */
    private void initCells() {
        for (int i = 0; i < TModel.SIZE; ++i) {
            for (int j = 0; j < TModel.SIZE; ++j) {
                $cells[i][j] = new TCell();
                $field.add($cells[i][j]);
                int finalI = i;
                int finalJ = j;
                $cells[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        $controller.onClick(finalI, finalJ);
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e) {}
                });
            }
        }
        pack();
    }

    /**
     * Updates the user interface
     * @param o the object being observed
     * @param arg the date sent by o
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            Point pos = (Point)arg;
            $cells[pos.x][pos.y].setFill(((TModel)o).getFill(pos.x, pos.y));
            if (((TModel)o).getWinner() != null) {
                JOptionPane.showMessageDialog(this, ((TModel)o).getTurn() + " has won!", "We have a winner!", JOptionPane.PLAIN_MESSAGE);
                reset();
            }
        }
    }

    private void reset() {
        for (TCell[] row : $cells) {
            for (TCell col : row) {
                col.setFill(TModel.FILL.EMPTY);
            }
        }
    }
}
