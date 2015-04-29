package ru.entel;

import ru.entel.smiu.protocols.modbus.*;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusChannelAlreadyExistException;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusOddQuantityRegException;
import ru.entel.smiu.protocols.modbus.registers.ModbusRegType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String temp = "";
        ModbusMaster mbm = null;
        String comName = null;
        if (args.length == 0) {
            System.out.println("Введите название COM-порта");
            System.exit(1);
        } else {
            comName = args[0];
        }
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
                mbm = new ModbusMaster(comName, 19200, 8, "none", 1, "rtu", false, 100);

                ModbusMasterSlave slave_1 = new ModbusMasterSlave(1, "V1");
                try {
                    slave_1.addChannel(new ModbusSlaveChannel("DIP Switch", 0, 6, ModbusFunction.READ_COIL_REGS_1, ModbusRegType.BIT, 50));
                    slave_1.addChannel(new ModbusSlaveChannel("Potentiometr's", 1, 3, ModbusFunction.READ_HOLDING_REGS_3 ,ModbusRegType.INT16, 50));
                } catch (ModbusChannelAlreadyExistException ex) {
                    System.out.println(ex.getMessage());
                    System.exit(1);
                } catch (ModbusOddQuantityRegException ex) {
                    System.out.println(ex.getMessage());
                    System.exit(1);
                }
                mbm.addModbusSlave(slave_1);

                new Thread(mbm).start();
            } else {
                System.out.println("Неправильный запрос.");
            }
        }
    }
}

