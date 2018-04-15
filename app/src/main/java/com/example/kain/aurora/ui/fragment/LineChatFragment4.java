package com.example.kain.aurora.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kain.aurora.R;
import com.example.kain.aurora.base.BaseFragment;
import com.example.kain.aurora.bean.Ray;
import com.example.kain.aurora.bean.TotalTime;
import com.example.kain.aurora.chart.LineChartView;
import com.example.kain.aurora.configdata.ConfigData;
import com.example.kain.aurora.utils.ACache;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LineChatFragment4 extends BaseFragment {

    private DataThread dataThread;
    private LineChartView lineChartView;
    private View view;

    private List<Float> y = new ArrayList<>();
    private List<Ray> rays = new ArrayList<>();
    private int time;
    private int result;
    private int i = 0;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("stop03") && dataThread != null) {
                dataThread.setRun(false);
                new Thread(dataThread).interrupt();
                dataThread = null;
                y.clear();
                lineChartView.setAxis(0, y);
                lineChartView.invalidate();
            } else if (action.equals("start03")) {
                rays = findSelectRays(4);
                TotalTime totalTime = (TotalTime) ACache.get(getActivity()).getAsObject("totalTime4");
                if (totalTime != null) {
                    time = totalTime.getTime();
                }
                if (dataThread == null && rays != null && rays.size() > 0) {
                    dataThread = new DataThread();
                    new Thread(dataThread).start();
                }
            } else if (action.equals("pause03")) {
                dataThread.setRun(false);
            } else if (action.equals("restart03")) {
                dataThread.setRun(true);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_line_chat, container, false);

        intiData();
        return view;
    }

    public void intiData() {
        lineChartView = view.findViewById(R.id.chartView);
        rays = findSelectRays(4);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("stop03");
        intentFilter.addAction("start03");
        intentFilter.addAction("pause03");
        intentFilter.addAction("restart03");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);

    }

    class DataThread implements Runnable {
        private volatile boolean run = true;

        public void setRun(boolean run) {
            this.run = run;
        }

        @Override
        public void run() {
            for (i = 0; i < time * 1000 / ConfigData.REFRESH_CYCLE; i++) {
                if (run) {
                    result = ConfigData.getConfigData().analyzeRays2(rays, i * ConfigData.REFRESH_CYCLE);
                    y.add(Float.valueOf(result));
                    if (y.size() > 500) {
                        y.remove(0);
                    }
                    try {
                        Thread.sleep(ConfigData.REFRESH_CYCLE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lineChartView.setAxis(i * ConfigData.REFRESH_CYCLE, y);
                            lineChartView.invalidate();
                        }
                    });
                } else {
                    try {
                        while (!run)
                            Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i--;
                }
            }
            Intent intentStop = new Intent("stop03");
            getActivity().sendBroadcast(intentStop);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dataThread != null)
            dataThread = null;
        new Thread(dataThread).interrupt();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
