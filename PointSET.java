import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> set;
    
    // construct an empty set of points
    public PointSET() {
        set = new SET<Point2D>();
    }
    
    // is the set empty?
    public boolean isEmpty() {
        return set.size() == 0;
    }
    
    // number of points in the set
    public int size() {
        return set.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        return set.contains(p);
    }
    
    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> stack = new Stack<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) stack.push(p);
        }
        return stack;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        Point2D nearestPoint;
        double nearestDistance;
        if(set.size() > 0) {
            nearestPoint = set.min();
            nearestDistance = p.distanceSquaredTo(set.min());
            for (Point2D ps : set) {
                if (p.distanceSquaredTo(ps) < nearestDistance) {
                    nearestPoint = ps;
                    nearestDistance = p.distanceSquaredTo(ps);
                }
            }
            return nearestPoint;
        }
        else return null;
    }
}
