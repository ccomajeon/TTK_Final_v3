package com.example.item;

import com.example.etc.ListModelType;
import com.example.module.RecyclerViewModelBase;

public class TitleItem extends RecyclerViewModelBase {
    public String title;
    public TitleItem(String title){
        this.title = title;
    }
    @Override
    public int getViewType() {
        return ListModelType.TITLE.getType();
    }
}
