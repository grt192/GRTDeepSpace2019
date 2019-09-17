package frc.drawing;

import java.util.ArrayList;
import java.util.List;

import frc.fieldmap.geometry.Vector;

public class Path {

    private List<Vector> points;

    public Path() {
        points = new ArrayList<Vector>();
    }

    private void Line() {
        points.add(new Vector(5.0, 5.0));
    }

    public List<Vector> getPoints(){
        return points;
    }

    public List<Vector> getAction(int i){
        if(i == 1){
            Line();
            return points;
        } else {
            return null;
        }
    }

}