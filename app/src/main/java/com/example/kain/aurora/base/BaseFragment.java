package com.example.kain.aurora.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.kain.aurora.R;
import com.example.kain.aurora.bean.Ray;
import com.example.kain.aurora.bean.Trigger;
import com.example.kain.aurora.configdata.ConfigData;
import com.example.kain.aurora.utils.ACache;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author Jiecc 2018/3/21
 */

public abstract class BaseFragment extends Fragment {

    private Trigger lastTrigger;

    public void createTriggerDialog(final String key) {
//        初始化Dialog
        View dialog = View.inflate(getActivity(), R.layout.dia_trigger, null);

        final EditText et_upper = dialog.findViewById(R.id.et_upper);
        final EditText et_lower = dialog.findViewById(R.id.et_lower);
        final EditText et_pretrigger_length = dialog.findViewById(R.id.et_pretrigger_length);
        final EditText et_delay = dialog.findViewById(R.id.et_delay);
        final RadioButton rad_enable_trigger = dialog.findViewById(R.id.rad_enable_trigger);
        final RadioButton rad_disable_trigger = dialog.findViewById(R.id.rad_disable_trigger);
        final RadioButton rad_single_trigger = dialog.findViewById(R.id.rad_single_trigger);
        final RadioButton rad_continuous_trigger = dialog.findViewById(R.id.rad_continuous_trigger);

        rad_enable_trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_upper.setEnabled(true);
                et_lower.setEnabled(true);
                et_pretrigger_length.setEnabled(true);
                et_delay.setEnabled(true);
                rad_single_trigger.setEnabled(true);
                rad_continuous_trigger.setEnabled(true);
            }
        });

        rad_disable_trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_upper.setEnabled(false);
                et_lower.setEnabled(false);
                et_pretrigger_length.setEnabled(false);
                et_delay.setEnabled(false);
                rad_single_trigger.setEnabled(false);
                rad_continuous_trigger.setEnabled(false);
            }
        });

//                加载trigger缓存
        Trigger trigger = (Trigger) ACache.get(getActivity()).getAsObject(key);
        if (trigger != null) {
            lastTrigger = trigger;
        } else {
            lastTrigger = ConfigData.getDefaultTrigger();
        }

        et_upper.setText(String.valueOf(lastTrigger.getUpper()));
        et_lower.setText(String.valueOf(lastTrigger.getLower()));
        et_pretrigger_length.setText(String.valueOf(lastTrigger.getPretriggerLength()));
        et_delay.setText(String.valueOf(lastTrigger.getDelay()));
        if (lastTrigger.isEnable()) {
            rad_enable_trigger.setChecked(true);
            et_upper.setEnabled(true);
            et_lower.setEnabled(true);
            et_pretrigger_length.setEnabled(true);
            et_delay.setEnabled(true);
            rad_single_trigger.setEnabled(true);
            rad_continuous_trigger.setEnabled(true);
        } else {
            rad_disable_trigger.setChecked(true);
            et_upper.setEnabled(false);
            et_lower.setEnabled(false);
            et_pretrigger_length.setEnabled(false);
            et_delay.setEnabled(false);
            rad_single_trigger.setEnabled(false);
            rad_continuous_trigger.setEnabled(false);
        }
        if (lastTrigger.getTriggerType() == 1) {
            rad_single_trigger.setChecked(true);
        } else if (lastTrigger.getTriggerType() == 2) {
            rad_continuous_trigger.setChecked(true);
        }

//                创建dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Trigger setting");
        builder.setCancelable(false);
        builder.setView(dialog);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int upper;
                int lower;
                int pretrigger_length;
                int delay;

                if (et_upper.getText().toString().equals("")) {
                    upper = 0;
                } else {
                    upper = Integer.parseInt(et_upper.getText().toString());
                }
                if (et_lower.getText().toString().equals("")) {
                    lower = 0;
                } else {
                    lower = Integer.parseInt(et_lower.getText().toString());
                }
                if (et_pretrigger_length.getText().toString().equals("")) {
                    pretrigger_length = 0;
                } else {
                    pretrigger_length = Integer.parseInt(et_pretrigger_length.getText().toString());
                }
                if (et_delay.getText().toString().equals("")) {
                    delay = 0;
                } else {
                    delay = Integer.parseInt(et_delay.getText().toString());
                }

                Trigger trigger = new Trigger();
                trigger.setType(1);
                trigger.setUpper(upper);
                trigger.setLower(lower);
                trigger.setPretriggerLength(pretrigger_length);
                trigger.setDelay(delay);
                if (rad_enable_trigger.isChecked()) {
                    trigger.setEnable(true);
                } else {
                    trigger.setEnable(false);
                }
                if (rad_single_trigger.isChecked()) {
                    trigger.setTriggerType(1);
                } else if (rad_continuous_trigger.isChecked()) {
                    trigger.setTriggerType(2);
                }
                if (upper < lower) {
                    Toast.makeText(getActivity(), "Upper limit can not less than lower limit !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pretrigger_length > 50) {
                    Toast.makeText(getActivity(), "pretrigger length can not more than 50ms!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ACache.get(getActivity()).put(key, trigger);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public List<Ray> findSelectRays(int type) {
        List<Ray> rays = new ArrayList<>();
        if (type == 1) {
            rays = DataSupport.where("type = ? and select = ?", "1", "1").find(Ray.class);
        } else if (type == 2) {
            rays = DataSupport.where("type = ? and select = ?", "2", "1").find(Ray.class);
        } else if (type == 3) {
            rays = DataSupport.where("type = ? and select = ?", "3", "1").find(Ray.class);
        } else if (type == 4) {
            rays = DataSupport.where("type = ? and select = ?", "4", "1").find(Ray.class);
        }
        return rays;
    }
}
