package com.example.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payment.R;
import com.example.payment.model.DataHistory;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ListViewHolder> {
    private List<DataHistory> dataHistories;
    private Context context;


    public HistoryAdapter(List<DataHistory> dataHistories) {
        this.dataHistories = dataHistories;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_history, parent, false);
        ListViewHolder holder = new ListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        final DataHistory dataHistory = dataHistories.get(position);
        holder.tvDate.setText(dataHistory.getPaymentDate());
        holder.tvTicketName.setText(dataHistory.getTicketName());
        holder.tvQty.setText(dataHistory.getQty()+"pcs");
        holder.tvTotal.setText("Rp. "+dataHistory.getTotal());

    }

    @Override
    public int getItemCount() {
        return dataHistories.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTicketName, tvQty, tvTotal;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date);
            tvTicketName = itemView.findViewById(R.id.tv_ticket_name);
            tvQty = itemView.findViewById(R.id.tv_qty);
            tvTotal = itemView.findViewById(R.id.tv_total);
        }
    }
}
