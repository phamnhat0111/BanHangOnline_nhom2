package com.example.banhangonline.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banhangonline.R;
import com.example.banhangonline.activity.retrofit.ApiBanHang;
import com.example.banhangonline.activity.retrofit.RetrofitClient;
import com.example.banhangonline.activity.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
Toolbar toolbar;
    TextView txtTongTien, txtSdtDat, txtEmaiDat ;
    EditText eddiachi;
    AppCompatButton btndathang;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    long tongtien;
    int totalItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
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
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                     tongtien = getIntent().getLongExtra("tongtien",0);
                     txtTongTien.setText(decimalFormat.format(tongtien));
                     txtEmaiDat.setText(Utils.user_current.getEmail());
                     txtSdtDat.setText(Utils.user_current.getMobile());


        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String str_diachi = eddiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)) {
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ",Toast.LENGTH_SHORT).show();

                }
                else {
                    // post data
                    String str_email = Utils.user_current.getEmail() ;
                    String str_sdt = Utils.user_current.getMobile();
                    int iduser = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.creatOrder(str_email,str_sdt,String.valueOf(tongtien),iduser,str_diachi,totalItem,new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(),"Thành công",Toast.LENGTH_LONG).show();
                                        Utils.mangmuahang.clear();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                            )
                    );
                }
            }
        });
    }

    private void countItem() {
        totalItem = 0;
        for(int i=0;i<Utils.mangmuahang.size();i++){
            totalItem = totalItem+ Utils.mangmuahang.get(i).getSoluong();
        }
    }


    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbarthanhtoan);
        txtTongTien= findViewById(R.id.txtTongTien);
        txtSdtDat = findViewById(R.id.txtSdtDat);
        txtEmaiDat = findViewById(R.id.txteEmailDat);
        eddiachi = findViewById(R.id.eddiachi);
        btndathang = findViewById(R.id.btndathang);
    }
}