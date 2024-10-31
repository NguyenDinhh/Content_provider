package com.example.content_provider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = findViewById(R.id.lv);
        ArrayList<Message> tinnhans = new ArrayList<>();
        AdapterMessage adapterMessage = new AdapterMessage(this, R.layout.row_message, tinnhans);
        lv.setAdapter(adapterMessage);

        // Kiểm tra và yêu cầu quyền
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_SMS);
        } else {
            loadSmsMessages(tinnhans, adapterMessage);
        }
    }

    private void loadSmsMessages(ArrayList<Message> tinnhans, AdapterMessage adapterMessage) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            tinnhans.clear();
            while (cursor.moveToNext()) {
                int indexsdt = cursor.getColumnIndex("address");
                int indexthoigian = cursor.getColumnIndex("date");
                int indexnd = cursor.getColumnIndex("body");

                String sdt = cursor.getString(indexsdt);
                String thoigian = cursor.getString(indexthoigian);
                String nd = cursor.getString(indexnd);

                tinnhans.add(new Message(sdt, sdf.format(Long.parseLong(thoigian)), nd));
            }
            cursor.close(); // Đóng cursor để giải phóng tài nguyên
            adapterMessage.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Không thể truy xuất tin nhắn SMS", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, tải tin nhắn
                ArrayList<Message> tinnhans = new ArrayList<>();
                AdapterMessage adapterMessage = new AdapterMessage(this, R.layout.row_message, tinnhans);
                loadSmsMessages(tinnhans, adapterMessage);
            } else {
                // Quyền bị từ chối
                Toast.makeText(this, "Bạn cần cấp quyền đọc SMS để sử dụng ứng dụng", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
