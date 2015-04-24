package ru.entel.smiu.protocols.modbus;

import com.ghgande.j2mod.modbus.net.SerialConnection;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusChannelAlreadyExistException;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusIllegalRegTypeException;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusNoResponseException;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusRequestException;
import ru.entel.smiu.protocols.modbus.registers.ModbusAbstractRegister;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by farades on 08.04.2015.
 */
public class ModbusMasterSlave  {
    private String name;
    private SerialConnection con;
    private int address;
    private HashSet<ModbusSlaveChannel> channels = new HashSet<ModbusSlaveChannel>();
    private HashMap<Integer, ModbusAbstractRegister> registersAllChannel = new HashMap<Integer, ModbusAbstractRegister>();

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
                channel.setExceptionRequest(null);
                registersAllChannel.putAll(channel.getRegisters());
            } catch (ModbusRequestException ex) {
                channel.setExceptionRequest(ex.getMessage());
            } catch (ModbusNoResponseException ex) {
                channel.setExceptionRequest(ex.getMessage());
            } catch (ModbusIllegalRegTypeException ex) {
                channel.setExceptionRequest(ex.getMessage());
            }
        }
        System.out.println(this);
    }

    public void addChannel(ModbusSlaveChannel channel) throws ModbusChannelAlreadyExistException {
        if (channels.contains(channel)) {
            throw new ModbusChannelAlreadyExistException("Channel with function: " + channel.getMbFunc() +
                    "; offset: " + channel.getOffset() +  "; length: " + channel.getLength() + " already exist.");
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
