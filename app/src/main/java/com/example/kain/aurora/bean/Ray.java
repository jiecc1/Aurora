package com.example.kain.aurora.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by wangs on 2018/3/21.
 */

public class Ray extends DataSupport implements Serializable{

    private Long id;
    /**
     * select
     * 0 false
     * 1 true
     */
    private int select;
    /**
     * type
     * 1 basic 单波长光线1
     * 2 basic 单波长光线2
     * 3 advance 复合光
     * 4 advance 复合光
     */
    private int type;
    private int number;
    private String waveform;
    private int pulse_width;
    /**
     * pulse_width_unit
     * 1 ms
     * 1000 s
     */
    private int pulse_width_unit;
    private Float frequency;
    /**
     * frequency_unit
     * 0 Hz
     */
    private int frequency_unit;
    private int energy;
    /**
     * energy_unit
     * 0 mW
     */
    private int energy_unit;
    private int duration;
    /**
     * duration_unit
     * 1 ms
     * 1000 s
     */
    private int duration_unit;
    private int delay;
    /**
     * delay_unit
     * 1 ms
     * 1000 s
     */
    private int delay_unit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getWaveform() {
        return waveform;
    }

    public void setWaveform(String waveform) {
        this.waveform = waveform;
    }

    public int getPulse_width() {
        return pulse_width;
    }

    public void setPulse_width(int pulse_width) {
        this.pulse_width = pulse_width;
    }

    public int getPulse_width_unit() {
        return pulse_width_unit;
    }

    public void setPulse_width_unit(int pulse_width_unit) {
        this.pulse_width_unit = pulse_width_unit;
    }

    public Float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public int getFrequency_unit() {
        return frequency_unit;
    }

    public void setFrequency_unit(int frequency_unit) {
        this.frequency_unit = frequency_unit;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy_unit() {
        return energy_unit;
    }

    public void setEnergy_unit(int energy_unit) {
        this.energy_unit = energy_unit;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration_unit() {
        return duration_unit;
    }

    public void setDuration_unit(int duration_unit) {
        this.duration_unit = duration_unit;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay_unit() {
        return delay_unit;
    }

    public void setDelay_unit(int delay_unit) {
        this.delay_unit = delay_unit;
    }


}