package com.example.manager.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manager.R;
import com.example.manager.appbanhang.retrofit.ApiBanHang;
import com.example.manager.appbanhang.retrofit.RetrofitClient;
import com.example.manager.appbanhang.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText email, pass, repass,username, mobile;
    AppCompatButton btndangki,btnhuy;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControll();
    }

    private void initControll() {
        btndangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi();
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void dangKi() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_user = username.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)|| TextUtils.isEmpty(str_user)
                ||TextUtils.isEmpty(str_pass)||TextUtils.isEmpty(str_repass)
        ||TextUtils.isEmpty(str_mobile)){
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đủ thông tin ",Toast.LENGTH_SHORT).show();
        }
        else if(str_pass.length()<6){
            Toast.makeText(getApplicationContext(),"Mật Khẩu phải trên 6 kí tự ",Toast.LENGTH_SHORT).show();}
        else if(str_mobile.length()!=10){
            Toast.makeText(getApplicationContext(),"Nhập chưa đúng Số điện thoại",Toast.LENGTH_SHORT).show();
        }
        else{
            if(str_pass.equals(str_repass)){
                compositeDisposable.add(apiBanHang.dangKi(str_email,str_pass,str_user, str_mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if(userModel.isSuccess()){
//                                        Utils.user_current=userModel.getResult().get(0);
//                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
                                        Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(DangKyActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(DangKyActivity.this, " Email đã có người khác sử dung, vui lòng nhập mail khác ", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(DangKyActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));

            }else{
                Toast.makeText(getApplicationContext(),"Mật khẩu chưa khớp",Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.edemail);
        pass = findViewById(R.id.edpass);
        repass = findViewById(R.id.edrepass);
        username = findViewById(R.id.eduser);
        mobile = findViewById(R.id.edmobile);
        btndangki = findViewById(R.id.btndangky);
        btnhuy =findViewById(R.id.btnhuy);
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}