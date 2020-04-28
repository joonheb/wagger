package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.gameZip.Game2_FlowerBomb;
import com.example.afinal.gameZip.Game1_Count10;
import com.example.afinal.gameZip.Game4_DrawingLots;
import com.example.afinal.gameZip.Game9_LadderGame;
import com.example.afinal.gameZip.Game6_PigCatch;
import com.example.afinal.gameZip.Game8_PushButton;
import com.example.afinal.gameZip.Game3_Roulette;
import com.example.afinal.gameZip.Game7_WhoseKing;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.HttpResponse;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    TextView name, coinCnt;
    Button coinGet;

    Button[] games;
    List<Class> classes;

    private RewardedAd rewardedAd;
    Activity activity;

    RewardedAdCallback adCallback;
    int cnt = 10;

    private String pref_data = "pref_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        activity = this;
        rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                adCallback = new RewardedAdCallback() {
                    public void onRewardedAdOpened() {
                        // Ad opened.
                    }

                    public void onRewardedAdClosed() {
                        rewardedAd = createAndLoadRewardedAd();
                    }

                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        // User earned reward.
                        //Log.d("TEST", " 머지? : " + reward.getAmount());
                        cnt += reward.getAmount();
                        setCoin();
                        //Log.d("TEST", "cnt : " + cnt);

                    }

                    public void onRewardedAdFailedToShow(int errorCode) {
                        // Ad failed to display
                    }
                };
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        initClass();

        initHeader();
        initMenu();

    }

    private void getCoin() {
        SharedPreferences prefs = getSharedPreferences(pref_data, MODE_PRIVATE);
        boolean check = prefs.getBoolean("check", false);
        if (!check) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("check", true);

            editor.putInt("coin", 10);
            editor.commit();
        } else {
            cnt = prefs.getInt("coin", 10);
        }
    }

    private void setCoin() {
        SharedPreferences prefs = getSharedPreferences(pref_data, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("coin", cnt);
        editor.commit();
    }

    // 헤더부분 초기화
    private void initHeader() {
        name = findViewById(R.id.name);
        Intent intent = getIntent();
        if(intent!=null) {
            String nick = getIntent().getStringExtra("nickname");
            name.setText(nick+" 님");
        }

        coinCnt = findViewById(R.id.coinCnt);
        coinGet = findViewById(R.id.coinGet);
        coinGet.setOnClickListener(this);
    }


    // intent 클래스 초기화
    private void initClass() {
        classes = new ArrayList<>();
        classes.add(Game1_Count10.class);
        classes.add(Game2_FlowerBomb.class);
        classes.add(Game3_Roulette.class);
        classes.add(Game4_DrawingLots.class);
        classes.add(null);
        classes.add(Game6_PigCatch.class);
        classes.add(Game7_WhoseKing.class);
        classes.add(Game8_PushButton.class);
        classes.add(Game9_LadderGame.class);
    }

    // 게임 메뉴 초기화
    private void initMenu() {
        games = new Button[9];
        for (int i = 0; i < games.length; i++) {
            int resId = getResources().getIdentifier("game" + String.valueOf(i + 1), "id", getPackageName());
            games[i] = findViewById(resId);
            games[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        Log.d("TEST", v.getId()+" 진입~" );
        int requestCode = 0;
        if(v.getId() == R.id.coinGet) {
            Log.d("TEST", "리워드 진입");
            createAndLoadRewardedAd();
            rewardedAd.show(activity, adCallback);
            return;
        }

        if(cnt > 0) {
            switch (v.getId()) {
                //10 초 카운트
                case R.id.game1:
                    requestCode = 1;
                    break;

                // 꽃 폭탄 돌리기
                case R.id.game2:
                    requestCode = 2;
                    break;

                // 룰렛
                case R.id.game3:
                    requestCode = 3;
                    break;

                // 화투 뽑기
                case R.id.game4:
                    requestCode = 4;
                    break;


                case R.id.game5:
                    Random rd = new Random();
                    do {
                        requestCode = rd.nextInt(classes.size()) + 1;
                    } while (requestCode == 5);
                    break;

                // 멧돼지 잡기
                case R.id.game6:
                    requestCode = 6;
                    break;

                // 왕 게임
                case R.id.game7:
                    requestCode = 7;
                    break;

                // 버튼 누르기
                case R.id.game8:
                    requestCode = 8;
                    break;

                //사다리 게임
                case R.id.game9:
                    requestCode = 9;
                    break;
            }

            intent = new Intent(this, classes.get(requestCode - 1));

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, requestCode);
            }
        } else {
            Toast.makeText(activity, "코인을 얻은 후에 이용해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            Log.d("RESULT", "진입~~~ㅇㅁㄴㅇㅁ");
            cnt--;
            setCoin();
        }
    }

    // 광고 생성기 - 현재 테스트라 나중에 수정필요
    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Log.d("TEST", "앙테스띠");
        getCoin();
        coinCnt.setText("x "+String.valueOf(cnt));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setCoin();
    }
}
