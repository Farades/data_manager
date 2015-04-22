package ru.entel.smiu.modbus;

import com.ghgande.j2mod.modbus.net.SerialConnection;

import java.util.HashSet;

/**
 * Created by farades on 08.04.2015.
 */
public class ModbusMasterSlave  {
    private String name;
    private SerialConnection con;
    private int address;
    private HashSet<SlaveChannel> channels = new HashSet<SlaveChannel>();

    public ModbusMasterSlave(int address, String name) {
        this.address = address;
        this.name = name;
    }

    public void setCon(SerialConnection con) {
        this.con = con;
    }

    public void launch() {
        for (SlaveChannel channel : channels) {
            try {
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

    public void addChannel(SlaveChannel channel) throws ModbusChannelAlreadyExist{
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
        for (SlaveChannel channel : channels) {
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
