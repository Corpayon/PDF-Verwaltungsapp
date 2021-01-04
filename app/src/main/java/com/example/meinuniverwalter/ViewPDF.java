package com.example.meinuniverwalter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.Arrays;

public class ViewPDF extends AppCompatActivity {

    String pathFromPDFHandler;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pathFromPDFHandler = getIntent().getStringExtra("saveDir");
        viewPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PDFPagerAdapter(this,new File(pathFromPDFHandler));
        viewPager.setAdapter(pagerAdapter);
    }

}
