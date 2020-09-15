package com.example.payment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.payment.R;
import com.example.payment.util.ApiServer;
import com.example.payment.util.PrefManager;
import com.squareup.picasso.Picasso;

public class ActivityQr extends AppCompatActivity {
ImageView qr;
TextView nama;
PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        prefManager = new PrefManager(this);
        nama = findViewById(R.id.txtUserNama);
        nama.setText(prefManager.getNamaUser());
        qr = findViewById(R.id.imgQr);
        Picasso.get().load(ApiServer.get_qr+prefManager.getIdUser()).into(qr);
    }
}
