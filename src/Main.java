import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends JPanel
{
    public static final int SCREEN_WIDTH = 2560, OFFSET_X = 560;
    public static final int WIDTH = SCREEN_WIDTH - OFFSET_X, HEIGHT = 1440;
    public static double UNIT_X = WIDTH / 20.0, UNIT_Y = HEIGHT / 20.0;
    public static final double CONST_UNIT_X = UNIT_X, CONST_UNIT_Y = UNIT_Y;
    public static final int HALF_X = (WIDTH / 2) + OFFSET_X, HALF_Y = (HEIGHT / 2);
    public BufferedImage image;
    public Graphics g;
    public ArrayList<ComplexFunction> functions;
    public Main()
    {
        super();
        image = new BufferedImage(SCREEN_WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g = image.getGraphics();
        functions = new ArrayList<>();
        g.setFont(new Font("Segoe Script", Font.BOLD, 24));
        addKeyListener(new KeyboardInput());
        setFocusable(true);
        drawGridlines();
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
    }
    public void drawGridlines()
    {
        g.setColor(Color.WHITE);
        g.fillRect(OFFSET_X, 0, SCREEN_WIDTH, HEIGHT);

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, OFFSET_X, HEIGHT);

        for(int x = OFFSET_X; x < SCREEN_WIDTH; x += UNIT_X)
        {
            drawThickLine(g, x - 1, 0, x - 1, HEIGHT, 2, Color.LIGHT_GRAY);
        }

        for(int y = 0; y < HEIGHT; y += UNIT_Y)
        {
            drawThickLine(g, OFFSET_X, y - 1, SCREEN_WIDTH, y - 1, 2, Color.LIGHT_GRAY);
        }

        /* Draw X Axis */
        drawThickLine(g, OFFSET_X, HALF_Y + 1, SCREEN_WIDTH, HALF_Y + 1, 6, Color.BLACK);

        /* Draw Y Axis */
        drawThickLine(g, HALF_X - 3, 0, HALF_X - 3, HEIGHT, 6, Color.BLACK);

        /* Draw Separator */
        drawThickLine(g, OFFSET_X - 2, 0, OFFSET_X - 2, HEIGHT, 4, Color.DARK_GRAY);
    }
    private class KeyboardInput implements KeyListener
    {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e)
        {
        }

        @Override
        public void keyReleased(KeyEvent e)
        {

            if(e.getKeyChar() == 'r')
            {
                functions.clear();
                UNIT_X = CONST_UNIT_X;
                UNIT_Y = CONST_UNIT_Y;
                drawGridlines();
                repaint();
            }

            if(e.getKeyChar() == '+')
            {
                if(UNIT_X >= WIDTH / 8.0) return;
                UNIT_X *= 2.0;
                UNIT_Y *= 2.0;
                drawGridlines();
                drawData();
                repaint();
                return;
            }

            if(e.getKeyChar() == '-') {
                if(UNIT_X <= WIDTH / 80.0) return;
                UNIT_X /= 2.0;
                UNIT_Y /= 2.0;
                drawGridlines();
                drawData();
                repaint();
                return;
            }

            if(e.getKeyChar() == 'e')
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

            if(e.getKeyChar() != 'n') return;

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
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
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

}