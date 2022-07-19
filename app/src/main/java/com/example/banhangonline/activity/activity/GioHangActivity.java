package com.example.banhangonline.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.banhangonline.R;
import com.example.banhangonline.activity.adapter.GioHangAdapter;
import com.example.banhangonline.activity.model.EventBus.TinhTongEvent;
import com.example.banhangonline.activity.model.GioHang;
import com.example.banhangonline.activity.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong,tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GioHangAdapter adapter ;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tinhTongTien();
    }

    private void tinhTongTien() {
        long tongtiensp =0;
        for(int i=0;i<Utils.manggiohang.size();i++){
            tongtiensp=tongtiensp+Utils.manggiohang.get(i).getGiasp()*Utils.manggiohang.get(i).getSoluong();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.manggiohang.size()==0){
            giohangtrong.setVisibility(View.VISIBLE);
        }else{
            adapter = new GioHangAdapter(getApplicationContext(),Utils.manggiohang);
            recyclerView.setAdapter(adapter);
        }

        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ThanhToanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        giohangtrong = findViewById(R.id.txtgiohangtrong);
        tongtien=findViewById(R.id.txttongtien);
        toolbar=findViewById(R.id.toobar);
        recyclerView =findViewById(R.id.recycleviewgiohang);
        btnmuahang=findViewById(R.id.btnmuahang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public  void eventTinhTien(TinhTongEvent event){
        if(event!=null){
            tinhTongTien();
        }
    }
}