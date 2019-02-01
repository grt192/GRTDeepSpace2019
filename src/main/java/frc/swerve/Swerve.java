package frc.swerve;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.config.Config;
import frc.robot.Robot;
import frc.util.GRTUtil;

public class Swerve implements Runnable {

	private static final double TWO_PI = Math.PI * 2;

	private final double SWERVE_WIDTH;
	private final double SWERVE_HEIGHT;
	private final double RADIUS;
	private final double WHEEL_ANGLE;
	private final double ROTATE_SCALE;
	private final double kP;
	private final double kD;

	private Notifier notifier;

	private NavXGyro gyro;
	private Wheel[] wheels;

	private volatile double userVX, userVY, userW, angle;
	private volatile boolean robotCentric;
	private volatile boolean withPID;
	private volatile SwerveData swerveData;

	public Swerve() {
		this.gyro = Robot.GYRO;
		gyro.reset();
		angle = 0.0;
		robotCentric = false;
		withPID = false;
		wheels = new Wheel[4];
		wheels[0] = new Wheel("fr");
		wheels[1] = new Wheel("br");
		wheels[2] = new Wheel("bl");
		wheels[3] = new Wheel("fl");

		SWERVE_WIDTH = Config.getDouble("swerve_width");
		SWERVE_HEIGHT = Config.getDouble("swerve_height");
		kP = Config.getDouble("swerve_kp");
		kD = Config.getDouble("swerve_kd");
		RADIUS = Math.sqrt(SWERVE_WIDTH * SWERVE_WIDTH + SWERVE_HEIGHT * SWERVE_HEIGHT) / 2;
		WHEEL_ANGLE = Math.atan2(SWERVE_WIDTH, SWERVE_HEIGHT);
		ROTATE_SCALE = 1 / RADIUS;
		calcSwerveData();
		notifier = new Notifier(this);
		notifier.startPeriodic(0.02);
	}

	public void run() {
		double w = userW;
		if (w == 0 && withPID)
			w = calcPID();
		changeMotors(userVX, userVY, w);
		calcSwerveData();
		Robot.POS_TRACKER.update();
		SmartDashboard.putNumber("X Position", Robot.POS_TRACKER.getX());
		SmartDashboard.putNumber("Y Position", Robot.POS_TRACKER.getY());
		SmartDashboard.putNumber("Angle", gyro.getAngle());
	}

	private double calcPID() {
		double currentAngle = GRTUtil.positiveMod(Math.toRadians(gyro.getAngle()), TWO_PI);
		double targetAngle = GRTUtil.positiveMod(angle, TWO_PI);
		double error = targetAngle - currentAngle;
		if (Math.abs(error) > Math.PI) {
			error -= Math.signum(error) * TWO_PI;
		}
		double w = error * kP - Math.toRadians(gyro.getRate()) * kD;
		return w;
	}

	public void drive(double vx, double vy, double w) {
		userVX = vx;
		userVY = vy;
		userW = w;
		if (w != 0)
			angle = Math.toRadians(gyro.getAngle());
		withPID = false;
	}

	public void setAngle(double angle) {
		userW = 0;
		withPID = true;
		this.angle = angle;
	}

	public void setRobotCentric(boolean mode) {
		robotCentric = mode;
	}

	public void changeMotors(double vx, double vy, double w) {
		w *= ROTATE_SCALE;
		double gyroAngle = (robotCentric ? 0 : Math.toRadians(gyro.getAngle()));
		for (int i = 0; i < 4; i++) {
			double wheelAngle = getRelativeWheelAngle(i) + gyroAngle;
			double dx = RADIUS * Math.cos(wheelAngle);
			double dy = RADIUS * Math.sin(wheelAngle);
			double wheelVX = vx - dy * w;
			double wheelVY = vy + dx * w;
			double wheelPos = Math.atan2(wheelVY, wheelVX) - gyroAngle;
			double power = Math.sqrt(wheelVX * wheelVX + wheelVY * wheelVY);
			wheels[i].set(wheelPos, power);
		}
	}

	public SwerveData getSwerveData() {
		return swerveData;
	}

	private void calcSwerveData() {
		double gyroAngle = Math.toRadians(gyro.getAngle());
		double gyroRate = Math.toRadians(gyro.getRate());
		double vx = 0;
		double vy = 0;
		double w = 0;
		for (int i = 0; i < 4; i++) {
			double wheelAngle = getRelativeWheelAngle(i);
			double wheelPos = wheels[i].getCurrentPosition();
			double speed = wheels[i].getDriveSpeed();
			w += Math.sin(wheelPos - wheelAngle) * speed / RADIUS;
			wheelPos += gyroAngle;
			vx += Math.cos(wheelPos) * speed;
			vy += Math.sin(wheelPos) * speed;
		}
		w /= 4.0;
		vx /= 4.0;
		vy /= 4.0;
		swerveData = new SwerveData(gyroAngle, gyroRate, vx, vy, w);
	}

	private double getRelativeWheelAngle(int i) {
		double angle = WHEEL_ANGLE;
		if (i == 1 || i == 3) {
			angle *= -1;
		}
		if (i == 1 || i == 2) {
			angle += Math.PI;
		}
		return angle;
	}

}
