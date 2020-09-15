package com.example.payment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payment.R;
import com.example.payment.activity.ActivityScan;
import com.example.payment.model.ModelTiket;
import com.example.payment.util.ApiServer;
import com.example.payment.util.PrefManager;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterTiket extends RecyclerView.Adapter<AdapterTiket.ListViewHolder> {

    private List<ModelTiket> tiket;
    private Context context;

    public AdapterTiket(List<ModelTiket> tiket) {
        this.tiket = tiket;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tiket, parent, false);
        ListViewHolder holder = new ListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Locale localeId = new Locale("in", "ID");
        final NumberFormat formatId = NumberFormat.getCurrencyInstance(localeId);
        final ModelTiket model = tiket.get(position);
        holder.txtNama.setText(model.getNama());
        holder.txtHarga.setText(formatId.format((double) model.getHarga()));
        Picasso.get().load(ApiServer.get_img+model.getFoto()).into(holder.img);
        PrefManager prefManager = new PrefManager(context);
        if (prefManager.getJenisUser().equalsIgnoreCase("1")){
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityScan.class);
                    intent.putExtra("idWisata", model.getId());
                    intent.putExtra("hargaTiket", model.getHarga());
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return tiket.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtNama;
        TextView txtHarga;
        CardView cardView;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            cardView = itemView.findViewById(R.id.cardTiket);
            img = itemView.findViewById(R.id.imgTiket);
            txtHarga = itemView.findViewById(R.id.txtHarga);
            txtNama = itemView.findViewById(R.id.txtNamaTiket);
        }
    }
}
