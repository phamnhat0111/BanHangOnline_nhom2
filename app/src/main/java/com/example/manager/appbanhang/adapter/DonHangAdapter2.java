package com.example.manager.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.appbanhang.Interface.ItemClickListener;
import com.example.manager.appbanhang.model.DonHang;
import com.example.manager.appbanhang.model.EventBus.DonHangEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DonHangAdapter2 extends RecyclerView.Adapter<DonHangAdapter2.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listdonhang;

    public DonHangAdapter2(Context context, List<DonHang> listdonhang) {
        this.context = context;
        this.listdonhang = listdonhang;
    }

    @NonNull
    @Override
    public DonHangAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang2,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangAdapter2.MyViewHolder holder, int position) {
        DonHang donHang = listdonhang.get(position);
        holder.txtdonhang.setText("Đơn hàng :" + donHang.getId());
        holder.trangthai.setText(trangThaiDon(donHang.getTrangthai()));


        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.reChiTiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());

        // adapter chi tiết
        ChiTietAdapter chiTietAdapter = new ChiTietAdapter(context, donHang.getItem());
        holder.reChiTiet.setLayoutManager(layoutManager);
        holder.reChiTiet.setAdapter(chiTietAdapter);
        holder.reChiTiet.setRecycledViewPool(viewPool);
        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(isLongClick){
                    EventBus.getDefault().postSticky(new DonHangEvent(donHang));
                }
            }
        });
    }

    private String trangThaiDon(int status){
        String result = "";
        switch (status){
            case 0:
                result = "Đơn hàng đang được xử lí";
                break;
            case 1:
                result = "Đơn hàng đã xử lí thành công";
                break;
            case 2:
                result = "Đơn hàng đã giao cho đơn vị vận chuyển";
                break;
            case 3:
                result = "Đơn hàng đã giao thành công";
                break;
            case 4:
                result = "Đơn hàng đã hủy";
                break;
        }
        return result;
    }
    @Override
    public int getItemCount() {
        return listdonhang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtdonhang ,trangthai;
        RecyclerView reChiTiet;
        ItemClickListener listener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            trangthai = itemView.findViewById(R.id.txttrangthai);
            reChiTiet = itemView.findViewById(R.id.recyclerview_chitiet);
            itemView.setOnLongClickListener(this);
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onClick(view,getAdapterPosition(),true);
            return false;
        }
    }

}
