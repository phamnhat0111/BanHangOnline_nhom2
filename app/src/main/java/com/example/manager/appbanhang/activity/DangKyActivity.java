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
    AppCompatButton btndangki;
    FirebaseAuth firebaseAuth;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    //FirebaseApp firebaseApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
         //firebaseApp = FirebaseApp.initializeApp(DangKyActivity.this);

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
    }

    private void dangKi() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_user = username.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_user)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập tên tài khoản",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập mật khẩu",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_mobile)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập số điện thoại",Toast.LENGTH_SHORT).show();
        }
        else{
            if(str_pass.equals(str_repass)){
                compositeDisposable.add(apiBanHang.dangKi(str_email,str_user,str_pass, str_mobile)
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
                                        Toast.makeText(DangKyActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(DangKyActivity.this, " Email da ton tai ", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(DangKyActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));

            }else{
                Toast.makeText(getApplicationContext(),"Mật khẩu chưa khớp",Toast.LENGTH_SHORT).show();
            }
//            Intent intent= new Intent(DangKyActivity.this, DangNhapActivity.class);
//            startActivity(intent);
        }

    }

//    private  void postData(String str_email,String str_pass,String str_user,String str_mobile){
//        // post data
//        compositeDisposable.add(apiBanHang.dangKi(str_email,str_pass,str_user,str_mobile)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        userModel -> {
//                            if(userModel.isSuccess()){
//                                Utils.user_current.setEmail(str_email);
//                                Utils.user_current.setPass(str_pass);
//                                Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }else{
//                                Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        },
//                        throwable -> {
//                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
//                        }
//                ));
//
//    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.edemail);
        pass = findViewById(R.id.edpass);
        repass = findViewById(R.id.edrepass);
        username = findViewById(R.id.eduser);
        mobile = findViewById(R.id.edmobile);
        btndangki = findViewById(R.id.btndangky);
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}