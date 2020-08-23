package com.example.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;
import com.example.item.NoneItem;
import com.example.item.PartItemListAdapter;
import com.example.module.CategoryID;
import com.example.module.Part;
import com.example.module.RecyclerViewModelBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchPartListActivity extends AppCompatActivity {
    ArrayList<RecyclerViewModelBase> arr;
    int offset = 0;
    final int limit = 15;

    boolean slide = false;
    boolean end = false;
    ConnectPHP con;

    CategoryID category;
    String search;
    RecyclerView clv;
    Response.Listener<String> responseListener;
    PartItemListAdapter itemsAdapter;

    public SearchPartListActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("검색 결과");
        setContentView(R.layout.fragment_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        category = CategoryID.values()[getIntent().getIntExtra("category", 1)-1];
        search = getIntent().getStringExtra("search");
        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                con.stop();
                slide = true;
                if (end)
                    return;
                try {
                    JSONArray jsonarr = new JSONArray(response);

                    if (jsonarr.length() > 0) {
                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject obj = (JSONObject) jsonarr.get(i);

                            arr.add(new Part(obj));
                        }

                    } else {
                        end = true;
                        if(offset==0){
                            arr.add(new NoneItem());
                        }
                    }
                    if (itemsAdapter == null) {
                        itemsAdapter = new PartItemListAdapter(SearchPartListActivity.this, arr, category);
                        clv.setAdapter(itemsAdapter);
                    }
                    else {
                        itemsAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        offset = 0;
        end = false;
        clv = findViewById(R.id.clist);
        arr = new ArrayList<RecyclerViewModelBase>();
        final FloatingActionButton fab_main = findViewById(R.id.fab_main);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clv.smoothScrollToPosition(0);
            }
        });

        clv.setLayoutManager(new LinearLayoutManager(SearchPartListActivity.this));
        clv.setHasFixedSize(true);

        clv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(!clv.canScrollVertically(1)) {
                    if (!end) {
                        if (slide) {
                            offset += limit;
                            loadData();
                        }
                    }
                }
            }
        });
    }

    public void loadData() {
        slide = false;
        if(!end) {
            if(con != null)
                con.stop();
            String url = "http://gebalnote.pe.kr/part_select_list.php?category=" + category.getID() + "&search=" + search + "&limit=" + limit + "&offset=" + offset;
            con = new ConnectPHP(url, 0, null, responseListener, this);
            con.start();
        }
    }

    @Override
    public void finish() {
        super.finish();
        con.stop();
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
