package com.example.kain.aurora.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kain.aurora.R;
import com.example.kain.aurora.base.BaseFragment;
import com.example.kain.aurora.bean.Ray;
import com.example.kain.aurora.bean.Trigger;
import com.example.kain.aurora.configdata.ConfigData;
import com.example.kain.aurora.ui.activity.SetActivity;
import com.example.kain.aurora.utils.ACache;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvanceFragment extends BaseFragment implements View.OnClickListener {

    private View view;

    private Button btn_adv_set01;
    private Button btn_adv_set02;
    private Button btn_adv_trigger;
    private Button btn_adv_stop;
    private Button btn_adv_start;

    private List<Ray> rays = new ArrayList<>();
    private List<Ray> rays2 = new ArrayList<>();

    private int status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_advance, container, false);

        initView();
        return view;
    }

    private void initView() {

        btn_adv_set01 = view.findViewById(R.id.btn_adv_set01);
        btn_adv_set02 = view.findViewById(R.id.btn_adv_set02);
        btn_adv_trigger = view.findViewById(R.id.btn_adv_trigger);
        btn_adv_stop = view.findViewById(R.id.btn_adv_stop);
        btn_adv_start = view.findViewById(R.id.btn_adv_start);

        btn_adv_set01.setOnClickListener(this);
        btn_adv_set02.setOnClickListener(this);
        btn_adv_trigger.setOnClickListener(this);
        btn_adv_stop.setOnClickListener(this);
        btn_adv_start.setOnClickListener(this);

        getChildFragmentManager().beginTransaction().add(R.id.adv_layout_green, new LineChatFragment3()).commit();
        getChildFragmentManager().beginTransaction().add(R.id.adv_layout_blue, new LineChatFragment4()).commit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_adv_set01:
                Intent intentSet3 = new Intent(getActivity(), SetActivity.class);
                intentSet3.putExtra("setType", 3);
                startActivity(intentSet3);
                break;
            case R.id.btn_adv_set02:
                Intent intentSet4 = new Intent(getActivity(), SetActivity.class);
                intentSet4.putExtra("setType", 4);
                startActivity(intentSet4);
                break;
            case R.id.btn_adv_trigger:
                createTriggerDialog("trigger03");
                break;
            case R.id.btn_adv_stop:
                Intent intentStop03 = new Intent("stop03");
                getActivity().sendBroadcast(intentStop03);
                btn_adv_start.setText("Start");
                status = 0;
                break;
            case R.id.btn_adv_start:
                if (status == 0) {
                    rays = findSelectRays(3);
                    rays2 = findSelectRays(4);
                    Trigger trigger = (Trigger) ACache.get(getActivity()).getAsObject("trigger03");
                    if (trigger == null) {
                        trigger = ConfigData.getDefaultTrigger();
                    }
                    if (rays != null && rays.size() != 0 && rays2 != null && rays2.size() != 0) {
                        if (!trigger.isEnable()) {
                            Intent intentStart03 = new Intent("start03");
                            intentStart03.putExtra("type", 3);
                            getActivity().sendBroadcast(intentStart03);
                            btn_adv_start.setText("Pause");
                            status = 1;
                        } else {
                            Toast.makeText(getActivity(), "Trigger is opening!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "No data !", Toast.LENGTH_SHORT).show();
                    }
                } else if (status == 1) {
                    Intent intentPause03 = new Intent("pause03");
                    getActivity().sendBroadcast(intentPause03);
                    btn_adv_start.setText("Resume");
                    status = 2;
                } else if (status == 2) {
                    Intent intentRestart03 = new Intent("restart03");
                    getActivity().sendBroadcast(intentRestart03);
                    btn_adv_start.setText("Pause");
                    status = 1;
                }
                break;
        }
    }
}
