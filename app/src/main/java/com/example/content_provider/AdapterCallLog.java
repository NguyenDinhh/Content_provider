package com.example.content_provider;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdapterCallLog extends ArrayAdapter<CallLogEntry> {
    private Activity context;
    private int resource;
    private List<CallLogEntry> objects;

    public AdapterCallLog(@NonNull Activity context, int resource, @NonNull List<CallLogEntry> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = this.context.getLayoutInflater();
            convertView = layoutInflater.inflate(this.resource, parent, false);
        }

        TextView txt_number = convertView.findViewById(R.id.text_view_call_number);
        TextView txt_type = convertView.findViewById(R.id.text_view_call_type);
        TextView txt_time = convertView.findViewById(R.id.text_view_call_time);
        TextView txt_duration = convertView.findViewById(R.id.text_view_call_duration);

        CallLogEntry callLog = this.objects.get(position);
        txt_number.setText(callLog.getNumber());
        txt_type.setText(callLog.getType());
        txt_time.setText(callLog.getDate());
        txt_duration.setText(callLog.getDuration());

        return convertView;
    }
}
