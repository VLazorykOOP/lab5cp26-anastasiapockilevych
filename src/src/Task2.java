import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.Path2D;

public class Task2 extends JFrame {

    private JSpinner numK;
    private JSpinner numA;
    private JSpinner numB;
    private DrawPanel drawPanel;

    public Task2() {
        setTitle("Фрактал: Дерево Архімеда");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null); 

        drawPanel = new DrawPanel();
        drawPanel.setLayout(null); 
        add(drawPanel);

        createControls();
    }

    private void createControls() {
        ChangeListener updateListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                drawPanel.repaint();
            }
        };

        JLabel lblK = new JLabel("K:");
        lblK.setBounds(10, 15, 20, 20);
        drawPanel.add(lblK);

        numK = new JSpinner(new SpinnerNumberModel(14, 0, 100, 1));
        numK.setBounds(35, 12, 50, 22);
        numK.addChangeListener(updateListener);
        drawPanel.add(numK);

        JLabel lblAB = new JLabel("A/B:");
        lblAB.setBounds(100, 15, 35, 20);
        drawPanel.add(lblAB);

        numA = new JSpinner(new SpinnerNumberModel(67, 1, 500, 1));
        numA.setBounds(140, 12, 60, 22);
        numA.addChangeListener(updateListener);
        drawPanel.add(numA);

        numB = new JSpinner(new SpinnerNumberModel(57, 1, 500, 1));
        numB.setBounds(210, 12, 60, 22);
        numB.addChangeListener(updateListener);
        drawPanel.add(numB);
    }

    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int k = (Integer) numK.getValue();
            float a = ((Number) numA.getValue()).floatValue();
            float b = ((Number) numB.getValue()).floatValue();

            drawTree(g2, 400, 580, a, b, 0, k);
        }

        private void drawTree(Graphics2D g, double x, double y, double a, double b, double angle, int k) {
            if (k <= 0) return;

            double rad = Math.toRadians(angle);

            double x2 = x + a * Math.cos(rad);
            double y2 = y - a * Math.sin(rad);

            double x3 = x2 - b * Math.sin(rad);
            double y3 = y2 - b * Math.cos(rad);

            double x4 = x - b * Math.sin(rad);
            double y4 = y - b * Math.cos(rad);

            Path2D.Double polygon = new Path2D.Double();
            polygon.moveTo(x, y);
            polygon.lineTo(x2, y2);
            polygon.lineTo(x3, y3);
            polygon.lineTo(x4, y4);
            polygon.closePath();

            g.setColor(Color.BLACK);
            g.draw(polygon);

            drawTree(g, x4, y4, a * 0.7, b * 0.7, angle + 30, k - 1);
            drawTree(g, x3, y3, a * 0.7, b * 0.7, angle - 30, k - 1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Task2().setVisible(true);
        });
    }
}
