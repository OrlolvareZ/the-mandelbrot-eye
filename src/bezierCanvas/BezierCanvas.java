package bezierCanvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Path2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * This class will handle a canvas in which we can draw lines and points, to
 * make Bèzier curves with them. The Graphics2D object will handle the drawing,
 * while elements in the geom package are the shapes we want to visualize. We
 * can add these shapes to our window by asking the Graphics2D object to draw a
 * stroke or fill to the shapes we make.
 *
 * @author Orlando Alvarez, Christopher Constancio
 *
 */
public class BezierCanvas extends JPanel {

    private final int width;
    private final int height;
    private BezierCalculator bezierCalc;
    private double drawingSpeed;
    private double precision;

    /**
     * Constructor for the drawing canvas
     *
     * @param width , the width of the canvas.
     * @param height , the height of the canvas.
     * @param drawingSpeed , the speed at which the curve is drawn.
     * @param precision , a value between 0 and 1 that determines how smooth the
     * curve is. The smaller the value, the smoother the curve becomes.
     */
    public BezierCanvas(int width, int height, double drawingSpeed, double precision) {

        if (drawingSpeed < 0.01 || drawingSpeed > 80) {
            throw new IllegalArgumentException("Draw speed must be between 0.01 and 80");
        }

        this.width = width;
        this.height = height;
        bezierCalc = new BezierCalculator();
        /*
            The drawing speed is a factor that modifies the amount of miliseconds
            between drawing one point in the Bèzier curve and another.
            Therefore, the greater the drawing speed is, the smaller the drawing 
            wait time will be.
         */
        this.drawingSpeed = drawingSpeed;
        this.precision = precision;
    }

    /**
     * Called by the JFrame that needs to draw the component.
     *
     * @param g , the Graphics object to handle the drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        
        /**
         * We want for the user to click on the pane to set up points for the
         * Bèzier curve, so we want to establish dimensions to give a better
         * sense of space.
         */
        /* We draw a grid. Uses same space between one line and another 
        * (based on the given heigth) to make each space of the grid square.
        * This space is represented by the "i" control variable on the next loop.
        
          Coordinates start from top left corner, as opposed from the traditional
          bottom left corner cartesian plane. This will require for us to do some
          adjustments in some places
         */
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.LIGHT_GRAY);

        // Determines if the width is greater than the height, or viceversa (the greatest fo both dimensuons)
        float greatestDim = Math.max(width, height);

        for (float i = height * 0.05f; i <= greatestDim; i += height * 0.05f) {

            if (height <= greatestDim) {
                Line2D.Float gridX = new Line2D.Float(0, i, width, i);
                g2D.draw(gridX);
            }
            if (width <= greatestDim) {
                Line2D.Float gridY = new Line2D.Float(i, 0, i, height);
                g2D.draw(gridY);
            }
        }

        // Draws horizontal and vertical axes to simulate a cartesian plane.
        Line2D.Float xAxis = new Line2D.Float(0, height * 0.9f, width, height * 0.9f);
        Line2D.Float yAxis = new Line2D.Float(height * 0.05f, 0, height * 0.05f, height);

        g2D.setColor(Color.black);
        // Draws horizontal axis at 5% of the canva's height, from bottom to top
        g2D.draw(xAxis);
        // Draws vertical axis at 5% of the canva's width, from left to right
        g2D.draw(yAxis);

    }

    /**
     * Draws the Bèzier curve given a set of control points.
     *
     * @param points , the control points to draw the curve.
     * @param g , the Graphics component to draw the curve
     * @throws java.lang.InterruptedException
     */
    public void createBezierCurve(Point2D[] points, Graphics g) throws InterruptedException {

        Graphics2D g2D = (Graphics2D) g;
        // Antialising will render a smoother curve
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHints(rh);

        // Starts drawing initial points
        Path2D initialPoints = new Path2D.Float();
        initialPoints.moveTo(points[0].getX(), points[0].getY());

        for (int i = 1; i < points.length; i++) {
            initialPoints.lineTo(points[i].getX(), points[i].getY());
        }

        g2D.setColor(Color.MAGENTA);
        g2D.draw(initialPoints);

        // Draws Bèzier curve (and the segments that help finding it)
        Path2D bezierCurve = new Path2D.Float();
        Path2D[] auxSegments = new Path2D[points.length - 2];
        Stroke bezierStroke = new BasicStroke(4);
        Stroke auxStroke = new BasicStroke(1);

        for (double t = 0; t <= 1; t += precision) {

            Point2D bezierPoint = bezierCalc.findBezierPoint(points, t);
            // Auxiliar points
            Point2D[][] intrpltdPoints = bezierCalc.getIntrpltdPoints();

            g2D.setColor(Color.gray);
            g2D.setStroke(auxStroke);

            // Draws auxiliary points
            for (int i = 0; i < intrpltdPoints.length; i++) {

                // Changes the color for each segments to be visualized better.
                switch (i) {
                    case 0:
                        g2D.setColor(Color.green);
                        break;
                    case 1:
                        g2D.setColor(Color.blue);
                        break;
                    case 2:
                        g2D.setColor(Color.ORANGE);
                        break;
                    case 3:
                        g2D.setColor(Color.magenta);
                        break;
                }

                // All of the segments resulting of an interpolation operation
                // will be drawin in a Path2D object
                auxSegments[i] = new Path2D.Float();
                Point2D firstPoint = intrpltdPoints[i][0];

                // Sets the first point of the path
                auxSegments[i].moveTo(firstPoint.getX(), firstPoint.getY());

                // Draws subscuent points 
                for (int j = 1; j < intrpltdPoints[i].length; j++) {
                    Point2D point = intrpltdPoints[i][j];
                    auxSegments[i].lineTo(point.getX(), point.getY());
                }

                // Draws auxiliary segments
                g2D.draw(auxSegments[i]);

            } // Finishes drawing auxiliary segments

            // Draws Bèzier curve
            g2D.setColor(Color.red);

            if (t == 0) {
                bezierCurve.moveTo(bezierPoint.getX(), bezierPoint.getY());
            } else {
                bezierCurve.lineTo(bezierPoint.getX(), bezierPoint.getY());
            }

            g2D.setStroke(bezierStroke);
            g2D.draw(bezierCurve);

            // A pause between each point to see the process.
            Thread.sleep((int) (80 / drawingSpeed));
        }

    }

    public void clear(Graphics g) {

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(this.getBackground());

        Rectangle2D bg = new Rectangle2D.Float(0, 0, width, height);
        g2D.fill(bg);

        paintComponent(g);
    }

    public void drawControlPoint(Point2D point, Graphics g, Color c, float radius) {

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(c);
        Ellipse2D e = new Ellipse2D.Float((float) point.getX() - radius, (float) point.getY() - radius, radius * 2, radius * 2);
        g2D.fill(e);
    }

    public void setDrawingSpeed(float drawingSpeed) {
        this.drawingSpeed = drawingSpeed;
    }

    public void setPrecision(float precision) {
        this.precision = precision;
    }
    
}
