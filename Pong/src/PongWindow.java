import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

public class PongWindow extends JFrame implements Observer {
    private PongController $controller;
    private PongCanvas $canvas;

    public PongWindow(PongController controller) {
        $controller = controller;
        $canvas = new PongCanvas();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        PongMenu $menu = new PongMenu(this);
        getContentPane().add($menu, BorderLayout.NORTH);

        setSize(new Dimension(Pong.WIDTH, Pong.HEIGHT+100));
        setResizable(false);
        setLocation(200, 100);
        setVisible(true);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                $controller.keyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                $controller.keyReleased(e);
            }
        });

        add($canvas);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            Pong p = (Pong)o;
            $canvas.update(p.getLeftPaddle(), p.getRightPaddle(), p.getBall(), p.getLeftScore(), p.getRightScore());
        }
    }

    public void startClicked() {
        requestFocus();
        requestFocusInWindow();
        $controller.onStart();
    }

    public void stopClicked() {
        $controller.onStop();
    }

    public void aiLeftClicked(boolean checked) {
        requestFocus();
        requestFocusInWindow();
        $controller.aiLeftClicked(checked);
    }

    public void aiRightClicked(boolean checked) {
        requestFocus();
        requestFocusInWindow();
        $controller.aiRightClicked(checked);
    }
}
