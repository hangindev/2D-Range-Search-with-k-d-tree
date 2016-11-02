import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stack;

public class KdTree {
    private int size = 0;
    private int keycoord;
    private double xmin, xmax, ymin, ymax;
    private Stack<Point2D> stack;
    private Point2D nearestPoint;
    private double nearestDistance;
    private Node root = null;     // root of the BST

    // BST helper node data type
    private class Node {
        private Point2D p;         // the point
        private RectHV rect;       // the axis-aligned rectangle corresponding to this node
        private Node left, right;  // links to left and right subtrees

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    // Initializes an empty symbol table
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return get(p) != null;
    }

    private Point2D get(Point2D p) {
        keycoord = -1;
        return get(root, p);
    }

    private Point2D get(Node n, Point2D p) {
        if (n == null) return null;
        keycoord = keycoord * (-1);
        double cmp;
        switch (keycoord) {
            case  1: {
                cmp = p.x() - n.p.x();
                if      (cmp < 0)            return get(n.left, p);
                else if (cmp > 0)            return get(n.right, p);
                else if (p.y() != n.p.y())   return get(n.right, p);
                else                         return n.p;
            }
            case -1: {
                cmp = p.y() - n.p.y();
                if      (cmp < 0)            return get(n.left, p);
                else if (cmp > 0)            return get(n.right, p);
                else if (p.x() != n.p.x())   return get(n.right, p);
                else                         return n.p;
            }
            default: return n.p;
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        keycoord = -1;
        xmin = 0;
        ymin = 0;
        xmax = 1;
        ymax = 1;
        root = put(root, p);
    };

    private Node put(Node n, Point2D p) {
        if (n == null) {
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            Node node = new Node(p, rect);
            size++;
            return node;
        }
        keycoord = keycoord * (-1);
        double cmp;
        switch (keycoord) {
            case  1: {
                cmp = p.x() - n.p.x();
                if      (cmp < 0) {
                    xmax = n.p.x();
                    n.left  = put(n.left,  p);
                }
                else if (cmp > 0) {
                    xmin = n.p.x();
                    n.right = put(n.right, p);
                }
                else if (p.y() != n.p.y()) {
                    xmin = n.p.x();
                    n.right = put(n.right, p);
                }
                break;
            }
            case -1: {
                cmp = p.y() - n.p.y();
                if      (cmp < 0) {
                    ymax = n.p.y();
                    n.left  = put(n.left,  p);
                }
                else if (cmp > 0) {
                    ymin = n.p.y();
                    n.right = put(n.right, p);
                }
                else if (p.x() != n.p.x()) {
                    ymin = n.p.y();
                    n.right = put(n.right, p);
                }
                break;
            }
            default: break;
        }
        return n;
    }

    // draw all points to standard draw
    public void draw() {
        keycoord = -1;
        draw(root, keycoord);
    }

    private void draw(Node n, int keycoord) {
        // draw the point
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        n.p.draw();

        // draw the rect
        StdDraw.setPenRadius(0.005);
        keycoord = keycoord * (-1);
        switch (keycoord) {
            case  1:  {
                StdDraw.setPenColor(StdDraw.BLUE);
                break;
            }
            case  -1:  {
                StdDraw.setPenColor(StdDraw.RED);
                break;
            }
            default: break;
        }
        n.rect.draw();

        if(n.left != null)  draw(n.left, keycoord);
        if(n.right != null) draw(n.right, keycoord);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        stack = new Stack<Point2D>();
        if (root != null) rangeSearch(root, rect);
        return stack;
    }

    private void rangeSearch(Node n, RectHV rect) {
        if (n.rect.intersects(rect))  {
            if(rect.contains(n.p))  {
                stack.push(n.p);
            }
            if (n.left != null)   rangeSearch(n.left, rect);
            if (n.right != null)  rangeSearch(n.right, rect);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (root == null) return null;
        else {
            nearestPoint = root.p;
            nearestDistance = p.distanceSquaredTo(root.p);
            keycoord = -1;
            nearestSearch(root, p);
            return nearestPoint;
        }
    }

    private void nearestSearch(Node n, Point2D p) {
        if (n == null) return;
        if (nearestDistance > p.distanceSquaredTo(n.p)) {
            nearestPoint = n.p;
            nearestDistance = p.distanceSquaredTo(n.p);
        }
        keycoord = keycoord * (-1);
        double cmp;
        switch (keycoord) {
            case  1:  {
                cmp = p.x() - n.p.x();
                if      (cmp < 0) {
                    nearestSearch(n.left,  p);
                    if (n.right != null) {
                        if (nearestDistance > n.right.rect.distanceSquaredTo(p))
                            nearestSearch(n.right,  p);
                    }
                }
                else if (cmp > 0) {
                    nearestSearch(n.right,  p);
                    if (n.left != null) {
                        if (nearestDistance > n.left.rect.distanceSquaredTo(p))
                            nearestSearch(n.left,  p);
                    }
                }
                else if (p.y() != n.p.y()) {
                    nearestSearch(n.right,  p);
                    if (n.left != null) {
                        if (nearestDistance > n.left.rect.distanceSquaredTo(p))
                            nearestSearch(n.left,  p);
                    }
                }
                break;
            }
            case  -1:  {
                cmp = p.y() - n.p.y();
                if      (cmp < 0) {
                    nearestSearch(n.left,  p);
                    if (n.right != null) {
                        if (nearestDistance > n.right.rect.distanceSquaredTo(p))
                            nearestSearch(n.right,  p);
                    }
                }
                else if (cmp > 0) {
                    nearestSearch(n.right,  p);
                    if (n.left != null) {
                        if (nearestDistance > n.left.rect.distanceSquaredTo(p))
                            nearestSearch(n.left,  p);
                    }
                }
                else if (p.x() != n.p.x()) {
                    nearestSearch(n.right,  p);
                    if (n.left != null) {
                        if (nearestDistance > n.left.rect.distanceSquaredTo(p))
                            nearestSearch(n.left,  p);
                    }
                }
                break;
            }
            default: break;
        }
    }
}
