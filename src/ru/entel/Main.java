package ru.entel;

import ru.entel.smiu.modbus.*;

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
                mbm = new ModbusMaster("COM3", 9600, 8, "none", 1, "rtu", false, 500);

                ModbusMasterSlave slave_1 = new ModbusMasterSlave(1, "V1");
                try {
                    slave_1.addChannel(new SlaveChannel("INPUT", 1, 10, ModbusFunction.READ_INPUT_REGS_4, mbm.getCon(), 50));
                    slave_1.addChannel(new SlaveChannel("Holding", 1, 10, ModbusFunction.READ_HOLDING_REGS_3, mbm.getCon(), 50));
                } catch (ModbusChannelAlreadyExist ex) {
                    System.out.println("Repeated slave channel.");
                }
                mbm.addModbusSlave(slave_1);

                ModbusMasterSlave slave_2 = new ModbusMasterSlave(5, "Amp");
                try {
                    slave_2.addChannel(new SlaveChannel("COIL", 1, 10, ModbusFunction.READ_COIL_REGS_1, mbm.getCon(), 50));
                    slave_2.addChannel(new SlaveChannel("DISCRETE", 1, 17, ModbusFunction.READ_DISCRETE_INPUT_2, mbm.getCon(), 50));
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

