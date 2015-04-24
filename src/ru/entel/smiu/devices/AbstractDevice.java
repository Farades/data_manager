package ru.entel.smiu.devices;

import ru.entel.smiu.protocols.modbus.registers.ModbusAbstractRegister;

import java.util.HashMap;

/**
 * Created by farades on 24.04.2015.
 */
public abstract class AbstractDevice implements DeviceObserver {
    private HashMap<String, ModbusAbstractRegister> values;
    private HashMap<String, Integer> regNumbers;

    @Override
    public void updateValues() {

    }
}
