package frc.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Pathfinding {

    public static void Search(Node start, Node end, double x, double y) {

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
                double temp_f_scores = temp_g_scores + child.hValue(x, y, child.xycoord);

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

    public static Node closestNode(double x, double y, Node[] nodeArr) {

        Node fnode = new Node("blank", 0, 0);

        for (Node n : nodeArr) {

            if (n.hValue(x, y, n.xycoord) < fnode.hValue(x, y, fnode.xycoord)) {

                fnode = n;

            }

        }
        return fnode;

    }

    public static void main(String[] args) {

        // this is the current xy position //
        double x = 0;
        double y = 0;

        Node N1 = new Node("N1", 0, 0);
        Node N2 = new Node("N2", 0, 0);
        Node N3 = new Node("N3", 0, 0);
        Node N4 = new Node("N4", 0, 0);
        Node N5 = new Node("N5", 0, 0);
        Node N6 = new Node("N6", 0, 0);
        Node N7 = new Node("N7", 0, 0);

        Node[] nodeArr = new Node[] { N1, N2, N3, N4, N5, N6, N7 };

        // will change cost vals later //

        N1.adjacencies = new Edge[] { new Edge(N2, 2), new Edge(N4, 2), new Edge(N5, 3) };

        N2.adjacencies = new Edge[] { new Edge(N1, 2), new Edge(N3, 2), new Edge(N4, .5), new Edge(N5, .5),
                new Edge(N6, 3), new Edge(N7, 3) };

        N3.adjacencies = new Edge[] { new Edge(N2, 2), new Edge(N4, 3), new Edge(N5, 2) };

        N4.adjacencies = new Edge[] { new Edge(N1, 2), new Edge(N2, .5), new Edge(N3, 3), new Edge(N5, 1),
                new Edge(N6, 1) };

        N5.adjacencies = new Edge[] { new Edge(N1, 3), new Edge(N2, .5), new Edge(N3, 2), new Edge(N4, 1),
                new Edge(N7, 1) };

        N6.adjacencies = new Edge[] { new Edge(N2, 3), new Edge(N4, 1) };

        N7.adjacencies = new Edge[] { new Edge(N2, 3), new Edge(N5, 1) };

        Node start = closestNode(x, y, nodeArr);
        Node end = N2;

        Search(start, end, x, y);
    }

}
