package frc.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Pathfinding {

    private final double FEET_TO_METERS = .3048;
    private final double INCHES_TO_FEET = 1 / 12;
    private final double RADIUS = 22 * INCHES_TO_FEET;

    private Rectangle2D[] obstacles;
    private HashSet<Node> nodes;
    private Node targetNode;

    public Pathfinding() {
        buildMap();
        initNodes();
        targetNode = new Node(0, 0);// to avoid null checks
    }

    public Node search(double x, double y) {
        HashSet<Node> closed = new HashSet<Node>();
        PriorityQueue<Node> open = new PriorityQueue<Node>();
        cleanTree();
        Node startNode = new Node(x, y);
        open.add(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();
            closed.add(current);

            if (current == targetNode) {
                Node next = current;
                while (next.parent != startNode) {
                    next = next.parent;
                }
                return next;
            }
            for (Node node : current.neighbors) {
                if (closed.contains(node))
                    continue;
                if (!open.contains(node))
                    open.add(node);
                node.update(current);
            }
        }
        return null;
    }

    public static List<Node> pathR(Node end) {

        List<Node> path = new ArrayList<Node>();

        for (Node node = end; node != null; node = node.parent) {

            path.add(node);

        }

        Collections.reverse(path);

        return path;
    }

    // creds: michael //
    public boolean lineOfSight(Node n1, Node n2) {

        double x1 = n1.x;
        double y1 = n1.y;

        double x2 = n2.x;
        double y2 = n2.y;

        double d_sq = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        if (d_sq <= 0)
            return true;

        double d = Math.sqrt(d_sq);
        double vx = (x2 - x1) / d;
        double vy = (y2 - y1) / d;

        double[][] res = new double[2][4];
        int j = 0;

        for (int sign1 = +1; sign1 >= -1; sign1 -= 2) {
            double c = (RADIUS - sign1 * RADIUS) / d;
            if (c * c > 1.0)
                continue;
            double h = Math.sqrt(Math.max(0.0, 1.0 - c * c));

            for (int sign2 = +1; sign2 >= -1; sign2 -= 2) {
                double nx = vx * c - sign2 * h * vy;
                double ny = vy * c + sign2 * h * vx;

                double[] a = res[j++];
                a[0] = x1 + RADIUS * nx;
                a[1] = y1 + RADIUS * ny;
                a[2] = x2 + sign1 * RADIUS * nx;
                a[3] = y2 + sign1 * RADIUS * ny;
            }
        }

        Line2D.Double upperLine = new Line2D.Double(res[0][0], res[0][1], res[0][2], res[0][3]);
        Line2D.Double lowerLine = new Line2D.Double(res[1][0], res[1][1], res[1][2], res[1][3]);
        Ellipse2D.Double endCircle = new Ellipse2D.Double(x2, y2, RADIUS, RADIUS);

        for (int i = 0; i < obstacles.length; i++) {
            if (upperLine.intersects(obstacles[i])) {
                return false;
            }
            if (lowerLine.intersects(obstacles[i])) {
                return false;
            }
            if (endCircle.intersects(obstacles[i])) {
                return false;
            }

        }

        return true;
    }

    private void initNodes() {
        nodes = new HashSet<>();
        addNode(new Node(0, 0));
        // etc, etc
    }

    public void setTargetNode(double x, double y) {
        removeNode(targetNode);
        targetNode = new Node(x, y);
        addNode(targetNode);
        for (Node node : nodes) {
            node.calcH(targetNode);
        }
    }

    private void cleanTree() {
        for (Node node : nodes) {
            node.g = Double.POSITIVE_INFINITY;
        }
    }

    private void addNode(Node n) {
        for (Node node : nodes) {
            if (lineOfSight(n, node)) {
                node.neighbors.add(n);
                n.neighbors.add(node);
            }
        }
        nodes.add(n);
    }

    private void removeNode(Node n) {
        nodes.remove(n);
        for (Node node : nodes) {
            node.neighbors.remove(n);
        }
    }

    private void buildMap() {
        Point2D.Double cargoBayUL = new Point2D.Double(36 * FEET_TO_METERS, 10 * FEET_TO_METERS);
        Point2D.Double cargoBayUR = new Point2D.Double(36 * FEET_TO_METERS, 17 * FEET_TO_METERS);
        Point2D.Double cargoBayBL = new Point2D.Double(18 * FEET_TO_METERS, 10 * FEET_TO_METERS);
        Point2D.Double cargoBayBR = new Point2D.Double(18 * FEET_TO_METERS, 17 * FEET_TO_METERS);
        Rectangle2D.Double cargoBay = new Rectangle2D.Double(cargoBayUL.getX(), cargoBayUR.getY(),
                cargoBayBR.getY() - cargoBayBL.getY(), cargoBayUL.getX() - cargoBayBL.getX());

        Point2D.Double leftFrontRocketUL = new Point2D.Double(25 * FEET_TO_METERS, 0 * FEET_TO_METERS);
        Point2D.Double leftFrontRocketUR = new Point2D.Double(25 * FEET_TO_METERS, 3 * FEET_TO_METERS);
        Point2D.Double leftFrontRocketBL = new Point2D.Double(13 * FEET_TO_METERS, 0 * FEET_TO_METERS);
        Point2D.Double leftFrontRocketBR = new Point2D.Double(13 * FEET_TO_METERS, 3 * FEET_TO_METERS);
        Rectangle2D.Double leftFrontRocket = new Rectangle2D.Double(leftFrontRocketUL.getX(), leftFrontRocketUR.getY(),
                leftFrontRocketBR.getY() - leftFrontRocketBL.getY(),
                leftFrontRocketUL.getX() - leftFrontRocketBL.getX());

        Point2D.Double leftBackRocketUL = new Point2D.Double(41 * FEET_TO_METERS, 0 * FEET_TO_METERS);
        Point2D.Double leftBackRocketUR = new Point2D.Double(41 * FEET_TO_METERS, 3 * FEET_TO_METERS);
        Point2D.Double leftBackRocketBL = new Point2D.Double(29 * FEET_TO_METERS, 0 * FEET_TO_METERS);
        Point2D.Double leftBackRocketBR = new Point2D.Double(29 * FEET_TO_METERS, 3 * FEET_TO_METERS);
        Rectangle2D.Double leftBackRocket = new Rectangle2D.Double(leftBackRocketUL.getX(), leftBackRocketUR.getY(),
                leftBackRocketBR.getY() - leftBackRocketBL.getY(), leftBackRocketUL.getX() - leftBackRocketBL.getX());

        Point2D.Double rightFrontRocketUL = new Point2D.Double(25 * FEET_TO_METERS, 24 * FEET_TO_METERS);
        Point2D.Double rightFrontRocketUR = new Point2D.Double(25 * FEET_TO_METERS, 27 * FEET_TO_METERS);
        Point2D.Double rightFrontRocketBL = new Point2D.Double(13 * FEET_TO_METERS, 24 * FEET_TO_METERS);
        Point2D.Double rightFrontRocketBR = new Point2D.Double(13 * FEET_TO_METERS, 27 * FEET_TO_METERS);
        Rectangle2D.Double rightFrontRocket = new Rectangle2D.Double(rightFrontRocketUL.getX(),
                rightFrontRocketUR.getY(), rightFrontRocketBR.getY() - rightFrontRocketBL.getY(),
                rightFrontRocketUL.getX() - rightFrontRocketBL.getX());

        Point2D.Double rightBackRocketUL = new Point2D.Double(41 * FEET_TO_METERS, 24 * FEET_TO_METERS);
        Point2D.Double rightBackRocketUR = new Point2D.Double(41 * FEET_TO_METERS, 27 * FEET_TO_METERS);
        Point2D.Double rightBackRocketBL = new Point2D.Double(29 * FEET_TO_METERS, 24 * FEET_TO_METERS);
        Point2D.Double rightBackRocketBR = new Point2D.Double(29 * FEET_TO_METERS, 27 * FEET_TO_METERS);
        Rectangle2D.Double rightBackRocket = new Rectangle2D.Double(rightBackRocketUL.getX(), rightBackRocketUR.getY(),
                rightBackRocketBR.getY() - rightBackRocketBL.getY(),
                rightBackRocketUL.getX() - rightBackRocketBL.getX());

        Point2D.Double closeHabZoneUL = new Point2D.Double(4 * FEET_TO_METERS, 9 * FEET_TO_METERS);
        Point2D.Double closeHabZoneUR = new Point2D.Double(4 * FEET_TO_METERS, 19 * FEET_TO_METERS);
        Point2D.Double closeHabZoneBL = new Point2D.Double(0 * FEET_TO_METERS, 9 * FEET_TO_METERS);
        Point2D.Double closeHabZoneBR = new Point2D.Double(0 * FEET_TO_METERS, 19 * FEET_TO_METERS);
        Rectangle2D.Double closeHabZone = new Rectangle2D.Double(closeHabZoneUL.getX(), closeHabZoneUR.getY(),
                closeHabZoneBR.getY() - closeHabZoneBL.getY(), closeHabZoneUL.getX() - closeHabZoneBL.getX());

        Point2D.Double farHabZoneUL = new Point2D.Double(54 * FEET_TO_METERS, 9 * FEET_TO_METERS);
        Point2D.Double farHabZoneUR = new Point2D.Double(54 * FEET_TO_METERS, 19 * FEET_TO_METERS);
        Point2D.Double farHabZoneBL = new Point2D.Double(50 * FEET_TO_METERS, 9 * FEET_TO_METERS);
        Point2D.Double farHabZoneBR = new Point2D.Double(50 * FEET_TO_METERS, 19 * FEET_TO_METERS);
        Rectangle2D.Double farHabZone = new Rectangle2D.Double(farHabZoneUL.getX(), farHabZoneUR.getY(),
                farHabZoneBR.getY() - farHabZoneBL.getY(), farHabZoneUL.getX() - farHabZoneBL.getX());

        obstacles = new Rectangle2D.Double[7];
        obstacles[0] = cargoBay;
        obstacles[1] = leftFrontRocket;
        obstacles[2] = leftBackRocket;
        obstacles[3] = rightFrontRocket;
        obstacles[4] = rightBackRocket;
        obstacles[5] = closeHabZone;
        obstacles[6] = farHabZone;
    }

    // public static void main(String[] args) {

    // // this is the current xy position //
    // double x = 0;
    // double y = 0;

    // Node N1 = new Node("N1", 0, 0);
    // Node N2 = new Node("N2", 0, 0);
    // Node N3 = new Node("N3", 0, 0);
    // Node N4 = new Node("N4", 0, 0);
    // Node N5 = new Node("N5", 0, 0);
    // Node N6 = new Node("N6", 0, 0);
    // Node N7 = new Node("N7", 0, 0);

    // Node[] nodeArr = new Node[] { N1, N2, N3, N4, N5, N6, N7 };

    // // will change cost vals later //

    // N1.adjacencies = new Edge[] { new Edge(N2, 2), new Edge(N4, 2), new Edge(N5,
    // 3) };

    // N2.adjacencies = new Edge[] { new Edge(N1, 2), new Edge(N3, 2), new Edge(N4,
    // .5), new Edge(N5, .5),
    // new Edge(N6, 3), new Edge(N7, 3) };

    // N3.adjacencies = new Edge[] { new Edge(N2, 2), new Edge(N4, 3), new Edge(N5,
    // 2) };

    // N4.adjacencies = new Edge[] { new Edge(N1, 2), new Edge(N2, .5), new Edge(N3,
    // 3), new Edge(N5, 1),
    // new Edge(N6, 1) };

    // N5.adjacencies = new Edge[] { new Edge(N1, 3), new Edge(N2, .5), new Edge(N3,
    // 2), new Edge(N4, 1),
    // new Edge(N7, 1) };

    // N6.adjacencies = new Edge[] { new Edge(N2, 3), new Edge(N4, 1) };

    // N7.adjacencies = new Edge[] { new Edge(N2, 3), new Edge(N5, 1) };

    // Node start = N1;
    // // get from GUI //
    // Node end = N2;

    // Search(start, end, x, y);
    // }

}
