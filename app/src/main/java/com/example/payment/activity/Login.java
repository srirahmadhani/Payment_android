package com.example.payment.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.payment.R;
import com.example.payment.util.ApiServer;
import com.example.payment.util.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    EditText edEmail, edPassword;
    Button  btnLogin;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(this);
        setContentView(R.layout.activity_login);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.setLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogin();
            }
        });
    }

    private void setLogin() {
        prefManager = new PrefManager(this);
        AndroidNetworking.post(ApiServer.get_login)
                .addBodyParameter("email", edEmail.getText().toString())
                .addBodyParameter("password", edPassword.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("sukses", "sukses :"+response.getString("status"));
                            if (response.getString("status").equalsIgnoreCase("true")){
                                Log.d("sukses", "sukses 2 :"+response.getString("status"));
                                JSONObject datalist = response.getJSONObject("data");


                                    prefManager.setIdUser(datalist.getString("id"));
                                    prefManager.setJenisUser(datalist.getString("level"));
                                    prefManager.setNamaUser(datalist.getString("name"));
                                    prefManager.setLoginStatus(true);
                                    finish();

                            }else {
                                Toast.makeText(getApplicationContext(),"EMAIL PASSWORD SALAH !", Toast.LENGTH_LONG).show();
//                                new AlertDialog.Builder(getApplicationContext())
//                                        .setTitle("PERHATIAN")
//                                        .setMessage("Password atau email salah!")
//                                        .setCancelable(false)
//                                        .setNegativeButton("OK", null)
//                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "error :"+anError);
                    }
                });
    }
}
