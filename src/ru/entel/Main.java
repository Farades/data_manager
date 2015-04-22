package ru.entel;

import ru.entel.smiu.modbus.ModbusFunction;
import ru.entel.smiu.modbus.ModbusMaster;
import ru.entel.smiu.modbus.ModbusMasterSlave;
import ru.entel.smiu.modbus.SlaveChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String temp;
        ModbusMaster mbm = null;
        while(true) {
            temp = br.readLine();
            if (temp.equals("x")) {
                if (mbm == null) continue;
                mbm.running = false;
            }
            else if (temp.equals("go")) {
                mbm = new ModbusMaster("COM2", 9600, 8, "none", 1, "rtu", false, 500);

                ModbusMasterSlave slave_1 = new ModbusMasterSlave(1, "V1");
                slave_1.addChannel(new SlaveChannel("U_linear", 1, 10, ModbusFunction.INPUT_REGS, mbm.getCon(), 50));
                slave_1.addChannel(new SlaveChannel("U_phase", 21, 10, ModbusFunction.INPUT_REGS, mbm.getCon(), 50));
                mbm.addModbusSlave(slave_1);

                ModbusMasterSlave slave_2 = new ModbusMasterSlave(5, "Amp");
                slave_2.addChannel(new SlaveChannel("I_phase", 1, 10, ModbusFunction.INPUT_REGS, mbm.getCon(), 50));
                slave_2.addChannel(new SlaveChannel("F", 21, 2, ModbusFunction.INPUT_REGS, mbm.getCon(), 50));
                mbm.addModbusSlave(slave_2);

                new Thread(mbm).start();
            } else {
                System.out.println("Неправильный запрос.");
            }
        }
    }
}

