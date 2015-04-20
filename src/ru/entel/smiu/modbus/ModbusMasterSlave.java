package ru.entel.smiu.modbus;

import com.ghgande.j2mod.modbus.net.SerialConnection;

/**
 * Created by farades on 08.04.2015.
 */
public class ModbusMasterSlave  {
    private SerialConnection con;
    private int address;

    public ModbusMasterSlave(int address) {
        this.address = address;
    }

    public void setCon(SerialConnection con) {
        this.con = con;
    }

    public void launch() {
        System.out.println(this.address + " launch.");
    }
}
