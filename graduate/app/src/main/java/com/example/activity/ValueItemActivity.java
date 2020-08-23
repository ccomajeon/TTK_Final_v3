package com.example.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.graduate.R;

public class ValueItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("가격별견적");
        setContentView(R.layout.value_category);
        final EditText minP = findViewById(R.id.minP);
        final EditText maxP = findViewById(R.id.maxP);
        Button value_search = findViewById(R.id.value_search);
        Button value4080 = findViewById(R.id.value4080);
        Button value80120 = findViewById(R.id.value80120);
        Button value120160 = findViewById(R.id.value120160);
        Button value160 = findViewById(R.id.value160);

        value_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int min = Integer.parseInt(minP.getText().toString());
                    int max = Integer.parseInt(maxP.getText().toString());

                    if (min < 0 || max < 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ValueItemActivity.this);
                        builder.setMessage("잘못된 숫자입니다")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        return;
                    }
                    if (min > max) {
                        new AlertDialog.Builder(ValueItemActivity.this)
                                .setMessage("최소가격은 최대 가격보다 적어야 합니다")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        return;
                    }
                    Intent search = new Intent(ValueItemActivity.this, EstimateSearchListActivity.class);
                    search.putExtra("min", min);
                    search.putExtra("max", max);
                    ValueItemActivity.this.startActivity(search);

                } catch (NumberFormatException exc) {
                    new AlertDialog.Builder(ValueItemActivity.this)
                            .setMessage("최대 2147483647까지 입력 가능합니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    return;
                }


            }
        });

        value4080.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int min = 400000;
                    int max = 800000;
                    Intent search = new Intent(ValueItemActivity.this, EstimateSearchListActivity.class);
                    search.putExtra("min", min);
                    search.putExtra("max", max);
                    ValueItemActivity.this.startActivity(search);

                } catch (NumberFormatException exc) {
                    //숫자 오류
                    return;
                }
            }
        });

        value80120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int min = 800000;
                    int max = 1200000;
                    Intent search = new Intent(ValueItemActivity.this, EstimateSearchListActivity.class);
                    search.putExtra("min", min);
                    search.putExtra("max", max);
                    ValueItemActivity.this.startActivity(search);

                } catch (NumberFormatException exc) {
                    //숫자 오류
                    return;
                }
            }
        });

        value120160.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int min = 1200000;
                    int max = 1600000;

                    Intent search = new Intent(ValueItemActivity.this, EstimateSearchListActivity.class);
                    search.putExtra("min", min);
                    search.putExtra("max", max);
                    ValueItemActivity.this.startActivity(search);

                } catch (NumberFormatException exc) {
                    //숫자 오류
                    return;
                }
            }
        });

        value160.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int min = 1600000;
                    int max = 2147483647;

                    Intent search = new Intent(ValueItemActivity.this, EstimateSearchListActivity.class);
                    search.putExtra("min", min);
                    search.putExtra("max", max);
                    ValueItemActivity.this.startActivity(search);

                } catch (NumberFormatException exc) {
                    //숫자 오류
                    return;
                }
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

