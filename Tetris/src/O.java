import java.awt.*;

public class O implements Tetrimino {
    private Point[] $points;
    private Point[] $prevPoints;
    private Color $color;

    public O() {
        $color = Color.YELLOW;
        $points = new Point[]{
                new Point(Tetris.COLS/2-1, 0),
                new Point(Tetris.COLS/2, 0),
                new Point(Tetris.COLS/2-1, 1),
                new Point(Tetris.COLS/2, 1)
        };
        $prevPoints = new Point[$points.length];
        Tetrimino.copyPoints($points, $prevPoints);
    }

    @Override
    public void spin() {
        //do nothing
    }

    @Override
    public void translate(int val, boolean checkWalls) {
        Tetrimino.copyPoints($points, $prevPoints);

        for (Point p : $points) {
            if (checkWalls && (p.x + val >= Tetris.COLS || p.x + val < 0))
                return;
        }
        for (Point p : $points)
            p.x += val;
    }

    @Override
    public void down(int val) {
        Tetrimino.copyPoints($points, $prevPoints);

        for (Point p : $points)
            p.y += val;
    }

    @Override
    public Color getColor() {
        return $color;
    }

    @Override
    public Point[] getPoints() {
        return $points;
    }

    @Override
    public Point[] getPrevPoints() {
        return $prevPoints;
    }

    @Override
    public void clearLine(int line) {
        boolean lineCleared = false;
        for (int i = 0; i < $points.length; ++i) {
            if ($points[i].y == line) {
                $points[i] = null;
                lineCleared = true;
            }
        }
        if (lineCleared)
            $points = Tetrimino.reformat($points);
    }

    @Override
    public void downAbove(int val, int line) {
        Tetrimino.copyPoints($points, $prevPoints);

        for (Point p : $points) {
            if (p.y < line)
                p.y += val;
        }
    }

    public void setPoints(Point[] points) {
        $points = points;
    }
}
