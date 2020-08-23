package com.example.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.android.volley.Response;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;
import com.example.item.EstimateItemListAdapter;
import com.example.item.NoneItem;
import com.example.module.Estimate;
import com.example.module.RecyclerViewModelBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EstimateListActivity extends AppCompatActivity {
    ArrayList<RecyclerViewModelBase> arr;
    int offset = 0;
    final int limit = 5;

    ConnectPHP con;
    boolean slide = false;
    boolean end = false;

    RecyclerView clv;
    Response.Listener<String> listListener;
    EstimateItemListAdapter itemsAdapter;

    public EstimateListActivity() {
        listListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                slide = true;
                con.stop();
                try {
                    JSONArray jsonarr = new JSONArray(response);
                    if (jsonarr.length() > 0) {
                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject obj = jsonarr.getJSONObject(i);
                            arr.add(new Estimate(obj));
                        }
                    } else {
                        if(offset == 0){
                            if (arr.size() == 0) {
                                arr.add(new NoneItem());
                            }
                        }
                        end = true;
                    }
                    if (itemsAdapter == null) {
                        itemsAdapter = new EstimateItemListAdapter(EstimateListActivity.this, arr);
                    } else {
                        itemsAdapter.notifyDataSetChanged();
                    }
                    if (offset == 0) {
                        clv.setAdapter(itemsAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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
        setTitle("견적리스트");

        clv = (RecyclerView) findViewById(R.id.clist);
        arr = new ArrayList<>();
        clv.setHasFixedSize(true);

        loadData();

        clv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(!clv.canScrollVertically(1)){
                    if(!end){
                        if(slide){
                            offset += limit;
                            loadData();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        con.stop();
    }

    public void loadData() {
        slide = false;
        if(!end) {
            if(con != null)
                con.stop();
            con = new ConnectPHP("http://gebalnote.pe.kr/estimate_select_list.php?limit=" + limit + "&offset=" + offset, 0, null, listListener, this);
            con.start();
        }
    }
}
