import java.awt.*;

public class Z implements Tetrimino {
    private Point[] $points;
    private Point[] $relPoints;
    private Point[] $prevPoints;
    private Color $color;

    public Z() {
        $color = Color.RED;
        $points = new Point[]{
                new Point(Tetris.COLS/2-1, 0),
                new Point(Tetris.COLS/2, 0),
                new Point(Tetris.COLS/2, 1),
                new Point(Tetris.COLS/2+1, 1)
        };
        $relPoints = new Point[] {
                new Point(-1, 0),
                new Point(0, 0),
                new Point(0, 1),
                new Point(1, 1)
        };
        $prevPoints = new Point[$points.length];
        Tetrimino.copyPoints($points, $prevPoints);
    }

    @Override
    public void spin() {
        Tetrimino.copyPoints($points, $prevPoints);

        for (Point p : $relPoints) {
            int x = p.y;
            int y = p.x * -1;
            p.x = x;
            p.y = y;
        }

        for (int i = 0; i < 4; ++i) {
            $points[i].x = $points[1].x + $relPoints[i].x;
            $points[i].y = $points[1].y + $relPoints[i].y;
        }

        int val = 0;
        while ((val = Tetrimino.checkOutOfBoundsSides($points)) != 0)
            translate(val, false);
        while ((val = Tetrimino.checkOutOfBoundsTopBottom($points)) != 0)
            down(val);
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
