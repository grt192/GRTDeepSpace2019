package frc.vision;

public class JeVoisMessage {

    public String originalLine;

    public Long receivedTime; // millis
    public Double cameraTime; // seconds since camera startup

    public Double translateX; // inches
    public Double translateY;
    public Double translateZ;

    public Double rotateX; // radians
    public Double rotateY;
    public Double rotateZ;

    public JeVoisMessage(String line, Long receivedTime) throws NumberFormatException {
        this.originalLine = line;
        this.receivedTime = receivedTime;

        String[] split = line.split(" ");

        this.cameraTime = Double.valueOf(split[0]);

        // this.retval = Double.valueOf(split[1]);

        this.translateX = Double.valueOf(split[2]);
        this.translateY = Double.valueOf(split[3]);
        this.translateZ = Double.valueOf(split[4]);

        this.rotateX = Double.valueOf(split[5]);
        this.rotateY = Double.valueOf(split[6]);
        this.rotateZ = Double.valueOf(split[7]);
    }

    public String toString() {
        return originalLine;
    }

}