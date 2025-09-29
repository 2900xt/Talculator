import Regression.ExponentialModel;
import Regression.LinearModel;
import Regression.LogarithmicModel;
import Regression.PolynomialModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

public class Main extends JPanel
{
    public static final int SCREEN_WIDTH = 1280, OFFSET_X = 300;
    public static final int WIDTH = SCREEN_WIDTH - OFFSET_X, HEIGHT = 720;
    public static double UNIT_X = WIDTH / 20.0, UNIT_Y = HEIGHT / 20.0;
    public static final double CONST_UNIT_X = UNIT_X, CONST_UNIT_Y = UNIT_Y;
    public static final int HALF_X = (WIDTH / 2) + OFFSET_X, HALF_Y = (HEIGHT / 2);
    public static final ImageIcon addIcon = new ImageIcon("src/Resources/plus.png");
    public static final ImageIcon evaluateIcon = new ImageIcon("src/Resources/evaluate.png");
    public static final ImageIcon regressionIcon = new ImageIcon("src/Resources/regression.png");
    public static final ImageIcon resetIcon = new ImageIcon("src/Resources/reset.png");
    public BufferedImage image;
    public Graphics g;
    public ArrayList<ComplexFunction> functions;
    RegressionUI lr;
    public Main()
    {
        image = new BufferedImage(SCREEN_WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g = image.getGraphics();
        g.setFont(new Font("Segoe Script", Font.BOLD, 24));
        functions = new ArrayList<>();
        addKeyListener(new KeyboardInput());
        addMouseListener(new MouseInput());
        setFocusable(true);

        lr = new RegressionUI(this::evaluateRegression);
        lr.setVisible(false);

        drawData();
    }
    public void evaluateRegression(HashMap<Double, Double> data)
    {
        LinearModel model;
        switch((String) Objects.requireNonNull(lr.regressionTypeComboBox.getSelectedItem()))
        {
            case "Linear" -> model = new LinearModel();
            case "Polynomial" -> model = new PolynomialModel(Double.parseDouble(lr.degreeField.getText()));
            case "Exponential" -> model = new ExponentialModel();
            case "Logarithmic" -> model = new LogarithmicModel();
            default -> {
                return;
            }
        }

        model.trainTo(data, 1000);
        ComplexFunction r = ComplexFunction.parseFunction(model.toEquation(), true);
        functions.add(r);
        drawGridlines();
        drawData();
        repaint();
    }
    public void plotPoint(double x, double y)
    {
        int xpos = (int)(x * UNIT_X) + HALF_X, ypos = (int)(-y * UNIT_Y) + HALF_Y;
        g.fillOval(xpos, ypos, 4, 4);
    }
    public void graphFunction(ComplexFunction f)
    {
        g.setColor(f.color);

        double minX = -(WIDTH / (double)UNIT_X) / 2.0;

        for(double x = minX; x < -minX; x += (1.0/(WIDTH)))
        {
            double y = f.evaluate(x);

            if(Double.isNaN(y)) continue;

            plotPoint(x, y);
        }
    }
    public void drawData()
    {
        drawGridlines();
        functions.forEach(this::graphFunction);

        AtomicInteger current = new AtomicInteger(0);
        functions.forEach((func) -> {
            int x = 0, y = (int) (CONST_UNIT_Y * current.get());

            g.setColor(func.color);
            g.fillOval((int) (x + CONST_UNIT_X/4), (int) (y + CONST_UNIT_Y/4), (int) (CONST_UNIT_X/2), (int) (CONST_UNIT_Y/2));
            g.setColor(Color.BLACK);
            g.drawOval((int) (x + CONST_UNIT_X/4), (int) (y + CONST_UNIT_Y/4), (int) (CONST_UNIT_X/2), (int) (CONST_UNIT_Y/2));
            g.drawString( func.toString(), (int) (x + CONST_UNIT_X), (int) (y + CONST_UNIT_Y - CONST_UNIT_Y/3));
            g.setColor(Color.DARK_GRAY);
            drawThickLine(g, 0, (int)(y + CONST_UNIT_Y), OFFSET_X, (int)(y + CONST_UNIT_Y), 4, Color.BLACK);
            current.getAndIncrement();
        });
        drawButtons();
        repaint();
    }
    public void drawGridlines()
    {
        g.setColor(Color.WHITE);
        g.fillRect(OFFSET_X, 0, SCREEN_WIDTH, HEIGHT);

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, OFFSET_X, HEIGHT);

        for(int x = OFFSET_X; x < SCREEN_WIDTH; x += UNIT_X)
        {
            drawThickLine(g, x , 0, x, HEIGHT, 2, Color.LIGHT_GRAY);
        }

        for(int y = 0; y < HEIGHT; y += UNIT_Y)
        {
            drawThickLine(g, OFFSET_X, y, SCREEN_WIDTH, y, 2, Color.LIGHT_GRAY);
        }

        /* Draw X Axis */
        drawThickLine(g, OFFSET_X, HALF_Y, SCREEN_WIDTH, HALF_Y, 6, Color.BLACK);

        /* Draw Y Axis */
        drawThickLine(g, HALF_X, 0, HALF_X, HEIGHT, 6, Color.BLACK);

        /* Draw Separator */
        drawThickLine(g, OFFSET_X, 0, OFFSET_X, HEIGHT, 4, Color.DARK_GRAY);
    }
    public void drawButtons()
    {
        g.setColor(Color.WHITE);
        g.fillOval(NF_BOX_X - 8, NF_BOX_Y - 8, NF_BOX_W + 16, NF_BOX_H + 16);
        g.drawImage(addIcon.getImage(), NF_BOX_X, NF_BOX_Y, NF_BOX_W, NF_BOX_H, null);
        g.fillOval(RG_BOX_X - 8, RG_BOX_Y - 8, RG_BOX_W + 16, RG_BOX_H + 16);
        g.drawImage(regressionIcon.getImage(), RG_BOX_X, RG_BOX_Y, RG_BOX_W, RG_BOX_H, null);
        g.fillOval(EV_BOX_X - 8, EV_BOX_Y - 8, EV_BOX_W + 16, EV_BOX_H + 16);
        g.drawImage(evaluateIcon.getImage(), EV_BOX_X, EV_BOX_Y, EV_BOX_W, EV_BOX_H, null);
        g.fillOval(CR_BOX_X - 8, CR_BOX_Y - 8, CR_BOX_W + 16, CR_BOX_H + 16);
        g.drawImage(resetIcon.getImage(), CR_BOX_X, CR_BOX_Y, CR_BOX_W, CR_BOX_H, null);
    }
    public void clearFunctions()
    {
        functions.clear();
        UNIT_X = CONST_UNIT_X;
        UNIT_Y = CONST_UNIT_Y;
        drawData();
    }
    public void zoom(double factor)
    {
        if((UNIT_X * factor > CONST_UNIT_X * 4) || (UNIT_X * factor < CONST_UNIT_X / 4.0))
        {
            return;
        }

        UNIT_X *= factor;
        UNIT_Y *= factor;
        drawData();
    }
    public void evaluateAt()
    {
        ArrayList<String> functionOptions = new ArrayList<>();
        functions.forEach(f -> functionOptions.add(f.name));
        String func = (String) JOptionPane.showInputDialog(null, "Choose a function", "Evaluate function", JOptionPane.QUESTION_MESSAGE, null, functionOptions.toArray(), functionOptions.get(0));
        if(func == null)
        {
            return;
        }

        ComplexFunction function = null;
        for(ComplexFunction f : functions)
        {
            if(f.name.equals(func))
            {
                function = f;
            }
        }

        if(function == null)
        {
            return;
        }

        String numIn = (String) JOptionPane.showInputDialog(null, "Enter an x value", "Evaluate Function - " + func + "(x)", JOptionPane.PLAIN_MESSAGE);
        if(numIn == null)
        {
            return;
        }

        double x = Double.parseDouble(numIn);
        JOptionPane.showMessageDialog(null, function.toString().replace("x", numIn) + " = " + function.evaluate(x), "Evaluate Function - " + func + "(x)", JOptionPane.PLAIN_MESSAGE);
    }
    public void createFunction()
    {
        ComplexFunction func = null;

        try {
            String data = JOptionPane.showInputDialog("Enter a function of x");
            if(data == null)
            {
                return;
            }
            func = ComplexFunction.parseFunction(data, true);
            functions.add(func);
            drawGridlines();
            drawData();
            repaint();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Syntax Error, use functional notation - 'f(x) = x^2' ");
            if(func != null){
                functions.remove(func);
            }
            exception.printStackTrace();
        }
    }
    public boolean isInRect(int x, int y, int width, int height, int mx, int my)
    {
        return     mx >= x
                && mx <= x + width
                && my >= y
                && my <= y + height;
    }
    private class KeyboardInput implements KeyListener
    {
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e)
        {
            if(e.getKeyCode() == KeyEvent.VK_R && e.isControlDown())
            {
                clearFunctions();
                return;
            }

            if(e.getKeyCode() == KeyEvent.VK_EQUALS && e.isControlDown())
            {
                zoom(2.0);
                return;
            }

            if(e.getKeyCode() == KeyEvent.VK_MINUS && e.isControlDown()) {
                zoom(0.5);
                return;
            }

            if(e.getKeyCode() == KeyEvent.VK_E && e.isControlDown())
            {
                evaluateAt();
            }

            if(e.getKeyCode() == KeyEvent.VK_R && e.isAltDown())
            {
                SwingUtilities.invokeLater(() -> lr.setVisible(true));
            }

            if(e.getKeyCode() == KeyEvent.VK_F1)
            {
                createFunction();
            }
        }
    }
    private static final int BUTTON_Y = HEIGHT - 60;
    private static final int BUTTON_SPACING = 60;
    private static final int BUTTON_START_X = (OFFSET_X - (4 * 40 + 3 * 20)) / 2;
    private static final int NF_BOX_X = BUTTON_START_X, NF_BOX_Y = BUTTON_Y, NF_BOX_W = 40, NF_BOX_H = 40;
    private static final int EV_BOX_X = BUTTON_START_X + BUTTON_SPACING, EV_BOX_Y = BUTTON_Y, EV_BOX_W = 40, EV_BOX_H = 40;
    private static final int RG_BOX_X = BUTTON_START_X + 2 * BUTTON_SPACING, RG_BOX_Y = BUTTON_Y, RG_BOX_W = 40, RG_BOX_H = 40;
    private static final int CR_BOX_X = BUTTON_START_X + 3 * BUTTON_SPACING, CR_BOX_Y = BUTTON_Y, CR_BOX_W = 40, CR_BOX_H = 40;
    private class MouseInput implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX(), y = e.getY();
            System.out.println(x + ", " + y);
            System.out.println(NF_BOX_X + ", " + NF_BOX_Y + ", " + NF_BOX_W + ", " + NF_BOX_H);
            if(isInRect(NF_BOX_X, NF_BOX_Y, NF_BOX_W, NF_BOX_H, x, y))
            {
                createFunction();
                return;
            }

            if(isInRect(RG_BOX_X, RG_BOX_Y, RG_BOX_W, RG_BOX_H, x, y))
            {
                SwingUtilities.invokeLater(() -> lr.setVisible(true));
                return;
            }

            if(isInRect(EV_BOX_X, EV_BOX_Y, EV_BOX_W, EV_BOX_H, x, y) && !functions.isEmpty())
            {
                evaluateAt();
                return;
            }

            if(isInRect(CR_BOX_X, CR_BOX_Y, CR_BOX_W, CR_BOX_H, x, y))
            {
                clearFunctions();
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e)
        {
            int x = e.getX(), y = e.getY();
            if(isInRect(NF_BOX_X, NF_BOX_Y, NF_BOX_W, NF_BOX_H, x, y))
            {
                createFunction();
                return;
            }

            if(isInRect(RG_BOX_X, RG_BOX_Y, RG_BOX_W, RG_BOX_H, x, y))
            {
                SwingUtilities.invokeLater(() -> lr.setVisible(true));
                return;
            }

            if(isInRect(EV_BOX_X, EV_BOX_Y, EV_BOX_W, EV_BOX_H, x, y) && !functions.isEmpty())
            {
                evaluateAt();
                return;
            }

            if(isInRect(CR_BOX_X, CR_BOX_Y, CR_BOX_W, CR_BOX_H, x, y))
            {
                clearFunctions();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
    public static void drawThickLine(Graphics g, int x1, int y1, int x2, int y2, int thickness, Color c) {

        // credit for the code: https://www.rgagnon.com/javadetails/java-0260.html
        // The thick line is in fact a filled polygon
        g.setColor(c);
        int dX = x2 - x1;
        int dY = y2 - y1;
        // line length
        double lineLength = Math.sqrt(dX * dX + dY * dY);

        double scale = (double)(thickness) / (2 * lineLength);

        // The x,y increments from an endpoint needed to create a rectangle...
        double ddx = -scale * (double)dY;
        double ddy = scale * (double)dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        int dx = (int)ddx;
        int dy = (int)ddy;

        // Now we can compute the corner points...
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
        xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

        g.fillPolygon(xPoints, yPoints, 4);
    }
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Taha's Graphing Calculator");
        frame.setSize(SCREEN_WIDTH, HEIGHT);
        frame.setContentPane(new Main());
        frame.setVisible(true);
    }
}