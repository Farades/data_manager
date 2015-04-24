package ru.entel.smiu.protocols.modbus;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.ModbusIOException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.*;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.util.ModbusUtil;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusIllegalRegTypeException;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusNoResponseException;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusOddQuantityRegException;
import ru.entel.smiu.protocols.modbus.exceptions.ModbusRequestException;
import ru.entel.smiu.protocols.modbus.registers.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by farades on 20.04.2015.
 */
public class ModbusSlaveChannel {
    private String name;
    private ModbusFunction mbFunc;
    private ModbusRegType regType;
    private int offset;
    private int length;
    private SerialConnection con;
    private int slaveID;
    private int timeOut;
    private String exceptionRequest; //Сообщение, показывающий успех последнего запроса по этому каналу
    private Map<Integer, ModbusAbstractRegister> registers;

    public ModbusSlaveChannel(String name, int offset, int length, ModbusFunction mbFunc, ModbusRegType regType, int timeOut) throws ModbusOddQuantityRegException{
        if (regType == ModbusRegType.FLOAT32 && (length % 2 != 0)) {
            throw new ModbusOddQuantityRegException("Odd quantity reg's for FLOAT32 channel: " + name);
        }
        this.name = name;
        this.offset = offset;
        this.length = length;
        this.mbFunc = mbFunc;
        this.regType = regType;
        this.timeOut = timeOut;
        registers = new HashMap<Integer, ModbusAbstractRegister>();
    }

    public Map<Integer, ModbusAbstractRegister> getRegisters() {
        return registers;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public ModbusFunction getMbFunc() {
        return mbFunc;
    }

    public void setSlaveID(int slaveID) {
        this.slaveID = slaveID;
    }

    public String getName() {
        return name;
    }

    public void setCon(SerialConnection con) {
        this.con = con;
    }

    public void setExceptionRequest(String exceptionRequest) {
        this.exceptionRequest = exceptionRequest;
    }

    public synchronized void requset() throws ModbusRequestException, ModbusNoResponseException, ModbusIllegalRegTypeException {
        ModbusRequest req;

        switch (mbFunc) {
            case READ_COIL_REGS_1: {
                req = new ReadCoilsRequest(offset, length);
                break;
            }
            case READ_DISCRETE_INPUT_2: {
                req = new ReadInputDiscretesRequest(offset, length);
                break;
            }
            case READ_HOLDING_REGS_3: {
                req = new ReadMultipleRegistersRequest(offset, length);
                break;
            }
            case READ_INPUT_REGS_4: {
                req = new ReadInputRegistersRequest(offset, length);
                break;
            }
            default:
                throw new IllegalArgumentException("Modbus function incorrect");
        }

        req.setUnitID(this.slaveID);
        req.setHeadless();
        ModbusSerialTransaction trans = new ModbusSerialTransaction(this.con);
        trans.setRequest(req);
        trans.setTransDelayMS(this.timeOut);

        switch (mbFunc) {
            case READ_COIL_REGS_1: {
                if (this.regType != ModbusRegType.BIT) {
                    throw new ModbusIllegalRegTypeException("Illegal reg type for READ_COIL_REGS_1");
                }
                try {
                    trans.execute();
                    ReadCoilsResponse resp = (ReadCoilsResponse) trans.getResponse();
                    for (int i = 0; i < this.length; i++) {
                        ModbusBitRegister reg = new ModbusBitRegister(offset + i, resp.getCoils().getBit(i));
                        registers.put(offset + i, reg);
                    }
                } catch (ModbusIOException ex) {
                    throw new ModbusRequestException(this);
                } catch (ModbusException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case READ_DISCRETE_INPUT_2: {
                if (this.regType != ModbusRegType.BIT) {
                    throw new ModbusIllegalRegTypeException("Illegal reg type for READ_DISCRETE_INPUT_2");
                }
                try {
                    trans.execute();
                    ReadInputDiscretesResponse resp = (ReadInputDiscretesResponse) trans.getResponse();
                    for (int i = 0; i < this.length; i++) {
                        ModbusBitRegister reg = new ModbusBitRegister(offset + i, resp.getDiscretes().getBit(i));
                        registers.put(offset + i, reg);
                    }
                } catch (ModbusIOException ex) {
                    throw new ModbusRequestException(this);
                } catch (ModbusException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case READ_HOLDING_REGS_3: {
                try {
                    trans.execute();
                    ModbusResponse tempResp = trans.getResponse();
                    if (tempResp == null) {
                        throw new ModbusNoResponseException("No response to READ HOLDING request.");
                    }
                    if(tempResp instanceof ExceptionResponse) {
                        ExceptionResponse data = (ExceptionResponse)tempResp;
                        System.out.println(data);
                    } else if(tempResp instanceof ReadMultipleRegistersResponse) {
                        ReadMultipleRegistersResponse resp = (ReadMultipleRegistersResponse)tempResp;
                        Register[] values = resp.getRegisters();

                        if (this.regType == ModbusRegType.INT16) {
                            for (int i = 0; i < values.length; i++) {
                                ModbusInt16Register reg = new ModbusInt16Register(this.offset + i, values[i].getValue());
                                registers.put(this.offset + i, reg);
                            }
                        } else if (this.regType == ModbusRegType.FLOAT32) {
                            for (int i = 0; i < resp.getWordCount() - 1; i+=2) {
                                ModbusFloat32Register reg = new ModbusFloat32Register(offset + i, values[i].getValue(), values[i + 1].getValue());
                                registers.put(this.offset + i, reg);
                            }
                        } else {
                            throw new ModbusIllegalRegTypeException("Illegal reg type for READ_HOLDING_REGS_3");
                        }
                    }
                } catch (ModbusIOException ex) {
                    throw new ModbusRequestException(this);
                } catch (ModbusException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case READ_INPUT_REGS_4: {
                try {
                    trans.execute();
                    ReadInputRegistersResponse resp = (ReadInputRegistersResponse) trans.getResponse();
                    if (this.regType == ModbusRegType.INT16) {
                        for (int n = 0; n < resp.getWordCount(); n++) {
                            ModbusInt16Register reg = new ModbusInt16Register(this.offset + n, resp.getRegisterValue(n));
                            registers.put(offset + n, reg);
                        }
                    } else if (this.regType == ModbusRegType.FLOAT32) {
                        for (int i = 0; i < resp.getWordCount()-1; i+=2) {
                            ModbusFloat32Register reg = new ModbusFloat32Register(offset + i, resp.getRegisterValue(i), resp.getRegisterValue(i+1));
                            registers.put(this.offset + i, reg);
                        }
                    } else {
                        throw new ModbusIllegalRegTypeException("Illegal reg type for READ_INPUT_REGS_4");
                    }
                } catch (ModbusIOException ex) {
                    throw new ModbusRequestException(this);
                } catch (ModbusException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer res = new StringBuffer();
        if (exceptionRequest == null) {
            res.append("[" + this.name + "]");
            res.append(" {");
            int i = 1;
            for (Map.Entry<Integer, ModbusAbstractRegister> entry : registers.entrySet()) {
                res.append(entry.getKey() + "=" + entry.getValue());
                if (i != registers.size()) {
                    res.append(", ");
                }
                i++;
            }
            res.append("}");
        } else {
            res.append("[" + this.name + "] " + this.exceptionRequest);
        }
        return res.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModbusSlaveChannel that = (ModbusSlaveChannel) o;

        if (length != that.length) return false;
        if (offset != that.offset) return false;
        if (mbFunc != that.mbFunc) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mbFunc.hashCode();
        result = 31 * result + offset;
        result = 31 * result + length;
        return result;
    }
}
