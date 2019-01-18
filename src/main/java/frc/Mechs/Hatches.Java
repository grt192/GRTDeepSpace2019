package frc.mechs;

import frc.robot.Mech;

import edu.wpi.first.wpilibj.Solenoid;
import frc.config.Config;
public class Hatches{

    private Solenoid hatch;
    public Hatches(boolean actuated){
        this.hatch = new Solenoid(Config.getInt("hatch"));
    }
}