import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;

public class Task1 extends JFrame {

    private JSpinner[] numX;
    private JSpinner[] numY;
    private DrawPanel drawPanel;

    public Task1() {
        setTitle("Крива Без'є - Задача 1");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрувати вікно

        JMenuBar menuStrip = new JMenuBar();
        JMenu menuTask1 = new JMenu("Завдання №1");
        JMenu menuTask2 = new JMenu("Завдання №2");

        JMenuItem itemTask2 = new JMenuItem("Відкрити");
        itemTask2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Відкриття другого вікна
                new Task2().setVisible(true);
            }
        });
        menuTask2.add(itemTask2);

        menuStrip.add(menuTask1);
        menuStrip.add(menuTask2);
        setJMenuBar(menuStrip);

        drawPanel = new DrawPanel();
        drawPanel.setLayout(null);
        add(drawPanel);

        createNumericInputs();
    }

    private void createNumericInputs() {
        numX = new JSpinner[4];
        numY = new JSpinner[4];
        int[] startX = {150, 250, 500, 600};
        int[] startY = {500, 200, 200, 500};

        ChangeListener updateListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                drawPanel.repaint();
            }
        };

        for (int i = 0; i < 4; i++) {
            numX[i] = new JSpinner(new SpinnerNumberModel(startX[i], -1000, 2000, 1));
            numX[i].setBounds(15 + (i * 140), 40, 60, 22);
            numX[i].addChangeListener(updateListener);
            drawPanel.add(numX[i]);

            numY[i] = new JSpinner(new SpinnerNumberModel(startY[i], -1000, 2000, 1));
            numY[i].setBounds(80 + (i * 140), 40, 60, 22);
            numY[i].addChangeListener(updateListener);
            drawPanel.add(numY[i]);
        }
    }

    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Point.Double[] p = new Point.Double[4];
            for (int i = 0; i < 4; i++) {
                double x = ((Number) numX[i].getValue()).doubleValue();
                double y = ((Number) numY[i].getValue()).doubleValue();
                p[i] = new Point.Double(x, y);
            }

            Stroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);
            g2.setStroke(dashed);
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(new Line2D.Double(p[0], p[1]));
            g2.draw(new Line2D.Double(p[1], p[2]));
            g2.draw(new Line2D.Double(p[2], p[3]));

            g2.setStroke(new BasicStroke(2.5f));
            g2.setColor(Color.RED);
            Point.Double prev = p[0];

            for (double t = 0.0; t <= 1.001; t += 0.01) {
                double mt = 1.0 - t;
                // Формула кривої Без'є 3-го порядку
                double x = Math.pow(mt, 3) * p[0].x +
                        3 * t * Math.pow(mt, 2) * p[1].x +
                        3 * t * t * mt * p[2].x +
                        Math.pow(t, 3) * p[3].x;

                double y = Math.pow(mt, 3) * p[0].y +
                        3 * t * Math.pow(mt, 2) * p[1].y +
                        3 * t * t * mt * p[2].y +
                        Math.pow(t, 3) * p[3].y;

                Point.Double current = new Point.Double(x, y);
                g2.draw(new Line2D.Double(prev, current));
                prev = current;
            }

            g2.setStroke(new BasicStroke(1.0f));
            for (int i = 0; i < 4; i++) {
                g2.setColor(Color.BLUE);
                g2.fillOval((int) p[i].x - 4, (int) p[i].y - 4, 8, 8);

                g2.setColor(Color.BLACK);
                g2.drawString("P" + i, (float) p[i].x + 5, (float) p[i].y + 5);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Task1().setVisible(true);
        });
    }
}