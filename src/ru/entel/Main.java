package ru.entel;

import ru.entel.smiu.modbus.ModbusMaster;
import ru.entel.smiu.modbus.ModbusMasterSlave;

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
                mbm.addModbusSlave(new ModbusMasterSlave(1));
                mbm.addModbusSlave(new ModbusMasterSlave(2));
                mbm.addModbusSlave(new ModbusMasterSlave(3));
                new Thread(mbm).start();
            } else {
                System.out.println("Неправильный запрос.");
            }
        }
    }

//    static class RequestFor implements Runnable {
//        volatile boolean running = true;
//        private long counter = 0;
//       // ModbusMaster mbm = new ModbusMaster("COM2", 9600, 8, "none", 1, "rtu", false);
//        @Override
//        public void run() {
//            mbm.openPort();
//            while(running) {
////                System.out.println(mbm.requestRead(3, 0, 10, ModbusFunction.INPUT_REGS));
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                counter++;
//            }
//            System.out.println("Поток " + Thread.currentThread() + " завершен. count = " + counter);
//            mbm.closePort();
//        }
//    }
}

