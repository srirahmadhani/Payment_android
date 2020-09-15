package com.example.payment.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.payment.R;
import com.example.payment.util.ApiServer;
import com.example.payment.util.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class ActivityScan extends AppCompatActivity {
ImageView btnCam;
EditText edIdUser, edJml;
PrefManager prefManager;
Button btnSubmit;

    String id = "";
    String jml="";
    int saldo = 0;
    int hargaTiket = 0;
    int qty=0;
//    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        AndroidNetworking.initialize(this);
        prefManager = new PrefManager(this);
        edJml = findViewById(R.id.edJmlUser);

        final Intent intent = new Intent(getIntent());
        id = intent.getStringExtra("id");


        edIdUser = findViewById(R.id.edIdUser);
        edIdUser.setText(id);
        jml = edJml.getText().toString();
//        qty= Integer.valueOf(jml);

        getSaldoUser();

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new AlertDialog.Builder(getApplicationContext())
//                        .setTitle("Perhatian")
//                        .setMessage("Yakin untuk membayar")
//                        .setCancelable(false)
//                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {

                                String tmp = edIdUser.getText().toString();
                                int value=0;
                                value=Integer.parseInt(tmp);
                                Intent intent1 = new Intent(getIntent());
                                int total = intent1.getIntExtra("hargaTiket",0)*value;
                                if (total>saldo){
                                    Toast.makeText(getApplicationContext(),qty+"Saldo Pengunjung Tidak Mencukupi "+saldo, Toast.LENGTH_LONG).show();
                                }else {
                                    submitTiket(total, value);
                                }
//                            }
//                        })
//                        .setNegativeButton("Tidak", null)
//                        .show();

            }
        });



        btnCam = findViewById(R.id.btnCam);
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityScan.this, ActivityCamQr.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getSaldoUser() {
        AndroidNetworking.get(ApiServer.get_profil+id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equalsIgnoreCase("true")){
                                JSONObject data = response.getJSONObject("data");
                                saldo = data.getInt("saldo");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "error :" + anError  );
                    }
                });
    }

    private void submitTiket(int i, int total) {
        Intent intent = new Intent(getIntent());
        final String idW = intent.getStringExtra("idWisata");

        prefManager = new PrefManager(this);
        Toast.makeText(getApplicationContext(), "berhasill", Toast.LENGTH_LONG).show();
        AndroidNetworking.post(ApiServer.payment)
                .addBodyParameter("qty", String.valueOf(total))
                .addBodyParameter("total", String.valueOf(i))
                .addBodyParameter("visitor_id", id)
                .addBodyParameter("ticket_id", "WS03")
                .addBodyParameter("employee_id", prefManager.getIdUser())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sukses", "berhasil :"+qty);
                        try {
                            if (response.getString("status").equalsIgnoreCase("true")){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("gagal", id+"gagal :"+anError + idW);
                    }
                });
    }
}
