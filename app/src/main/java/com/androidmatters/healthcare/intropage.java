package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class intropage extends AppCompatActivity {
    private ViewPager viewPager2;
    GetStartViewPageAdpter getStartViewPageAdpter;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_intropage);

        ArrayList<ScreenItem> screenItems = new ArrayList<>();
        screenItems.add(new ScreenItem("Doctor Chanel" ,"safe chanel We Can Protect ",R.drawable.doctor_illustration));
        screenItems.add(new ScreenItem("Quick Services" ,"We Can Protect Your Health",R.drawable.img1));
        screenItems.add(new ScreenItem("Upload Your Prescription" ,"Just simple click to Upload",R.drawable.doctor));
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        getStartViewPageAdpter = new GetStartViewPageAdpter(this,screenItems);
        viewPager2.setAdapter(getStartViewPageAdpter);

        tabLayout.setupWithViewPager(viewPager2);

    }
}