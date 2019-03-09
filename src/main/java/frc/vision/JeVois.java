package frc.vision;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.wpi.first.hal.util.UncleanStatusException;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class JeVois extends Thread {

    private boolean enabled = true;

    private String name;
    private BufferedReader camera;
    private SerialPort serialPort;

    public String lastString;
    public volatile JeVoisMessage lastMessage = null;
    public volatile long lastReceivedTimestamp;

    public JeVois(String device) {
        this.name = device;
        try {
            camera = new BufferedReader(new FileReader(device));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public JeVois(String name, Port port) {
        this.name = name;
        try {
            serialPort = new SerialPort(115200, port);
        } catch (UncleanStatusException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (camera == null && serialPort == null)
            return;
        while (true) {
            if (this.enabled) {
                try {
                    String line = readLine();
                    if (line == null)
                        continue;
                    // if (line.split(" ").length != 8) {
                    // continue;
                    // }
                    this.lastString = line;
                    if (!lastString.equals("")) {
                        System.out.println(line);
                        lastReceivedTimestamp = System.currentTimeMillis();
                        try {
                            this.lastMessage = new JeVoisMessage(lastString, lastReceivedTimestamp);
                        } catch (NumberFormatException e) {
                            System.out.println(name + ": " + line);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String readLine() throws IOException {
        if (camera != null)
            return camera.readLine();
        String body = serialPort.readString().trim();
        String[] lines = body.split("\n");
        if (lines.length == 0) {
            return null;
        }
        String line = lines[lines.length - 1];
        return line;
    }

    public JeVoisMessage getLastMessage() {
        return lastMessage;
    }

    public long getLastReceivedTimestamp() {
        return lastReceivedTimestamp;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

}