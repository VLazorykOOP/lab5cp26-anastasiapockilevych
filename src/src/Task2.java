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
        // Налаштування головного вікна
        setTitle("Фрактал: Дерево Архімеда");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Закриває тільки це вікно
        setLocationRelativeTo(null); // Центрувати вікно

        // Панель для малювання
        drawPanel = new DrawPanel();
        drawPanel.setLayout(null); // Абсолютне позиціювання для елементів керування
        add(drawPanel);

        // Ініціалізація елементів керування
        createControls();
    }

    private void createControls() {
        // Слухач змін для перемальовування
        ChangeListener updateListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                drawPanel.repaint();
            }
        };

        // Мітка K
        JLabel lblK = new JLabel("K:");
        lblK.setBounds(10, 15, 20, 20);
        drawPanel.add(lblK);

        // Спінер K (глибина рекурсії)
        numK = new JSpinner(new SpinnerNumberModel(14, 0, 100, 1));
        numK.setBounds(35, 12, 50, 22);
        numK.addChangeListener(updateListener);
        drawPanel.add(numK);

        // Мітка A/B
        JLabel lblAB = new JLabel("A/B:");
        lblAB.setBounds(100, 15, 35, 20);
        drawPanel.add(lblAB);

        // Спінер A
        numA = new JSpinner(new SpinnerNumberModel(67, 1, 500, 1));
        numA.setBounds(140, 12, 60, 22);
        numA.addChangeListener(updateListener);
        drawPanel.add(numA);

        // Спінер B
        numB = new JSpinner(new SpinnerNumberModel(57, 1, 500, 1));
        numB.setBounds(210, 12, 60, 22);
        numB.addChangeListener(updateListener);
        drawPanel.add(numB);
    }

    // Внутрішній клас для малювання
    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Увімкнення згладжування (AntiAlias)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Отримання значень зі спінерів
            int k = (Integer) numK.getValue();
            float a = ((Number) numA.getValue()).floatValue();
            float b = ((Number) numB.getValue()).floatValue();

            // Запуск рекурсивного малювання (початкова точка 400, 580)
            drawTree(g2, 400, 580, a, b, 0, k);
        }

        private void drawTree(Graphics2D g, double x, double y, double a, double b, double angle, int k) {
            if (k <= 0) return;

            double rad = Math.toRadians(angle); // Переведення кута в радіани

            // Розрахунок координат вершин полігону
            // Формули переписані 1-в-1 з C++ коду
            double x2 = x + a * Math.cos(rad);
            double y2 = y - a * Math.sin(rad);

            double x3 = x2 - b * Math.sin(rad);
            double y3 = y2 - b * Math.cos(rad);

            double x4 = x - b * Math.sin(rad);
            double y4 = y - b * Math.cos(rad);

            // Малювання полігону (прямокутника/трапеції)
            Path2D.Double polygon = new Path2D.Double();
            polygon.moveTo(x, y);
            polygon.lineTo(x2, y2);
            polygon.lineTo(x3, y3);
            polygon.lineTo(x4, y4);
            polygon.closePath();

            g.setColor(Color.BLACK);
            g.draw(polygon);

            // Рекурсивні виклики
            // Зверніть увагу: параметри a та b зменшуються на множник 0.7
            drawTree(g, x4, y4, a * 0.7, b * 0.7, angle + 30, k - 1);
            drawTree(g, x3, y3, a * 0.7, b * 0.7, angle - 30, k - 1);
        }
    }

    // Main метод для тестування саме цього вікна окремо
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Task2().setVisible(true);
        });
    }
}