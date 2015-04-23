package ru.entel.smiu.protocols.modbus;

/*
 * Created by Farades on 22.04.2015.
 */

import com.ghgande.j2mod.modbus.ModbusCoupler;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import java.util.HashSet;

public class ModbusMaster implements Runnable {
    private SerialConnection con;
    private HashSet<ModbusMasterSlave> slaves = new HashSet<ModbusMasterSlave>();
    private int timePause;
    public volatile boolean running = true;

    public ModbusMaster(String portName, int baudRate, int dataBits, String parity, int stopbits, String encoding, boolean echo, int timePause) {
        this.timePause = timePause;
        ModbusCoupler.getReference().setUnitID(128);
        SerialParameters params = new SerialParameters();
        params.setPortName(portName);
        params.setBaudRate(baudRate);
        params.setDatabits(dataBits);
        params.setParity(parity);
        params.setStopbits(stopbits);
        params.setEncoding(encoding);
        params.setEcho(echo);
        con = new SerialConnection(params);
    }

    public void openPort() {
        try {
            con.open();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void closePort() {
        con.close();
    }

    public void addModbusSlave(ModbusMasterSlave slave) {
        slave.setCon(this.con);
        slaves.add(slave);
    }

    @Override
    public void run() {
        if (slaves.size() != 0) {
            openPort();
            while(running) {
                for (ModbusMasterSlave slave : slaves) {
                    slave.launch(this.con);
                    try {
                        Thread.sleep(timePause);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Поток Modbus завершен.");
        closePort();
    }
}
