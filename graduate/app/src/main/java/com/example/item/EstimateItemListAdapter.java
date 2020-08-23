package com.example.item;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.Totheking;
import com.example.activity.DibbsActivity;
import com.example.activity.EstimateInfoActivity;
import com.example.etc.ListModelType;
import com.example.graduate.R;
import com.example.module.CategoryID;
import com.example.module.Estimate;
import com.example.module.MoneyFormat;
import com.example.module.RecyclerViewModelBase;

import java.util.ArrayList;

public class EstimateItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
    ArrayList<RecyclerViewModelBase> arr;
    boolean dib = false;

    public EstimateItemListAdapter(Activity f, ArrayList<RecyclerViewModelBase> arr){
        activity = f;
        this.arr = arr;
    }

    public EstimateItemListAdapter(Activity f, ArrayList<RecyclerViewModelBase> arr, boolean dib){
        activity = f;
        this.arr = arr;
        this.dib = dib;
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewModelBase data = arr.get(position);
        return data.getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        ListModelType t = ListModelType.getType(type);
        View view;
        switch(t){
            case NONE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ttt_none, parent, false);
                EstimateInfoAdapter.NoneHolder nholder = new EstimateInfoAdapter.NoneHolder(view);
                return nholder;
            case ESTIMATE_LIST_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estimate_list_item, parent, false);
                EstimateItemListAdapter.EstimateItemViewHolder viewHolder = new EstimateItemListAdapter.EstimateItemViewHolder(view);
                return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
        ListModelType type = ListModelType.getType(arr.get(i).getViewType());
        RecyclerViewModelBase d = arr.get(i);

        switch (type){
            case NONE:
                break;
            case ESTIMATE_LIST_ITEM:
                final Estimate data = (Estimate) d;
                final int index = i;
                EstimateItemListAdapter.EstimateItemViewHolder viewHolder = (EstimateItemListAdapter.EstimateItemViewHolder) holder;
                for(CategoryID id: CategoryID.values()){
                    if(!data.getPartName(id).equals("없음")){
                        viewHolder.tlist[id.getID()-1].setTextColor(ContextCompat.getColor(activity, R.color.fontmain));
                    }
                    viewHolder.tlist[id.getID()-1].setText(id.getCategoryName() + " : " + data.getPartName(id));
                }
                viewHolder.price.setText("최저가 합산 : " + MoneyFormat.formatter.format(data.price) + "원");
                viewHolder.ratio.setRating(data.score);
                if(!dib) {
                    viewHolder.favorit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Totheking ttk = (Totheking) activity.getApplication();
                            if (ttk.addBukkit(data.code)) {
                                final Snackbar snackbar = Snackbar.make(v, "성공적으로 추가되었습니다.", Snackbar.LENGTH_LONG);

                                snackbar.setAction("찜목록으로 가기", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent registerIntent = new Intent(activity, DibbsActivity.class);
                                        activity.startActivity(registerIntent);
                                        snackbar.dismiss();
                                    }
                                })
                                .show();
                                return;
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                final Snackbar snackbar = Snackbar.make(v, "추가 최대 갯수를 초과했거나\n 이미 추가된 견적입니다.", Snackbar.LENGTH_LONG);

                                snackbar.setAction("찜목록으로 가기", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent registerIntent = new Intent(activity, DibbsActivity.class);
                                        activity.startActivity(registerIntent);
                                        snackbar.dismiss();
                                    }
                                }).show();
                                return;
                            }
                        }
                    });
                }
                else{
                    viewHolder.favorit.setText("삭제");
                    viewHolder.favorit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Totheking ttk = (Totheking) activity.getApplication();
                            new AlertDialog.Builder(activity)
                                    .setMessage("삭제하시겠습니까?")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(ttk.removeBukkit(data.code)){
                                                arr.remove(index);
                                                if(arr.size()==0){
                                                    arr.add(new NoneItem());
                                                }
                                                EstimateItemListAdapter.this.notifyDataSetChanged();
                                                new AlertDialog.Builder(activity)
                                                        .setMessage("삭제되었습니다.")
                                                        .setPositiveButton("확인", null)
                                                        .create()
                                                        .show();

                                                return;
                                            }
                                            else{
                                                new AlertDialog.Builder(activity)
                                                        .setMessage("이미 삭제된 견적입니다.")
                                                        .setPositiveButton("확인", null)
                                                        .create()
                                                        .show();
                                                return;
                                            }
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .create()
                                    .show();
                        }
                    });
                }
        }
    }

    @Override
    public int getItemCount() {
        if(arr == null)
            return 0;
        return arr.size();
    }

    public class EstimateItemViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView[] tlist;
        TextView cpu;
        TextView mb;
        TextView vga;
        TextView power;
        TextView cooler;
        TextView box;
        TextView ram;
        TextView hdd;
        TextView price;
        RatingBar ratio;
        Button favorit;

        public EstimateItemViewHolder(View item){
            super(item);
            img = (ImageView) item.findViewById(R.id.image);
            cpu = (TextView) item.findViewById(R.id.cpu);
            mb = (TextView) item.findViewById(R.id.dmb);
            vga = (TextView) item.findViewById(R.id.vga);
            power = (TextView) item.findViewById(R.id.power);
            cooler = (TextView) item.findViewById(R.id.cooler);
            box = (TextView) item.findViewById(R.id.box);
            ram = (TextView) item.findViewById(R.id.ram);
            hdd = (TextView) item.findViewById(R.id.hdd);
            price = (TextView) item.findViewById(R.id.price);
            ratio = item.findViewById(R.id.ratio);
            favorit = item.findViewById(R.id.favorite);
            tlist = new TextView[]{cpu, mb, vga, power, cooler, box, ram, hdd};

            item.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Estimate data = (Estimate) arr.get(getAdapterPosition());
                    Intent infoIntent = new Intent(activity, EstimateInfoActivity.class);
                    infoIntent.putExtra("code", data.code);
                    activity.startActivity(infoIntent);
                    //Toast.makeText(activity, arr.get(getAdapterPosition()).code, Toast.LENGTH_SHORT).show();
                }
            });

            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //Toast.makeText(activity, "remove " + arr.get(getAdapterPosition()).getLink(), Toast.LENGTH_SHORT).show();
                    //removeItemView(getAdapterPosition());
                    return false;
                }
            });
        }

    }
}