package ru.entel.smiu.protocols.modbus;

import com.ghgande.j2mod.modbus.net.SerialConnection;

import java.util.HashSet;

/**
 * Created by farades on 08.04.2015.
 */
public class ModbusMasterSlave  {
    private String name;
    private SerialConnection con;
    private int address;
    private HashSet<ModbusSlaveChannel> channels = new HashSet<ModbusSlaveChannel>();

    public ModbusMasterSlave(int address, String name) {
        this.address = address;
        this.name = name;
    }

    public void setCon(SerialConnection con) {
        this.con = con;
    }

    public synchronized void launch(SerialConnection con) {
        this.con = con;
        for (ModbusSlaveChannel channel : channels) {
            try {
                channel.setCon(con);
                channel.requset();
                channel.setSuccessRead(true);
            } catch (ModbusRequestException ex) {
                channel.setSuccessRead(false);
            } catch (ModbusNoResponseException ex) {
                channel.setSuccessRead(false);
            }
        }
        System.out.println(this);
    }

    public void addChannel(ModbusSlaveChannel channel) throws ModbusChannelAlreadyExist{
        if (channels.contains(channel)) {
            throw new ModbusChannelAlreadyExist();
        }
        channel.setSlaveID(this.address);
        channels.add(channel);
    }

    @Override
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append("[" + this.name + "(" + this.address + ")" + "]\n");
        int i = 1;
        for (ModbusSlaveChannel channel : channels) {
            res.append("\t");
            res.append(channel.toString());
            if (i != channels.size()) {
                res.append("\n");
            }
            i++;
        }
        return res.toString();
    }
}
