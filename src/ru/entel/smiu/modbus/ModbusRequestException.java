package ru.entel.smiu.modbus;

/**
 * Created by Артем on 22.04.2015.
 */
public class ModbusRequestException extends Exception {
    private SlaveChannel channel;

    public ModbusRequestException(SlaveChannel channel) {
        this.channel = channel;
    }

    public SlaveChannel getChannel() {
        return channel;
    }
}
