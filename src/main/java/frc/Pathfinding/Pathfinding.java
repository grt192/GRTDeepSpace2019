package frc.Pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Pathfinding {

    public static void Search(Node start, Node end) {

        HashSet<Node> closed = new HashSet<Node>();
        PriorityQueue<Node> open = new PriorityQueue<Node>();

        boolean finished = false;

        while (!open.isEmpty() && !finished) {

            Node current = open.poll();
            closed.add(current);

            if (current.value.equals(end.value)) {

                finished = true;
                List<Node> path = pathR(end);
                System.out.println(path);

            }

            for (Edge e : current.adjacencies) {

                Node child = e.target;
                double cost = e.cost;
                double temp_g_scores = current.g_scores + cost;
                double temp_f_scores = temp_g_scores + child.h_scores;

                if ((closed.contains(child)) && (temp_f_scores >= child.f_scores)) {
                    continue;

                }

                else if ((!open.contains(child)) || (temp_f_scores < child.f_scores)) {

                    child.parent = current;
                    child.g_scores = temp_g_scores;
                    child.f_scores = temp_f_scores;

                    if (open.contains(child)) {

                        open.remove(child);

                    }

                    open.add(child);

                }
            }

        }

    }

    public static List<Node> pathR(Node end) {

        List<Node> path = new ArrayList<Node>();

        for (Node node = end; node != null; node = node.parent) {

            path.add(node);

        }

        Collections.reverse(path);

        return path;
    }

    public static void closestNode(int x, int y) {

        // will write later //

    }

    public static void main(String[] args) {

        // will write method for changing h vals later //

        Node N1 = new Node("N1", 0);
        Node N2 = new Node("N2", 0);
        Node N3 = new Node("N3", 0);
        Node N4 = new Node("N4", 0);
        Node N5 = new Node("N5", 0);
        Node N6 = new Node("N6", 0);
        Node N7 = new Node("N7", 0);

        // will change cost vals later //

        N1.adjacencies = new Edge[] { new Edge(N2, 0), new Edge(N4, 0), new Edge(N5, 0) };

        N2.adjacencies = new Edge[] { new Edge(N1, 0), new Edge(N3, 0), new Edge(N4, 0), new Edge(N5, 0),
                new Edge(N6, 0), new Edge(N7, 0) };

        N3.adjacencies = new Edge[] { new Edge(N2, 0), new Edge(N4, 0), new Edge(N5, 0) };

        N4.adjacencies = new Edge[] { new Edge(N1, 0), new Edge(N2, 0), new Edge(N3, 0), new Edge(N5, 0),
                new Edge(N6, 0) };

        N5.adjacencies = new Edge[] { new Edge(N1, 0), new Edge(N2, 0), new Edge(N3, 0), new Edge(N4, 0),
                new Edge(N6, 0) };

        N6.adjacencies = new Edge[] { new Edge(N2, 0), new Edge(N4, 0) };

        N7.adjacencies = new Edge[] { new Edge(N2, 0), new Edge(N5, 0) };

        Node start = N1;
        Node end = N2;

        Search(start, end);
    }

}
