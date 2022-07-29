package com.example.manager.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manager.R;
import com.example.manager.appbanhang.retrofit.ApiBanHang;
import com.example.manager.appbanhang.retrofit.RetrofitClient;
import com.example.manager.appbanhang.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki;
    EditText email, pass;
    AppCompatButton buttondangnhap;
    CheckBox chk1;
    ApiBanHang apiBanHang ;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String strUser, strPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
        SharedPreferences pref = getSharedPreferences("USER_FILE",MODE_PRIVATE);
        email.setText(pref.getString("USERNAME",""));
        pass.setText(pref.getString("PASSWORD",""));
        chk1.setChecked(pref.getBoolean("REMEMBER",false));

    }

    private void initControl() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DangKyActivity.class);
                startActivity(intent
                );
            }
        });
        buttondangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strUser= email.getText().toString().trim();
                strPass = pass.getText().toString().trim();
                if(TextUtils.isEmpty(strUser)){
                    Toast.makeText(DangNhapActivity.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(strPass)){
                    Toast.makeText(DangNhapActivity.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                }else{
                    compositeDisposable.add(apiBanHang.dangNhap(strUser,strPass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            Utils.user_current=userModel.getResult().get(0);
                                            rememberUser(strUser,strPass,chk1.isChecked());
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
        chk1=findViewById(R.id.chkRememberPass);
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
    private void rememberUser(String strUser, String strPass, boolean checked) {
        SharedPreferences pref = getSharedPreferences("USER_FILE",MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        if(!checked){
            edit.clear();
        }else {
            edit.putString("USERNAME",strUser);
            edit.putString("PASSWORD",strPass);
            edit.putBoolean("REMEMBER",checked);
        }edit.commit();
    }

}