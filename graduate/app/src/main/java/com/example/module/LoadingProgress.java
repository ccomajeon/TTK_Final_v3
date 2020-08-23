package com.example.module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.etc.ReloadInterface;

public class LoadingProgress {
    ProgressDialog dialog;
    ReloadInterface reload;
    AlertDialog message;
    TTKTimer timer;
    int count;
    public LoadingProgress(final Activity activity, ReloadInterface reload){
        this.reload = reload;
        timer = new TTKTimer(12000, 1000, new Runnable() {
            @Override
            public void run() {
                progressLoading();
            }
        });
        count = 0;

        dialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("잠시만 기다려 주세요");

        message = new AlertDialog.Builder(activity)
                .setMessage("데이터를 불러오는데 실패하였습니다 다시 시도해주세요")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setCancelable(false)
                .create();
    }

    public void progressLoading(){
        dialog.setMessage("잠시만 기다려주세요..(" + count+")");
        if(count > 10) {
            stopLoading();
            message.show();
        }
        count++;
    }

    public void startLoading(){
        dialog.show();
        timer.start();
    }

    public void stopLoading(){
        if(dialog.isShowing())
            dialog.dismiss();
        timer.cancel();
    }

    public void loadingComplete(){
        stopLoading();
        message.dismiss();
    }
}