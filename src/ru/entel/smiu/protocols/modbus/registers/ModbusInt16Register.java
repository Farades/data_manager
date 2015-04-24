package ru.entel.smiu.protocols.modbus.registers;

/**
 * Created by farades on 24.04.2015.
 */
public class ModbusInt16Register extends ModbusAbstractRegister {
    private ModbusRegType regType;

    public ModbusInt16Register(int regNumb, int value) {
        this.regNumb = regNumb;
        this.regType = ModbusRegType.INT16;
        this.value = value;
    }

    public ModbusRegType getRegType() {
        return regType;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
