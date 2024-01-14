import java.awt.event.KeyEvent;

public class PongController {
    private Pong $model;

    public PongController(Pong model) {
        $model = model;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z:
                $model.getLeftPaddle().changeState(Paddle.STATE.UP);
                break;
            case KeyEvent.VK_S:
                $model.getLeftPaddle().changeState(Paddle.STATE.DOWN);
                break;
            case KeyEvent.VK_O:
                $model.getRightPaddle().changeState(Paddle.STATE.UP);
                break;
            case KeyEvent.VK_L:
                $model.getRightPaddle().changeState(Paddle.STATE.DOWN);
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z:
            case KeyEvent.VK_S:
                $model.getLeftPaddle().changeState(Paddle.STATE.STILL);
                break;
            case KeyEvent.VK_O:
            case KeyEvent.VK_L:
                $model.getRightPaddle().changeState(Paddle.STATE.STILL);
                break;
        }
    }

    public void onStart() {
        $model.start();
    }

    public void onStop() {
        $model.stop();
    }

    public void aiLeftClicked(boolean checked) {
        $model.setLeftAi(checked);
    }

    public void aiRightClicked(boolean checked) {
        $model.setRightAi(checked);
    }
}
