package com.shanejansen.thelodge;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class Device {
    private String mId, mName, mType;
    private int mPin;
    private boolean mIsOn;

    public Device(String id, String name, String type, int pin, String state) {
        mId = id;
        mName = name;
        mType = type;
        mPin = pin;
        mIsOn = state.equals("on");
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public int getPin() {
        return mPin;
    }

    public boolean isOn() {
        return mIsOn;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setType(String type) {
        mType = type;
    }

    public void setPin(int pin) {
        mPin = pin;
    }

    public void setIsOn(boolean isOn) {
        mIsOn = isOn;
    }
}
