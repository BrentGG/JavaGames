import java.awt.*;
import java.awt.event.KeyEvent;

public class SudokuController {
    private Sudoku $model;

    public SudokuController(Sudoku model) {
        $model = model;
    }

    public void onNew() {
        $model.newGame();
    }

    public void onReset() {
        $model.resetGame();
    }

    //keylisteners no working (this method is fine though)
    public void onKey(KeyEvent key, Point selected) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_4:
            case KeyEvent.VK_5:
            case KeyEvent.VK_6:
            case KeyEvent.VK_7:
            case KeyEvent.VK_8:
            case KeyEvent.VK_9:
                $model.setNumber(selected.x, selected.y, Integer.parseInt(String.valueOf(key.getKeyChar())));
                break;
            case KeyEvent.VK_BACK_SPACE:
            case KeyEvent.VK_DELETE:
                $model.setNumber(selected.x, selected.y, 0);
                break;
        }
    }

    public void onInput(Point pos, int number) {
        $model.setNumber(pos.x, pos.y, number);
    }

    public void onDelete(Point pos) {
        $model.setNumber(pos.x, pos.y, 0);
    }

    public void onCheck() {
        $model.checkSolution();
    }
}
