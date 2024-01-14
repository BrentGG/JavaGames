import java.awt.*;
import java.util.*;

public class Tetris extends Observable {
    private ArrayList<Tetrimino> $tetriminos;
    private FILL[][] $field;
    private Tetrimino $current;
    private Tetrimino $upcoming;
    public static int ROWS = 20;
    public static int COLS = 12;
    private Timer $timer;
    private int $ticksUntilDown = 10;
    private int $ticks = 0;
    private int $score = 0;
    private static int SCORE_PER_SPEEDUP = 2500;

    private String $cheatCode = "";
    private boolean $onli = false;

    private enum FILL {
        EMPTY,
        FILLED
    }

    public Tetris() {
        $tetriminos = new ArrayList<>();
        initField();
        $current = randomTetrimino();
        $upcoming = randomTetrimino();
        $tetriminos.add($current);
        start();
    }

    private void initField() {
        $field = new FILL[ROWS][COLS];
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                $field[i][j] = FILL.EMPTY;
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Tetris tetris = new Tetris();
                TetrisWindow window = new TetrisWindow(new TetrisController(tetris));
                tetris.addObserver(window);
            }
        });
    }

    class TickTask extends TimerTask {
        public void run() {
            tick();
        }
    }

    public void start() {
        $timer = new Timer();
        $timer.schedule(new TickTask(), 0, 50);
    }

    public void stop() {
        $timer.cancel();
    }

    public void tick() {
        ++$ticks;
        if ($ticks >= $ticksUntilDown) {
            $ticks = 0;
            down();
        }
    }

    private void down() {
        $current.down(1);
        setChanged();
        notifyObservers(this);
        if (checkCollision($current))
            collisionOccured();
        clearLines(calcFullLines());
        if (checkLost())
            $timer.cancel();
    }

    private void collisionOccured() {
        for (Point p : $current.getPoints())
            $field[p.y][p.x] = FILL.FILLED;

        $current = $upcoming;
        $upcoming = randomTetrimino();
        $tetriminos.add($current);
    }

    private ArrayList<Integer> calcFullLines() {
        ArrayList<Integer> output = new ArrayList<>();
        for (int i = 0; i < ROWS; ++i) {
            boolean full = true;
            for (int j = 0; j < COLS; ++j) {
                if ($field[i][j] == FILL.EMPTY) {
                    full = false;
                    break;
                }
            }
            if (full)
                output.add(i);
        }
        return output;
    }

    private void clearLines(ArrayList<Integer> lines) {
        if (lines.size() < 4)
            updateScore(100 * lines.size());
        else
            updateScore(800);
        if (lines.size() == 0)
            return;
        for (int l : lines) {
            for (Tetrimino t : $tetriminos) {
                if (t != $current)
                    t.clearLine(l);
            }
        }
        removeEmpty();
        for (Tetrimino t : $tetriminos) {
            if (t != $current)
                t.downAbove(lines.size(), lines.get(0));
        }
        fillField();
        setChanged();
        notifyObservers(this);
    }

    private Tetrimino randomTetrimino() {
        if ($onli)
            return new I();
        Random rng = new Random();
        switch (rng.nextInt(7)) {
            case 0: return new I();
            case 1: return new O();
            case 2: return new T();
            case 3: return new J();
            case 4: return new L();
            case 5: return new S();
            case 6: return new Z();
        }
        return null;
    }

    public Tetrimino getCurrent() {
        return $current;
    }

    private boolean checkCollision(Tetrimino t) {
        for (Point p : t.getPoints()) {
            if (p.y+1 >= ROWS)
                return true;
            for (Tetrimino t1 : $tetriminos) {
                if (t == t1)
                    continue;
                for (Point p1 : t1.getPoints()) {
                    if (p.y+1 == p1.y && p.x == p1.x)
                        return true;
                }
            }
        }
        return false;
    }

    public void translateCurrent(int val) {
        $current.translate(val, true);
        setChanged();
        notifyObservers(this);
    }

    public void softDropCurrent(int val) {
        down();
        updateScore(val);
        setChanged();
        notifyObservers(this);
    }

    public void hardDropCurrent() {
        int distance = 0;
        while (true) {
            $current.down(1);
            ++distance;
            if (checkCollision($current)) {
                $current.down(-1);
                break;
            }
        }
        down();

        updateScore(2 * distance);
        setChanged();
        notifyObservers(this);
    }

    public void spinCurrent() {
        $current.spin();
        setChanged();
        notifyObservers(this);
    }

    private void removeEmpty() {
        for (int i = 0; i < $tetriminos.size(); ++i) {
            Point[] points = $tetriminos.get(i).getPoints();
            boolean empty = true;
            for (Point p : points) {
                if (p != null) {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                $tetriminos.remove(i);
                --i;
            }
        }
    }

    private void fillField() {
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                $field[i][j] = FILL.EMPTY;
            }
        }
        for (Tetrimino t : $tetriminos) {
            if (t != $current) {
                for (Point p : t.getPoints()) {
                    $field[p.y][p.x] = FILL.FILLED;
                }
            }
        }
    }

    private boolean checkLost() {
        for (Tetrimino t : $tetriminos) {
            if (t == $current)
                continue;
            for (Point p : t.getPoints()) {
                if (p.y == 2 && $upcoming.getClass() != I.class)
                    return true;
                if (p.y == 1)
                    return true;
            }
        }
        return false;
    }

    public void addToCheatCode(Character c) {
        $cheatCode += c;
        checkCheatCode();
    }

    public void clearCheatCode() {
        $cheatCode = "";
    }

    private void checkCheatCode() {
        switch ($cheatCode) {
            case "clear":
                clearField();
                break;
            case "onli":
                $upcoming = new I();
                $onli = !$onli;
                break;
            case "lol":
                displayLol();
                break;
        }
    }

    private void clearField() {
        $tetriminos = new ArrayList<>();
        $tetriminos.add($current);
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                $field[i][j] = FILL.EMPTY;
            }
        }
        $cheatCode = "";
    }

    private void displayLol() {
        clearField();
        L L1 = new L();
        L1.spin();
        L1.spin();
        L1.spin();
        L1.down(17);
        L1.translate(-4, false);

        L O1 = new L();
        O1.spin();
        O1.spin();
        O1.spin();
        O1.down(17);
        O1.translate(-1, false);

        L O2 = new L();
        O2.spin();
        O2.down(17);
        O2.translate(1, false);

        L L2 = new L();
        L2.spin();
        L2.spin();
        L2.spin();
        L2.down(17);
        L2.translate(3, false);

        $tetriminos.add(L1);
        $tetriminos.add(O1);
        $tetriminos.add(O2);
        $tetriminos.add(L2);

        $cheatCode = "";
    }

    private void updateScore(int amount) {
        int temp = (int)($score / SCORE_PER_SPEEDUP);
        $score += amount;
        if ((int)($score / SCORE_PER_SPEEDUP) != temp && $ticksUntilDown > 1)
            $ticksUntilDown -= 1;
        System.out.println($ticksUntilDown);
    }

    public ArrayList<Tetrimino> getTetrominos() {
        return $tetriminos;
    }

    public int getScore() {
        return $score;
    }
}
