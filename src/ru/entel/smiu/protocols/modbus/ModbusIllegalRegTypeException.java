package ru.entel.smiu.protocols.modbus;

/**
 * Created by farades on 24.04.2015.
 */
public class ModbusIllegalRegTypeException extends Exception {
    public ModbusIllegalRegTypeException(String msg) {
        super(msg);
    }
}
