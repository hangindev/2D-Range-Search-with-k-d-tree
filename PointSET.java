import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.SET;


public class PointSET {
    private SET<Point2D> set;
    
    // construct an empty set of points
    // - Initializes an empty SET.
    public PointSET() {
        set = new SET<Point2D>();
    }
    
    // is the set empty?
    // - size() = 0
    public boolean isEmpty() {
        return set.size() == 0;
    }
    
    // number of points in the set
    // - SET.size()
    public int size() {
        return set.size();
    }
    
    // - SET.add()
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }
    
    // does the set contain point p?
    // - SET.contains()
    public boolean contains(Point2D p) {
        return set.contains(p);
    }
    
    // draw all points to standard draw
    // - for(...) p.draw()
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }
    
    
    // all points that are inside the rectangle
    // - for(...) rect.contains(p), stack/queue
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> stack = new Stack<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) stack.push(p);
        }
        return stack;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    // - for(...) , if(...) min = this.distanceSquaredTo(p)
    public Point2D nearest(Point2D p) {
        Point2D nearestPoint;
        double nearestDistance;
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
    
    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        
        StdDraw.enableDoubleBuffering();
        
        // initialize the data structures with N points from standard input
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        
        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle
        
        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.show();
        
        while (true) {
            
            // user starts to drag a rectangle
            if (StdDraw.mousePressed() && !isDragging) {
                x0 = StdDraw.mouseX();
                y0 = StdDraw.mouseY();
                isDragging = true;
                continue;
            }
            
            // user is dragging a rectangle
            else if (StdDraw.mousePressed() && isDragging) {
                x1 = StdDraw.mouseX();
                y1 = StdDraw.mouseY();
                continue;
            }
            
            // mouse no longer pressed
            else if (!StdDraw.mousePressed() && isDragging) {
                isDragging = false;
            }
            
            
            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                                     Math.max(x0, x1), Math.max(y0, y1));
            // draw the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();
            
            // draw the rectangle
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            rect.draw();
            
            // draw the range search results for brute-force data structure in red
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            for (Point2D p : brute.range(rect))
                p.draw();
            
            StdDraw.show();
            StdDraw.pause(40);
        }
    }
}

