package com.example.kain.aurora.bean;

import java.io.Serializable;

/**
 * Created by wangs on 2018/3/27.
 */

public class Trigger implements Serializable{
    private boolean enable;
    /**
     * type
     * 1 basic 单波长光源1
     * 2 basic 单波长光源2
     * 3 advice 复合光源
     */
    private int type;
    /**
     * triggerType
     * 1 single trigger
     * 2 continuous trigger
     */
    private int triggerType;
    private int upper;
    private int lower;
    private int pretriggerLength;
    private int delay;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getPretriggerLength() {
        return pretriggerLength;
    }

    public void setPretriggerLength(int pretriggerLength) {
        this.pretriggerLength = pretriggerLength;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
