package ru.entel;

import ru.entel.smiu.modbus.ModbusFunction;
import ru.entel.smiu.modbus.ModbusMaster;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String temp;
        RequestFor requestRun = null;
        Thread requestThread;
        while(true) {
            temp = br.readLine();
            if (temp.equals("x")) {
                requestRun.running = false;
            }
            else if (temp.equals("go")) {
                requestRun = new RequestFor();
                requestThread = new Thread(requestRun);
                requestThread.start();
            }
        }
    }

    static class RequestFor implements Runnable {
        volatile boolean running = true;
        private long counter = 0;
        ModbusMaster mbm = new ModbusMaster("COM3", 9600, 8, "none", 1, "rtu", false);
        @Override
        public void run() {
            mbm.openPort();
            while(running) {
                System.out.println(mbm.requestRead(3, 0, 10, ModbusFunction.INPUT_REGS));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                counter++;
            }
            System.out.println("Поток " + Thread.currentThread() + " завершен. count = " + counter);
            mbm.closePort();
        }
    }
}

