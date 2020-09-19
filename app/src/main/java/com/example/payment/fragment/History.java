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
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.payment.R;
import com.example.payment.adapter.AdapterTiket;
import com.example.payment.adapter.HistoryAdapter;
import com.example.payment.model.DataHistory;
import com.example.payment.model.ModelTiket;
import com.example.payment.util.ApiServer;
import com.example.payment.util.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class History extends Fragment {
    View view;
    RecyclerView rvHistory;
    List<DataHistory> history;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout emptyLayout;
    PrefManager prefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        AndroidNetworking.initialize(getContext());
        swipeRefreshLayout = view.findViewById(R.id.swHistori);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        rvHistory = view.findViewById(R.id.rvHistory);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        history = new ArrayList<>();
        emptyLayout.setVisibility(View.VISIBLE);

        prefManager = new PrefManager(getContext());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getHistory();

            }
        });

        if (!prefManager.getLoginStatus()) {
            emptyLayout.setVisibility(View.VISIBLE);
        }


        getHistory();

        return view;
    }

    private void getHistory() {
        prefManager = new PrefManager(getContext());
        swipeRefreshLayout.setRefreshing(true);
        AndroidNetworking.get(ApiServer.payment)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("berhasil", " berhasil :" + response);
                            swipeRefreshLayout.setRefreshing(false);
                            history.clear();
                            if (prefManager.getLoginStatus()) {


                                if (response.getString("status").equalsIgnoreCase("true")) {
                                    JSONArray dataRespon = response.getJSONArray("data");
                                    for (int i = 0; i < dataRespon.length(); i++) {
                                        JSONObject d = dataRespon.getJSONObject(i);
                                        if (prefManager.getJenisUser().equalsIgnoreCase("1")) {
                                            emptyLayout.setVisibility(View.GONE);
                                            rvHistory.setVisibility(View.VISIBLE);
                                            if (d.getString("employee_id").equalsIgnoreCase(prefManager.getIdUser())) {

                                                history.add(new DataHistory(
                                                        d.getString("payment_date"),
                                                        d.getString("ticket_name"),
                                                        d.getString("qty"),
                                                        d.getString("total")
                                                ));
                                            }
                                        } else {
                                            emptyLayout.setVisibility(View.GONE);
                                            rvHistory.setVisibility(View.VISIBLE);
                                            if (d.getString("visitor_id").equalsIgnoreCase(prefManager.getIdUser())) {

                                                history.add(new DataHistory(
                                                        d.getString("payment_date"),
                                                        d.getString("ticket_name"),
                                                        d.getString("qty"),
                                                        d.getString("total")
                                                ));
                                            }
                                        }
                                    }
                                }else {
                                    emptyLayout.setVisibility(View.VISIBLE);
                                }
                                HistoryAdapter historyAdapter = new HistoryAdapter(history);
                                rvHistory.setAdapter(historyAdapter);
                                historyAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        emptyLayout.setVisibility(View.VISIBLE);
                        Log.d("gagal", "gagal :" + anError);
                    }
                });
    }

}
