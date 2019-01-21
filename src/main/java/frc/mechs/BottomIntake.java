package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import frc.config.Config;

public class BottomIntake {

    private Solenoid intake1;
    private Solenoid intake2;
    private TalonSRX intakeMotor1;

    public BottomIntake() {
        this.intake1 = new Solenoid(Config.getInt("roller_sol"));
        this.intakeMotor1 = new TalonSRX(Config.getInt("roller"));
    }

    public void setPower(double power) {
        intakeMotor1.set(ControlMode.PercentOutput, power);
    }

    public void in() {
        setPower(0.0);
        intake1.set(false);
        intake2.set(false);

    }

    public void out() {
        intake1.set(true);
        intake2.set(true);
    }
}