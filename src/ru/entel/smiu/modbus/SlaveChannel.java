package ru.entel.smiu.modbus;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.net.SerialConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by farades on 20.04.2015.
 */
public class SlaveChannel {
    private String name;
    private ModbusFunction mbFunc;
    private int offset;
    private int length;
    private SerialConnection con;
    private int slaveID;
    private int timeOut;
    private Map<Integer, Integer> registers;

    public SlaveChannel(String name, int offset, int length, ModbusFunction mbFunc, SerialConnection con, int timeOut) {
        this.name = name;
        this.offset = offset;
        this.length = length;
        this.mbFunc = mbFunc;
        this.con = con;
        this.timeOut = timeOut;
        registers = new HashMap<Integer, Integer>();
    }

    public void setSlaveID(int slaveID) {
        this.slaveID = slaveID;
    }

    public synchronized void requset() {
        int repeat = 1; //a loop for repeating the transaction

        switch (mbFunc) {
            case INPUT_REGS:
                ModbusRequest req;
                ReadInputRegistersResponse resp;
                req = new ReadInputRegistersRequest(offset, length);
                req.setUnitID(this.slaveID);
                req.setHeadless();
                ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
                trans.setRequest(req);
                trans.setTransDelayMS(this.timeOut);
                int k = 0;
                do {
                    try {
                        trans.execute();
                    } catch (ModbusException ex) {
                        ex.printStackTrace();
                    }
                    resp = (ReadInputRegistersResponse) trans.getResponse();
                    for (int n = 0; n < resp.getWordCount(); n++) {
                        registers.put(offset + n, resp.getRegisterValue(n));
                    }
                    k++;
                } while (k < repeat);

                break;
            default:
                throw new IllegalArgumentException("Modbus function incorrect");
        }
    }

    @Override
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append("[Channel: " + this.name + "]");
        res.append(" {");
        int i = 1;
        for (Map.Entry<Integer, Integer> entry : registers.entrySet()) {
            res.append(entry.getKey() + "=" + entry.getValue());
            if (i != registers.size()) {
                res.append(", ");
            }
            i++;
        }
        res.append("}");
        return res.toString();
    }
}
