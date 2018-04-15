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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicFragment extends BaseFragment implements View.OnClickListener {

    private View view;

    private Button btn_bas_set01;
    private Button btn_bas_trigger01;
    private Button btn_bas_stop01;
    private Button btn_bas_start01;
    private Button btn_bas_set02;
    private Button btn_bas_trigger02;
    private Button btn_bas_stop02;
    private Button btn_bas_start02;

    private List<Fragment> fragments = new ArrayList<>();
    private List<Ray> rays = new ArrayList<>();

    /**
     * status
     * 0 = stop
     * 1 = run
     * 2 = pause
     */
    private int status;
    private int status2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_basic, container, false);

        initView();
        return view;
    }

    private void initView() {
        btn_bas_set01 = view.findViewById(R.id.btn_bas_set01);
        btn_bas_trigger01 = view.findViewById(R.id.btn_bas_trigger01);
        btn_bas_stop01 = view.findViewById(R.id.btn_bas_stop01);
        btn_bas_start01 = view.findViewById(R.id.btn_bas_start01);
        btn_bas_set02 = view.findViewById(R.id.btn_bas_set02);
        btn_bas_trigger02 = view.findViewById(R.id.btn_bas_trigger02);
        btn_bas_stop02 = view.findViewById(R.id.btn_bas_stop02);
        btn_bas_start02 = view.findViewById(R.id.btn_bas_start02);

        btn_bas_set01.setOnClickListener(this);
        btn_bas_trigger01.setOnClickListener(this);
        btn_bas_stop01.setOnClickListener(this);
        btn_bas_start01.setOnClickListener(this);
        btn_bas_set02.setOnClickListener(this);
        btn_bas_trigger02.setOnClickListener(this);
        btn_bas_stop02.setOnClickListener(this);
        btn_bas_start02.setOnClickListener(this);

//        LineChatFragment lineChatFragment1 = new LineChatFragment();
//        LineChatFragment lineChatFragment2 = new LineChatFragment();
//        Bundle bundle1 = new Bundle();
//        bundle1.putInt("type",1);
//        lineChatFragment1.setArguments(bundle1);
//        Bundle bundle2 = new Bundle();
//        bundle1.putInt("type",2);
//        lineChatFragment2.setArguments(bundle2);
        fragments.add(new LineChatFragment());
        fragments.add(new LineChatFragment2());

        getChildFragmentManager().beginTransaction().add(R.id.chat_layout_green, fragments.get(0)).commit();
        getChildFragmentManager().beginTransaction().add(R.id.chat_layout_blue, fragments.get(1)).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bas_set01:
                Intent intentSet1 = new Intent(getActivity(), SetActivity.class);
                intentSet1.putExtra("setType", 1);
                startActivity(intentSet1);
                break;
            case R.id.btn_bas_trigger01:
                createTriggerDialog("trigger01");
                break;
            case R.id.btn_bas_stop01:
                Intent intentStop01 = new Intent("stop01");
                getActivity().sendBroadcast(intentStop01);
                btn_bas_start01.setText("Start");
                status = 0;
                break;
            case R.id.btn_bas_start01:
                if (status == 0) {
                    rays = findSelectRays(1);
                    Trigger trigger = (Trigger) ACache.get(getActivity()).getAsObject("trigger01");
                    if (trigger == null) {
                        trigger = ConfigData.getDefaultTrigger();
                    }
                    if (rays != null && rays.size() != 0) {
                        if (!trigger.isEnable()) {
                            Intent intentStart01 = new Intent("start01");
                            intentStart01.putExtra("type", 1);
                            getActivity().sendBroadcast(intentStart01);
                            btn_bas_start01.setText("Pause");
                            status = 1;
                        } else {
                            Toast.makeText(getActivity(), "Trigger is opening!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "No data !", Toast.LENGTH_SHORT).show();
                    }
                } else if (status == 1) {
                    Intent intentPause01 = new Intent("pause01");
                    getActivity().sendBroadcast(intentPause01);
                    btn_bas_start01.setText("Resume");
                    status = 2;
                } else if (status == 2) {
                    Intent intentRestart01 = new Intent("restart01");
                    getActivity().sendBroadcast(intentRestart01);
                    btn_bas_start01.setText("Pause");
                    status = 1;
                }
                break;
            case R.id.btn_bas_set02:
                Intent intentSet2 = new Intent(getActivity(), SetActivity.class);
                intentSet2.putExtra("setType", 2);
                getActivity().sendBroadcast(intentSet2);
                startActivity(intentSet2);
                break;
            case R.id.btn_bas_trigger02:
                createTriggerDialog("trigger02");
                break;
            case R.id.btn_bas_stop02:
                Intent intentStop02 = new Intent("stop02");
                getActivity().sendBroadcast(intentStop02);
                btn_bas_start02.setText("Start");
                status2 = 0;
                break;
            case R.id.btn_bas_start02:
                if (status2 == 0) {
                    rays = findSelectRays(2);
                    Trigger trigger = (Trigger) ACache.get(getActivity()).getAsObject("trigger02");
                    if (trigger == null) {
                        trigger = ConfigData.getDefaultTrigger();
                    }
                    if (rays != null && rays.size() != 0) {
                        if (!trigger.isEnable()) {
                            Intent intentStart02 = new Intent("start02");
                            intentStart02.putExtra("type", 2);
                            getActivity().sendBroadcast(intentStart02);
                            btn_bas_start02.setText("Pause");
                            status2 = 1;
                        } else {
                            Toast.makeText(getActivity(), "Trigger is opening!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "No data !", Toast.LENGTH_SHORT).show();
                    }
                } else if (status2 == 1) {
                    Intent intentPause02 = new Intent("pause02");
                    getActivity().sendBroadcast(intentPause02);
                    btn_bas_start02.setText("Resume");
                    status2 = 2;
                } else if (status2 == 2) {
                    Intent intentRestart02 = new Intent("restart02");
                    getActivity().sendBroadcast(intentRestart02);
                    btn_bas_start02.setText("Pause");
                    status2 = 1;
                }
                break;
        }
    }

    public void dialogEnable() {
    }
}