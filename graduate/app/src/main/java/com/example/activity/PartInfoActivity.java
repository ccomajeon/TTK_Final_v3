package com.example.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.Totheking;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;
import com.example.module.DrawUrlImageTask;
import com.example.module.MoneyFormat;
import com.example.module.Part;

import org.json.JSONArray;
import org.json.JSONObject;

public class PartInfoActivity extends AppCompatActivity {

    int index;
    Part part;
    Response.Listener<String> responseListener;

    ConnectPHP con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("상세페이지");
        setContentView(R.layout.part_info_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("select", 0);

        if (index == 0) {
            finish();
            return;
        }

        final TextView id = (TextView) findViewById(R.id.index);
        id.setText(index + "");
        final TextView name = (TextView) findViewById(R.id.name);
        final TextView link = (TextView) findViewById(R.id.link);
        final ImageView img = (ImageView) findViewById(R.id.image);
        final TextView minprice = (TextView) findViewById(R.id.minP);
        final TextView maxprice = (TextView) findViewById(R.id.maxP);
        final TextView ex = (TextView) findViewById(R.id.ex);
        Button part_add = (Button) findViewById(R.id.part_info_select);

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                con.stop();
                try {
                    JSONArray jsonarr = new JSONArray(response);
                    JSONObject t = (JSONObject) jsonarr.get(0);
                    part = new Part(t);
                    name.setText(part.name);
                    link.setText(part.link);

                    minprice.setText("최저가 " + MoneyFormat.formatter.format(part.minPrice) + "원");
                    maxprice.setText("최고가 " + MoneyFormat.formatter.format(part.maxPrice) + "원");
                    //pID.setText(model.pID+"");
                    ex.setText(part.ex + "");
                    if (!part.image.equals("null"))
                        new DrawUrlImageTask(img).execute(part.image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        loadData();

        part_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PartInfoActivity.this, "부품이 선택되었습니다.", Toast.LENGTH_SHORT).show();
                Totheking app = (Totheking) PartInfoActivity.this.getApplication();
                app.setEstimate(part.category, part);
            }
        });
    }

    public void loadData(){
        con = new ConnectPHP("http://gebalnote.pe.kr/part_select_info.php?index=" + index, 0, null, responseListener, this);
        con.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        con.stop();
    }

    @Override
    public void onBackPressed() {
        //startActivity(getSupportParentActivityIntent());
        finish();
    }
}

