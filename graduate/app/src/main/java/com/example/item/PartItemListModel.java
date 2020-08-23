package com.example.item;

import java.io.Serializable;

public class PartItemListModel implements Serializable {

    public int index;
    public String name;
    public int minPrice;
    public int maxPrice;
    public String tag;

    public PartItemListModel(int index, String name, int minPrice, int maxPrice, String tag){
        this.index = index;
        this.name = name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.tag = tag;
    }

    public String getName(){
        return name;
    }

    public String getminP(){
        return minPrice +"";
    }

    public String getmaxP(){
        return maxPrice +"";
    }
}
