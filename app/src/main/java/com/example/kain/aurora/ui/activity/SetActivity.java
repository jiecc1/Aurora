package com.example.kain.aurora.ui.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kain.aurora.R;
import com.example.kain.aurora.adapter.SetAdpter;
import com.example.kain.aurora.base.BaseActivity;
import com.example.kain.aurora.bean.Ray;
import com.example.kain.aurora.bean.RaysFile;
import com.example.kain.aurora.bean.TotalTime;
import com.example.kain.aurora.configdata.ConfigData;
import com.example.kain.aurora.utils.ACache;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SetActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private RecyclerView recyclerView;
    private Button btn_bas_file;
    private Button btn_bas_insert;
    private Button btn_bas_edit;
    private Button btn_bas_delete;
    private Button btn_bas_OK;
    private Button btn_bas_cancel;
    private Button btn_type;
    private RadioButton rad_all_checked;
    private TextView tv_cycle;
    private EditText et_total_time;

    private List<Ray> rays = new ArrayList<>();
    private List<Ray> selectRays = new ArrayList<>();
    RaysFile raysFile;
    private boolean allChecked;
    private int type;
    /**
     * memory
     * 0 internal
     * 1 external
     */
    private int memory;
    private String fileName;
    private float PerCycleTime;

    private SetAdpter setAdpter;
    private Ray lastRay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_basic_set);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initView() {
        recyclerView = findViewById(R.id.rv_list);
        btn_bas_file = findViewById(R.id.btn_bas_file);
        btn_bas_insert = findViewById(R.id.btn_bas_insert);
        btn_bas_edit = findViewById(R.id.btn_bas_edit);
        btn_bas_delete = findViewById(R.id.btn_bas_delete);
        btn_bas_OK = findViewById(R.id.btn_bas_OK);
        btn_bas_cancel = findViewById(R.id.btn_bas_cancel);
        rad_all_checked = findViewById(R.id.rad_all_checked);
        btn_type = findViewById(R.id.btn_type);
        tv_cycle = findViewById(R.id.tv_cycle);
        et_total_time = findViewById(R.id.et_total_time);

        btn_bas_file.setOnClickListener(this);
        btn_bas_insert.setOnClickListener(this);
        btn_bas_edit.setOnClickListener(this);
        btn_bas_delete.setOnClickListener(this);
        btn_bas_OK.setOnClickListener(this);
        btn_bas_cancel.setOnClickListener(this);
        rad_all_checked.setOnClickListener(this);

        rays = findRays(type);

//        初始化type并加载标题
        type = getIntent().getIntExtra("setType", 0);
        if (type == 1 | type == 2) {
            btn_type.setText("Basic");
        } else if (type == 3 | type == 4) {
            btn_type.setText("Advance");
        }

        TotalTime totalTime = (TotalTime) ACache.get(this).getAsObject("totalTime" + type);
        if (totalTime != null) {
            et_total_time.setText(String.valueOf(totalTime.getTime()));
        }

//        加载recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        rays = findRays(type);
        setAdpter = new SetAdpter(rays);
        recyclerView.setAdapter(setAdpter);

//        加载循环数和每次循环时间
        selectRays = findSelectRays(type);
        PerCycleTime = ConfigData.analyzeCycleTime(selectRays, false) / 1000f;
        tv_cycle.setText("Per cycle time:" + PerCycleTime + "s");
    }

    private void initData() {
//        加载rad_all_checked缓存
        String checked = ACache.get(this).getAsString("allChecked");
        if (checked != null && checked.equals("true")) {
            allChecked = true;
            rad_all_checked.setChecked(true);
        } else {
            allChecked = false;
            rad_all_checked.setChecked(false);
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.btn_bas_file:
                createFileMenu(view);
                break;
            case R.id.btn_bas_insert:
                createInsertDialog();
                break;
            case R.id.btn_bas_edit:
                setEditListener();
                break;
            case R.id.btn_bas_delete:
                createDeleteDialog();
                break;
            case R.id.btn_bas_OK:
                setOKListener();
                break;
            case R.id.btn_bas_cancel:
                setCancelListener();
                break;
            case R.id.rad_all_checked:
                setAllCheckedListener();
                break;
        }
    }

    private void createFileMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_file, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    private void createInsertDialog() {
        //                初始化Dialog View
        View dialog = View.inflate(this, R.layout.dia_insert, null);

        final EditText et_pulse_width = dialog.findViewById(R.id.et_pulse_width);
        final EditText et_frequency = dialog.findViewById(R.id.et_frequency);
        final EditText et_energy = dialog.findViewById(R.id.et_energy);
        final EditText et_duration = dialog.findViewById(R.id.et_duration);
        final EditText et_delay = dialog.findViewById(R.id.et_delay);
        final RadioButton rad_wave_form = dialog.findViewById(R.id.rad_wave_form);
        final RadioButton rad_pulse_width_ms = dialog.findViewById(R.id.rad_pulse_width_ms);
        final RadioButton rad_pulse_width_s = dialog.findViewById(R.id.rad_pulse_width_s);
        final RadioButton rad_duration_ms = dialog.findViewById(R.id.rad_duration_ms);
        final RadioButton rad_duration_s = dialog.findViewById(R.id.rad_duration_s);
        final RadioButton rad_delay_ms = dialog.findViewById(R.id.rad_delay_ms);
        final RadioButton rad_delay_s = dialog.findViewById(R.id.rad_delay_s);

//                加载insert缓存
        Ray ray = (Ray) ACache.get(this).getAsObject("lastRay");
        if (ray != null) {
            lastRay = ray;
        } else {
            lastRay = ConfigData.getDefaultRay();
        }

        et_pulse_width.setText(String.valueOf(lastRay.getPulse_width()));
        et_frequency.setText(String.valueOf(lastRay.getFrequency()));
        et_energy.setText(String.valueOf(lastRay.getEnergy()));
        et_duration.setText(String.valueOf(lastRay.getDuration()));
        et_delay.setText(String.valueOf(lastRay.getDelay()));
        rad_wave_form.setChecked(true);
        if (lastRay.getPulse_width_unit() == 1) {
            rad_pulse_width_ms.setChecked(true);
        } else {
            rad_pulse_width_s.setChecked(true);
        }
        if (lastRay.getDuration_unit() == 1) {
            rad_duration_ms.setChecked(true);
        } else {
            rad_duration_s.setChecked(true);
        }
        if (lastRay.getDuration_unit() == 1) {
            rad_delay_ms.setChecked(true);
        } else {
            rad_delay_s.setChecked(true);
        }

//                创建Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Square Setting");
        builder.setCancelable(false);
        builder.setView(dialog);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int pulse_width;
                float frequency;
                int energy;
                int duration;
                int delay;

                if (et_pulse_width.getText().toString().equals("")) {
                    pulse_width = 0;
                } else {
                    pulse_width = Integer.parseInt(et_pulse_width.getText().toString());
                }
                if (et_frequency.getText().toString().equals("")) {
                    frequency = 0f;
                } else {
                    frequency = Float.parseFloat(et_frequency.getText().toString());
                }
                if (et_energy.getText().toString().equals("")) {
                    energy = 0;
                } else {
                    energy = Integer.parseInt(et_energy.getText().toString());
                }
                if (et_duration.getText().toString().equals("")) {
                    duration = 0;
                } else {
                    duration = Integer.parseInt(et_duration.getText().toString());
                }
                if (et_delay.getText().toString().equals("")) {
                    delay = 0;
                } else {
                    delay = Integer.parseInt(et_delay.getText().toString());
                }

                if (rays.size() >= 10) {
                    Toast.makeText(SetActivity.this, "Maximum data volume is 10!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Ray ray = new Ray();
                ray.setSelect(1);
                ray.setType(type);
                ray.setWaveform("square");
                ray.setPulse_width(pulse_width);
                if (rad_pulse_width_ms.isChecked()) {
                    ray.setPulse_width_unit(1);
                } else if (rad_pulse_width_s.isChecked()) {
                    ray.setPulse_width_unit(1000);
                }
                ray.setFrequency(frequency);
                ray.setFrequency_unit(0);
                ray.setEnergy(energy);
                ray.setEnergy_unit(0);
                ray.setDuration(duration);
                if (rad_duration_ms.isChecked()) {
                    ray.setDuration_unit(1);
                } else if (rad_duration_s.isChecked()) {
                    ray.setDuration_unit(1000);
                }
                ray.setDelay(delay);
                if (rad_delay_ms.isChecked()) {
                    ray.setDelay_unit(1);
                } else if (rad_delay_s.isChecked()) {
                    ray.setDelay_unit(1000);
                }
                if (ray.getFrequency() == 0) {
                    Toast.makeText(SetActivity.this, "frequency can not be 0 !", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ray.getPulse_width_unit() == 1 &&
                        ray.getPulse_width() / 1000 > 1 / ray.getFrequency()) {
                    Toast.makeText(SetActivity.this, "pulse width can not exceed 1/frequency !", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ray.getPulse_width_unit() == 1000 &&
                        ray.getPulse_width() > 1 / ray.getFrequency()) {
                    Toast.makeText(SetActivity.this, "pulse width can not exceed 1/frequency !", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ray.getFrequency() > 500) {
                    Toast.makeText(SetActivity.this, "frequency must less than or equal to 500Hz ！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ray.getEnergy() > 60) {
                    Toast.makeText(SetActivity.this, "energy must less than or equal to 60mV ！", Toast.LENGTH_SHORT).show();
                    return;
                }
                ACache.get(SetActivity.this).put("lastRay", ray);
                ray.save();
                rays = findRays(type);
                refreshData(rays);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void setEditListener() {
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete?");
        builder.setCancelable(false);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteSelectRays(type);
                rays = findRays(type);
                refreshData(rays);
            }
        });
        builder.setNeutralButton("Delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteRays(type);
                rays = findRays(type);
                refreshData(rays);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void setOKListener() {
        if (et_total_time.getText() != null) {
            int time;
            if (et_total_time.getText().toString().equals("")) {
                time = 0;
            } else {
                time = Integer.parseInt(et_total_time.getText().toString());
            }
            if (time > 99999) {
                time = 99999;
            }
            TotalTime totalTime = new TotalTime();
            totalTime.setType(type);
            totalTime.setTime(time);
            ACache.get(this).put("totalTime" + type, totalTime);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setCancelListener() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setAllCheckedListener() {
        if (allChecked) {
            rad_all_checked.setChecked(false);
            ContentValues values = new ContentValues();
            values.put("select", 0);
            updateAllRays(type, values);
            rays = findRays(type);
            setAdpter = new SetAdpter(rays);
            recyclerView.setAdapter(setAdpter);
            allChecked = false;
            ACache.get(this).put("allChecked", "false");
        } else {
            rad_all_checked.setChecked(true);
            ContentValues values = new ContentValues();
            values.put("select", 1);
            updateAllRays(type, values);
            rays = findRays(type);
            setAdpter = new SetAdpter(rays);
            recyclerView.setAdapter(setAdpter);
            allChecked = true;
            ACache.get(this).put("allChecked", "true");
        }
    }

    @Override
    public boolean onMenuItemClick(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_new:
                createNewDialog();
                break;
            case R.id.item_open:
                createOpenDialog();
                break;
            case R.id.item_save:
                setSaveListener();
                break;
            case R.id.item_save_as:
                createSaveAsDialog();
                break;
            case R.id.item_cancel:
                findSerialPort();
                break;
        }
        return false;
    }

    public void createNewDialog() {
        View dialog = View.inflate(this, R.layout.dia_file_new, null);

        final EditText et_new = dialog.findViewById(R.id.et_new);
        final Spinner spi_new = dialog.findViewById(R.id.spi_new);

        spi_new.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                memory = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New");
        builder.setCancelable(false);
        builder.setView(dialog);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (et_new.getText().toString().equals("")) {
                    Toast.makeText(SetActivity.this, "Please input file name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                fileName = et_new.getText().toString();
                ACache.get(SetActivity.this).put("fileName", fileName);
                deleteRays(type);
                rays = findRays(type);
                refreshData(rays);
                raysFile = new RaysFile();
                raysFile.setName(fileName);
                if (memory == 0) {
                    raysFile.save();
                } else if (memory == 1) {
//                    TODO 存入U盘
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void createOpenDialog() {
        readFromUSB();
        makeToast("Data has been read");
//        rays = getFromSDCard("abc");
//        if (rays != null) {
//            setAdpter = new SetAdpter(rays);
//            recyclerView.setAdapter(setAdpter);
//            PerCycleTime = ConfigData.getConfigData().analyzeCycleTime(rays, false) / 1000;
//            tv_cycle.setText("Per cycle time:" + PerCycleTime + "s");
//        }
    }

    public void setSaveListener() {
//        saveToSDCard("abc", selectRays);
        saveToUSB();
        makeToast("Data has been saved");
    }

    public void createSaveAsDialog() {
        View dialog = View.inflate(this, R.layout.dia_file_new, null);

        final EditText et_new = dialog.findViewById(R.id.et_new);
        final Spinner spi_new = dialog.findViewById(R.id.spi_new);

        spi_new.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                memory = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save");
        builder.setCancelable(false);
        builder.setView(dialog);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (et_new.getText().toString().equals("")) {
                    Toast.makeText(SetActivity.this, "Please input file name!", Toast.LENGTH_SHORT).show();
                } else {
                    fileName = et_new.getText().toString();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void refreshData(List<Ray> rays) {
        setAdpter = new SetAdpter(rays);
        recyclerView.setAdapter(setAdpter);
        PerCycleTime = ConfigData.analyzeCycleTime(rays, false) / 1000f;
        tv_cycle.setText("Per cycle time:" + String.valueOf(PerCycleTime) + "s");
    }
}
