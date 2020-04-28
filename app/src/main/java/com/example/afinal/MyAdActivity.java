package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MyAdActivity extends AppCompatActivity {
    private RewardedAd rewardedAd;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ad);
        activity = this;

        rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                RewardedAdCallback adCallback = new RewardedAdCallback() {
                    public void onRewardedAdOpened() {
                        // Ad opened.
                    }

                    public void onRewardedAdClosed() {
                        // Ad closed.
                        setResult(-100);
                        finish();
                    }

                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        // User earned reward.
                        Log.d("TEST", " 머지? : "+reward.getAmount());
                        setResult(999);
                        finish();
                    }

                    public void onRewardedAdFailedToShow(int errorCode) {
                        // Ad failed to display
                    }
                };
                rewardedAd.show(activity, adCallback);
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

}
