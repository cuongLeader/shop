package com.example.shop.AllActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.Adapter.adapterProduct;
import com.example.shop.R;
import com.example.shop.model.G_pr;
import com.example.shop.model.checkInternetConnect;
import com.example.shop.model.new_product;
import com.example.shop.model.onClickItem;
import com.example.shop.model.server;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class phone extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    List<new_product> myList;
    adapterProduct adapterProduct;
    ImageView btnCart;
    TextView amountCart;
    TextView btnIphone, btnSamSung, btnVsmart, btnRealme, btnHuawei, btnLG, btnXiaomi, btnOppo;
    TextView nguon;
    //initialize nestedScrollview
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        nestedScrollView = findViewById(R.id.Scroll_view);
        recyclerView = findViewById(R.id.rcl);
        progressBar = findViewById(R.id.progressBar);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);

        if (checkInternetConnect.haveNetworkConnect(getApplicationContext())) {
            myList = new ArrayList<>();
            findID();
        } else {
            checkInternetConnect.shortToat(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.phone);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.phone:
                        return true;
                    case R.id.laptop:
                        startActivity(new Intent(getApplicationContext(), laptop.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.accessories:
                        startActivity(new Intent(getApplicationContext(), accessories.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), about.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

    }


    private void findID() {
        amountCart = findViewById(R.id.amountOfCart);
        getJsonData(1);
        putDataInRecy();
        shimmerFrameLayout.startShimmer();
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    progressBar.setVisibility(View.VISIBLE);
                    putDataInRecy();
                }
            }
        });
        amountCart = findViewById(R.id.amountOfCart);
        int al = 0;
        for (int i = 0; i<MainActivity.listCart.size();i++)
        {
            al +=MainActivity.listCart.get(i).getSoLuong();
        }
        amountCart.setText(al + "");
        btnIphone = findViewById(R.id.btnIphone);
        btnSamSung = findViewById(R.id.btnSamSung);
        btnVsmart = findViewById(R.id.btnVsmart);
        btnRealme = findViewById(R.id.btnRealme);
        btnHuawei = findViewById(R.id.btnHuawei);
        btnLG = findViewById(R.id.btnLG);
        btnXiaomi = findViewById(R.id.btnXiaomi);
        btnOppo = findViewById(R.id.btnOppo);
        nguon = findViewById(R.id.nguon);
        btnIphone.setOnClickListener(this);
        btnSamSung.setOnClickListener(this);
        btnVsmart.setOnClickListener(this);
        btnRealme.setOnClickListener(this);
        btnHuawei.setOnClickListener(this);
        btnLG.setOnClickListener(this);
        btnXiaomi.setOnClickListener(this);
        btnOppo.setOnClickListener(this);
        cart();
    }

    public void ReSetList(int x, int y) {
        shimmerFrameLayout.startShimmer();
        getProductByID(x, y);
        adapterProduct = new adapterProduct(getApplicationContext(), myList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapterProduct);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    progressBar.setVisibility(View.VISIBLE);
                    getProductByID(x,y);
                }
            }
        });

       pressItem();
    }

    private void cart() {
        btnCart = findViewById(R.id.cart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(phone.this, cartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void putDataInRecy() {

        adapterProduct = new adapterProduct(getApplicationContext(), myList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(adapterProduct);
       pressItem();
    }
      void pressItem()
      {
          adapterProduct.setItemClick(new onClickItem() {
              @Override
              public void ClickImage(int ID,String name, String img, int price, String infor) {
                  Intent intent = new Intent(phone.this, inforOfProduct.class);
                  intent.putExtra("ID",ID);
                  intent.putExtra("name",name);
                  intent.putExtra("img",img);
                  intent.putExtra("price",price);
                  intent.putExtra("infor",infor);
                  startActivity(intent);
              }

              @Override
              public void onClickName(int ID,String name, String img, int price, String infor) {
                  Intent intent = new Intent(phone.this, inforOfProduct.class);
                  intent.putExtra("ID",ID);
                  intent.putExtra("name",name);
                  intent.putExtra("img",img);
                  intent.putExtra("price",price);
                  intent.putExtra("infor",infor);
                  startActivity(intent);
              }
          });
      }
    public void getJsonData(int a) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        G_pr service = retrofit.create(G_pr.class);
        Call<List<new_product>> call = service.getAllNewPr();
        call.enqueue(new Callback<List<new_product>>() {
            @Override
            public void onResponse(Call<List<new_product>> call, Response<List<new_product>> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.GONE);
                getData(response.body(), a);
            }

            @Override
            public void onFailure(Call<List<new_product>> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(phone.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(List<new_product> list, int x) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIDLoaiSanPham() == x) {
                myList.add(list.get(i));
            }
        }
    }

    public void getProductByID(int a, int b) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        G_pr service = retrofit.create(G_pr.class);
        Call<List<new_product>> call = service.getAllNewPr();
        call.enqueue(new Callback<List<new_product>>() {
            @Override
            public void onResponse(Call<List<new_product>> call, Response<List<new_product>> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.GONE);
                getData1(response.body(), a, b);
            }

            @Override
            public void onFailure(Call<List<new_product>> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                Toast.makeText(phone.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData1(List<new_product> list, int x, int y) {
       myList.clear();
        for (int i = 0; i < list.size(); i++) {
            if ( list.get(i).getIDLoaiSanPham() == y && list.get(i).getID_HangSX() == x ) {
                adapterProduct.notifyDataSetChanged();
                myList.add(list.get(i));

            }
        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnIphone:
                nguon.setText("Điện thoại/ Iphone");
                ReSetList(1, 1);
                break;
            case R.id.btnVsmart:
                nguon.setText("Điện thoại/ Vsmart");
                ReSetList(2, 1);
                break;
            case R.id.btnSamSung:
                nguon.setText("Điện thoại/ SamSung");
                ReSetList(3, 1);
                break;
            case R.id.btnRealme:
                nguon.setText("Điện thoại/ Realme");
                ReSetList(5, 1);
                break;
            case R.id.btnHuawei:
                nguon.setText("Điện thoại/ Huawei");
                ReSetList(7, 1);
                break;
            case R.id.btnLG:
                nguon.setText("Điện thoại/ LG");
                ReSetList(4, 1);
                break;
            case R.id.btnXiaomi:
                nguon.setText("Điện thoại/ Xiaomi");
                ReSetList(6, 1);
                break;
            case R.id.btnOppo:
                nguon.setText("Điện thoại/ Oppo");
                ReSetList(8, 1);
                break;
        }
    }
}