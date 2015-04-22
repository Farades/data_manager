package ru.entel.smiu.modbus;

/**
 * Created by Артем on 22.04.2015.
 */
public class ModbusNoResponseException extends Exception {
    public ModbusNoResponseException(String msg) {
        super(msg);
    }
}
