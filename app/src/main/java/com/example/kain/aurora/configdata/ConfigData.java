package com.example.kain.aurora.configdata;

import com.example.kain.aurora.bean.Ray;
import com.example.kain.aurora.bean.Trigger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangs on 2018/3/22.
 */

public class ConfigData {

    public static int REFRESH_CYCLE = 40;

    //创建 ConfigData 的一个对象
    private static ConfigData configData = new ConfigData();

    //让构造函数为 private，这样该类就不会被实例化
    private ConfigData() {
    }

    //获取唯一可用的对象
    public static ConfigData getConfigData() {
        return configData;
    }

    public static Ray getDefaultRay() {
        Ray defaultRay = new Ray();
        defaultRay.setId(0l);
        defaultRay.setSelect(0);
        defaultRay.setType(-1);
        defaultRay.setNumber(0);
        defaultRay.setWaveform("null");
        defaultRay.setPulse_width(0);
        defaultRay.setPulse_width_unit(1);
        defaultRay.setFrequency(0f);
        defaultRay.setFrequency_unit(0);
        defaultRay.setEnergy(0);
        defaultRay.setEnergy_unit(0);
        defaultRay.setDuration(0);
        defaultRay.setEnergy_unit(1);
        defaultRay.setDelay(0);
        defaultRay.setDuration_unit(1);
        return defaultRay;
    }

    public static Trigger getDefaultTrigger() {
        Trigger defaultTrigger = new Trigger();
        defaultTrigger.setEnable(false);
        defaultTrigger.setType(0);
        defaultTrigger.setTriggerType(0);
        defaultTrigger.setUpper(0);
        defaultTrigger.setLower(0);
        defaultTrigger.setPretriggerLength(0);
        defaultTrigger.setDelay(0);
        return defaultTrigger;
    }

    public static int analyzeCycleTime(List<Ray> rays, boolean isFirstCycle) {

        int cycleTime = 0;
        int rayTime_ms;

        if (isFirstCycle) {
            for (int i = 0; i < rays.size(); i++) {
                if (i == 0) {
                    rayTime_ms = rays.get(i).getDuration() * rays.get(i).getDuration_unit();
                } else {
                    rayTime_ms = rays.get(i).getDelay() * rays.get(i).getDelay_unit() + rays.get(i).getDuration() * rays.get(i).getDuration_unit();
                }
                cycleTime += rayTime_ms;
            }
            return cycleTime;
        } else {
            for (int i = 0; i < rays.size(); i++) {
                rayTime_ms = rays.get(i).getDelay() * rays.get(i).getDelay_unit() + rays.get(i).getDuration() * rays.get(i).getDuration_unit();
                cycleTime += rayTime_ms;
            }
            return cycleTime;
        }

    }

    public static List<Integer> analyzeCycle(List<Ray> rays, int currentTime) {

        int rayTime_ms;

        List<Integer> currentCycle = new ArrayList<>();
        int cycle;
        int lastCycleTime;
        int rayNumber = 0;
        int lastTime = 0;

        int firstCycleTime = analyzeCycleTime(rays, true);
        int cycleTime = analyzeCycleTime(rays, false);

        if (currentTime >= 0 && currentTime < firstCycleTime) {
            cycle = 1;
            lastCycleTime = currentTime;
            for (int i = 0; i < rays.size(); i++) {
                if (i == 0) {
                    rayTime_ms = rays.get(i).getDuration() * rays.get(i).getDuration_unit();
                } else {
                    rayTime_ms = rays.get(i).getDelay() * rays.get(i).getDelay_unit() + rays.get(i).getDuration() * rays.get(i).getDuration_unit();
                }
                if (lastCycleTime - rayTime_ms <= 0) {
                    rayNumber = i;
                    lastTime = lastCycleTime;
                } else {
                    lastCycleTime -= rayTime_ms;
                }
            }
        } else {
            cycle = (currentTime - firstCycleTime) / cycleTime + 1;
            lastCycleTime = (currentTime - firstCycleTime) % cycleTime;
            for (int i = 0; i < rays.size(); i++) {
                rayTime_ms = rays.get(i).getDelay() * rays.get(i).getDelay_unit() + rays.get(i).getDuration() * rays.get(i).getDuration_unit();
                if (lastCycleTime - rayTime_ms <= 0) {
                    rayNumber = i;
                    lastTime = lastCycleTime;
                } else {
                    lastCycleTime -= rayTime_ms;
                }
            }

        }
        currentCycle.add(cycle);
        currentCycle.add(rayNumber);
        currentCycle.add(lastTime);
        return currentCycle;
    }

    public static int analyzeRays2(List<Ray> rays, int currentTime) {

        List<Integer> currentCycle = analyzeCycle(rays,currentTime);

        int cycle = currentCycle.get(0);
        int rayNumber = currentCycle.get(1);
        int lastTime = currentCycle.get(2);
        int result;

        int pulseWidth_ms;
        int delayTime_ms;
        int durationTime_ms;
        int durationTime_s;
        int pulseCycleTime_ms;

        pulseWidth_ms = rays.get(rayNumber).getPulse_width() * rays.get(rayNumber).getPulse_width_unit();
        delayTime_ms = rays.get(rayNumber).getDelay() * rays.get(rayNumber).getDelay_unit();
        durationTime_ms = rays.get(rayNumber).getDuration() * rays.get(rayNumber).getDuration_unit();
        durationTime_s = durationTime_ms / 1000;
        pulseCycleTime_ms = (int) (1 / rays.get(rayNumber).getFrequency() * 1000);

        if (cycle == 1 && rayNumber == 0) {
            lastTime = lastTime % pulseCycleTime_ms;
            if (lastTime - pulseWidth_ms <= 0) {
                result = rays.get(rayNumber).getEnergy();
            } else {
                result = 0;
            }
        } else {
            if (lastTime >= 0 && lastTime <= delayTime_ms) {
                result = 0;
            } else {
                lastTime -= delayTime_ms;
                lastTime = lastTime % pulseCycleTime_ms;
                if (lastTime - pulseWidth_ms <= 0) {
                    result = rays.get(rayNumber).getEnergy();
                } else {
                    result = 0;
                }
            }
        }
        return result;
    }

}
