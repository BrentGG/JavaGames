import java.awt.*;
import java.util.*;

public class Pong extends Observable {
    private Timer $timer;
    public static int WIDTH = 400;
    public static int HEIGHT = 300;
    public static double MAXBOUNCEANGLE = Math.PI/3; // 5*Math.PI/12
    private Ball $ball;
    private Paddle $leftPaddle;
    private Paddle $rightPaddle;
    private int $leftScore = 0;
    private int $rightScore = 0;
    private boolean $leftScoredLast = false;
    private boolean $leftAi = false;
    private boolean $rightAi = false;

    public Pong() {
        resetBallAndPaddles();
        //start();
    }

    private void resetBallAndPaddles() {
        $ball = new Ball(new Point(WIDTH/2, HEIGHT/2), !$leftScoredLast);
        $leftPaddle = new Paddle(new Point(25, HEIGHT/2));
        $rightPaddle = new Paddle(new Point(WIDTH-50+Paddle.PADDLEWIDTH, HEIGHT/2));
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Pong p = new Pong();
                PongWindow w = new PongWindow(new PongController(p));
                p.addObserver(w);
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
        $leftScoredLast = false;
        resetBallAndPaddles();
        $leftScore = 0;
        $rightScore = 0;
    }

    public void tick() {
        doAiMove();
        $ball.move();
        $leftPaddle.move();
        $rightPaddle.move();
        $ball.checkPaddleCollision($leftPaddle, $rightPaddle);
        $ball.checkWallCollision();
        checkBallOutOfBounds($ball);
        setChanged();
        notifyObservers();
    }

    public Paddle getLeftPaddle() {
        return $leftPaddle;
    }

    public Paddle getRightPaddle() {
        return $rightPaddle;
    }

    public Ball getBall() {
        return $ball;
    }

    public int getLeftScore() {
        return $leftScore;
    }

    public int getRightScore() {
        return $rightScore;
    }

    private void checkBallOutOfBounds(Ball ball) {
        Point pos = ball.getPosition();
        boolean scored = false;
        if (pos.x < 0) {
            ++$rightScore;
            scored = true;
            $leftScoredLast = false;
        }
        else if (pos.x > WIDTH) {
            ++$leftScore;
            scored = true;
            $leftScoredLast = true;
        }
        if (scored) {
            resetBallAndPaddles();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void doAiMove() {
        if ($leftAi) {
            if ($ball.getPosition().y == $leftPaddle.getPosition().y)
                $leftPaddle.changeState(Paddle.STATE.STILL);
            else if ($ball.getPosition().y < $leftPaddle.getPosition().y)
                $leftPaddle.changeState(Paddle.STATE.UP);
            else
                $leftPaddle.changeState(Paddle.STATE.DOWN);
        }
        if ($rightAi) {
            if ($ball.getPosition().y == $rightPaddle.getPosition().y)
                $rightPaddle.changeState(Paddle.STATE.STILL);
            else if ($ball.getPosition().y < $rightPaddle.getPosition().y)
                $rightPaddle.changeState(Paddle.STATE.UP);
            else
                $rightPaddle.changeState(Paddle.STATE.DOWN);
        }
    }

    public void setLeftAi(boolean active) {
        $leftAi = active;
    }

    public void setRightAi(boolean active) {
        $rightAi = active;
    }
}
