import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TetrisWindow extends JFrame implements Observer {
    private TetrisController $controller;
    private JLabel[][] $field;
    private JLabel $score;

    public TetrisWindow(TetrisController controller) {
        $controller = controller;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setSize(new Dimension(500, 800));
        setResizable(false);
        setVisible(true);
        setTitle("Tetris");
        setLayout(new BorderLayout());
        $score = new JLabel("Score: 0");
        $score.setHorizontalAlignment(0);
        getContentPane().add($score, BorderLayout.NORTH);
        initField();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) {
                $controller.onKey(e);
            }
            @Override
            public void keyReleased(KeyEvent e) { }
        });
    }

    private void initField() {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(Tetris.ROWS, Tetris.COLS));
        $field = new JLabel[Tetris.ROWS][Tetris.COLS];
        for (int i = 0; i < Tetris.ROWS; ++i) {
            for (int j = 0; j < Tetris.COLS; ++j) {
                $field[i][j] = new JLabel();
                $field[i][j].setOpaque(true);
                $field[i][j].setBackground(new Color(0,0,0));
                $field[i][j].invalidate();
                fieldPanel.add($field[i][j]);
            }
        }
        getContentPane().add(fieldPanel, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.getClass() == Tetris.class) {
            Tetris model = (Tetris)arg;
            $score.setText("Score: " + (model.getScore()));
            for (JLabel[] row : $field) {
                for (JLabel col : row) {
                    col.setBackground(Color.BLACK);
                    col.setBorder(null);
                    col.invalidate();
                }
            }
            for (Tetrimino t : model.getTetrominos()) {
                Color color = t.getColor();
                for (Point p : t.getPoints()) {
                    $field[p.y][p.x].setBackground(color);
                    $field[p.y][p.x].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false));
                    $field[p.y][p.x].invalidate();
                }
            }
            for (int i = 0; i < Tetris.COLS; ++i) {
                $field[0][i].setBackground(Color.GRAY);
                $field[0][i].invalidate();
            }
        }
    }
}
