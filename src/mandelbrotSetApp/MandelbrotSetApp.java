package mandelbrotSetApp;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import mandelbrotSetDrawer.MandelbrotFractal;

public class MandelbrotSetApp {

    /**
     * This class puts a GUI on screen to navigate through the Mandelbrot Set fractal
     */

    public static void main(String[] args) throws InterruptedException {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setTitle("Mandelbrot Fractal");
        frame.setResizable(false);
        frame.setBounds(0,0,600,700);
        frame.setLayout(null);

        // List for specifying drawing contrast:
        JComboBox<String> contrastOptions = new JComboBox<String>();

        contrastOptions.addItem("Hi");
        contrastOptions.addItem("Med");
        contrastOptions.addItem("Low");
        contrastOptions.setSelectedIndex(1); // Since medium contrast is default

        /* Spinners to determine the point in the complex plane to zoom in */
        double cIncrease = 0.01;

        // Spinner for point in real numbers' axis
        JSpinner c_realSpinner = new JSpinner(new SpinnerNumberModel(0, -2, 2, cIncrease));
        c_realSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double value = Double.parseDouble(c_realSpinner.getValue().toString());

                if(value > 2)
                    c_realSpinner.setValue(new Double(2));
                else if(value < -2)
                    c_realSpinner.setValue(new Double(-2));
            }
        });

        // Spinner for point in imaginary numbers' axis spinner
        JSpinner c_imgSpinner = new JSpinner(new SpinnerNumberModel(0, -2, 2, cIncrease));
        c_imgSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double value = Double.parseDouble(c_imgSpinner.getValue().toString());

                if(value > 2)
                    c_imgSpinner.setValue(new Double(2));
                else if(value < -2)
                    c_imgSpinner.setValue(new Double(-2));
            }
        });

        // Spinner for zooming factor
        double zoomLowerLim = 2;
        double zoomUpperLim = 10000;
        double zoomHop = 4;

        JSpinner zoomSpinner = new JSpinner(new SpinnerNumberModel(zoomLowerLim, zoomLowerLim, zoomUpperLim, zoomHop));
        zoomSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double value = Double.parseDouble(zoomSpinner.getValue().toString());

                if(value > zoomUpperLim)
                    zoomSpinner.setValue(new Double(zoomUpperLim));
                else if(value < zoomLowerLim)
                    zoomSpinner.setValue(new Double(zoomLowerLim));
            }
        });

        /* Creating the panel where the Mandelbrot Fractal will be drawn */
        int width = 600;
        int height = 600;
        int iterations = 128;

        MandelbrotFractal mandelbrotFractal = new MandelbrotFractal(width, height, iterations, 25);

        /* Labels for UI elements: */
        JLabel zoomC_realLabel = new JLabel("Real axis: ");
        JLabel zoomC_imgLabel = new JLabel("Img. axis:");
        JLabel zoomFactorLabel = new JLabel("Zoom: ");
        JLabel contrastLabel = new JLabel("Contrast: ");

        // Begins the zoom in the given point:
        JButton zoomButton = new JButton("Zoom");
        zoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double c_real = Double.parseDouble(c_realSpinner.getValue().toString());
                double c_img = Double.parseDouble(c_imgSpinner.getValue().toString());
                double zoomFactor = Double.parseDouble(zoomSpinner.getValue().toString());

                mandelbrotFractal.zoomIn(c_real, c_img, zoomFactor);
            }
        });

        // Puts the initial Mandelbrot Set scene back:
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mandelbrotFractal.drawFractal();
            }
        });

        // Allows for the drawing contrast to be updated:
        contrastOptions.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {

                int contrast;
                int option = contrastOptions.getSelectedIndex();
                switch(option){
                    case 0: // High contrast
                        contrast = 50;
                        break;
                    case 1: // Medium contrast
                        contrast = 25;
                        break;
                    case 2: // Low contrast
                        contrast = 10;
                        break;
                    default:
                        contrast = 25;
                        break;
                }
                mandelbrotFractal.setContrast(contrast);
            }
        });

        frame.add(mandelbrotFractal);
        frame.add(zoomC_realLabel);
        frame.add(c_realSpinner);
        frame.add(zoomC_imgLabel);
        frame.add(c_imgSpinner);
        frame.add(zoomFactorLabel);
        frame.add(zoomSpinner);
        frame.add(zoomButton);
        frame.add(contrastLabel);
        frame.add(contrastOptions);
        frame.add(restartButton);

        mandelbrotFractal.setBounds(0,0, width, height);
        zoomC_realLabel.setBounds(10,610,60,40);
        c_realSpinner.setBounds(70,610,60,40);
        zoomC_imgLabel.setBounds(140,610,60,40);
        c_imgSpinner.setBounds(200,610,60,40);
        zoomFactorLabel.setBounds(270, 610, 40, 40);
        zoomSpinner.setBounds(310, 610, 50, 40);
        zoomButton.setBounds(360,610,70,40);
        contrastLabel.setBounds(435, 610, 60, 40);
        contrastOptions.setBounds(500,610,20,40);
        restartButton.setBounds(530, 610, 60, 40);

        frame.setVisible(true);
        mandelbrotFractal.drawFractal();

        while(true);
    }

}
