package frc.swerve;

import edu.wpi.first.wpilibj.Notifier;
import frc.config.Config;
import frc.robot.Robot;
import frc.util.GRTUtil;

public class Swerve implements Runnable {

	private static final double TWO_PI = Math.PI * 2;

	private final double SWERVE_WIDTH;
	private final double SWERVE_HEIGHT;
	private final double RADIUS;
	private final double WHEEL_ANGLE;
	private final double kP;
	private final double kD;

	private Notifier notifier;

	private NavXGyro gyro;
	private Wheel[] wheels;

	private volatile double userVX, userVY, userW, angle;

	public Swerve() {
		this.gyro = Robot.GYRO;
		gyro.reset();
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
	}

	public void run() {
		double w = userW;
		if (w == 0) {
			double currentAngle = GRTUtil.positiveMod(Math.toRadians(gyro.getAngle()), TWO_PI);
			double targetAngle = GRTUtil.positiveMod(angle, TWO_PI);
			double error = targetAngle - currentAngle;
			if (error > Math.PI) {
				error -= Math.PI;
			}

		}
	}

	public void drive(double vx, double vy, double w) {
		userVX = vx;
		userVY = vy;
		userW = w;
		if (w != 0)
			angle = gyro.getAngle();
	}

	public void setAngle(double angle) {
		userW = 0;
		this.angle = angle;
	}

	public void changeMotors(double vx, double vy, double w) {
		double gyroAngle = Math.toRadians(gyro.getAngle());
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
		return new SwerveData(gyroAngle, gyroRate, vx, vy, w);
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
