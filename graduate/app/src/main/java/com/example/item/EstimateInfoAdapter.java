package com.example.item;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.Totheking;
import com.example.account.LoginActivity;
import com.example.activity.EstimateInfoActivity;
import com.example.activity.PartInfoActivity;
import com.example.etc.ListModelType;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;
import com.example.module.EstimateInfoPart;
import com.example.module.MoneyFormat;
import com.example.module.RecyclerViewModelBase;
import com.example.module.ReviewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EstimateInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    EstimateInfoActivity activity;
    ArrayList<RecyclerViewModelBase> arr;
    String code;

    public EstimateInfoAdapter(EstimateInfoActivity f, ArrayList<RecyclerViewModelBase> arr, String code){
        activity = f;
        this.arr = arr;
        this.code = code;
    }

    @Override //1은 파트 2는 리뷰읽기 3은 리뷰쓰기
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view;
        ListModelType t = ListModelType.getType(type);
        switch(t){
            case NONE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ttt_none, parent, false);
                NoneHolder nitem = new NoneHolder(view);
                return nitem;
            case TITLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estimate_info_title_item, parent, false);
                TitleHolder item = new TitleHolder(view);
                return item;
            case ESTIMATE_INFO_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estimate_info_item, parent, false);
                EstimateHolder eholder = new EstimateHolder(view);
                return eholder;
            case ESTIMATE_INFO_PART_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estimate_info_part_item, parent, false);
                PartHolder pholder1 = new PartHolder(view);
                return pholder1;
            case ESTIMATE_INFO_REVIEW_READ:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_read_item, parent, false);
                ReviewRHolder rrholder = new ReviewRHolder(view);
                return rrholder;
            case ESTIMATE_INFO_REVIEW_WRITE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_write_item, parent, false);
                ReviewWHolder rwholder = new ReviewWHolder(view);
                return rwholder;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewModelBase data = arr.get(position);
        return data.getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        ListModelType type = ListModelType.getType(arr.get(i).getViewType());
        RecyclerViewModelBase data = arr.get(i);
        switch(type){
            case NONE:
                break;
            case TITLE:
                TitleHolder tholder = (TitleHolder) holder;
                TitleItem titem = (TitleItem) data;
                tholder.title.setText(titem.title);
                break;
            case ESTIMATE_INFO_ITEM:
                EstimateHolder eholder = (EstimateHolder) holder;
                EstimateInfoItem eitem = (EstimateInfoItem) data;
                eholder.code.setText(eitem.estimate.code);
                eholder.minp.setText(MoneyFormat.formatter.format(eitem.estimate.price)+"원");
                eholder.rating.setRating(eitem.estimate.score);
                eholder.tag.setText(eitem.estimate.tag);
                break;
            case ESTIMATE_INFO_PART_ITEM:
                PartHolder pholder = (PartHolder) holder;
                final EstimateInfoPart pmodel = (EstimateInfoPart) data;
                pholder.category.setText(pmodel.part.category.getCategoryName());
                pholder.name.setText(pmodel.part.name);
                pholder.maxP.setText("최고가 : "+ MoneyFormat.formatter.format(pmodel.part.maxPrice)+ "원");
                pholder.minP.setText("최저가 : "+ MoneyFormat.formatter.format(pmodel.part.minPrice)+"원");
                pholder.select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "부품이 선택되었습니다.", Toast.LENGTH_SHORT).show();
                        Totheking app = (Totheking) activity.getApplication();
                        app.setEstimate(pmodel.part.category, pmodel.part);
                    }
                });
                break;
            case ESTIMATE_INFO_REVIEW_READ:
                ReviewRHolder rrholder = (ReviewRHolder) holder;
                final ReviewModel rmodel = (ReviewModel) data;
                rrholder.nick_name.setText(rmodel.nickname);
                rrholder.date.setText(rmodel.date);
                rrholder.review_read.setText(rmodel.review);
                rrholder.ratio.setRating(rmodel.score);
                rrholder.modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Totheking app = (Totheking) activity.getApplication();
                        if(!app.isLogin()){
                            new AlertDialog.Builder(activity)
                                    .setMessage("로그인이 필요한 작업입니다.")
                                    .setPositiveButton("로그인하러가기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent registerIntent = new Intent(activity, LoginActivity.class);
                                            activity.startActivity(registerIntent);
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .setCancelable(false)
                                    .show();
                            return;
                        }
                        if(app.getMemberIndex() == rmodel.wid){
                            AlertDialog.Builder ad = new AlertDialog.Builder(activity);
                            ad.setNegativeButton("취소", null);
                            ad.setCancelable(false);
                            ad.setTitle("리뷰 수정");
                            final View view = activity.getLayoutInflater().inflate(R.layout.review_write_item, null);
                            final EditText review = view.findViewById(R.id.review_write);
                            final RatingBar rating = view.findViewById(R.id.ratio);
                            Button button = view.findViewById(R.id.select);
                            view.findViewById(R.id.name).setVisibility(View.INVISIBLE);
                            review.setText(rmodel.review);
                            rating.setRating(rmodel.score);
                            button.setVisibility(View.INVISIBLE);
                            ad.setView(view);
                            ad.setNegativeButton("취소", null);
                            ad.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("index", rmodel.rID+"");
                                    param.put("score", rating.getRating()+"");
                                    param.put("review", review.getText().toString());
                                    ConnectPHP con = new ConnectPHP("http://gebalnote.pe.kr/review_edit.php", 1, param, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try{
                                                JSONObject obj = new JSONObject(response);
                                                if(obj.getBoolean("result")){
                                                    //성공적 삭제 메세지
                                                    Toast.makeText(activity, "수정 되었습니다.", Toast.LENGTH_LONG).show();
                                                    rmodel.score = (int) rating.getRating();
                                                    rmodel.review = review.getText().toString();
                                                    EstimateInfoAdapter.this.notifyDataSetChanged();
                                                }
                                                else{
                                                    Toast.makeText(activity, "이미 삭제 되었거나 삭제에 실패하였습니다.", Toast.LENGTH_LONG).show();
                                                    EstimateInfoAdapter.this.notifyDataSetChanged();
                                                }
                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }, activity);
                                }
                            });
                            ad.show();
                            return;
                        }
                        else{
                            Toast.makeText(activity, "본인의 리뷰가 아닙니다.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
                rrholder.report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Totheking app = (Totheking) activity.getApplication();
                        if(app.getMemberIndex() == rmodel.wid){
                            HashMap<String, String> param = new HashMap<>();
                            param.put("index", rmodel.rID + "");
                            ConnectPHP con = new ConnectPHP("http://gebalnote.pe.kr/review_delete.php", 1, param, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject obj = new JSONObject(response);
                                        if(obj.getBoolean("result")){
                                            //성공적 삭제 메세지
                                            Toast.makeText(activity, "삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                            arr.remove(i);
                                            EstimateInfoAdapter.this.notifyDataSetChanged();
                                            return;
                                        }
                                        else{
                                            Toast.makeText(activity, "이미 삭제 되었거나 삭제에 실패하였습니다.", Toast.LENGTH_LONG).show();
                                            EstimateInfoAdapter.this.notifyDataSetChanged();
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }, activity);
                        }
                    }
                });
                break;
            case ESTIMATE_INFO_REVIEW_WRITE:
                final ReviewWHolder rwholder = (ReviewWHolder) holder;
                rwholder.select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String review = rwholder.review_write.getText().toString();
                        int score = (int)rwholder.ratio.getRating();
                        Totheking ttk = (Totheking) activity.getApplication();
                        int writer;
                        if(!ttk.isLogin()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage("로그인 후 이용해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(activity, LoginActivity.class);
                                            activity.startActivity(intent);
                                            activity.finish();
                                            return;
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .create()
                                    .show();
                            return;
                        }
                        writer = ttk.getMemberIndex();

                        if(score == 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage("점수를 입력하세요")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();
                            return;
                        }

                        Response.Listener<String> listener;
                        listener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                                try{
                                    JSONObject obj = new JSONObject(response);
                                    if(obj.getBoolean("result")){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("정상적으로 입력되었습니다.")
                                                .setPositiveButton("확인", null)
                                                .create()
                                                .show();
                                        activity.loadData();
                                    }
                                    else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("입력 실패하였습니다.\n실패메세지:"+obj.getString("error"))
                                                .setPositiveButton("확인", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        Map<String, String> param = new HashMap<>();
                        param.put("code", code);
                        param.put("score", score+"");
                        param.put("writer", writer+"");
                        param.put("review", review);
                        ConnectPHP php = new ConnectPHP("http://gebalnote.pe.kr/review_write.php", 1, param, listener, activity);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(arr == null)
            return 0;
        return arr.size();
    }

    class PartHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView minP;
        TextView maxP;
        Button select;
        TextView category;

        public PartHolder(@NonNull View item) {
            super(item);
            name = (TextView) item.findViewById(R.id.name);
            minP = (TextView) item.findViewById(R.id.minP);
            maxP = (TextView) item.findViewById(R.id.maxP);
            category = item.findViewById(R.id.estimate_category);
            select = item.findViewById((R.id.select));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent infoIntent = new Intent(activity, PartInfoActivity.class);
                    infoIntent.putExtra("select", ((EstimateInfoPart)arr.get(getAdapterPosition())).part.index);
                    activity.startActivity(infoIntent);
                    //activity.finish();
                }
            });
        }
    }

    //읽기 리스트
    class ReviewRHolder extends RecyclerView.ViewHolder{
        TextView nick_name;
        TextView date;
        TextView review_read;
        RatingBar ratio;
        Button modify;
        Button report;

        public ReviewRHolder(@NonNull View item) {
            super(item);
            nick_name = item.findViewById(R.id.name);
            date =  item.findViewById(R.id.date);
            review_read =  item.findViewById(R.id.review_read);
            modify = item.findViewById(R.id.modify);
            report = item.findViewById(R.id.report);
            ratio = item.findViewById(R.id.ratio);
        }
    }

    //쓰기 (마지막에)
    class ReviewWHolder extends RecyclerView.ViewHolder{
        TextView nick_name;
        EditText review_write;
        Button select;
        RatingBar ratio;

        public ReviewWHolder(@NonNull View item) {
            super(item);
            nick_name = item.findViewById(R.id.nick_name);
            review_write = item.findViewById(R.id.review_write);
            select = item.findViewById(R.id.select);
            ratio = item.findViewById(R.id.ratio);
        }
    }

    public static class TitleHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TitleHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            itemView.setEnabled(false);
        }
    }

    public static class NoneHolder extends RecyclerView.ViewHolder{

        public NoneHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setEnabled(false);
        }
    }

    public static  class EstimateHolder extends RecyclerView.ViewHolder{
        public TextView code;
        public TextView minp;
        public RatingBar rating;
        public TextView tag;
        public EstimateHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.code);
            minp = itemView.findViewById(R.id.minp);
            rating = itemView.findViewById(R.id.rating);
            tag = itemView.findViewById(R.id.tag);
        }
    }

}