import javax.swing.*;
import java.awt.*;

public class ScreenTwo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("New Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawLine(500, 0, 500, getHeight());
            }
        };
        panel.setLayout(null);

        JButton button = new JButton();
        button.setText("Go show Gordon Ramsey");
        int buttonWidth = 200;
        int buttonHeight = 30;
        int frameWidth = frame.getWidth();
        button.setBounds(frameWidth - buttonWidth - 10, 10, buttonWidth, buttonHeight);

        panel.add(button);
        frame.add(panel);
        frame.setVisible(true);


    }
}