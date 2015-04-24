package ru.entel.smiu.protocols.modbus.registers;

/**
 * Created by farades on 24.04.2015.
 */
public class ModbusBitRegister extends ModbusAbstractRegister {
    private ModbusRegType regType;

    public ModbusBitRegister(int regNumb, boolean value) {
        this.regType = ModbusRegType.BIT;
        this.regNumb = regNumb;
        this.value = value ? 1 : 0;
    }

    public ModbusRegType getRegType() {
        return regType;
    }
}
