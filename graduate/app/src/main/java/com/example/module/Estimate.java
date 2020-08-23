package com.example.module;

import com.example.etc.ListModelType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Estimate extends  RecyclerViewModelBase{

    public String code;
    public Part[] parts;
    public int price;
    public float score;
    public String tag;

    public Estimate(){
        parts = new Part[8];
        price = 0;
        score = 0.0F;
        tag = "";
        for(int i=0; i<8; i++){
            parts[i] = new Part();
        }
    }

    public Estimate(JSONObject obj){
        this();
        try {
            code = obj.getString("code");
            if(!obj.getString("score").equals("null"))
                score = (float) obj.getDouble("score");
            price = obj.getInt("minP");
            JSONArray arr = new JSONArray(obj.getString("parts"));
            for(int i=0; i<arr.length(); i++){
                JSONObject jo = arr.getJSONObject(i);
                parts[jo.getInt("categoryID")-1] = new Part(jo);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public Estimate(JSONObject estimate, JSONArray parts){
        this();
        try{
            code = estimate.getString("code");
            if(!estimate.getString("score").equals("null"))
                score = (float) estimate.getDouble("score");
            price = estimate.getInt("minP");
            for(int i=0; i<parts.length(); i++){
                JSONObject jo = parts.getJSONObject(i);
                this.parts[jo.getInt("categoryID")-1] = new Part(jo);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public Estimate(String code, Part[] parts){
        this.code = code;
        this.parts = parts;
    }

    @Override
    public int getViewType() {
        return ListModelType.ESTIMATE_LIST_ITEM.getType();
    }

    public void resetParts(){
        for(int i=0; i<8; i++){
                parts[i]=new Part();
        }
    }

    public String getPartName(CategoryID index){
        if(parts[index.getID()-1]==null || parts[index.getID()-1].index == 0){
            return "없음";
        }
        return parts[index.getID()-1].name;
    }

    public String getCode(int type){
        String code = parts[0].getCode(type);
        for(int i=1; i<8; i++){
            code += "-"+parts[i].getCode(type);
        }
        return code;
    }

    public String getCreateCode(int type){
        String code = parts[0].getCode(type);
        for(int i=1; i<8; i++){
            code += "-"+parts[i].getCode(type);
        }
        return code;
    }

    public int getCreatePrice(){
        int sum=0;
        for(CategoryID id : CategoryID.values()){
            sum += parts[id.getID()-1].minPrice;
        }
        return  sum;
    }
}
