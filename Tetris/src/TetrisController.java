import java.awt.event.KeyEvent;

public class TetrisController {
    private Tetris $model;

    public TetrisController(Tetris model) {
        $model = model;
    }

    public void onKey(KeyEvent e) {
        $model.addToCheatCode(e.getKeyChar());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Q:
            case KeyEvent.VK_LEFT:
                $model.translateCurrent(-1);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                $model.translateCurrent(1);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                $model.softDropCurrent(1);
                break;
            case KeyEvent.VK_Z:
            case KeyEvent.VK_UP:
                $model.spinCurrent();
                $model.clearCheatCode();
                break;
            case KeyEvent.VK_SPACE:
                $model.hardDropCurrent();
                break;
        }
    }
}
