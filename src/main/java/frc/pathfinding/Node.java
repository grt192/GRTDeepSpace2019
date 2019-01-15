package frc.pathfinding;

import java.util.HashSet;

public class Node implements Comparable<Node> {

    public double g;
    public double h;
    public double f;
    public final HashSet<Node> neighbors;
    public Node parent;
    public final double x, y;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        neighbors = new HashSet<>();
    }

    public void update(Node node) {
        double newG = node.g + distance(node);
        if (newG < g) {
            parent = node;
            g = newG;
            f = g + h;
        }
    }

    public void calcH(Node end) {
        h = distance(end);
        f = g + h;
    }

    private double distance(Node n) {
        double dx = x - n.x;
        double dy = y - n.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public int compareTo(Node other) {
        return (int) Math.signum(f - other.f);
    }

}