package com.example.banhangonline.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki;
    EditText email, pass;
    AppCompatButton buttondangnhap;
    ApiBanHang apiBanHang ;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
    }

    private void initControl() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DangKiActivity.class);
                startActivity(intent
                );
            }
        });
        buttondangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email= email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(DangNhapActivity.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(DangNhapActivity.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                }else{
                    compositeDisposable.add(apiBanHang.dangNhap(str_email,str_pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            Utils.user_current=userModel.getResult().get(0);
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            //Toast.makeText(DangNhapActivity.this, "", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(DangNhapActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void initView() {
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki=findViewById(R.id.txtdangki);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        buttondangnhap=findViewById(R.id.btnLogin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail()!=null&&Utils.user_current.getPass()!=null){
            email.setText(Utils.user_current.getEmail());
            pass.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}