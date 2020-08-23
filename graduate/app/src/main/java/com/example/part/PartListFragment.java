package com.example.part;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;
import com.example.item.PartItemListAdapter;
import com.example.module.CategoryID;
import com.example.module.Part;
import com.example.module.RecyclerViewModelBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PartListFragment extends Fragment {
    ArrayList<RecyclerViewModelBase> arr;
    int offset = 0;
    final int limit = 15;

    ConnectPHP con;
    boolean slide = false;
    boolean end = false;

    CategoryID category;
    RecyclerView clv;
    Response.Listener<String> responseListener;
    PartItemListAdapter itemsAdapter;

    public PartListFragment() {
        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                con.stop();
                slide = true;
                if (end)
                    return;
                if (response.equals("") || response == null || response.equals("[]")) {
                    end = true;
                    return;
                }
                try {
                    JSONArray jsonarr = new JSONArray(response);
                    JSONObject t = (JSONObject) jsonarr.get(0);
                    //Toast.makeText(Mainboard.this.getActivity(), t.getString("name"), Toast.LENGTH_SHORT).show();
                    if (jsonarr.length() > 0) {
                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject obj = (JSONObject) jsonarr.get(i);

                            arr.add(new Part(obj));
                        }
                        if (itemsAdapter == null)
                            itemsAdapter = new PartItemListAdapter(PartListFragment.this.getActivity(), arr, category);
                        else {
                            itemsAdapter.notifyDataSetChanged();
                        }
                        //Toast.makeText(CASE.this.getActivity(), count+"개를 불러옴", Toast.LENGTH_SHORT).show();
                        if (offset == 0)
                            clv.setAdapter(itemsAdapter);
                    } else {
                        end = true;
                        //Toast.makeText(CPU.this.getActivity(), "너랑 나랑 이제 끝이야.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void setCategory(CategoryID category){
        this.category = category;
        //Toast.makeText(getActivity(), "일단 뜨긴함", Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        return rootView;
    }

    public void loadData(){
        slide = false;
        if(!end) {
            if (con != null)
                con.stop();
            String url = "http://gebalnote.pe.kr/part_select_list.php?category=" + category.getID() + "&limit=" + limit + "&offset=" + offset;
            //Toast.makeText(CPU.this.getActivity(), url, Toast.LENGTH_SHORT).show();
            con = new ConnectPHP(url, 0, null, responseListener, this.getActivity());
            con.start();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**/
        offset = 0;
        clv = (RecyclerView) view.findViewById(R.id.clist);
        arr = new ArrayList<RecyclerViewModelBase>();

        loadData();

        final FloatingActionButton fab_main = view.findViewById(R.id.fab_main);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clv.smoothScrollToPosition(0);
            }
        });

        clv.setLayoutManager(new LinearLayoutManager(PartListFragment.this.getActivity()));
        clv.setHasFixedSize(true);

        clv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                int firstVisible = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                if (firstVisible > 0)
                    fab_main.setVisibility(View.VISIBLE);
                else
                    fab_main.setVisibility(View.INVISIBLE);
            }
        });
    }
}
