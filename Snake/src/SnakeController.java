import java.awt.event.KeyEvent;
import java.util.Observer;

public class SnakeController {
    private SnakeModel $model;

    public SnakeController(SnakeModel model) {
        $model = model;
    }

    public void onKey(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_Z:
                $model.changeDirection(SnakeModel.DIRECTION.NORTH);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                $model.changeDirection(SnakeModel.DIRECTION.EAST);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                $model.changeDirection(SnakeModel.DIRECTION.SOUTH);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_Q:
                $model.changeDirection(SnakeModel.DIRECTION.WEST);
                break;
        }
    }

    public void onStart(int speed, boolean walls, boolean obstacles, boolean ai) {
        $model.start(speed, walls, obstacles, ai);
    }

    public void onStop() {
        $model.lost();
    }

    public void newObserver(Observer o) {
        $model.addObserver(o);
    }
}
