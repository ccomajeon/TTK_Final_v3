package com.example.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.Totheking;
import com.example.etc.ListModelType;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;
import com.example.item.EstimateInfoAdapter;
import com.example.item.EstimateInfoItem;
import com.example.item.TitleItem;
import com.example.module.CategoryID;
import com.example.module.DrawUrlImageTask;
import com.example.module.MoneyFormat;
import com.example.module.Part;
import com.example.module.RecyclerViewModelBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PartItemActivity extends AppCompatActivity {

    SelectPartItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("부품별견적");
        setContentView(R.layout.part_category);

        Button part_search = findViewById(R.id.part_search);
        Button addEstimate = findViewById(R.id.estimate_add);
        final Button part_reset = findViewById(R.id.part_reset);

        RecyclerView clist = findViewById(R.id.clist);
        clist.setHasFixedSize(true);
        adapter = new SelectPartItemAdapter(this);
        clist.setAdapter(adapter);

        part_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Totheking totheking = (Totheking) getApplication();
                if (totheking.isPartBukkitSearch()) {
                    //totheking.resetPartBukkit();
                    Intent search = new Intent(PartItemActivity.this, EstimateSearchListActivity.class);
                    search.putExtra("part", totheking.partbukkitToSearchCode());
                    PartItemActivity.this.startActivity(search);
                } else {
                    new AlertDialog.Builder(PartItemActivity.this)
                            .setMessage("부품을 선택해주세요")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }
            }
        });
        addEstimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Totheking totheking = (Totheking) getApplication();
                if (!totheking.isPartBukkitSearch()) {
                    new AlertDialog.Builder(PartItemActivity.this)
                            .setMessage("부품을 선택해주세요")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(PartItemActivity.this);
                builder.setMessage("선택하신 부품들을 추가 하시겠습니까?")
                        //아무것도 선택안했을시 처리
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Totheking totheking = (Totheking) getApplication();

                                ConnectPHP php = new ConnectPHP("http://gebalnote.pe.kr/estimate_add.php", 1, totheking.getParameter(), new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //Toast.makeText(PartItemActivity.this, response + " dkdkdkdkdk", Toast.LENGTH_LONG).show();
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            if (obj.getBoolean("result")) {
                                                new AlertDialog.Builder(PartItemActivity.this)
                                                        .setMessage("성공적으로 추가되었습니다.")
                                                        .setPositiveButton("확인", null)
                                                        .create()
                                                        .show();
                                            } else {
                                                new AlertDialog.Builder(PartItemActivity.this)
                                                        .setMessage("추가하는데 에러가 발생하였습니다.\n에러메세지 : " + obj.getString("error"))
                                                        .setPositiveButton("확인", null)
                                                        .create()
                                                        .show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                }, PartItemActivity.this);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .create()
                        .show();
            }
        });
        part_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PartItemActivity.this);
                builder.setMessage("초기화 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Totheking totheking = (Totheking) getApplication();
                                totheking.cleanEstimate();
                                adapter.loadData();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(PartItemActivity.this, "초기화 되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .create()
                        .show();
                return;

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.loadData();
        adapter.notifyDataSetChanged();
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
    public static class SelectPartItem extends RecyclerViewModelBase{
        Part part;
        public SelectPartItem(Part part){
            this.part = part;
        }
        @Override
        public int getViewType() {
            return ListModelType.SELECT_PART_ITEM.getType();
        }
    }
}

class SelectPartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    ArrayList<RecyclerViewModelBase> arr;

    public SelectPartItemAdapter(Activity activity) {
        this.activity = activity;
        loadData();
    }

    public void loadData(){
        arr = new ArrayList<>();
        Totheking ttk = (Totheking) activity.getApplication();
        arr.add(new EstimateInfoItem(ttk.estimate));
        arr.add(new TitleItem("부품 리스트"));
        for(CategoryID id : CategoryID.values()){
            arr.add(new PartItemActivity.SelectPartItem(ttk.estimate.parts[id.getID()-1]));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        ListModelType t = ListModelType.getType(type);
        View view;
        switch (t){
            case TITLE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.estimate_info_title_item, viewGroup, false);
                EstimateInfoAdapter.TitleHolder tholder = new EstimateInfoAdapter.TitleHolder(view);
                return tholder;
            case ESTIMATE_INFO_ITEM:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.estimate_info_item, viewGroup, false);
                EstimateInfoAdapter.EstimateHolder pholder = new EstimateInfoAdapter.EstimateHolder(view);
                return pholder;
            case SELECT_PART_ITEM:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ttt, viewGroup, false);
                SelectPartItemHolder sholder = new SelectPartItemHolder(view);
                return sholder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        ListModelType type = ListModelType.getType(arr.get(i).getViewType());
        RecyclerViewModelBase data = arr.get(i);

        switch (type){
            case TITLE:
                EstimateInfoAdapter.TitleHolder tholder = (EstimateInfoAdapter.TitleHolder) holder;
                TitleItem titem = (TitleItem) data;
                tholder.title.setText(titem.title);
                break;
            case ESTIMATE_INFO_ITEM:
                EstimateInfoAdapter.EstimateHolder eholder = (EstimateInfoAdapter.EstimateHolder) holder;
                EstimateInfoItem eitem = (EstimateInfoItem) data;
                eholder.code.setText(eitem.estimate.getCreateCode(1));
                eholder.minp.setText(MoneyFormat.formatter.format(eitem.estimate.getCreatePrice())+"원");
                eholder.rating.setRating(eitem.estimate.score);
                eholder.tag.setText(eitem.estimate.tag);
                eholder.rating.setVisibility(View.INVISIBLE);
                break;
            case SELECT_PART_ITEM:
                final SelectPartItemHolder sholder = (SelectPartItemHolder) holder;
                final PartItemActivity.SelectPartItem p = (PartItemActivity.SelectPartItem) data;
                sholder.category.setText(CategoryID.values()[i-2].getCategoryName());
                if (p.part == null || p.part.name == null) {
                    sholder.name.setText("");
                    sholder.price.setText("클릭하여 부품을 선택해주세요.");
                    sholder.ex.setText("");
                    sholder.image.setImageResource(R.drawable.logo1_1);
                    sholder.info.setEnabled(false);
                    sholder.delete.setEnabled(false);
                    return;
                }

                sholder.name.setText(p.part.name);
                sholder.price.setText("최저가 " + MoneyFormat.formatter.format(p.part.minPrice) + "원");
                sholder.ex.setText(p.part.ex);
                sholder.info.setEnabled(true);
                sholder.delete.setEnabled(true);
                if (p != null && !p.part.image.equals("null"))
                    new DrawUrlImageTask(sholder.image).execute(p.part.image);
                sholder.info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent infoIntent = new Intent(activity, PartInfoActivity.class);
                        infoIntent.putExtra("select", p.part.index);
                        activity.startActivity(infoIntent);
                    }
                });
                sholder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Totheking ttk = (Totheking) activity.getApplication();
                        ttk.removeEstimate(i-2);
                        SelectPartItemAdapter.this.loadData();

                        sholder.info.setEnabled(false);
                        sholder.delete.setEnabled(false);
                        SelectPartItemAdapter.this.notifyDataSetChanged();
                    }
                });
        }
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewModelBase data = arr.get(position);
        return data.getViewType();
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class SelectPartItemHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public ImageView image;
        public TextView name;
        public TextView price;
        public TextView ex;
        public Button info;
        public Button delete;

        public SelectPartItemHolder(@NonNull View item) {
            super(item);
            category = item.findViewById(R.id.part_category);
            image = item.findViewById(R.id.imageView);
            name = item.findViewById(R.id.part_name);
            price = item.findViewById(R.id.part_price);
            ex = item.findViewById(R.id.title);
            info = item.findViewById(R.id.part_binfo);
            delete = item.findViewById(R.id.part_delete);
            info.setEnabled(false);
            delete.setEnabled(false);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("category", getAdapterPosition()-2);
                    activity.startActivity(intent);
                }
            });
        }
    }
}