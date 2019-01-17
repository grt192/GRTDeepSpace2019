package frc.pathfinding;

import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.PriorityQueue;

import frc.robot.Robot;

public class Pathfinding {

    private HashSet<Node> nodes;
    private Node targetNode;

    public Pathfinding() {
        initNodes();
        targetNode = new Node(0, 0);// to avoid null checks
    }

    public Node search(double x, double y) {
        HashSet<Node> closed = new HashSet<>();
        PriorityQueue<Node> open = new PriorityQueue<>();
        cleanTree();
        Node startNode = new Node(x, y);
        addNode(startNode);
        startNode.calcH(targetNode);
        open.add(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();
            closed.add(current);

            if (current == targetNode) {
                Node next = current;
                while (next.parent != startNode) {
                    next = next.parent;
                }
                removeNode(startNode);
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
        removeNode(startNode);
        return null;
    }

    private void initNodes() {
        nodes = new HashSet<>();
        addNode(new Node(142, 30));
        addNode(new Node(142, 97));
        addNode(new Node(18, 97));
        addNode(new Node(18, 30));
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
            if (Robot.FIELD_MAP.lineOfSight(n.pos, node.pos)) {
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

}
