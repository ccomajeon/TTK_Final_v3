package com.example.module;

import android.os.CountDownTimer;

public class TTKTimer extends CountDownTimer {
    Runnable runnable;
    public TTKTimer(long millisInFuture, long countDownInterval, Runnable runnable) {
        super(millisInFuture, countDownInterval);
        this.runnable = runnable;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        runnable.run();
    }

    @Override
    public void onFinish() {

    }
}
