package com.example.item;

import com.example.etc.ListModelType;
import com.example.module.Estimate;
import com.example.module.RecyclerViewModelBase;

public class EstimateInfoItem extends RecyclerViewModelBase {
    public Estimate estimate;

    public EstimateInfoItem(Estimate e){
        estimate = e;
    }

    @Override
    public int getViewType() {
        return ListModelType.ESTIMATE_INFO_ITEM.getType();
    }
}
