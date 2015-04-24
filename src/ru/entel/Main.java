package ru.entel;

import ru.entel.smiu.protocols.modbus.*;
import ru.entel.smiu.protocols.modbus.registers.ModbusRegType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String temp = "";
        ModbusMaster mbm = null;
        while(true) {
            try {
                temp = br.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (temp.equals("x")) {
                if (mbm == null) continue;
                mbm.running = false;
            }
            else if (temp.equals("go")) {
                mbm = new ModbusMaster("COM2", 19200, 8, "none", 1, "rtu", false, 500);

                ModbusMasterSlave slave_1 = new ModbusMasterSlave(1, "V1");
                try {
                    slave_1.addChannel(new ModbusSlaveChannel("INPUT", 21, 10, ModbusFunction.READ_INPUT_REGS_4, ModbusRegType.INT16, 50));
                    slave_1.addChannel(new ModbusSlaveChannel("Holding", 1, 10, ModbusFunction.READ_HOLDING_REGS_3 ,ModbusRegType.INT16, 50));
                } catch (ModbusChannelAlreadyExist ex) {
                    System.out.println("Repeated slave channel.");
                }
                mbm.addModbusSlave(slave_1);

                ModbusMasterSlave slave_2 = new ModbusMasterSlave(5, "Amp");
                try {
                    slave_2.addChannel(new ModbusSlaveChannel("COIL", 1, 10, ModbusFunction.READ_COIL_REGS_1, ModbusRegType.INT16, 50));
                    slave_2.addChannel(new ModbusSlaveChannel("DISCRETE", 1, 17, ModbusFunction.READ_DISCRETE_INPUT_2, ModbusRegType.BIT, 50));
                } catch (ModbusChannelAlreadyExist ex) {
                    System.out.println("Repeated slave channel.");
                }
                mbm.addModbusSlave(slave_2);

                new Thread(mbm).start();
            } else {
                System.out.println("Неправильный запрос.");
            }
        }
    }
}

