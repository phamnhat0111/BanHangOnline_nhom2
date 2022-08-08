package com.example.manager.appbanhang.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.manager.R;
import com.example.manager.appbanhang.adapter.DonHangAdapter2;
import com.example.manager.appbanhang.model.DonHang;
import com.example.manager.appbanhang.model.EventBus.DonHangEvent;
import com.example.manager.appbanhang.retrofit.ApiBanHang;
import com.example.manager.appbanhang.retrofit.RetrofitClient;
import com.example.manager.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity2 extends AppCompatActivity {
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView reDonHang;
    Toolbar toolbar;
    DonHang donHang;
    int tinhtrang = 0;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolBar();
        getOrder();


    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter2 adapter = new DonHangAdapter2(getApplicationContext(),donHangModel.getResult());
                            reDonHang.setAdapter(adapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                )

        );
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        reDonHang = findViewById(R.id.recyclerview_donhang);
        toolbar = findViewById(R.id.toolbar_xemdon);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reDonHang.setLayoutManager(layoutManager);
    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }



    private void showCustomDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_donhang,null);
        Spinner spinner = view.findViewById(R.id.spinner_dialog_don);
        AppCompatButton btndialog = view.findViewById(R.id.btn_dialog);
        List<String> list = new ArrayList<>();
        list.add("Đơn hàng đang được xử lí");
        list.add("Đơn hàng đã xử lí thành công");
        list.add("Đơn hàng đã giao cho đơn vị vận chuyển");
        list.add("Đơn hàng đã giao thành công");
        list.add("Đơn hàng đã hủy");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(adapter);
        spinner.setSelection(donHang.getTrangthai());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tinhtrang = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTinhTrangDon();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }

    private void updateTinhTrangDon() {
        compositeDisposable.add(apiBanHang.updateTinhTrang(donHang.getId(),tinhtrang )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            getOrder();
                            dialog.dismiss();
                        },
                        throwable -> {

                        }
                )
        );
    }

//    private void pushNotiToUser() {
//        // gettoken
//        compositeDisposable.add(apiBanHang.getToken(0, donHang.getIduser())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        userModel -> {
//                            if(userModel.isSuccess()){
//                                for (int i=0 ; i<userModel.getResult().size();i++){
//                                    Map<String,String> data = new HashMap<>();
//                                    data.put("title","Thông Báo");
//                                    data.put("body",trangThaiDon(tinhtrang));
//                                    NotiSendData notiSendData = new NotiSendData(userModel.getResult().get(i).getToken(),data);
//                                    ApiPushNotification apiPushNotification = RetrofitClientNoti.getInstance().create(ApiPushNotification.class);
//                                    compositeDisposable.add(apiPushNotification.sendNotification(notiSendData)
//                                            .subscribeOn(Schedulers.io())
//                                            .observeOn(AndroidSchedulers.mainThread())
//                                            .subscribe(
//                                                    notiResponse -> {
//
//                                                    },
//                                                    throwable -> {
//                                                        Log.d("logg",throwable.getMessage());
//                                                    }
//                                            )
//                                    );
//
//                                }
//                            }
//
//                        },
//                        throwable -> {
//                            Log.d("loggg",throwable.getMessage());
//                        }
//                )
//        );
//    }
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


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventDonHang(DonHangEvent event){
        if(event != null){
            donHang = event.getDonHang();
            showCustomDialog();
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