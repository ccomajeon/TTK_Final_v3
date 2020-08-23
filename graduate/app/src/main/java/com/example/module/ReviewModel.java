package com.example.module;

import com.example.etc.ListModelType;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewModel extends RecyclerViewModelBase {
    public int rID;
    public String code;
    public String review;
    public int wid;
    public String nickname;
    public int score;
    public String date;
    public boolean write; //리뷰쓰기(true)면 3 읽기(false)면 2

    public ReviewModel(int rID, String code, String review, int wid, String nickname, int score, String date){
        this.rID = rID;
        this.code = code;
        this.review = review;
        this.wid = wid;
        this.nickname = nickname;
        this.score = score;
        this.date = date;
        this.write = false;
    }

    public ReviewModel(JSONObject obj){
        try {
            this.write=false;
            rID = obj.getInt("rIndex");
            code = obj.getString("code");
            review = obj.getString("review");
            wid = obj.getInt("wIndex");
            nickname = obj.getString("nickname");
            score = obj.getInt("score");
            date = obj.getString("date");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ReviewModel(){
        write = true;
    }

    @Override
    public int getViewType(){
        return write ? ListModelType.ESTIMATE_INFO_REVIEW_WRITE.getType() : ListModelType.ESTIMATE_INFO_REVIEW_READ.getType();
    }
}
