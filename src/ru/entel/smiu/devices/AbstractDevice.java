package ru.entel.smiu.devices;

import java.util.HashMap;

/**
 * Created by farades on 24.04.2015.
 */
public abstract class AbstractDevice implements DeviceObserver {
    private HashMap<String, Integer> values;
    private HashMap<String, Integer> regNumbers;

    @Override
    public void updateValues() {

    }
}
