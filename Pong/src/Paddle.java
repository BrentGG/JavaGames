import java.awt.*;

public class Paddle {
    private Point $position;
    private STATE $state;
    public static int PADDLEHEIGHT = 50;
    public static int PADDLEWIDTH = 10;
    public static int PADDLESPEED = 15;
    public enum STATE {
        UP,
        DOWN,
        STILL
    }

    public Paddle(Point pos) {
        $position = pos;
        $state = STATE.STILL;
    }

    public void move() {
        if ($state == STATE.UP) {
            if ($position.y - PADDLESPEED >= 0)
                $position.y -= PADDLESPEED;
        }
        else if ($state == STATE.DOWN) {
            if ($position.y + PADDLESPEED <= Pong.HEIGHT)
                $position.y += PADDLESPEED;
        }
    }

    public Point getPosition() {
        return $position;
    }

    public void changeState(STATE s) {
        $state = s;
    }
}
