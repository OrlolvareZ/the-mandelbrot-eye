package app;

import bezierCanvas.BezierCanvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * This application uses a BezierCanvas object to draw Bèzier curves. Last
 * modification: 20/09/2021
 */
public class BezierCurveMakerApp {

    //Points 
    private static ArrayList<Point2D> pointList = new ArrayList<>(10);
    private static BezierCanvas bzCanvas;
    private static int width;
    private static int height;
    private static double drawingSpeed;
    private static double precision;
    private static int maxPoints;

    public static void main(String[] args) {

        maxPoints = 10;
        drawingSpeed = 20;
        precision = 0.01;
        
        // We start customizing a window for the user to choose the size of the screen.
        
        String[] dimentions = {"Medium (600 x 500px)", "Big (1000 x 700px)"};
        int option = JOptionPane.showOptionDialog(null, "Choose the size of the canvas", null, -1, -1,
                null, dimentions, dimentions);
        switch (option) {
            case 0:
                width = 600;
                height = 500;
                break;
            case 1:
                width = 1000;
                height = 700;
                break;
            default: // Closing window
                System.exit(0);
                break;
        }
        
        // Creates, adds and displays app instructions
        
        JFrame frame = new JFrame();
        frame.setSize(width + 300, height);
        frame.setTitle("Cartesian Plane w/ Bèzier Curves (x: " + width + ", y: " + height + ")");
        
        String instructions = "Instructions:\n\n" +
                             "1.- Click somewhere on the grid to set the\n points of your curve\n(per curve: min. 2 - max. 10).\n\n" +
                             "2.- Click on \"Go!\" to see\n the curve be drawn.\n\n" +
                             "*** You may also adjust how ***\n" + 
                             "*** smooth the curve gets, ***\n" +
                             "*** or how fast it's drawn. ***";
        
        JTextArea instructionsArea = new JTextArea(instructions);
        instructionsArea.setBounds((int)(width + 25), (int)(width*0.03),
                                   230, (int)(height*0.40));
        instructionsArea.setBackground(frame.getBackground());
        instructionsArea.setEditable(false);
        
        frame.add(instructionsArea);

        // Defines a button to run the app
        
        JButton btnGo = new JButton();
        btnGo.setBounds(width + 25, (7*height) / 10, 200, 30);
        btnGo.setText("Go!");
        btnGo.setEnabled(false);
        btnGo.setBorder(new RoundedBorder(25));
        
        btnGo.addActionListener((ActionEvent e) -> {
            Point2D[] pts = pointList.toArray(new Point2D[pointList.size()]);
            
            try {
                // Draw the curve
                bzCanvas.createBezierCurve(pts, bzCanvas.getGraphics());
                pointList.clear();
                btnGo.setEnabled(false);
                btnGo.setBackground(frame.getBackground());
            } catch (InterruptedException ex) {
                // Logger.getLogger(BezierCurveMakerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        frame.add(btnGo);
        
        // Adds clear button to reset canvas
        
        JButton btnClear = new JButton();
        btnClear.setBounds(width + 25, (8*height) / 10 , 200, 30);
        btnClear.setText("Clear");
        btnClear.setEnabled(false);
        btnClear.setBackground(frame.getBackground());
        btnClear.setBorder(new RoundedBorder(25));
        
        btnClear.addActionListener((ActionEvent e) -> {
            bzCanvas.clear(bzCanvas.getGraphics());
            pointList.clear();
            btnGo.setEnabled(false);
            btnGo.setBackground(frame.getBackground());
            btnClear.setEnabled(false);
            btnClear.setBackground(frame.getBackground());
        });
        frame.add(btnClear);
        
        // Adds BezierCanvas to be drawn
        bzCanvas = new BezierCanvas(width, height, drawingSpeed, precision);
        bzCanvas.setBounds(0,0,width,height);
        bzCanvas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
                
                // Checks if the point limit hasn't been reached
                if(pointList.size() == maxPoints){
                    JOptionPane.showMessageDialog(null, "You can't put more than " + maxPoints + " points!\n" +
                                                  "Try clicking Go!", 
                                                  "Point limit reached", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                final Point pos = e.getPoint();
                final int x = pos.x;
                final int y = pos.y;
                
                // Disables drawing outside the grid.
                if(x > width) return;
                
                Point2D point = new Point2D.Float(x, y);
                bzCanvas.drawControlPoint(point, bzCanvas.getGraphics(), Color.red, 4);
                pointList.add(point);
                
                btnClear.setEnabled(true);
                btnClear.setBackground(new Color(88, 197, 210));
                
                if(pointList.size() > 1){
                    btnGo.setEnabled(true);
                    btnGo.setBackground(new Color(57, 202, 28));
                }
            }
        });
        
        // Adds the canvas (a JPanel) to the frame
        frame.add(bzCanvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        
        // Keeps the drawings on the screen
        while (true);
    }

}
