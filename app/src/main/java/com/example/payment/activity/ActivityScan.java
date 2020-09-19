package com.example.payment.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.payment.MainActivity;
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

    String idVisitor = "";
    String jml;
    int saldo, hargaTiket, qty;
    String idTiket = "";

    //    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Intent intent = new Intent(getIntent());
        idVisitor = intent.getStringExtra("idVisitor");
        hargaTiket = intent.getIntExtra("hargaTiket", 0);
        idTiket = intent.getStringExtra("idTiket");

        AndroidNetworking.initialize(this);
        prefManager = new PrefManager(this);

        edIdUser = findViewById(R.id.edIdUser);
        edJml = findViewById(R.id.et_jml_beli);
        btnSubmit = findViewById(R.id.btnSubmit);

        edIdUser.setText(idVisitor);

        getSaldoUser();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                jml = edJml.getText().toString();
                qty = Integer.valueOf(jml);

//                Intent intent1 = new Intent(getIntent());
//                idTiket = intent1.getStringExtra("idTiket");

                final int total = hargaTiket * qty;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityScan.this);
                final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_detail_tiket, null);
                alertDialogBuilder.setView(view);

                TextView tvHargaTiket = view.findViewById(R.id.tv_ticket_price_pcs);
                TextView tvQty = view.findViewById(R.id.tv_quantity);
                TextView tvTotal = view.findViewById(R.id.tv_total);

                tvHargaTiket.setText("Rp. " + String.valueOf(hargaTiket));
                tvQty.setText(jml + " pcs");
                tvTotal.setText("Rp. " + String.valueOf(total));

                alertDialogBuilder
                        .setTitle("Konfirmasi pembayaran")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (total > saldo) {
                                    Toast.makeText(getApplicationContext(), "Saldo Pengunjung Tidak Mencukupi, saldo saat ini: " + saldo, Toast.LENGTH_LONG).show();
                                } else {
                                    submitTiket(qty, total);
                                }
                            }
                        });

                alertDialogBuilder
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });


        btnCam = findViewById(R.id.btnCam);
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityScan.this, ActivityCamQr.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("hargaTiket", hargaTiket);
                intent.putExtra("idTiket", idTiket);
                startActivity(intent);
                finish();
            }
        });
    }

//    Start Fungsi Menambil Saldo User
    private void getSaldoUser() {
        AndroidNetworking.get(ApiServer.get_profil + idVisitor)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONObject data = response.getJSONObject("data");
                                saldo = data.getInt("saldo");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "error :" + anError);
                    }
                });
    }
//    End Fungsi mengamnbil saldo user
    private void submitTiket(int quantity, int total) {

        prefManager = new PrefManager(this);
        AndroidNetworking.post(ApiServer.payment)
                .addBodyParameter("qty", String.valueOf(quantity))
                .addBodyParameter("total", String.valueOf(total))
                .addBodyParameter("visitor_id", idVisitor)
                .addBodyParameter("ticket_id", idTiket)
                .addBodyParameter("employee_id", prefManager.getIdUser())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sukses", "berhasil :" + idTiket);
                        try {
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                Toast.makeText(getApplicationContext(), "berhasill", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ActivityScan.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("gagal", idVisitor + "gagal :" + anError + idTiket);
                    }
                });
    }
}
