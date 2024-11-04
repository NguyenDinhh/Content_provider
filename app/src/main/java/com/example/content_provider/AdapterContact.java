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

public class AdapterContact extends ArrayAdapter<Contact> {
    private Activity context;
    private int resource;
    private List<Contact> objects;

    public AdapterContact(@NonNull Activity context, int resource, @NonNull List<Contact> objects) {
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

        TextView txt_name = convertView.findViewById(R.id.text_view_contact_name);
        TextView txt_phone = convertView.findViewById(R.id.text_view_contact_phone);

        Contact contact = this.objects.get(position);
        txt_name.setText(contact.getName());
        txt_phone.setText(contact.getPhoneNumber());

        return convertView;
    }
}
