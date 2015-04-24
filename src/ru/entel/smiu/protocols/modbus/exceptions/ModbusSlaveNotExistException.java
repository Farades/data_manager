package ru.entel.smiu.protocols.modbus.exceptions;

/**
 * Created by farades on 20.04.2015.
 */
public class ModbusSlaveNotExistException extends Exception {
    public ModbusSlaveNotExistException(String msg) {
        super(msg);
    }
}
