package ru.entel.smiu.protocols.modbus.registers;

import com.ghgande.j2mod.modbus.util.ModbusUtil;

import java.nio.ByteBuffer;

/**
 * Created by farades on 24.04.2015.
 */
public class ModbusFloat32Register extends ModbusAbstractRegister {
    private ModbusRegType regType;
    private Integer tempValue1;
    private Integer tempValue2;

    public ModbusFloat32Register(int regNumb, int value1, int value2) {
        this.regType = ModbusRegType.FLOAT32;
        this.regNumb = regNumb;
        this.tempValue1 = value1;
        this.tempValue2 = value2;
        convertTwoIntToFloat();
    }

    private void convertTwoIntToFloat() {
        byte[] tempValue1Bytes = ByteBuffer.allocate(4).putInt(tempValue1).array();
        byte[] tempValue2Bytes = ByteBuffer.allocate(4).putInt(tempValue2).array();
        byte[] tempFloatValueBytes = new byte[4];
        System.arraycopy(tempValue1Bytes, 2, tempFloatValueBytes, 0, 2);
        System.arraycopy(tempValue2Bytes, 2, tempFloatValueBytes, 2, 2);
        this.value = ModbusUtil.registersToFloat(tempFloatValueBytes);
        System.out.println("");
    }
}
