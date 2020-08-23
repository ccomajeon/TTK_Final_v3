package com.example.item;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Totheking;
import com.example.activity.PartInfoActivity;
import com.example.etc.ListModelType;
import com.example.graduate.R;
import com.example.module.CategoryID;
import com.example.module.MoneyFormat;
import com.example.module.Part;
import com.example.module.RecyclerViewModelBase;

import java.util.ArrayList;

public class PartItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
    ArrayList<RecyclerViewModelBase> arr;
    CategoryID category;

    public PartItemListAdapter(Activity f, ArrayList<RecyclerViewModelBase> arr, CategoryID category){
        activity = f;
        this.arr = arr;
        this.category = category;
    }

    /*public void setOnItemClickListener(ItemListener listener) {
        mListener = listener;
    }

    public void onScrollListener(){

    }*/

    @Override
    public int getItemViewType(int position) {
        return arr.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        ListModelType type = ListModelType.getType(i);
        View view;
        switch (type){
            case NONE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ttt_none, parent, false);
                EstimateInfoAdapter.NoneHolder nholder = new EstimateInfoAdapter.NoneHolder(view);
                return nholder;
            case PART_LIST_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.part_list_item, parent, false);
                PartViewHolder viewHolder = new PartViewHolder(view);
                return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        RecyclerViewModelBase d = arr.get(i);
        ListModelType type = ListModelType.getType(d.getViewType());
        switch (type){
            case NONE:
                break;
            case PART_LIST_ITEM:
                final Part data = (Part) d;
                PartViewHolder pHolder = (PartViewHolder) holder;
                final int in = i;

                // 데이터 결합
                pHolder.name.setText(data.name);
                pHolder.maxP.setText("최고가 : " + MoneyFormat.formatter.format(data.maxPrice) + "원");
                pHolder.minP.setText("최저가 : " + MoneyFormat.formatter.format(data.minPrice) + "원");

                pHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Totheking app = (Totheking) activity.getApplication();
                        if(app.setEstimate(category, data)){
                            Toast.makeText(activity, "부품이 선택되었습니다." + app.getSelectSocket(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(activity, "호환하지 않는 부품입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

    }

    @Override
    public int getItemCount() {
        if(arr == null)
            return 0;
        return arr.size();
    }

    public class PartViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView minP;
        TextView maxP;
        Button button;

        public PartViewHolder(View item){
            super(item);
            name = (TextView) item.findViewById(R.id.name);
            minP = (TextView) item.findViewById(R.id.minP);
            maxP = (TextView) item.findViewById(R.id.maxP);
            button = item.findViewById((R.id.select));

            item.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Toast.makeText(activity, "click " + arr.get(getAdapterPosition()).getLink(), Toast.LENGTH_SHORT).show();
                    Intent infoIntent = new Intent(activity, PartInfoActivity.class);
                    infoIntent.putExtra("select", ((Part)arr.get(getAdapterPosition())).index);
                    activity.startActivity(infoIntent);
                    //activity.finish();
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
    /*public void setData(RecyclerViewModel pName) {
        this.pName = pName;
        name.setText(pName.getmItemName());
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(pName, getAdapterPosition());
        }
        Toast.makeText(context,pName.getmItemName(),Toast.LENGTH_SHORT).show();

    }*/
}

