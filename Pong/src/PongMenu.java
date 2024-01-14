import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class PongMenu extends JPanel implements Observer {
    private PongWindow $parent;
    private JButton $startButton;
    private JButton $stopButton;
    private JCheckBox $aiLeft;
    private JCheckBox $aiRight;

    public PongMenu(PongWindow parent) {
        $parent = parent;

        setLayout(new GridLayout(2, 4));

        $startButton = new JButton("Start");
        $startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                $parent.startClicked();
            }
        });
        add($startButton);
        $startButton.setFocusable(false);

        add(new JPanel());

        $stopButton = new JButton("Stop");
        $stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                $parent.stopClicked();
            }
        });
        add($stopButton);
        $stopButton.setFocusable(false);

        $aiLeft = new JCheckBox("AI Left");
        $aiLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                $parent.aiLeftClicked($aiLeft.isSelected());
            }
        });
        add($aiLeft);
        $aiLeft.setHorizontalAlignment(SwingConstants.CENTER);

        $aiRight = new JCheckBox("AI Right");
        $aiRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                $parent.aiRightClicked($aiRight.isSelected());
            }
        });
        add($aiRight);
        $aiRight.setHorizontalAlignment(SwingConstants.CENTER);

        setFocusable(false);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
