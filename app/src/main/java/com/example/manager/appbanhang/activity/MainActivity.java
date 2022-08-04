package com.example.manager.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.manager.R;
import com.example.manager.appbanhang.adapter.LoaiSpAdapter;
import com.example.manager.appbanhang.adapter.SanPhamMoiAdapter;
import com.example.manager.appbanhang.model.LoaiSp;
import com.example.manager.appbanhang.model.SanPhamMoi;
import com.example.manager.appbanhang.model.User;
import com.example.manager.appbanhang.model.UserModel;
import com.example.manager.appbanhang.retrofit.ApiBanHang;
import com.example.manager.appbanhang.retrofit.RetrofitClient;
import com.example.manager.appbanhang.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi ;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbartrangchu);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        Anhxa();
        Actionbar();

        if(isConnected(this)){
            Toast.makeText(getApplicationContext()," có Internet",Toast.LENGTH_LONG).show();
            ActionbarViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();



        }else{
            Toast.makeText(getApplicationContext(),"không có Internet",Toast.LENGTH_LONG).show();
        }
    }


    private void getEventClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 3:
                        Intent donhang = new Intent(getApplicationContext(),XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 6:
                        if(Utils.user_current.getEmail().contains("admin")){
                            Intent quanli = new Intent(getApplicationContext(),QuanLiActivity.class);
                            startActivity(quanli);
                            break;
                        }
                        else {
                            Intent dangxuat = new Intent(getApplicationContext(),DangNhapActivity.class);
                            startActivity(dangxuat);
                            break;

                        }
                    case 7:
                        Intent dangxuat = new Intent(getApplicationContext(),DangNhapActivity.class);
                        startActivity(dangxuat);
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                                recyclerView.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối với server"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                )

        );
    }


    private void ActionbarViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://i.ytimg.com/vi/vMVwdSp489E/maxresdefault.jpg");
        mangquangcao.add("https://cdn.tgdd.vn/2022/06/banner/s22-800-200-800x200-1.png");
        mangquangcao.add("https://cdn.tgdd.vn/2022/06/banner/poco40-800-200-800x200.png");
        mangquangcao.add("https://cdn.tgdd.vn/2022/06/banner/800-200-800x200-105.png");

        for (int i=0;i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel ->  {
                            if(loaiSpModel.isSuccess()){
                                Toast.makeText(getApplicationContext(), loaiSpModel.getResult().get(0).getTensanpham(), Toast.LENGTH_SHORT).show();
                                mangloaisp = loaiSpModel.getResult();
                                if(Utils.user_current.getEmail().contains("admin")){
                                    mangloaisp.add(new LoaiSp("Quản lí","https://cdn-icons.flaticon.com/png/512/3273/premium/3273070.png?token=exp=1659624043~hmac=d919ff0ed1d91fb8b72943628b19161d"));
                                    mangloaisp.add(new LoaiSp("Đăng Xuất", "https://png.pngtree.com/png-vector/20190917/ourlarge/pngtree-logout-icon-vectors-png-image_1737872.jpg"));
                                    loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangloaisp);
                                    listView.setAdapter(loaiSpAdapter);
                                }
                                else {
                                    mangloaisp.add(new LoaiSp("Đăng Xuất", "https://png.pngtree.com/png-vector/20190917/ourlarge/pngtree-logout-icon-vectors-png-image_1737872.jpg"));
                                    loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangloaisp);
                                    listView.setAdapter(loaiSpAdapter);
                                }
                            }
                        }
                ));
    }

    private void Actionbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void Anhxa() {

        viewFlipper= findViewById(R.id.viewflipper);
        recyclerView = findViewById(R.id.rcvtrangchu);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        navigationView= findViewById(R.id.ngvtrangchu);
        listView= findViewById(R.id.lvtrangchu);
        drawerLayout=findViewById(R.id.drawlayouttrangchu);
        badge=findViewById(R.id.menu_sl);
        frameLayout=findViewById(R.id.framegiohang);
        search=findViewById(R.id.imgsearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(inten);
            }
        });
        //khoi tao list
        mangloaisp = new ArrayList<>();
//        // khoi tao adapter
//        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
//
//        listView.setAdapter(loaiSpAdapter);
       mangSpMoi= new ArrayList<>();
       if(Utils.manggiohang==null){
           Utils.manggiohang=new ArrayList<>();
       }else{
           int totalItem=0;
           for(int i=0;i<Utils.manggiohang.size();i++){
               totalItem=totalItem+Utils.manggiohang.get(i).getSoluong();
           }
           badge.setText(String.valueOf(totalItem));
       }
       frameLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
               startActivity(giohang);
           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem=0;
        for(int i=0;i<Utils.manggiohang.size();i++){
            totalItem=totalItem+Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wifi !=null && wifi.isConnected()|| (mobile!=null && mobile.isConnected())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}