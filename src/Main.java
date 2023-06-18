import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends JPanel
{
    public static final int WIDTH = 1920, HEIGHT = 1080;
    public static final int UNIT_X = WIDTH / 20, UNIT_Y = HEIGHT / 20;
    public static final int HALF_X = WIDTH / 2, HALF_Y = HEIGHT / 2;
    public BufferedImage image;
    public Graphics g;
    public HashMap<ComplexFunction, Color> functions;
    public Main()
    {
        super();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g = image.getGraphics();
        functions = new HashMap<>();
        g.setFont(new Font("Arial", Font.BOLD, 20));
        addKeyListener(new KeyboardInput());
        setFocusable(true);
        drawGridlines();
    }

    public void plotPoint(double x, double y)
    {
        int xpos = (int)(x * UNIT_X) + HALF_X, ypos = (int)(-y * UNIT_Y) + HALF_Y;
        g.fillOval(xpos, ypos, 4, 4);
    }
    public void graphFunction(ComplexFunction f, Color c)
    {
        g.setColor(c);
        for(double x = -10; x < 10; x += (1.0/WIDTH))
        {
            double y = f.evaluate(x);

            if(Double.isNaN(y)) continue;

            plotPoint(x, y);
        }
    }

    public void drawData()
    {
        g.setColor(new Color(180, 180, 180, 180));
        g.fillRect(UNIT_X, UNIT_Y, UNIT_X * 4, UNIT_Y * functions.size());

        AtomicInteger current = new AtomicInteger(1);
        functions.forEach((func, col) -> {
            g.setColor(col);
            int x = UNIT_X, y = UNIT_Y * current.get();
            g.fillOval(x + UNIT_X/4, y + UNIT_Y/4, UNIT_X/2, UNIT_Y/2);
            g.setColor(Color.WHITE);
            g.drawString(func.toString(), x + UNIT_X, y + UNIT_Y - UNIT_Y/4);
            graphFunction(func, col);
            current.getAndIncrement();
        });
    }
    public void drawGridlines()
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.DARK_GRAY);
        for(int x = 0; x < WIDTH; x += UNIT_X)
        {
            g.drawLine(x, 0, x, HEIGHT);
        }

        for(int y = 0; y < HEIGHT; y += UNIT_Y)
        {
            g.drawLine(0, y, WIDTH, y);
        }

        /* Draw X Axis */
        drawThickLine(g, 0, HALF_Y, WIDTH, HALF_Y, 4, Color.LIGHT_GRAY);

        /* Draw Y Axis */
        drawThickLine(g, HALF_X, 0, HALF_X, HEIGHT, 4, Color.LIGHT_GRAY);
    }

    private static final Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.PINK, Color.CYAN, Color.YELLOW, Color.ORANGE};
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
            if(e.getKeyChar() != 'n') return;
            try {
                String data = JOptionPane.showInputDialog("Enter a function - f(x)");
                ComplexFunction func = ComplexFunction.parseFunction(data);

                Color col = colors[(int)(Math.random() * colors.length)];
                functions.put(func, col);
                drawGridlines();
                drawData();
                repaint();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "Syntax Error");
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
        frame.setSize(WIDTH, HEIGHT);
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