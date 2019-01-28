/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.positiontracking;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.video.KalmanFilter;

import frc.robot.Robot;
import frc.swerve.SwerveData;

/**
 * Add your docs here.
 */
public class KalmanFilterPositionTracker implements PositionTracker {

    private static final int TYPE = CvType.CV_64F;
    private static final int STATES = 2;

    private static final double INITIAL_VARIANCE = 0.5;
    private static final double PROCESS_NOISE = 0.2;
    private static final double MEASUREMENT_NOISE = 1.0;

    private long lastUpdate;
    private KalmanFilter kf;

    public KalmanFilterPositionTracker() {
        kf = new KalmanFilter(STATES, STATES, STATES, TYPE);
        kf.set_transitionMatrix(Mat.eye(STATES, STATES, TYPE));
        kf.set_measurementMatrix(Mat.eye(STATES, STATES, TYPE));
        Mat R = new Mat(STATES, STATES, TYPE);
        R.put(0, 0, PROCESS_NOISE, PROCESS_NOISE, PROCESS_NOISE, PROCESS_NOISE);
        kf.set_processNoiseCov(R);
        Mat Q = new Mat(STATES, STATES, TYPE);
        Q.put(0, 0, MEASUREMENT_NOISE, 0, 0, MEASUREMENT_NOISE);
        kf.set_measurementNoiseCov(Q);
    }

    @Override
    public void set(double x, double y) {
        Mat state = new Mat(STATES, 1, TYPE);
        state.put(0, 0, x, y);
        kf.set_statePre(state);
        kf.set_statePost(state);
        Mat error = new Mat(STATES, STATES, TYPE);
        error.put(0, 0, INITIAL_VARIANCE, 0, 0, INITIAL_VARIANCE);
        kf.set_errorCovPre(error);
        kf.set_errorCovPost(error);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public double getX() {
        return kf.get_statePost().get(0, 0)[0];
    }

    @Override
    public double getY() {
        return kf.get_statePost().get(1, 0)[0];
    }

    @Override
    public void update() {
        SwerveData data = Robot.SWERVE.getSwerveData();
        long temp = System.currentTimeMillis();
        long ticks = (temp - lastUpdate);
        double dt = ticks / 1000.0;
        kf.get_controlMatrix().put(0, 0, dt, 0, 0, dt);
        lastUpdate = temp;
        Mat U = new Mat(STATES, 1, TYPE);
        U.put(0, 0, data.encoderVX, data.encoderVY);
        kf.predict(U);
        Position estimate = Robot.HATCH_JEVOIS.getPositionEstimate(ticks);
        if (estimate != null) {
            Mat Z = new Mat(STATES, 1, TYPE);
            Z.put(0, 0, estimate.pos.x, estimate.pos.y);
            kf.correct(Z);
        }
    }
}
