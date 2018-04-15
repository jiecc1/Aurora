package com.example.kain.aurora.bean;

import java.io.Serializable;

/**
 * Created by apple on 2018/4/1.
 */

public class TotalTime implements Serializable{
    /**
     * type
     * 1 basic 单波长光线1
     * 2 basic 单波长光线2
     * 3 advance 复合光
     * 4 advance 复合光
     */
    private int type;
    private int time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
