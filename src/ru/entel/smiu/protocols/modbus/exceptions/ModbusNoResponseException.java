package ru.entel.smiu.protocols.modbus.exceptions;

/**
 * Created by Farades on 22.04.2015.
 *
 */
public class ModbusNoResponseException extends Exception {
    public ModbusNoResponseException(String msg) {
        super(msg);
    }
}
