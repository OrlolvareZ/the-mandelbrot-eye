package bezierCanvas;

import java.awt.geom.Point2D;

/**
 * This class will compute the necessary points to draw a Bêzier curve given a set of points.
 */

public class BezierCalculator {
    
    /**
     * The points that form the segments that should be interpolated to find a point in the Bèzier curve.
     */
    private Point2D[][] intrpltdPoints;
  
    public BezierCalculator(){
        intrpltdPoints =  null;
    }
    
    /**
     * Determines a new point in between the line made by A and B.
     * The factor *t* works as a percentage that indicates how much
     * of the distance of AB has been traveled by the new point.
     * 
     * @param A, the beggining point of the segment.
     * @param B, the ending point of the segment.
     * @param t, the value between 0 and 1 (inclusive) that determines 
     *           the new point in between the line made by A and B.
     * @return 
     */
    private Point2D interpolate(Point2D A, Point2D B, double t){
        
        float newX = (float) ((1-t)*A.getX() + t*B.getX());
        float newY = (float) ((1-t)*A.getY() + t*B.getY());
        
        return new Point2D.Float(newX, newY);
    }
    
    /**
     * Given a set of points and a parameter t, finds the point in the Bèzier curve
     * made by said points.
     * 
     * @param points , the control points of the Bèzier curve.
     * @param t , the parameter whose value is between 0 and 1 (inclusive) and determines
     *            the location of the point.
     * @return the point in the Bèzier curve made by the given points and value of t.
     */
    public Point2D findBezierPoint(Point2D[] points, double t){
        
        /* This method is called with the initial control points of the Bèzier point. Both this set of
            points and the last point will not be stored as part of our set of interpolated segments.
        */
        intrpltdPoints = new Point2D[points.length - 2][];
        
        return findBezierPoint(points, t, 0);
    }
    
    /**
     * Finds recursively a point in the Bèzier curve made my the given points and value of t.
     * 
     * @param points , the set of points to be interpolated..
     * @param t , the parameter whose value is between 0 and 1 (inclusive) and determines
     *            the location of the interpolation points.
     * @return the point in the Bèzier curve made by the given points and value of t.
     */
    private Point2D findBezierPoint(Point2D[] points, double t, int index){
        
        // Base case. Found a point of the Bèzier curve, result of the succesive interpolation of segments.
        if(points.length == 1)
            return new Point2D.Float(
                            (float)points[0].getX(),
                            (float)points[0].getY());
        
        // Knowing we still have to interpolate points, we save them as part of the interpolation round.
        // We will not save the beggining control points nor the final Bèzier point.
        if(index > 0)
            intrpltdPoints[index - 1] = points;
        
        // Saves space for the result of interpolation of the set of points given as parameter.
        // For each two points interpolated we know that we will have a resulting point. Therefore,
        // new amount of points = points to interpolate - 1;
        Point2D[] newPoints = new Point2D[points.length - 1];
        
        // Begins interpolation process
        for(int i = 0; i + 1 < points.length; i++)           
            newPoints[i] = interpolate(points[i], points[i+1], t);
        
        
        // Seeks for the point in the new segments, result of interpolation.
        return findBezierPoint(newPoints, t, index + 1);     
    }

    public Point2D[][] getIntrpltdPoints() {
        return intrpltdPoints;
    }
        
}
