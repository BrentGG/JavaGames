import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class SudokuView extends JFrame implements Observer {
    private SudokuController $controller;
    private JPanel $menu;
    private JPanel $field;
    private JPanel $input;
    private JLabel[][] $matrix;
    private Point $selected;
    private JLabel $info;
    private JButton $checkButton;

    public SudokuView(SudokuController controller) {
        $controller = controller;

        $selected = new Point(0, 0);

        setLayout(new BorderLayout());

        initMenu();
        initFieldMatrix();
        initInput();

        setSize(600, 600);
        setLocation(100, 100);
        setVisible(true);
        setTitle("Sudoku");
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //keylistener not working (prob some shitty focus issue)
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                $controller.onKey(e, $selected);
            }

            public void keyPressed(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}
        });
        setFocusable(true);
    }

    private void initMenu() {
        $menu = new JPanel();

        JButton newButton = new JButton("New Game");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                $controller.onNew();
            }
        });
        $menu.add(newButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                $controller.onReset();
            }
        });
        $menu.add(resetButton);

        $checkButton = new JButton("Check");
        $checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                $controller.onCheck();
            }
        });
        $checkButton.setEnabled(false);
        $menu.add($checkButton);

        $info = new JLabel();
        $menu.add($info);

        add($menu, BorderLayout.NORTH);
    }

    /**
     * Initializes the field and the matrix.
     */
    private void initFieldMatrix() {
        $matrix = new JLabel[Sudoku.SIZE][Sudoku.SIZE];
        $field = new JPanel();
        $field.setLayout(new GridLayout(3, 3, 3, 3));

        for (int i = 0; i < 9; ++i) {
            JPanel unit = new JPanel(new GridLayout(3, 3, 1, 1));
            for (int j = 0; j < 9; ++j) {
                JLabel box = new JLabel();
                box.setOpaque(true);
                box.setBackground(new Color(255, 255, 255));
                box.setFont(new Font("SansSerif", Font.PLAIN, 30));
                box.setHorizontalAlignment(SwingConstants.CENTER);
                box.setVerticalAlignment(SwingConstants.CENTER);
                int finalI = i;
                int finalJ = j;
                box.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        onClick(finalI, finalJ);
                    }
                    public void mousePressed(MouseEvent e) {}
                    public void mouseReleased(MouseEvent e) {}
                    public void mouseEntered(MouseEvent e) {}
                    public void mouseExited(MouseEvent e) {}
                });
                $matrix[i][j] = box;
                unit.add(box);
                $matrix[i][j].invalidate();
            }
            $field.add(unit);
        }
        add($field, BorderLayout.CENTER);
    }

    /**
     * Initializes the input menu.
     */
    private void initInput() {
        $input = new JPanel();
        for (int i = 1; i <= 9; ++i) {
            JButton button = new JButton(Integer.toString(i));
            int finalI = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { $controller.onInput($selected, finalI); }
            });
            $input.add(button);
        }
        JButton delete = new JButton("<");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { $controller.onDelete($selected); }
        });
        $input.add(delete);
        add($input, BorderLayout.SOUTH);
    }

    /**
     * Highlight the box which the user clicked on and all the boxes with the same number
     * @param unit the index of the first dimension of the matrix
     * @param box the index of the second dimension of the matrix
     */
    private void onClick(int unit, int box) {
        $matrix[$selected.x][$selected.y].setBackground(new Color(255, 255, 255));
        if (!$matrix[$selected.x][$selected.y].getText().equals(""))
            highlightNumbers($matrix[$selected.x][$selected.y].getText(), new Color(255, 255, 255));
        if (!$matrix[unit][box].getText().equals(""))
            highlightNumbers($matrix[unit][box].getText(), new Color(232, 232, 232));
        $matrix[unit][box].setBackground(new Color(100, 149, 237));
        $selected = new Point(unit, box);
    }

    /**
     * Highlights all boxes which contain a specific number.
     * @param number the number
     */
    private void highlightNumbers(String number, Color color) {
        for (JLabel[] row : $matrix) {
            for (JLabel col : row) {
                if (col.getText().equals(number))
                    col.setBackground(color);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        int[][] matrix = ((Sudoku)o).getMatrix();
        for (int i = 0; i < Sudoku.SIZE; ++i) {
            for (int j = 0; j < Sudoku.SIZE; ++j) {
                if (matrix[i][j] == 0)
                    $matrix[i][j].setText("");
                else
                    $matrix[i][j].setText(Integer.toString(matrix[i][j]));
                $matrix[i][j].invalidate();
            }
        }

        if (arg != null) {
            if (((String) arg).equals("won"))
                JOptionPane.showMessageDialog(this, "You Won!", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
            else if (((String) arg).equals("lost"))
                JOptionPane.showMessageDialog(this, "Incorrect.", "Try Again", JOptionPane.PLAIN_MESSAGE);
        }

        if (isFull($matrix))
            $checkButton.setEnabled(true);
        else
            $checkButton.setEnabled(false);
    }

    /**
     * Checks if every box has been filled
     */
    private boolean isFull(JLabel[][] matrix) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j].getText().equals(""))
                    return false;
            }
        }
        return true;
    }
}
