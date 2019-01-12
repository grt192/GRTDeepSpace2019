package frc.pathfinding;

class Node {

    public final String value;
    public double g_scores = 0;
    public double h_scores;
    public double f_scores = 0;
    public Edge[] adjacencies;
    public Node parent;
    public double[] xycoord;

    public double hVal;

    public Node(String val, double x, double y) {
        value = val;
        xycoord = new double[] { x, y };
    }

    public String toString() {
        return value;
    }

    public double[] getCoord() {
        return xycoord;

    }

    public double hValue(double x, double y, double[] coord) {

        double NX = coord[0];
        double NY = coord[1];

        double ac = Math.abs(NY - y);
        double cb = Math.abs(NX - x);

        hVal = Math.hypot(cb, ac);

        return hVal;

    }

}