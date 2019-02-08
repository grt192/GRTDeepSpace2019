package frc.pathfinding;

import frc.fieldmap.geometry.Polygon;
import frc.fieldmap.geometry.Vector;
import frc.robot.Robot;

public class PotentialFieldPathfinding {

	private Vector target;

	public PotentialFieldPathfinding() {
		target = new Vector(0, 0);
	}

	public Vector search(double x, double y) {
		double radius = Robot.ROBOT_RADIUS + 3.0;
		double r2 = radius * radius;
		Vector pos = new Vector(x, y);
		Vector velocity = target.subtract(pos).multiply(1 / target.distanceTo(pos));
		for (Polygon p : Robot.FIELD_MAP.getObstacles()) {
			Vector close = p.closestPoint(pos);
			double d2 = close.distanceSquaredTo(pos);
			if (d2 < r2) {
				Vector displacement = pos.subtract(close);
				double project = (displacement.dot(velocity) / d2);
				System.out.println(project);
				if (project < 0) {
					Vector force = displacement.multiply(-project);
					velocity = velocity.add(force);
				}
			}
		}
		Vector close = Robot.FIELD_MAP.closestWallPoint(pos);
		double d2 = close.distanceSquaredTo(pos);
		if (d2 < r2) {
			Vector displacement = pos.subtract(close);
			double project = (displacement.dot(velocity) / d2);
			System.out.println(project);
			if (project < 0) {
				Vector force = displacement.multiply(-project);
				velocity = velocity.add(force);
			}
		}
		velocity = velocity.multiply(1 / velocity.magnitude());
		return velocity;
	}

	public void setTarget(double x, double y) {
		target = new Vector(x, y);
	}

}
