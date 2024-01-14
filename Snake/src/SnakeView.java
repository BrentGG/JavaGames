import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class SnakeView extends JFrame implements Observer {
    SnakeController $controller;
    private SnakeMenu $menu;
    private JPanel $gridPanel;
    private JLabel[][] $grid = new JLabel[Snake.ROWS][Snake.COLS];

    public SnakeView(SnakeController controller) {
        $controller = controller;

        setLayout(new BorderLayout());

        $menu = new SnakeMenu(this);
        getContentPane().add($menu, BorderLayout.NORTH);
        $controller.newObserver($menu);

        $gridPanel = new JPanel();
        $gridPanel.setLayout(new GridLayout(Snake.ROWS, Snake.COLS, 1, 1));
        //$gridPanel.setLayout(new GridLayout(Snake.ROWS, Snake.COLS));
        $gridPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        $gridPanel.setBackground(new Color(0,0,0));
        initGrid();
        resetGrid();
        getContentPane().add($gridPanel, BorderLayout.CENTER);

        pack();
        setTitle("Snake");
        setSize(new Dimension(500, 500));
        setLocation(200, 200);
        setVisible(true);
        setResizable(false);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                $controller.onKey(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        setFocusable(true);
    }

    private void initGrid() {
        for (int i = 0; i < Snake.ROWS; ++i) {
            for (int j = 0; j < Snake.COLS; ++j) {
                $grid[i][j] = new JLabel();
                $grid[i][j].setOpaque(true);
                $grid[i][j].setBackground(new Color(0,0,0));
                $grid[i][j].invalidate();
                $gridPanel.add($grid[i][j]);
            }
        }
        pack();
    }

    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SnakeView.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!arg.equals("won") && !arg.equals("lost")) {
            updateSnake(o, arg);
        }
        else if (arg.equals("lost")) {
            lostAnimation();
        }
        else if (arg.equals("won")) {
            wonAnimation();
        }
    }

    private void updateSnake(Observable o, Object arg) {
        /* more efficient but leaves green blocks when you turn at the same moment of getting an apple
        SnakeNode lastnode = ((ArrayList<SnakeNode>) arg).get(((ArrayList<SnakeNode>) arg).size() - 1);
        $grid[lastnode.getPrevY()][lastnode.getPrevX()].setBackground(new Color(0, 0, 0));
        SnakeNode firstnode = ((ArrayList<SnakeNode>)arg).get(0);
        $grid[firstnode.getY()][firstnode.getX()].setBackground(new Color(65,255,0));
        */
        resetGrid();
        for (SnakeNode n : (ArrayList<SnakeNode>) arg) {
            $grid[n.getY()][n.getX()].setBackground(new Color(65, 255, 0));
            $grid[n.getY()][n.getX()].invalidate();
        }
        SnakeNode firstnode = ((ArrayList<SnakeNode>)arg).get(0);
        $grid[firstnode.getY()][firstnode.getX()].setBackground(new Color(0,200,0));

        for (int[] obs: ((SnakeModel) o).getObstacles()) {
            $grid[obs[1]][obs[0]].setBackground(new Color(0, 0, 255));
            $grid[obs[1]][obs[0]].invalidate();
        }

        int[] applepos = ((SnakeModel) o).getApplePos();
        $grid[applepos[1]][applepos[0]].setBackground(new Color(255,0,0));
    }

    private void lostAnimation() {
        slideAnimation(new Color(255, 255, 255), new Color(0, 0, 0), 3);
        String[] text = new String[]{" G", " A", " M", " E", " O", " V", " E", " R"};
        int row = (Snake.ROWS/2) - 1;
        int col = (Snake.COLS/2) - 2;
        int k = 0;
        for (int i = row; i <= row+1; ++i) {
            for (int j = col; j <= col+3; ++j) {
                $grid[i][j].setText(text[k]);
                ++k;
                $grid[i][j].setFont(new Font("SansSerif", Font.PLAIN, (int)(30.0 * (10.0 / Snake.ROWS))));
                $grid[i][j].setForeground(new Color(255, 0, 0));
            }
        }
    }

    private void wonAnimation() {
        slideAnimation(new Color(255, 255, 255), new Color(0, 0, 0), 3);
        String[] text = new String[]{" Y", " O", " U", " W", " O", " N"};
        int row = (Snake.ROWS/2) - 1;
        int col = (Snake.COLS/2) - 2;
        int k = 0;
        for (int i = row; i <= row+1; ++i) {
            for (int j = col; j <= col+2; ++j) {
                $grid[i][j].setText(text[k]);
                ++k;
                $grid[i][j].setFont(new Font("SansSerif", Font.PLAIN, 30));
                $grid[i][j].setForeground(new Color(0, 255, 0));
            }
        }
    }

    private void slideAnimation(Color color1, Color color2, int sliderLength) {
        for (int i = 0; i < $grid.length; ++i) {
            for (int j = 0; j < $grid[i].length; ++j) {
                if (j > sliderLength-1) {
                    $grid[i][j-sliderLength].setBackground(color2);
                    $grid[i][j-sliderLength].invalidate();
                }
                else if (i > 0){
                    $grid[i-1][j+(Snake.COLS-sliderLength)].setBackground(color2);
                    $grid[i-1][j+(Snake.COLS-sliderLength)].invalidate();
                }
                $grid[i][j].setBackground(color1);
                $grid[i][j].invalidate();
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = sliderLength; i > 0; --i) {
            $grid[Snake.ROWS-1][Snake.COLS-i].setBackground(color2);
            $grid[Snake.ROWS-1][Snake.COLS-i].invalidate();
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetGrid() {
        for (JLabel[] row : $grid) {
            for (JLabel col : row) {
                col.setText("");
                col.setBackground(new Color(0,0,0));
                col.invalidate();
            }
        }
    }

    public void startClicked(int speed, boolean walls, boolean obstacles, boolean ai) {
        requestFocus();
        requestFocusInWindow();
        $controller.onStart(speed, walls, obstacles, ai);
    }

    public void stopClicked() {
        $controller.onStop();
    }
}
