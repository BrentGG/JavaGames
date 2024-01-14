import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Snake {
    private SnakeModel $model;
    private SnakeView $view;
    public static int ROWS = 20;
    public static int COLS = 20;

    public Snake() {
        $model = new SnakeModel();
        $view = new SnakeView(new SnakeController($model));
        $view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        $model.addObserver($view);
        //$model.start();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Snake snake = new Snake();
            }
        });
    }
}
