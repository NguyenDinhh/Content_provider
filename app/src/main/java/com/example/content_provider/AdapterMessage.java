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

public class AdapterMessage extends ArrayAdapter<Message> {
    Activity context;
    int resource;
    List<Message> objects;

    public AdapterMessage(@NonNull Activity context, int resource, @NonNull List<Message> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Sử dụng convertView nếu có
        if (convertView == null) {
            LayoutInflater layoutInflater = this.context.getLayoutInflater();
            convertView = layoutInflater.inflate(this.resource, parent, false);
        }

        // Lấy View
        TextView txt_sdt = convertView.findViewById(R.id.text_view_phone);
        TextView txt_thoigian = convertView.findViewById(R.id.text_view_time);
        TextView txt_nd = convertView.findViewById(R.id.text_view_content);

        // Thiết lập nội dung cho các TextView
        txt_sdt.setText(this.objects.get(position).getSdt());
        txt_thoigian.setText(this.objects.get(position).getThoigian());
        txt_nd.setText(this.objects.get(position).getNd());

        return convertView;
    }
}
