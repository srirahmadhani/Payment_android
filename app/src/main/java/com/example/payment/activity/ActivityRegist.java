package com.example.payment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class ActivityRegist extends AppCompatActivity {
    EditText nama, alamat, email, pass;
    Button btnRegist;
    SwipeRefreshLayout swipeRefreshLayout;
    PrefManager prefManager;
    TextView label;

    int selectGen;
    RadioGroup rgGen;
    RadioButton rb;
    String gend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        AndroidNetworking.initialize(this);
        nama = findViewById(R.id.regNama);
        alamat = findViewById(R.id.regAlamat);
        email = findViewById(R.id.regEmail);
        pass = findViewById(R.id.regPas);
        btnRegist = findViewById(R.id.setRegist);
        swipeRefreshLayout = findViewById(R.id.swForm);
        label = findViewById(R.id.labelForm);

        prefManager = new PrefManager(this);
        if (prefManager.getLoginStatus()){
            getData();
            label.setText("Edit Profil");
            btnRegist.setText("Edit");
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData();
                }
            });
        }

        rgGen = findViewById(R.id.rgGen);
        rgGen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rb1){
                    gend="1";
                }else {
                    gend="2";
                }
            }
        });

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nama.getText().toString())){
                    nama.setError("isi Nama!");
                }else if (TextUtils.isEmpty(email.getText().toString())){
                    email.setError("isi email!");
                }else if (TextUtils.isEmpty(alamat.getText().toString())){
                    alamat.setError("isi Alamat!");
                }else if (TextUtils.isEmpty(pass.getText().toString())){
                    pass.setError("isi password!");
                }else {
                    if (prefManager.getLoginStatus()){
                        setEdit();
                    }else {
                        setDaftar();
                    }
                }
            }
        });
    }




    private void getData() {
        swipeRefreshLayout.setRefreshing(true);
        prefManager = new PrefManager(this);
        AndroidNetworking.get(ApiServer.get_profil+prefManager.getIdUser())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("cek", "cek:"+gend);
                            if (response.getString("status").equalsIgnoreCase("true")){
                                JSONObject data = response.getJSONObject("data");
                                nama.setText(data.getString("visitor_name"));
//                                gen.setText(data.getString("gender"));
                                alamat.setText(data.getString("address"));
                                email.setText(prefManager.getEmailUser());

                                Log.d("isi", "isi :"+data.getString("address"));
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("error", "error :" + anError  );
                    }
                });
    }



    private void setDaftar() {
        AndroidNetworking.post(ApiServer.set_regist)
                .addBodyParameter("name", nama.getText().toString())
                .addBodyParameter("gender", gend)
                .addBodyParameter("address", alamat.getText().toString())
                .addBodyParameter("email", email.getText().toString())
                .addBodyParameter("password", pass.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("sukses", "sukses :"+response + gend);
                            if (response.getString("status").equalsIgnoreCase("true")){
                                Toast.makeText(getApplicationContext(), "berhasill Regist", Toast.LENGTH_LONG).show();
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), response.getString("data"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("gagal", "gagal ="+anError);
                    }
                });
    }



    private void setEdit() {
        AndroidNetworking.post(ApiServer.edit_profil+prefManager.getIdUser())
                .addBodyParameter("name", nama.getText().toString())
                .addBodyParameter("gender", gend)
                .addBodyParameter("address", alamat.getText().toString())
                .addBodyParameter("email", email.getText().toString())
                .addBodyParameter("password", pass.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("sukses", "sukses :"+response + gend);
                            if (response.getString("status").equalsIgnoreCase("true")){
                                Toast.makeText(getApplicationContext(), "berhasill Regist", Toast.LENGTH_LONG).show();
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), response.getString("data"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("gagal", "gagal ="+anError);
                    }
                });
    }
}
