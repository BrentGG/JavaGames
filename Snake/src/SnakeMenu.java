import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

public class SnakeMenu extends JPanel implements Observer {
    private SnakeView $parent;
    private JButton $startButton;
    private JButton $stopButton;
    private JSpinner $speedSpinner;
    private JLabel $score;
    private JCheckBox $wallsCheckbox;
    private JCheckBox $obstaclesCheckbox;
    private JCheckBox $aiCheckbox;

    public SnakeMenu(SnakeView parent) {
        $parent = parent;

        setLayout(new GridLayout(2, 4));

        $startButton = new JButton("Start");
        $startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset(false);
                $parent.startClicked((int)$speedSpinner.getValue(), $wallsCheckbox.isSelected(), $obstaclesCheckbox.isSelected(), $aiCheckbox.isSelected());
            }
        });
        add($startButton);
        $startButton.setFocusable(false);

        $score = new JLabel("Score: 0");
        add($score);
        $score.setHorizontalAlignment(SwingConstants.CENTER);

        add(new JPanel());

        $stopButton = new JButton("Stop");
        $stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset(true);
                $parent.stopClicked();
            }
        });
        add($stopButton);
        $stopButton.setFocusable(false);

        JPanel speed = new JPanel();
        $speedSpinner = new JSpinner();
        SpinnerNumberModel smodel = new SpinnerNumberModel(3, 1, 10, 1);
        $speedSpinner.setModel(smodel);
        speed.add(new JLabel("Speed: "));
        speed.add($speedSpinner);
        add(speed);

        $wallsCheckbox = new JCheckBox("Walls");
        add($wallsCheckbox);
        $wallsCheckbox.setHorizontalAlignment(SwingConstants.CENTER);

        $obstaclesCheckbox = new JCheckBox("Obstacles");
        add($obstaclesCheckbox);
        $obstaclesCheckbox.setHorizontalAlignment(SwingConstants.CENTER);

        $aiCheckbox = new JCheckBox("AI");
        add($aiCheckbox);
        $aiCheckbox.setHorizontalAlignment(SwingConstants.CENTER);

        setFocusable(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        $score.setText("Score: " + ((SnakeModel) o).getScore());
        if (arg.equals("won") || arg.equals("lost")) {
            reset(true);
        }
    }

    private void reset(boolean stopped) {
        $startButton.setEnabled(stopped);
        $stopButton.setEnabled(!stopped);
        $speedSpinner.setEnabled(stopped);
        $wallsCheckbox.setEnabled(stopped);
        $obstaclesCheckbox.setEnabled(stopped);
    }
}
