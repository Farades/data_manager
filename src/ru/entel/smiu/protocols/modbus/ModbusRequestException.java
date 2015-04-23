package ru.entel.smiu.protocols.modbus;

/**
 * Created by Артем on 22.04.2015.
 */
public class ModbusRequestException extends Exception {
    private ModbusSlaveChannel channel;

    public ModbusRequestException(ModbusSlaveChannel channel) {
        this.channel = channel;
    }

    public ModbusSlaveChannel getChannel() {
        return channel;
    }
}
