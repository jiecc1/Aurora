package com.example.kain.aurora.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kain.aurora.R;
import com.example.kain.aurora.bean.Ray;
import com.example.kain.aurora.ui.activity.SetActivity;

import java.util.List;

/**
 * Created by wangs on 2018/3/21.
 */

public class SetAdpter extends RecyclerView.Adapter<SetAdpter.ViewHolder> {

    private List<Ray> rays;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView waveform;
        TextView pluse_width;
        TextView frequency;
        TextView energy;
        TextView duration;
        TextView delay;
        RadioButton check;

        View tableView;

        public ViewHolder(View itemView) {
            super(itemView);

            tableView = itemView;

            waveform = itemView.findViewById(R.id.tv_basic_waveform01);
            pluse_width = itemView.findViewById(R.id.tv_basic_pluse_width01);
            frequency = itemView.findViewById(R.id.tv_basic_frequency01);
            energy = itemView.findViewById(R.id.tv_basic_energy01);
            duration = itemView.findViewById(R.id.tv_basic_duration01);
            delay = itemView.findViewById(R.id.tv_basic_delay01);
            check = itemView.findViewById(R.id.rad_check);

        }

    }

    public SetAdpter(List<Ray> rays) {
        this.rays = rays;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_basic_set_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.tableView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                int position = holder.getAdapterPosition();
                final Ray ray = rays.get(position);
                View dialog = View.inflate(view.getContext(), R.layout.dia_insert, null);

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

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Square Editing");
                builder.setCancelable(false);
                builder.setView(dialog);

                et_pulse_width.setText(String.valueOf(ray.getPulse_width()));
                et_frequency.setText(String.valueOf(ray.getFrequency()));
                et_energy.setText(String.valueOf(ray.getEnergy()));
                et_duration.setText(String.valueOf(ray.getDuration()));
                et_delay.setText(String.valueOf(ray.getDelay()));
                rad_wave_form.setChecked(true);
                if (ray.getPulse_width_unit() == 1) {
                    rad_pulse_width_ms.setChecked(true);
                } else {
                    rad_pulse_width_s.setChecked(true);
                }
                if (ray.getDuration_unit() == 1) {
                    rad_duration_ms.setChecked(true);
                } else {
                    rad_duration_s.setChecked(true);
                }
                if (ray.getDelay_unit() == 1) {
                    rad_delay_ms.setChecked(true);
                } else {
                    rad_delay_s.setChecked(true);
                }

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int pulse_width = Integer.parseInt(et_pulse_width.getText().toString());
                        float frequency = Float.parseFloat(et_frequency.getText().toString());
                        int energy = Integer.parseInt(et_energy.getText().toString());
                        int duration = Integer.parseInt(et_duration.getText().toString());
                        int delay = Integer.parseInt(et_delay.getText().toString());

                        if (et_pulse_width.getText() != null && pulse_width > 0) {
                            ray.setPulse_width(pulse_width);
                            if (rad_pulse_width_ms.isChecked()) {
                                ray.setPulse_width_unit(1);
                            } else if (rad_pulse_width_s.isChecked()) {
                                ray.setPulse_width_unit(1000);
                            }
                            ray.setFrequency(frequency);
                            ray.setEnergy(energy);
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
                                Toast.makeText(view.getContext(), "frequency can not be 0 !", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (ray.getPulse_width_unit() == 1 &&
                                    ray.getPulse_width() / 1000 > 1 / ray.getFrequency()) {
                                Toast.makeText(view.getContext(), "pulse width can not exceed 1/frequency !", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (ray.getPulse_width_unit() == 1000 &&
                                    ray.getPulse_width() > 1 / ray.getFrequency()) {
                                Toast.makeText(view.getContext(), "pulse width can not exceed 1/frequency !", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (ray.getFrequency() > 500) {
                                Toast.makeText(view.getContext(), "frequency must less than or equal to 500Hz ！", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (ray.getEnergy() > 60) {
                                Toast.makeText(view.getContext(), "energy must less than or equal to 60mV ！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            ray.save();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(view.getContext(), "Please input correct data!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return false;
            }
        });
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Ray ray = rays.get(position);
                if (ray.getSelect() == 0) {
                    ray.setSelect(1);
                    ray.save();
                } else if (ray.getSelect() == 1) {
                    ray.setSelect(0);
                    ray.save();
                }
                notifyDataSetChanged();
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ray ray = rays.get(position);
        if (ray.getSelect() == 1) {
            holder.check.setChecked(true);
        } else {
            holder.check.setChecked(false);
        }
        holder.waveform.setText(ray.getWaveform());
        if (ray.getPulse_width_unit() == 1) {
            holder.pluse_width.setText(String.valueOf(ray.getPulse_width()) + "ms");
        } else {
            holder.pluse_width.setText(String.valueOf(ray.getPulse_width()) + "s");
        }
        holder.frequency.setText(String.valueOf(ray.getFrequency()) + "Hz");
        holder.energy.setText(String.valueOf(ray.getEnergy()) + "mW");
        if (ray.getDuration_unit() == 1) {
            holder.duration.setText(String.valueOf(ray.getDuration()) + "ms");
        } else {
            holder.duration.setText(String.valueOf(ray.getDuration()) + "s");
        }
        if (ray.getDelay_unit() == 1) {
            holder.delay.setText(String.valueOf(ray.getDelay()) + "ms");
        } else {
            holder.delay.setText(String.valueOf(ray.getDelay()) + "s");
        }

    }

    @Override
    public int getItemCount() {
        return rays.size();
    }

}
