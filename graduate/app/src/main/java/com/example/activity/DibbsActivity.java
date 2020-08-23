package com.example.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.android.volley.Response;
import com.example.Totheking;
import com.example.etc.ReloadInterface;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;
import com.example.item.EstimateItemListAdapter;
import com.example.item.NoneItem;
import com.example.module.Estimate;
import com.example.module.LoadingProgress;
import com.example.module.RecyclerViewModelBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DibbsActivity extends AppCompatActivity {

    ArrayList<RecyclerViewModelBase> arr;
    RecyclerView clv;
    Response.Listener<String> listListener;
    LoadingProgress loading;

    public DibbsActivity() {
        listListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonarr = new JSONArray(response);
                    if (jsonarr.length() > 0) {
                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject obj = jsonarr.getJSONObject(i);
                            arr.add(new Estimate(obj));
                        }
                    }
                    else{
                        arr.add(new NoneItem());
                    }
                    final EstimateItemListAdapter itemsAdapter = new EstimateItemListAdapter(DibbsActivity.this, arr, true);
                    clv.setAdapter(itemsAdapter);
                    loading.loadingComplete();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("찜 목록");
        setContentView(R.layout.estimate_list);
        clv = (RecyclerView) findViewById(R.id.clist);
        arr = new ArrayList<>();
        clv.setHasFixedSize(true);

        loading=new LoadingProgress(this, new ReloadInterface() {
            @Override
            public void reload() {
                loadBukkit();
            }
        });
        loading.startLoading();
    }

    public void loadBukkit() {
        Totheking app = (Totheking) this.getApplication();
        String codes = app.getCodeBukkit();
        if (codes == null) {
            arr.add(new NoneItem());
            final EstimateItemListAdapter itemsAdapter = new EstimateItemListAdapter(DibbsActivity.this, arr, true);
            clv.setAdapter(itemsAdapter);
            loading.stopLoading();
            return;
        }
        else {
            HashMap<String, String> param = new HashMap<>();
            param.put("codes", codes);
            ConnectPHP con = new ConnectPHP("http://gebalnote.pe.kr/bukkit_list.php", 1, param, listListener, this);
        }
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
    public void onBackPressed() {
        finish();
    }
}
