package com.example.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.android.volley.Response;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;
import com.example.item.EstimateInfoAdapter;
import com.example.item.EstimateInfoItem;
import com.example.item.TitleItem;
import com.example.module.Estimate;
import com.example.module.EstimateInfoPart;
import com.example.module.RecyclerViewModelBase;
import com.example.module.ReviewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EstimateInfoActivity extends AppCompatActivity {
    String code;
    //Estimate estimate;
    ConnectPHP con;
    RecyclerView clv;
    Response.Listener<String> listListener;
    ArrayList<RecyclerViewModelBase> arr;

    public EstimateInfoActivity() {
        listListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                con.stop();
                try {
                    JSONArray jsonarr = new JSONArray(response);
                    JSONObject estimate = jsonarr.getJSONObject(0);
                    JSONArray parts = jsonarr.getJSONArray(1);
                    JSONArray reviews = jsonarr.getJSONArray(2);
                    arr = new ArrayList<RecyclerViewModelBase>();
                    arr.add(new EstimateInfoItem(new Estimate(estimate, parts)));
                    arr.add(new TitleItem("부품정보"));
                    for(int i=0; i<parts.length(); i++){
                        JSONObject obj = parts.getJSONObject(i);
                        arr.add(new EstimateInfoPart(obj));
                    }
                    arr.add(new TitleItem("리뷰"));
                    arr.add(new ReviewModel());
                    for(int i=0; i<reviews.length(); i++){
                        JSONObject obj = reviews.getJSONObject(i);
                        arr.add(new ReviewModel(obj));
                    }
                    EstimateInfoAdapter itemsAdapter = new EstimateInfoAdapter(EstimateInfoActivity.this, arr, code);
                    clv.setAdapter(itemsAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void loadData() {
        con = new ConnectPHP("http://gebalnote.pe.kr/estimate_select_code.php?code=" + code, 0, null, listListener, this);
        con.start();
    }

    @Override
    public void finish() {
        con.cancel();
        super.finish();
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estimate_list);
        code = getIntent().getStringExtra("code");
        clv = (RecyclerView) findViewById(R.id.clist);
        clv.setHasFixedSize(true);
        setTitle("견적정보");
        loadData();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
