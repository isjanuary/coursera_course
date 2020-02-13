import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

// import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class KdTree {
  // x-coordinate as key in the odd level of BST, divide into 2 vertical planes
  // y-coordinate as key in the even level of BST, divide into 2 horizontal planes
  private static final boolean XAXIS = true;
  private static final boolean YAXIS = false;
  private int cnt;
  private Node root;

  private class Node {
    private final double key;
    private final Point2D val;
    private Node left, right;
    private final boolean axis;

    public Node(Double key, Point2D val, boolean axis) {
      this.key = key;
      this.val = val;
      this.left = null;
      this.right = null;
      this.axis = axis;
    }
  }

  // construct an empty set of points
  public KdTree() {
    cnt = 0;
    root = null;
  }

  // is the set empty? 
  public boolean isEmpty() {
    return cnt < 1;
  }

  // number of points in the set 
  public int size() {
    return cnt;
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) throw new IllegalArgumentException("inserted point CANNOT be null");
    double x = p.x();
    double y = p.y();
    if (root == null) {
      root = new Node(p.x(), p, XAXIS);
      cnt++;
      return;
    }

    Node curr = root;
    Node prev = null;
    while (curr != null) {
      // this point is already in the set, do nothing
      if (p.equals(curr.val)) return;

      prev = curr;
      if (XAXIS == curr.axis) {
        if (x < curr.key) {
          curr = curr.left;
        } else {
          curr = curr.right;
        }
      } else {
        if (y < curr.key) {
          curr = curr.left;
        } else {
          curr = curr.right;
        }
      }
    }

    if (XAXIS == prev.axis) { 
      if (x < prev.key) {
        prev.left = new Node(y, p, YAXIS);
      } else {
        prev.right = new Node(y, p, YAXIS);
      }
    } else {
      if (y < prev.key) {
        prev.left = new Node(x, p, XAXIS);
      } else {
        prev.right = new Node(x, p, XAXIS);
      }
    }
    cnt++;
  }

  // does the set contain point p? 
  public boolean contains(Point2D p) {
    if (p == null) throw new IllegalArgumentException("search point CANNOT be null");

    double px = p.x();
    double py = p.y();
    Node curr = root;
    while (curr != null) {
      if (p.equals(curr.val)) return true;
      if (XAXIS == curr.axis) {
        if (px < curr.key) curr = curr.left;
        else curr = curr.right;
      } else {
        if (py < curr.key) curr = curr.left;
        else curr = curr.right;
      }
    }
    return false;
  }

  // draw all points to standard draw
  public void draw() {
    // inorder traversal
    draw(root);
  }

  private void draw(Node curr) {
    if (curr == null) return;
    if (null != curr.left) draw(curr.left);
    curr.val.draw();
    if (null != curr.right) draw(curr.right);
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) throw new IllegalArgumentException("input point CANNOT be null");
    if (isEmpty()) return null;
    Queue<Point2D> rangePointsQ = new Queue<Point2D>();
    RectHV rootRect = new RectHV(0.0, 0.0, 1.0, 1.0);
    searchPointsInRect(rect, rootRect, rangePointsQ, root);
    return rangePointsQ;
  }

  private void searchPointsInRect(RectHV query, RectHV parent, Queue<Point2D> q, Node curr) {
    if (curr == null) return;
    // pruning rule
    RectHV smaller = null;
    RectHV larger = null;
    if (curr.axis == XAXIS) {
      smaller = new RectHV(parent.xmin(), parent.ymin(), curr.val.x(), parent.ymax());
      larger = new RectHV(curr.val.x(), parent.ymin(), parent.xmax(), parent.ymax());
    } else {
      smaller = new RectHV(parent.xmin(), parent.ymin(), parent.xmax(), curr.val.y());
      larger = new RectHV(parent.xmin(), curr.val.y(), parent.xmax(), parent.ymax());
    }

    // inorder traverse
    if (smaller.intersects(query)) searchPointsInRect(query, smaller, q, curr.left);
    if (query.contains(curr.val)) q.enqueue(curr.val);
    if (larger.intersects(query)) searchPointsInRect(query, larger, q, curr.right);
  }

  // a nearest neighbor in the set to point p; null if the set is empty 
  public Point2D nearest(Point2D p) {
    if (p == null) throw new IllegalArgumentException("input point CANNOT be null");
    if (isEmpty()) return null;
    return searchNearest(p, root.val, root);
  }

  private Point2D searchNearest(Point2D query, Point2D nearest, Node curr) {
    if (curr == null) return nearest;
    double query2Rect = 0.0;
    double query2Nearest = query.distanceSquaredTo(nearest);
    if (query.distanceSquaredTo(curr.val) < query2Nearest) nearest = curr.val;

    /**
     * step 1:
     *    calculate the distance from the query point to the rectangle according to the curr node
     * step 2:
     *    if curr.left is on the same side of query point, search left subtree first
     *    otherwise, search right subtree first
     * step 3:
     *    if the search in the first substree is done, then judge whether the distance calculated in
     *    step 1 is smaller than the distance from the query point to the nearest point so far.
     *    if the distance in step 1 is smaller, which means there may exist closer points to the query point
     *    in the other subtree, then search the other subtree.
     *    otherwise, continue
    */
    if (XAXIS == curr.axis) {
       query2Rect = (query.x() - curr.key) * (query.x() - curr.key);

      if (query.x() < curr.key) {
        nearest = searchNearest(query, nearest, curr.left);
        query2Nearest = query.distanceSquaredTo(nearest);
        if (query2Rect <= query2Nearest) nearest = searchNearest(query, nearest, curr.right);
      } else {
        nearest = searchNearest(query, nearest, curr.right);
        query2Nearest = query.distanceSquaredTo(nearest);
        if (query2Rect <= query2Nearest) nearest = searchNearest(query, nearest, curr.left);
      }
    } else {
      query2Rect = (query.y() - curr.key) * (query.y() - curr.key);

       if (query.y() < curr.key) {
        nearest = searchNearest(query, nearest, curr.left);
        query2Nearest = query.distanceSquaredTo(nearest);
        if (query2Rect <= query2Nearest) nearest = searchNearest(query, nearest, curr.right);
      } else {
        nearest = searchNearest(query, nearest, curr.right);
        query2Nearest = query.distanceSquaredTo(nearest);
        if (query2Rect <= query2Nearest) nearest = searchNearest(query, nearest, curr.left);
      }
    }

    return nearest;
  }

  public static void main(String[] args) {
    // KdTreeVisualizer.java
    // RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
    // StdDraw.enableDoubleBuffering();
    // KdTree kdtree = new KdTree();
    // while (true) {
    //   if (StdDraw.isMousePressed()) {
    //     double x = StdDraw.mouseX();
    //     double y = StdDraw.mouseY();
    //     StdOut.printf("%8.6f %8.6f\n", x, y);
    //     Point2D p = new Point2D(x, y);
    //     if (rect.contains(p)) {
    //       System.out.println("rect contains the point");
    //       StdOut.printf("%8.6f %8.6f\n", x, y);
    //       System.out.println("will insert new point");
    //       kdtree.insert(p);
    //       StdDraw.clear();
    //       kdtree.draw();
    //       StdDraw.show();
    //     }
    //   }
    //   StdDraw.pause(20);
    // }








    // RangeSearchVisualizer.java
    // initialize the data structures from file
    // String filename = args[0];
    // In in = new In(filename);
    // KdTree kdtree = new KdTree();
    // while (!in.isEmpty()) {
    //   double x = in.readDouble();
    //   double y = in.readDouble();
    //   Point2D p = new Point2D(x, y);
    //   kdtree.insert(p);
    // }

    // double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
    // double x1 = 0.0, y1 = 0.0;      // current location of mouse
    // boolean isDragging = false;     // is the user dragging a rectangle

    // // draw the points
    // StdDraw.clear();
    // StdDraw.setPenColor(StdDraw.BLACK);
    // StdDraw.setPenRadius(0.01);
    // kdtree.draw();
    // StdDraw.show();

    // // process range search queries
    // StdDraw.enableDoubleBuffering();
    // while (true) {

    //   // user starts to drag a rectangle
    //   if (StdDraw.isMousePressed() && !isDragging) {
    //     x0 = x1 = StdDraw.mouseX();
    //     y0 = y1 = StdDraw.mouseY();
    //     isDragging = true;
    //   }

    //   // user is dragging a rectangle
    //   else if (StdDraw.isMousePressed() && isDragging) {
    //     x1 = StdDraw.mouseX();
    //     y1 = StdDraw.mouseY();
    //   }

    //   // user stops dragging rectangle
    //   else if (!StdDraw.isMousePressed() && isDragging) {
    //     isDragging = false;
    //   }

    //   // draw the points
    //   StdDraw.clear();
    //   StdDraw.setPenColor(StdDraw.BLACK);
    //   StdDraw.setPenRadius(0.01);
    //   kdtree.draw();

    //   // draw the rectangle
    //   RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
    //                             Math.max(x0, x1), Math.max(y0, y1));
    //   StdDraw.setPenColor(StdDraw.BLACK);
    //   StdDraw.setPenRadius();
    //   rect.draw();

    //   // draw the range search results for brute-force data structure in red
    //   StdDraw.setPenRadius(0.03);
    //   StdDraw.setPenColor(StdDraw.RED);
    //   for (Point2D p : kdtree.range(rect))
    //     p.draw();

    //   // draw the range search results for kd-tree in blue
    //   // StdDraw.setPenRadius(0.02);
    //   // StdDraw.setPenColor(StdDraw.BLUE);
    //   // for (Point2D p : kdtree.range(rect))
    //   //   p.draw();

    //   StdDraw.show();
    //   StdDraw.pause(20);
    // }







    // NearestNeighborVisualizer.java
    // initialize the two data structures with point from file
    String filename = args[0];
    In in = new In(filename);
    KdTree kdtree = new KdTree();
    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      kdtree.insert(p);
    }

    System.out.println(kdtree.nearest(new Point2D(0.19, 0.69)));

    // // process nearest neighbor queries
    // StdDraw.enableDoubleBuffering();
    // while (true) {
    //   // the location (x, y) of the mouse
    //   double x = StdDraw.mouseX();
    //   double y = StdDraw.mouseY();
    //   System.out.printf("=========: (%f, %f)\n", x, y);
    //   Point2D query = new Point2D(x, y);

    //   // draw all of the points
    //   StdDraw.clear();
    //   StdDraw.setPenColor(StdDraw.BLACK);
    //   StdDraw.setPenRadius(0.01);
    //   kdtree.draw();

    //   // draw in red the nearest neighbor (using brute-force algorithm)
    //   // StdDraw.setPenRadius(0.03);
    //   // StdDraw.setPenColor(StdDraw.RED);
    //   // brute.nearest(query).draw();
    //   StdDraw.setPenRadius(0.02);

    //   // draw in blue the nearest neighbor (using kd-tree algorithm)
    //   StdDraw.setPenColor(StdDraw.BLUE);
    
    //   System.out.println(kdtree.nearest(query));
    //   kdtree.nearest(query).draw();
    //   StdDraw.show();
    //   StdDraw.pause(40);
    // }
  }
}
