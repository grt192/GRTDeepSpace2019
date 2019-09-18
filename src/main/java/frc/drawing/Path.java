package frc.drawing;

import java.util.ArrayList;
import java.util.List;

import frc.fieldmap.geometry.Vector;

public class Path {

    private List<Vector> points;

    public Path() {
        points = new ArrayList<Vector>();
    }

    private void Smile(){
        points.add(new Vector(1.0, 5.0, true));
        points.add(new Vector(1.0, 3.5, true));
        points.add(new Vector(3.0, 5.0, false));
        points.add(new Vector(3.0, 3.5, true));
        points.add(new Vector(4.0, 2.0, false));
        points.add(new Vector(3.0, 1.4, true));
        points.add(new Vector(2.0, 1.0, true));
        points.add(new Vector(1.0, 1.4, true));
        points.add(new Vector(0.0, 2.0, true));
    }

    public List<Vector> getPoints(){
        return points;
    }

    public List<Vector> getAction(){
        Smile();
        return points;
    }

}