package com.example.smsmanagerlab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_SEND_SMS = 201;

    EditText edtPhone, edtMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPhone = findViewById(R.id.edtPhone);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            String message = edtMessage.getText().toString().trim();

            if (phone.isEmpty()) {
                Toast.makeText(this, "Please enter a phone number.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (message.isEmpty()) {
                Toast.makeText(this, "Please enter a message.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Check SEND_SMS permission
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                sendSms(phone, message);
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.SEND_SMS},
                        REQ_SEND_SMS
                );
            }
        });
    }

    private void sendSms(String phone, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(this, "SMS sent (request issued).",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_SEND_SMS) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phone = edtPhone.getText().toString().trim();
                String message = edtMessage.getText().toString().trim();
                if (!phone.isEmpty() && !message.isEmpty()) {
                    sendSms(phone, message);
                }
            } else {
                Toast.makeText(this, "SEND_SMS permission denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
