package frc.mechs;

import frc.robot.Mech;

import edu.wpi.first.wpilibj.Solenoid;
import frc.config.Config;
public class Hatches{

    private Solenoid hatch1;
    private Soldenoid hatch2;

    public Hatches(){
        this.hatch1 = new Solenoid(Config.getInt("hatch1"));
        this.hatch2= new Solenoid(Config.getInt("hatch2"));
    }
    public void setIn(){
        hatch1.set(false);
        hatch2.set(false);
    }
    public void setOut(){
        hatch1.set(true);
        hatch2.set(True;)
    }
}