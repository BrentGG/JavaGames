import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Ball {
    private Point $position;
    private Point $prevPosition;
    private double $angle;
    private int $speed;
    public static int MAXSPEED = 20;
    public static int BALLSIZE = 8;

    public Ball(Point pos, boolean goLeft) {
        $position = pos;
        $prevPosition = pos;
        Random rng = new Random();
        if (goLeft)
            $angle = Math.PI;
        else
            $angle = 0;
        $speed = (int)(MAXSPEED / 3);
    }

    public void move() {
        $prevPosition = new Point($position);
        $position.x += (int)($speed * Math.cos($angle));
        $position.y += (int)($speed * -Math.sin($angle));
    }

    public void checkPaddleCollision(Paddle leftPaddle, Paddle rightPaddle) {
        if ($position.x >= leftPaddle.getPosition().x - Paddle.PADDLEWIDTH && $position.x <= leftPaddle.getPosition().x + Paddle.PADDLEWIDTH && $position.y >= leftPaddle.getPosition().y - (Paddle.PADDLEHEIGHT/2) && $position.y <= leftPaddle.getPosition().y + (Paddle.PADDLEHEIGHT/2))
            calcNewAngle(leftPaddle.getPosition());
        else if ($position.x >= rightPaddle.getPosition().x - Paddle.PADDLEWIDTH && $position.x <= rightPaddle.getPosition().x + Paddle.PADDLEWIDTH && $position.y >= rightPaddle.getPosition().y - (Paddle.PADDLEHEIGHT/2) && $position.y <= rightPaddle.getPosition().y + (Paddle.PADDLEHEIGHT/2))
            calcNewAngle(rightPaddle.getPosition());
    }

    public void checkWallCollision() {
        if ($position.y < 0 || $position.y >= Pong.HEIGHT)
            $angle *= -1;
    }

    private void calcNewAngle(Point paddlePos) {
        double relativeIntersectY = paddlePos.y - $position.y;
        double normalizedRelativeIntersectionY = (relativeIntersectY/(double)(Paddle.PADDLEHEIGHT/2));
        if (paddlePos.x > Pong.WIDTH/2) {
            $angle = normalizedRelativeIntersectionY * Pong.MAXBOUNCEANGLE + Math.PI;
            if ($position.y > paddlePos.y)
                $angle += Math.PI/2;
            else if ($position.y < paddlePos.y)
                $angle -= Math.PI/2;
        }
        else
            $angle = normalizedRelativeIntersectionY * Pong.MAXBOUNCEANGLE;
        if (normalizedRelativeIntersectionY != 0) {
            $speed = (int)(Math.abs(normalizedRelativeIntersectionY) * $speed + $speed);
            if ($speed > MAXSPEED)
                $speed = MAXSPEED;
        }
    }

    public Point getPosition() {
        return $position;
    }
}
