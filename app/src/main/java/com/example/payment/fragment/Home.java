package com.example.payment.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.payment.R;
import com.example.payment.adapter.AdapterTiket;
import com.example.payment.model.ModelTiket;
import com.example.payment.util.ApiServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {
    RecyclerView rvTiket;
    List<ModelTiket> tiket;
    SwipeRefreshLayout swHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AndroidNetworking.initialize(getContext());

        swHome = view.findViewById(R.id.swHome);
        swHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTiket();
            }
        });

        rvTiket = view.findViewById(R.id.rvTiket);
        rvTiket.setHasFixedSize(true);
        rvTiket.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        tiket = new ArrayList<>();

        getTiket();

        return view;
    }

    private void getTiket() {
        swHome.setRefreshing(true);
        AndroidNetworking.get(ApiServer.get_tiket)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            swHome.setRefreshing(false);
                            Log.d("sukses", "sukses :"+ response.getString("status"));
                            if (response.getString("status").equalsIgnoreCase("true")){
                                    JSONArray dataRespon = response.getJSONArray("data");
                                    for (int i =0; i< dataRespon.length(); i++){
                                        JSONObject d = dataRespon.getJSONObject(i);
                                        tiket.add(new ModelTiket(
                                                d.getString("ticket_id"),
                                                d.getString("ticket_name"),
                                                d.getInt("price"),
                                                d.getString("image")
                                        ));
                                    }
                                    AdapterTiket adapterTiket = new AdapterTiket(tiket);
                                    rvTiket.setAdapter(adapterTiket);
                                    adapterTiket.notifyDataSetChanged();
                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            swHome.setRefreshing(false);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        swHome.setRefreshing(false);
                        Log.d("sukses", "sukses :"+ anError);
                    }
                });
    }

}
