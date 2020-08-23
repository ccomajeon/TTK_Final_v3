package com.example.module;

import com.example.etc.ListModelType;

import org.json.JSONException;
import org.json.JSONObject;

public class Part extends RecyclerViewModelBase{

    public int index;
    public CategoryID category;
    public String name;
    public String link;
    public String image;
    public int minPrice;
    public int maxPrice;
    public String pID;
    public String ex;
    public byte grade;
    public String socket;
    public String tag;

    public Part(int index, int categoryID, String name, String link, String image, int minPrice, int maxPrice, String pID, String ex, byte grade, String socket) {
        this.index = index;
        category = CategoryID.values()[categoryID];
        this.name = name;
        this.link = link;
        this.image = image;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.pID = pID;
        this.ex = ex;
        this.grade = grade;
        this.socket = socket;
    }

    public Part(JSONObject obj) {

        try {
            if(obj.has("categoryID"))
                category = CategoryID.values()[obj.getInt("categoryID")-1];
            if(obj.has("index"))
                index = obj.getInt("index");
            if(obj.has("name"))
                name = obj.getString("name");
            if(obj.has("link"))
                link = obj.getString("link");
            if(obj.has("image"))
                image = obj.getString("image");
            if(obj.has("minP"))
                minPrice = obj.getInt("minP");
            if(obj.has("maxP"))
                maxPrice = obj.getInt("maxP");
            if(obj.has("pID"))
                pID = obj.getString("pID");
            if(obj.has("ex"))
                ex = obj.getString("ex");
            if(obj.has("grade"))
                grade = (byte)obj.getInt("grade");
            if(obj.has("socket"))
                socket = obj.getString("socket");
            if(obj.has("tag"))
                tag = obj.getString("tag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCode(int type){
        if(index == 0){
            switch (type){
                case 1:
                    return "0000";
                case 2:
                    return "____";
            }
        }
        return String.format("%04x", index);
    }

    public String getName(){
        if(name == null)
            return "없음";
        return name;
    }

    @Override
    public int getViewType() {
        return ListModelType.PART_LIST_ITEM.getType();
    }

    public Part() {

    }
}
