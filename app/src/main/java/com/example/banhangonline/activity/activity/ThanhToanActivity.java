package com.example.banhangonline.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banhangonline.R;
import com.example.banhangonline.activity.retrofit.ApiBanHang;
import com.example.banhangonline.activity.retrofit.RetrofitClient;
import com.example.banhangonline.activity.utils.Utils;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

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
            public void onClick(View view) {
                finish();
            }
        });

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien =getIntent().getLongExtra("tongtien",0);
        txtTongTien.setText((decimalFormat.format(tongtien) + " Đồng "));
//        txtSdtDat.setText(Utlis.user_current.getMobile());
//        txtEmaiDat.setText(Utlis.user_current.getEmail());
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = eddiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ",Toast.LENGTH_SHORT).show();
                }
                else if (str_diachi.length()<5){
                    Toast.makeText(getApplicationContext()," địa chỉ quá ngắn",Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(getApplicationContext(),"Thanh Công",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void countItem() {
        
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