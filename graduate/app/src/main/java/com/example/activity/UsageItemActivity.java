package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.graduate.R;

public class UsageItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("용도별견적");
        setContentView(R.layout.usage_category);
        Button first = findViewById(R.id.first);
        Button second = findViewById(R.id.second);
        Button third = findViewById(R.id.third);
        Button fourth = findViewById(R.id.fourth);
        Button fifth = findViewById(R.id.fifth);


        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(UsageItemActivity.this, EstimateSearchListActivity.class);
                search.putExtra("grade", "1,2,3,4,5");
                UsageItemActivity.this.startActivity(search);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(UsageItemActivity.this, EstimateSearchListActivity.class);
                search.putExtra("grade", "3,4,5,6");
                UsageItemActivity.this.startActivity(search);
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(UsageItemActivity.this, EstimateSearchListActivity.class);
                search.putExtra("grade", "5,6,7");
                UsageItemActivity.this.startActivity(search);
            }
        });

        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(UsageItemActivity.this, EstimateSearchListActivity.class);
                search.putExtra("grade", "6,7,8");
                UsageItemActivity.this.startActivity(search);
            }
        });

        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(UsageItemActivity.this, EstimateSearchListActivity.class);
                search.putExtra("grade", "8,9,10");
                UsageItemActivity.this.startActivity(search);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

