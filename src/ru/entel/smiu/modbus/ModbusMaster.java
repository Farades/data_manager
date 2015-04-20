package ru.entel.smiu.modbus;

import com.ghgande.j2mod.modbus.ModbusCoupler;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;

import java.util.HashMap;
import java.util.Map;

public class ModbusMaster {
    private SerialConnection con;

    public ModbusMaster(String portName, int baudRate, int dataBits, String parity, int stopbits, String encoding, boolean echo) {
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

    public Map<Integer, Integer> requestRead(int unitId, int offset, int count, ModbusFunction function) {
        int repeat = 1; //a loop for repeating the transaction
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();

        switch (function) {
            case INPUT_REGS:
                ModbusRequest req;
                ReadInputRegistersResponse resp;
                req = new ReadInputRegistersRequest(offset, count);
                req.setUnitID(unitId);
                req.setHeadless();
                ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
                trans.setRequest(req);
                trans.setTransDelayMS(50);
                int k = 0;
                do {
                    try {
                        trans.execute();
                    } catch (ModbusException ex) {
                        ex.printStackTrace();
                    }

                    resp = (ReadInputRegistersResponse) trans.getResponse();
                    for (int n = 0; n < resp.getWordCount(); n++) {
                        result.put(offset+n, resp.getRegisterValue(n));
                    }
                    k++;
                } while (k < repeat);

                break;
            default:
                return null;
        }
        return result;
    }
}
