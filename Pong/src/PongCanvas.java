import javax.swing.*;
import java.awt.*;

public class PongCanvas extends JPanel {
    private Point $leftPaddlePos;
    private Point $rightPaddlePos;
    private Point $ballPos;
    private int $leftScore;
    private int $rightScore;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Pong.WIDTH, Pong.HEIGHT);

        if ($leftPaddlePos != null && $rightPaddlePos != null && $ballPos != null) {
            g.setColor(Color.WHITE);
            g.fillRect($leftPaddlePos.x - Paddle.PADDLEWIDTH / 2, $leftPaddlePos.y - Paddle.PADDLEHEIGHT / 2, Paddle.PADDLEWIDTH, Paddle.PADDLEHEIGHT);
            g.fillRect($rightPaddlePos.x - Paddle.PADDLEWIDTH / 2, $rightPaddlePos.y - Paddle.PADDLEHEIGHT / 2, Paddle.PADDLEWIDTH, Paddle.PADDLEHEIGHT);
            g.fillOval($ballPos.x - (Ball.BALLSIZE / 2), $ballPos.y - (Ball.BALLSIZE / 2), Ball.BALLSIZE, Ball.BALLSIZE);
        }
        g.fillRect(Pong.WIDTH/2-2, 0, 4, Pong.HEIGHT);
        g.drawString(String.valueOf($leftScore), Pong.WIDTH/2-50, 50);
        g.drawString(String.valueOf($rightScore), Pong.WIDTH/2+40, 50);
    }

    public void update(Paddle leftPaddle, Paddle rightPaddle, Ball ball, int leftScore, int rightScore) {
        repaint(0, 0, Pong.WIDTH, Pong.HEIGHT);
        $leftPaddlePos = leftPaddle.getPosition();
        $rightPaddlePos = rightPaddle.getPosition();
        $ballPos = ball.getPosition();
        $leftScore = leftScore;
        $rightScore = rightScore;
        repaint();
    }
}
