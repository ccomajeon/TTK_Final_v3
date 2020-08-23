package com.example.module;

import com.example.etc.ListModelType;

import org.json.JSONObject;

public class EstimateInfoPart extends RecyclerViewModelBase {
    public Part part;

    public EstimateInfoPart(JSONObject obj){
        part = new Part(obj);
    }

    @Override
    public int getViewType() {
        return ListModelType.ESTIMATE_INFO_PART_ITEM.getType();
    }
}
