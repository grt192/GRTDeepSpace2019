/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import frc.config.Config;

/**
 * Add your docs here.
 */
public class Climber {

    private Solenoid railSolenoid;
    private TalonSRX leftRack;
    private TalonSRX rightRack;

    private static final double defaultPower = 0.5;
    private static final int limit = 1000;
    private static final int maxError = 10;

    public Climber() {
        railSolenoid = new Solenoid(Config.getInt("climber_sol"));
        leftRack = new TalonSRX(Config.getInt("left_climber"));
        rightRack = new TalonSRX(Config.getInt("right_climber"));
        configTalon(leftRack);
        configTalon(rightRack);
    }

    public void deployRails() {
        railSolenoid.set(true);
    }

    public void ascend() {
        int leftPos = leftRack.getSelectedSensorPosition();
        int rightPos = rightRack.getSelectedSensorPosition();
        int error = leftPos - rightPos;
        double differential = defaultPower * error / maxError;
        if (leftPos < limit) {
            leftRack.set(ControlMode.PercentOutput, defaultPower - differential);
        } else {
            leftRack.set(ControlMode.PercentOutput, 0);
        }
        if (rightPos < limit) {
            rightRack.set(ControlMode.PercentOutput, defaultPower + differential);
        } else {
            rightRack.set(ControlMode.PercentOutput, 0);
        }
    }

    public boolean isUp() {
        return leftRack.getSelectedSensorPosition() >= limit && rightRack.getSelectedSensorPosition() >= limit;
    }

    public double getPercentUp() {
        return (leftRack.getSelectedSensorPosition() + rightRack.getSelectedSensorPosition()) / (2.0 * limit);
    }

    public void pullUp() {
        if (rightRack.getSelectedSensorPosition() > limit * 0.1) {
            rightRack.set(ControlMode.PercentOutput, -defaultPower);
        } else {
            rightRack.set(ControlMode.PercentOutput, 0);
        }
        if (leftRack.getSelectedSensorPosition() > limit * 0.1) {
            leftRack.set(ControlMode.PercentOutput, -defaultPower);
        } else {
            leftRack.set(ControlMode.PercentOutput, 0);
        }
    }

    private void configTalon(TalonSRX talon) {
        Config.defaultConfigTalon(talon);
        talon.setNeutralMode(NeutralMode.Brake);
        talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        talon.setSelectedSensorPosition(0);
    }
}
