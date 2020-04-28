package com.example.afinal.gameZip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.gameInform.GameInform;

import java.util.ArrayList;
import java.util.List;

public class Game8_PushButton extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mainLayout;
    Vibrator vibrator;

    TextView scoreText, backText, resetText;
    ImageView timerImage;

    ImageView touchButton;
    ImageView[] deco;

    private int time = 10;
    int score;

    String gameTitle = "버튼 누르기";
    Dialog dialog;
    List<Integer> viewPagerList;

    FrameLayout rewardLayout;
    ConstraintLayout reward1, reward2, reward3, reward4;

    Animation animation;
    Activity activity;
    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_button);

        activity = this;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mainLayout = findViewById(R.id.mainLayout);
        rewardLayout = findViewById(R.id.rewardLayout);
        reward1 = findViewById(R.id.reward1);
        reward2 = findViewById(R.id.reward2);
        reward3 = findViewById(R.id.reward3);
        reward4 = findViewById(R.id.reward4);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.timer);

        touchButton = findViewById(R.id.touchButton);
        timerImage = findViewById(R.id.timerImage);
        scoreText = findViewById(R.id.scoreText);
        backText = findViewById(R.id.backText);
        resetText = findViewById(R.id.resetText);

        deco = new ImageView[10];
        for(int i=0; i<deco.length; i++) {
            int decoId = getResources().getIdentifier("deco"+String.valueOf(i+1), "id", getPackageName());
            deco[i] = findViewById(decoId);
            deco[i].setVisibility(View.INVISIBLE);
        }

        mainLayout.setVisibility(View.INVISIBLE);
        rewardLayout.setVisibility(View.INVISIBLE);

        timerImage.setVisibility(View.GONE);
        scoreText.setVisibility(View.VISIBLE);
        backText.setVisibility(View.GONE);
        resetText.setVisibility(View.GONE);
        score = 0;

        informGameDialog();

        touchButton.setOnClickListener(this);

        backText.setOnClickListener(this);
        resetText.setOnClickListener(this);
    }

    private void informGameDialog() {
        viewPagerList = new ArrayList<>();

        // 게임 설명 스크린샷 3장 첨부 필수.
        viewPagerList.add(R.raw.pushbutton_explain1);
        viewPagerList.add(R.raw.pushbutton_explain2);
        viewPagerList.add(R.raw.pushbutton_explain3);

        final GameInform gameInform = new GameInform(this, gameTitle, viewPagerList, 0);
        dialog = gameInform.setDialog();

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
        dialog.setCancelable(false);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!activity.isFinishing()) {
                    mainLayout.setVisibility(View.VISIBLE);
                    backText.setVisibility(View.VISIBLE);
                    startThread();
                }
            }
        });

        dialog.show();
    }

    public void startThread() {
        isStart = true;
        resetText.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = time; i >= -1; i--) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int timer = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (timer == -1) {
                                touchButton.setVisibility(View.GONE);
                                backText.setVisibility(View.VISIBLE);
                                resetText.setVisibility(View.VISIBLE);
                                for(int i=0; i<deco.length; i++){
                                    deco[i].setVisibility(View.GONE);
                                }
                                if(timerImage.getAnimation() != null) {
                                    animation.cancel();
                                    vibrator.vibrate(500);
                                }
                                timerImage.setVisibility(View.INVISIBLE);

                            } else if(timer <6){
                                if(timerImage.getAnimation() == null) {
                                    timerImage.startAnimation(animation);
                                }
                                timerImage.setVisibility(View.VISIBLE);

                            } else {

                                for(int i=0; i<deco.length; i++){
                                    deco[i].setVisibility(View.VISIBLE);
                                }
                            }


                            if(score >=5 && score <40) {
                                rewardLayout.setVisibility(View.VISIBLE);
                                rewardRefresh();
                                reward1.setVisibility(View.VISIBLE);
                            } else if(score>= 40 && score < 70) {
                                rewardRefresh();
                                reward2.setVisibility(View.VISIBLE);
                            } else if(score>= 70&& score < 100) {
                                rewardRefresh();
                                reward3.setVisibility(View.VISIBLE);
                            } else if(score>=100) {
                                rewardRefresh();
                                reward4.setVisibility(View.VISIBLE);
                            } else {
                                rewardLayout.setVisibility(View.INVISIBLE);
                                rewardRefresh();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.touchButton:
                score++;
                scoreText.setText("점수 : " + score);
                scoreText.setVisibility(View.VISIBLE);
                break;
            case R.id.backText:
                if (isStart) {
                    setResult(RESULT_OK);
                }

                finish();
                break;
            case R.id.resetText:
                reset();
                break;
        }
    }

    private void reset() {
        score = 0;
        touchButton.setVisibility(View.VISIBLE);
        startThread();
        scoreText.setVisibility(View.GONE);
        timerImage.setVisibility(View.GONE);
        rewardLayout.setVisibility(View.INVISIBLE);
        rewardRefresh();
    }

    private void rewardRefresh() {
        reward1.setVisibility(View.GONE);
        reward2.setVisibility(View.GONE);
        reward3.setVisibility(View.GONE);
        reward4.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(isStart = true) {
            Log.d("RESULT", "종료전 RESULTOK");
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }
}