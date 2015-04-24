package ru.entel.smiu.protocols.modbus.registers;

/**
 * Created by farades on 24.04.2015.
 */
public class ModbusFloat32Register extends ModbusAbstractRegister {
    private ModbusRegType regType;
    private int tempValue1;
    private int tempValue2;

    public ModbusFloat32Register(int regNumb, int value1, int value2) {
        this.regType = ModbusRegType.FLOAT32;
        this.regNumb = regNumb;
        this.tempValue1 = value1;
        this.tempValue2 = value2;
        convertTwoIntToFloat();
    }

    private void convertTwoIntToFloat() {
        this.value = 235.5f;
    }
}
