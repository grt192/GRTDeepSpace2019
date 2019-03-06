package frc.vision;

public class JeVoisMessage {

    public final String originalLine;

    public final long receivedTime; // millis
    public final double cameraTime; // seconds since camera startup
    public final boolean retval;

    public final double translateX; // inches
    public final double translateY;
    public final double translateZ;

    public final double rotateX; // radians
    public final double rotateY;
    public final double rotateZ;

    public JeVoisMessage(String line, Long receivedTime) throws NumberFormatException {
        this.originalLine = line;
        this.receivedTime = receivedTime;

        String[] split = line.split(" ");

        this.cameraTime = Double.parseDouble(split[0]);

        this.retval = Boolean.parseBoolean(split[1]);

        this.translateX = Double.parseDouble(split[2]);
        this.translateY = Double.parseDouble(split[3]);
        this.translateZ = Double.parseDouble(split[4]);

        this.rotateX = Double.parseDouble(split[5]);
        this.rotateY = Double.parseDouble(split[6]);
        this.rotateZ = Double.parseDouble(split[7]);
    }

    public String toString() {
        return originalLine;
    }

}