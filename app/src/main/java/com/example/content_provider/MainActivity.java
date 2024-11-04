package com.example.content_provider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SMS = 1;
    private static final int REQUEST_CODE_CONTACTS = 2;
    private static final int REQUEST_CODE_CALL_LOG = 3;
    private Button btn_message,btn_call_log,btn_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_contact =findViewById(R.id.btn_contact);
        btn_message = findViewById(R.id.btn_message);
        btn_call_log =findViewById(R.id.btn_call_log);
        ListView lv = findViewById(R.id.lv);
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Message> tinnhans = new ArrayList<>();
                AdapterMessage adapterMessage = new AdapterMessage(MainActivity.this, R.layout.row_message, tinnhans);
                lv.setAdapter(adapterMessage);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_SMS);
                } else {
                    loadSmsMessages(tinnhans, adapterMessage);
                }
            }
        });
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Contact> contacts = new ArrayList<>();
                AdapterContact contactAdapter = new AdapterContact(MainActivity.this, R.layout.row_contact, contacts);
                lv.setAdapter(contactAdapter);

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_CONTACTS);
                } else {
                    loadContacts(contacts, contactAdapter);
                }
            }
        });

        btn_call_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CallLogEntry> callLogs = new ArrayList<>();
                AdapterCallLog callLogAdapter = new AdapterCallLog(MainActivity.this, R.layout.row_call_log, callLogs);
                lv.setAdapter(callLogAdapter);

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_CODE_CALL_LOG);
                } else {
                    loadCallLog(callLogs, callLogAdapter);
                }
            }
        });
}

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CODE_SMS) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ArrayList<Message> tinnhans = new ArrayList<>();
                    AdapterMessage adapterMessage = new AdapterMessage(this, R.layout.row_message, tinnhans);
                    loadSmsMessages(tinnhans, adapterMessage);
                } else {
                    Toast.makeText(this, "Bạn cần cấp quyền đọc SMS để sử dụng ứng dụng", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_CONTACTS) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ArrayList<Contact> contacts = new ArrayList<>();
                    AdapterContact contactAdapter = new AdapterContact(this, R.layout.row_contact, contacts);
                    loadContacts(contacts, contactAdapter);
                } else {
                    Toast.makeText(this, "Bạn cần cấp quyền đọc danh bạ để sử dụng ứng dụng", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_CALL_LOG) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ArrayList<CallLogEntry> callLogs = new ArrayList<>();
                    AdapterCallLog callLogAdapter = new AdapterCallLog(this, R.layout.row_call_log, callLogs);
                    loadCallLog(callLogs, callLogAdapter);
                } else {
                    Toast.makeText(this, "Bạn cần cấp quyền đọc nhật ký cuộc gọi để sử dụng ứng dụng", Toast.LENGTH_SHORT).show();
                }
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
            cursor.close();
            adapterMessage.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Không thể truy xuất tin nhắn SMS", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    private void loadContacts(ArrayList<Contact> contacts, AdapterContact contactAdapter) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            contacts.clear();
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Cursor phoneCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                );
                String phoneNumber = "";
                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneCursor.close();
                }
                contacts.add(new Contact(name, phoneNumber));
            }
            cursor.close();
            contactAdapter.notifyDataSetChanged(); // Cập nhật adapter để hiển thị danh bạ mới
        } else {
            Toast.makeText(this, "Không thể truy xuất danh bạ", Toast.LENGTH_SHORT).show();
        }
    }
private void loadCallLog(ArrayList<CallLogEntry> callLogs, AdapterCallLog callLogAdapter) {
    Uri uri = CallLog.Calls.CONTENT_URI;
    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
    if (cursor != null) {
        callLogs.clear();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
            String callType = "";
            switch (Integer.parseInt(type)) {
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;
            }
            callLogs.add(new CallLogEntry(number, callType, new SimpleDateFormat("HH:mm:ss").format(new Date(Long.parseLong(date))), duration));
        }
        cursor.close();
        callLogAdapter.notifyDataSetChanged();
    } else {
        Toast.makeText(this, "Không thể truy xuất nhật ký cuộc gọi", Toast.LENGTH_SHORT).show();
    }
    }
}
