package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.config.Config;

public class TopIntake {

    private TalonSRX intakeMotor2;

    // start uneccessary numbers here bc i am an idiot and looked them up
    // private final Double LEVEL_ONE = 2.7917;
    // private final Double LEVEL_TWO = 5.125;
    // private final Double LEVEL_THREE = 7.4583;
    // private final Double CARGO_BAY = 3.3125;

    public TopIntake() {
        this.intakeMotor2 = new TalonSRX(Config.getInt("top-intake-motor"));
    }

    public void setPower(Double power) {
        intakeMotor2.set(ControlMode.PercentOutput, power);
    }
}
