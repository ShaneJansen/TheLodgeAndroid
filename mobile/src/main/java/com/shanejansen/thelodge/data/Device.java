package com.shanejansen.thelodge.data;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class Device {
    private int mPin;
    private String mName, mType;
    private boolean mIsOn;

    public Device(int pin, String name, String type, String state) {
        mPin = pin;
        mName = name;
        mType = type;
        mIsOn = state.equals("on");
    }

    public int getPin() {
        return mPin;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public boolean isOn() {
        return mIsOn;
    }

    public void setPin(int pin) {
        mPin = pin;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setType(String type) {
        mType = type;
    }

    public void setIsOn(boolean isOn) {
        mIsOn = isOn;
    }
}
