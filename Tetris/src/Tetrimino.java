import java.awt.*;
import java.util.ArrayList;

public interface Tetrimino {
    public void spin();
    public void translate(int val, boolean checkWalls);
    public void down(int val);
    public Color getColor();
    public Point[] getPoints();
    public Point[] getPrevPoints();
    public void clearLine(int line);
    public void downAbove(int val, int line);
    public static void copyPoints(Point[] src, Point[] dest) {
        for (int i = 0; i < src.length; ++i) {
            dest[i] = new Point(src[i].x, src[i].y);
        }
    }
    public static Point[] reformat(Point[] points) {
        int size = 4;
        for (Point p : points) {
            if (p == null)
                --size;
        }
        ArrayList<Point> newPoints = new ArrayList<>();
        for (Point p : points) {
            if (p != null)
                newPoints.add(new Point(p.x, p.y));
        }
        points = new Point[newPoints.size()];
        for (int i = 0; i < newPoints.size(); ++i)
            points[i] = new Point(newPoints.get(i).x, newPoints.get(i).y);
        return points;
    }
    public static int checkOutOfBoundsSides(Point[] points) {
        for (Point p : points) {
            if (p.x < 0)
                return 1;
            else if (p.x >= Tetris.COLS)
                return -1;
        }
        return 0;
    }
    public static int checkOutOfBoundsTopBottom(Point[] points) {
        for (Point p : points) {
            if (p.y < 0)
                return 1;
            else if (p.y >= Tetris.ROWS)
                return -1;
        }
        return 0;
    }
}
