package com.example.payment.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.payment.R;
import com.example.payment.activity.ActivityQr;
import com.example.payment.activity.ActivityRegist;
import com.example.payment.activity.Login;
import com.example.payment.util.ApiServer;
import com.example.payment.util.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class Profil extends Fragment {
    Button btnLogin, btnRegist;
    LinearLayout login, notLogin;
    PrefManager prefManager;
    TextView txtNama, txtSaldo, txtEmail;
    TextView btnQr, btnEdit, btnLogout;
    CardView cardSaldoo;

    SwipeRefreshLayout swProfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profil, container, false);
        AndroidNetworking.initialize(getContext());
        prefManager = new PrefManager(getContext());
        txtNama = view.findViewById(R.id.txtNama);
        txtSaldo = view.findViewById(R.id.txtSaldo);
        txtEmail = view.findViewById(R.id.txtEmail);

        swProfil = view.findViewById(R.id.swProfil);
        swProfil.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProfil();
            }
        });

        btnQr = view.findViewById(R.id.btnShowQr);
        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityQr.class);
                startActivity(intent);
            }
        });
        btnEdit = view.findViewById(R.id.btnEditProfil);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getContext(), ActivityRegist.class);
                startActivity(intent1);
            }
        });
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setNamaUser("");
                prefManager.setIdUser("");
                prefManager.setJenisUser("");
                prefManager.setLoginStatus(false);
                login.setVisibility(View.GONE);
                notLogin.setVisibility(View.VISIBLE);
            }
        });

        btnRegist = view.findViewById(R.id.btnRegist);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityRegist.class);
                startActivity(intent);
            }
        });



        cardSaldoo = view.findViewById(R.id.saldoo);
        if (prefManager.getJenisUser().equalsIgnoreCase("1")){
            btnQr.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            cardSaldoo.setVisibility(View.GONE);
        }else {
            btnQr.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            cardSaldoo.setVisibility(View.VISIBLE);

        }


        prefManager =new PrefManager(getContext());
        login = view.findViewById(R.id.layoutLogin);
        notLogin = view.findViewById(R.id.layoutNotLogin);
        if (prefManager.getLoginStatus()){
            login.setVisibility(View.VISIBLE);
            notLogin.setVisibility(View.GONE);
            if (prefManager.getJenisUser().equalsIgnoreCase("1")){
                txtNama.setText(prefManager.getNamaUser());
                txtEmail.setText(prefManager.getEmailUser());
            }else {
                getProfil();
                txtSaldo.setText(prefManager.getSaldoUser());

            }
//            txtNama.setText(prefManager.getNamaUser());
        }else {
            login.setVisibility(View.GONE);
            notLogin.setVisibility(View.VISIBLE);
        }


        btnLogin = view.findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.VISIBLE);
                notLogin.setVisibility(View.GONE);
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });


        return view;
    }
    private void getProfil() {
        swProfil.setRefreshing(true);
        login.setVisibility(View.GONE);
        notLogin.setVisibility(View.GONE);

        prefManager = new PrefManager(getContext());
        AndroidNetworking.get(ApiServer.get_profil+prefManager.getIdUser())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            swProfil.setRefreshing(false);
                            login.setVisibility(View.VISIBLE);
                            notLogin.setVisibility(View.GONE);

                            if (response.getString("status").equalsIgnoreCase("true")){
                                JSONObject data = response.getJSONObject("data");
                                Locale locale = new Locale("in", "ID");
                                final NumberFormat formatId = NumberFormat.getCurrencyInstance(locale);

                                txtNama.setText(data.getString("visitor_name"));
                                txtSaldo.setText(formatId.format((double)data.getInt("saldo")));
                                txtEmail.setText(data.getString("email"));
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


}
