package ru.entel.smiu.protocols.modbus.registers;

/**
 * Created by farades on 24.04.2015.
 */
public abstract class ModbusAbstractRegister {
    protected int regNumb;
    protected Number value;

    public int getRegNumb() {
        return regNumb;
    }

    public void setRegNumb(int regNumb) {
        this.regNumb = regNumb;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
