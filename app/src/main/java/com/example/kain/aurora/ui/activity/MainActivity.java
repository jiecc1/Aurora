package com.example.kain.aurora.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.example.kain.aurora.R;
import com.example.kain.aurora.base.BaseActivity;
import com.example.kain.aurora.ui.fragment.AdvanceFragment;
import com.example.kain.aurora.ui.fragment.BasicFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.kain.aurora.R.color.colorPrimary;
import static com.example.kain.aurora.R.color.white;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_basic;
    private Button btn_advance;

    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        btn_basic = findViewById(R.id.btn_basic);
        btn_advance = findViewById(R.id.btn_advance);

        btn_basic.setOnClickListener(this);
        btn_advance.setOnClickListener(this);

        fragments = new ArrayList<>();
        fragments.add(new BasicFragment());
        fragments.add(new AdvanceFragment());

        addOrShowFragments(fragments.get(0));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_basic:
                btn_basic.setTextColor(Color.parseColor("#000000"));
                btn_advance.setTextColor(Color.parseColor("#818181"));
                addOrShowFragments(fragments.get(0));
                break;
            case R.id.btn_advance:
                btn_advance.setTextColor(Color.parseColor("#000000"));
                btn_basic.setTextColor(Color.parseColor("#818181"));
                addOrShowFragments(fragments.get(1));
                break;
        }
    }
}
