package frc.pathfinding;

import java.util.HashSet;

import frc.fieldmap.geometry.Vector;

public class Node implements Comparable<Node> {

    public double g;
    public double h;
    public double f;
    public final HashSet<Node> neighbors;
    public Node parent;
    public final Vector pos;

    public Node(double x, double y) {
        pos = new Vector(x, y);
        neighbors = new HashSet<>();
    }

    /**
     * values used to evaluate best node
     */
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
        return pos.distanceTo(n.pos);
    }

    @Override
    public int compareTo(Node other) {
        if (other.f < f)
            return 1;
        return -1;
    }

}