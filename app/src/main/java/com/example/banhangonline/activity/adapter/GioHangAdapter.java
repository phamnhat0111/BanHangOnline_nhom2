package com.example.banhangonline.activity.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.banhangonline.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhangonline.activity.Interface.ImageClickListenner;
import com.example.banhangonline.activity.model.EventBus.TinhTongEvent;
import com.example.banhangonline.activity.model.GioHang;
import com.example.banhangonline.activity.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  GioHangAdapter.MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(gioHang.getSoluong()+" ");
        if(gioHang.getHinhsp().contains("http")){
            Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_img);
        }else{
            String hinh = Utils.BASE_URL + "images/"+gioHang.getHinhsp();
            Glide.with(context).load(hinh).into(holder.item_giohang_img);
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText(decimalFormat.format((gioHang.getGiasp()))+"đ/c");
        long gia= gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia) + "Đ");

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Utils.mangmuahang.add(gioHang);
                    EventBus.getDefault().postSticky( new TinhTongEvent());
                }else{
                    for(int i = 0; i<Utils.mangmuahang.size();i++){
                        if(Utils.mangmuahang.get(i).getIdsp() == gioHang.getIdsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky( new TinhTongEvent());
                        }
                    }
                }
            }
        });


        holder.setListenner(new ImageClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {

                if(giatri==1){
                    if(gioHangList.get(pos).getSoluong() >1){
                        int soLuongMoi = gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soLuongMoi);
                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText((decimalFormat.format(gia) +"Đ"));
                        EventBus.getDefault().postSticky( new TinhTongEvent());
                    }else if(gioHangList.get(pos).getSoluong() ==1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage(" Bạn có muốn xóa sản phẩm khỏi giỏ hàng không?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky( new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                }else if( giatri==2){
                    if(gioHangList.get(pos).getSoluong() < 11){
                        int soLuongMoi = gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(soLuongMoi);
                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText((decimalFormat.format(gia)+"Đ"));
                        EventBus.getDefault().postSticky( new TinhTongEvent());
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_img,imgtru,imgcong;
        TextView item_giohang_tensp,item_giohang_gia,item_giohang_soluong,item_giohang_giasp2;
        ImageClickListenner listenner;
        CheckBox checkBox;
        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            item_giohang_img =itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia =itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp2 =itemView.findViewById(R.id.item_giohang_giasp2);
            imgtru = itemView.findViewById(R.id.item_giohang_tru);
            imgcong = itemView.findViewById(R.id.item_giohang_cong);
            checkBox = itemView.findViewById(R.id.item_giohang_check);

            // event click
            imgtru.setOnClickListener(this);
            imgcong.setOnClickListener(this);

        }

        public void setListenner(ImageClickListenner listenner) {
            this.listenner = listenner;
        }


        @Override
        public void onClick(View view) {
            if(view == imgtru){
                listenner.onImageClick(view,getAdapterPosition(),1);
            }else if(view == imgcong){
                listenner.onImageClick(view, getAdapterPosition(),2);
            }
        }
    }
}
