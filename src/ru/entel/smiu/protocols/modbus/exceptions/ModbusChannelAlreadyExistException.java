package ru.entel.smiu.protocols.modbus.exceptions;

/**
 * Created by Farades on 22.04.2015.
 * ModbusChannelAlreadyExistException - исключение, возникающее при попытке добавить уже сушествующий канал в ModbusMasterSlave
 */
public class ModbusChannelAlreadyExistException extends Exception {
    public ModbusChannelAlreadyExistException(String msg) {
        super(msg);
    }
}
