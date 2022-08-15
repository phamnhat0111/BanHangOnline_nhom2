package com.example.manager.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.manager.R;
import com.example.manager.appbanhang.adapter.SanPhamMoiAdapter;
import com.example.manager.appbanhang.model.EventBus.SuaXoaEvent;
import com.example.manager.appbanhang.model.SanPhamMoi;
import com.example.manager.appbanhang.retrofit.ApiBanHang;
import com.example.manager.appbanhang.retrofit.RetrofitClient;
import com.example.manager.appbanhang.utils.Utils;
import com.google.android.gms.common.api.Api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import soup.neumorphism.NeumorphCardView;


public class QuanLiActivity extends AppCompatActivity {
    ImageView img_themsp;
    RecyclerView recyclerViewQuanLi;
    List<SanPhamMoi> list;
    SanPhamMoiAdapter spAdapter;
    SanPhamMoi sanPhamSuaXoa;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Context context;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initView();
        ActionToolBar();
        initControl();
        getSpMoi();
    }

    private void initControl() {
        img_themsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ThemSPActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        toolbar= findViewById(R.id.toolbar_ql);
        recyclerViewQuanLi = findViewById(R.id.recycleview_ql);
        img_themsp = findViewById(R.id.img_themsp);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewQuanLi.setHasFixedSize(true);
        recyclerViewQuanLi.setLayoutManager(layoutManager);
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Sửa")){
            suaSanPham();
        }else if(item.getTitle().equals("Xóa")){
            xoaSanPham();
        }
        return super.onContextItemSelected(item);
    }

    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(),ThemSPActivity.class);
        intent.putExtra("sua",sanPhamSuaXoa);
        startActivity(intent);
    }

    private void xoaSanPham() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLiActivity.this);
        builder.setTitle("Xóa").setMessage("Bạn có chắc chắn muốn xóa không ");
        builder.setCancelable(true);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                compositeDisposable.add(apiBanHang.XoaSanPham(sanPhamSuaXoa.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageModel ->  {
                                    if(messageModel.isSuccess()){
                                        Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                                        getSpMoi();
                                    }else{
                                        Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                }
                        )

                );
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Hủy ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(QuanLiActivity.this,"Hủy bỏ",
                        Toast.LENGTH_SHORT).show();
                //  Cancel
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                list = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(),list);
                                recyclerViewQuanLi.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối với server"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                )

        );
    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventSuaXoa(SuaXoaEvent event){
        if(event != null){
            sanPhamSuaXoa = event.getSanPhamMoi();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}