package mandelbrotSetDrawer;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MandelbrotFractal extends JPanel {

    /**
     * This class in in charge of drawing the Mandelbrot Set on the complex plane, in a JPanel.
     * It allows to have strong or smooth color transitions between some areas and another ones
     * and to zoom 'n' into a certain area in the complex plane.
     *
     * The Mandelbrot Set fractal is only visible when the components of z = a + ib
     *       a E [-2,2]
     *       b E [-2.2]
     *
     * Therefore, it is the default start area to draw the set.
     */

    private MandelbrotIterator mandelbrotIterator;
    private int contrast;

    public MandelbrotFractal(int width, int height, int iterations, int contrast){

        mandelbrotIterator = new MandelbrotIterator(iterations);
        setSize(width, height);
        this.contrast = contrast;

    }

    /**
     * Draws the initial Mandelbrot set
     */
    public void drawFractal(){
        // These values are the ones that display the Mandelbrot Set fully
        drawFractal(-2, 2, 4, 4);
    }

    /**
     * Draws a section of the Mandelbrot Set, indicated by the center and dimensions of a
     * complex sub-plane
     * @param realCenter the value of the real axis of the center coordinates
     * @param imgCenter the value of the imaginary axis of the center coordinates
     * @param zoomFactor the factor that determines how small the complex plane to be shown will be
     */
    public void zoomIn(double realCenter, double imgCenter, double zoomFactor){

        double complexPlaneWidth = 4/zoomFactor;
        double complexPlaneHeight = 4/zoomFactor;

        double minC_real = realCenter - complexPlaneWidth/2;
        double maxC_img = imgCenter + complexPlaneHeight/2;

        drawFractal(minC_real, maxC_img, complexPlaneWidth, complexPlaneHeight);
    }

    /**
     * Draws the Mandelbrot Set, given a top left starting complex number and the dimensions
     * of the complex plane to display
     * @param minC_real the starting number in the real axis
     * @param maxC_img the starting number in the imaginary axis
     * @param complexPlaneWidth the lenght of the real axis of the plane
     * @param complexPlaneHeight the lenght of the imaginary axis of the plane
     */
    private void drawFractal(double minC_real, double maxC_img, double complexPlaneWidth, double complexPlaneHeight){

        Graphics2D g2D = (Graphics2D) this.getGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // We start drawing pixels at the top left corner of the screen. Adjust complex plane values to do the same:
        double c_real = minC_real;
        double c_img = maxC_img;

        // The length in the complex plane that represents a pixel in our JPanel:
        double c_real_Increment = complexPlaneWidth/getSize().width;
        double c_img_Decrement = complexPlaneHeight/getSize().height;

        // Navigating through the screen, finding a complex number to fill each pixel accordingly:
        for (int y = 0; y < getSize().height ; y++) {
            for (int x = 0; x < getSize().width ; x++) {

                /* Find the convergence index of the complex number(how soon it diverges when iterated in the Mandelbrot's function).
                    Display the pixel according to the amount the iterations it takes to show its divergence (or lack thereof).
                */
                int iterations = mandelbrotIterator.iterateComplex(c_real, c_img, 0, 0);
                g2D.setColor(findColor(iterations));
                g2D.drawRect(x, y, 1, 1);

                c_real += c_real_Increment;
            }
            c_real = minC_real; // Restarts at the minimum real value of our drawing area after drawing a pixel row.
            c_img -= c_img_Decrement;
        }
    }

    /**
     * Determines the color of a pixel, given the iterations that its corresponding complex number tolerated under
     * the Mandelbrot function
     * @param iterations
     * @return
     */
    private Color findColor(int iterations){

        int red = 255;
        int green = iterations * contrast;
        int blue = 0;

        // Fades the color from red to blue in the RGB color model
        if(green > 255) {
            int greenOver = green - 255;
            red -= greenOver;
            green = 255;

            if (red < 0) {
                red = 0;
                blue = greenOver - 255;

                if (blue > 255) {
                    int blueOver = blue - 255;
                    green -= blueOver;
                    blue = 255;

                    if (green < 0) {
                        green = 0;
                        blue -= blueOver - 255;

                        if(blue < 0)
                            blue = 0;
                    }
                }
            }
        }

        return new Color(red, green, blue);
    }

    /**
     * Sets the maximum amount of iterations under the Mandelbrot function that a complex number can be submmited to
     * @param iterations the maximum amount of iterations a complex number is iterated through the Mandelbrot function
     */
    public void setIterations(int iterations){
        mandelbrotIterator.setMaxIterations(iterations);
    }

    public void setContrast(int contrast){
        this.contrast = contrast;
    }

}
