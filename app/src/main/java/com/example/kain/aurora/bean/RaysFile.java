package com.example.kain.aurora.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangs on 2018/4/3.
 */

public class RaysFile extends DataSupport implements Serializable {
    private String name;
    private List<Ray> rays;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ray> getRays() {
        return rays;
    }

    public void setRays(List<Ray> rays) {
        this.rays = rays;
    }
}
