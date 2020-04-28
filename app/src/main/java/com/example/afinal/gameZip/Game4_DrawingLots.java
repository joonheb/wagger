package com.example.afinal.gameZip;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.gameInform.GameInform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game4_DrawingLots extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mainLayout;

    Button buttons[];
    TextView resultText, backText, resetText;
    int personCnt, bombCnt;

    int[] bombArr;
    List<Integer> bombList;
    String msg = null;
    int n = 0;

    Dialog dialog;
    List<Integer> viewPagerList;
    String gameTitle = "화투 뽑기";

    Activity activity;
    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_lots);

        activity = this;
        mainLayout = findViewById(R.id.mainLayout);
        backText = findViewById(R.id.backText);
        backText.setOnClickListener(this);

        resultText = findViewById(R.id.resultText);
        resultText.setVisibility(View.VISIBLE);

        resetText = findViewById(R.id.resetText);
        resetText.setVisibility(View.INVISIBLE);
        resetText.setOnClickListener(this);
        //Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        mainLayout.setVisibility(View.INVISIBLE);
        informGameDialog();

    }

    private void settingVisibility(Button[] buttons, int visibleNum) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisibility(View.GONE);
        }

        for (int i = 0; i < buttons.length; i++) {
            if (visibleNum > i) {
                buttons[i].setVisibility(View.VISIBLE);
            }
        }
    }

    private void informGameDialog() {

        viewPagerList = new ArrayList<>();

        // 게임 설명 스크린샷 3장 첨부 필수.
        viewPagerList.add(R.raw.drawinglots_explain1);
        viewPagerList.add(R.raw.drawinglots_explain2);
        viewPagerList.add(R.raw.drawinglots_explain3);


        final GameInform gameInform = new GameInform(this, gameTitle, viewPagerList, 1);
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
                    bombCnt = gameInform.getBombCnt();
                    personCnt = gameInform.getPersonCnt();

                    startGame();
                }
            }
        });

        dialog.show();

    }

    private void startGame() {
        isStart = true;
        bombArr = new int[bombCnt];
        bombList = new ArrayList<>();
        Random rc = new Random();
        for (int i = 0; i < bombCnt; i++) {
            bombArr[i] = rc.nextInt(personCnt) + 1;
            for (int j = 0; j < i; j++) {
                if (bombArr[i] == bombArr[j]) {
                    i--;
                }
            }
        }
        for (int value : bombArr) {
            bombList.add(value);
        }

        buttons = new Button[9];
        for (int i = 0; i < buttons.length; i++) {
            int resId = getResources().getIdentifier("button" + (i + 1), "id", getPackageName());
            buttons[i] = findViewById(resId);
            buttons[i].setBackgroundResource(R.drawable.back);
            buttons[i].setTag("close");

            final int temp = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    n = temp +1;
                    anim(n, compare());
                }
            });
        }
        settingVisibility(buttons, personCnt);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backText:
                if (isStart) {
                    setResult(RESULT_OK);
                }

                finish();
                break;
            case R.id.resetText:
                startGame();
                resetText.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private boolean compare() {
        boolean isBomb = false;
        for (int j = 0; j < bombCnt; j++) {
            if (bombList.get(j) == n) {
                msg = "당첨!!";
                isBomb = true;
                break;
            } else {
                msg = "휴~";
            }
        }
        return isBomb;
    }

    private void anim(final int animNum, final boolean isBomb)  {

        buttons[animNum - 1].setTag("open");
        ObjectAnimator animator = ObjectAnimator.ofFloat(buttons[animNum - 1], "rotationY", 0, 180);
        animator.setDuration(2000);
        animator.start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isBomb) {
                            buttons[animNum - 1].setBackgroundResource(R.drawable.bomb);
                        } else {
                            buttons[animNum - 1].setBackgroundResource(R.drawable.card3);
                        }
                        resultText.setText(msg);
                    }
                });

            }
        }).start();
        buttons[animNum-1].setOnClickListener(null);

        int finishCnt = 0;
        for(int i=0; i<buttons.length; i++) {
            if(buttons[i].getTag().equals("open")) {
               finishCnt++;
            }
        }
        Log.d("TEST2", "finish : "+finishCnt);


        if(finishCnt == personCnt) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resetText.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            }
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
