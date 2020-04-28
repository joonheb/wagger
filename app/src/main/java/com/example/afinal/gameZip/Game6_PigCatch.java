package com.example.afinal.gameZip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.gameInform.GameInform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game6_PigCatch extends AppCompatActivity {

    // 객체 선언
    ImageView timer;
    TextView backText, resetText, scoreText;

    // 애니메이션
    Animation animation;

    // MediaPlayer 객체생성
    MediaPlayer mediaPlayer, mediaPlayer2;
    LinearLayout Layout0, Layout1, Layout2, Layout3;
    String str1 = "현재 점수 : ";
    String str2 = "점";

    ImageView[] img_array = new ImageView[9];

    Thread thread = null;
    List<Thread> threadList;

    Dialog dialog;
    List<Integer> viewPagerList;
    String gameTitle = "멧돼지 잡기";
    LinearLayout mainLyaout;

    final String TAG_ON = "on"; // 태그용
    final String TAG_OFF = "off";
    int score = 0;
    private int time = 0;     // 시간 초기화
    private int backupTimer = 0;
    Dialog resultDialog = null;
    Activity activity;
    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pig_catch);


        activity = this;

        mainLyaout = findViewById(R.id.mainLayout);

        timer = findViewById(R.id.timer);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.timer);

        Layout0 = findViewById(R.id.Layout0);
        Layout1 = findViewById(R.id.Layout1);
        Layout2 = findViewById(R.id.Layout2);
        Layout3 = findViewById(R.id.Layout3);

        timer.setVisibility(View.INVISIBLE);
        Layout0.setVisibility(View.INVISIBLE);
        threadList = new ArrayList<>();
        for (int i = 0; i < img_array.length; i++) {
            int resId = getResources().getIdentifier("imageView" + (i + 1), "id", getPackageName());
            img_array[i] = findViewById(resId);
            img_array[i].setImageResource(R.drawable.pig);
        }

        informGameDialog();

    }

    private void informGameDialog() {

        viewPagerList = new ArrayList<>();

        // 게임 설명 스크린샷 3장 첨부 필수.
        viewPagerList.add(R.raw.pigcatch_explain1);
        viewPagerList.add(R.raw.pigcatch_explain2);
        viewPagerList.add(R.raw.pigcatch_explain3);


        final GameInform gameInform = new GameInform(this, gameTitle, viewPagerList, 3);
        dialog = gameInform.setDialog();

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
        dialog.setCancelable(false);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface dialog) {
                if (!activity.isFinishing()) {
                    mainLyaout.setVisibility(View.VISIBLE);
                    time = gameInform.getPersonCnt();
                    backupTimer = time;
                    backText = findViewById(R.id.backText);
                    resetText = findViewById(R.id.resetText);
                    scoreText = findViewById(R.id.scoreText);

                    scoreText.setVisibility(View.INVISIBLE);
                    resetText.setVisibility(View.INVISIBLE);

                    backText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isStart) {
                                setResult(RESULT_OK);
                            }
                            finish();
                        }
                    });
                    resetText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gameStart();
                        }
                    });
                    gameStart();
                }
            }
        });

        dialog.show();
    }

    private void gameStart() {
        mediaPlayer2 = MediaPlayer.create(this, R.raw.countdown);
        isStart = true;
        Log.d("TEST", "게임스타터");
        score = 0;
        scoreText.setText("점수 : 0");
        scoreText.setVisibility(View.VISIBLE);
        Layout0.setVisibility(View.VISIBLE);
        threadList.clear();
        resultDialog = null;

        //Log.d("TEST", "layout1 : "+ Layout1.getX() +", "+Layout1.getY());
        //Log.d("TEST", "layout2 : "+ Layout2.getX() +", "+Layout2.getY());
        //Log.d("TEST", "layout3 : "+ Layout3.getX() +", "+Layout3.getY());

        for (int i = 0; i < img_array.length; i++) {
            img_array[i].setTag(TAG_OFF);
            final int temp = i;
            img_array[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getTag().toString().equals(TAG_ON)) {
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hook);
                        mediaPlayer.start();
                        Message t = new Message();
                        t.arg1 = temp;
                        hideHandler.sendMessage(t);
                        score++;
                    } else {
                        if (score <= 0) {
                            score = 0;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.miss);
                        mediaPlayer.start();
                    }
                }
            });
        }

        // 타이머 관련 이벤트 초기화
        resetText.setVisibility(View.INVISIBLE);
        thread = new Thread(new timeCheck());
        thread.start();


        for (int i = 0; i < img_array.length; i++) {
            Thread childThread = new Thread(new DThread(i));
            childThread.start();
            threadList.add(childThread);
        }
    }


    Handler showHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //img_array[msg.arg1].setImageResource(R.drawable.pig);
            img_array[msg.arg1].setTag(TAG_ON); // 올라오면 ON태그

            img_array[msg.arg1].animate().translationY(-100).setDuration(500).start();
        }
    };

    Handler hideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //img_array[msg.arg1].setImageResource(0);
            img_array[msg.arg1].setTag(TAG_OFF); // 내려오면 OFF태그
            img_array[msg.arg1].animate().translationY(0).setDuration(50).start();

            Message sm = new Message();
            sm.arg1 = score;
            handler.sendMessage(sm);
        }
    };

    public class DThread implements Runnable {
        int index = 0; //두더지 번호

        DThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            while (true) {
                if (thread.isAlive()) {
                    try {
                        Message msg1 = new Message();
                        int pigShow = new Random().nextInt(4000) + 500;
                        if (thread.isAlive()) {
                            Thread.sleep(pigShow);
                        }
                        msg1.arg1 = index;
                        showHandler.sendMessage(msg1);


                        Message msg2 = new Message();
                        int pigHide = new Random().nextInt(2000) + 1500;
                        if (thread.isAlive()) {
                            Thread.sleep(pigHide); // 돼지가 내려가있는 시간
                        }
                        msg2.arg1 = index;
                        hideHandler.sendMessage(msg2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //Log.d("TEST", "차일드 인터럽트!!");
                    }
                }
            }


        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            scoreText.setText("점수 : " + msg.arg1);
        }
    };

    public class timeCheck implements Runnable {
        @Override
        public void run() {
            for (int i = time; i >= 0; i--) {
                if (!thread.isInterrupted()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (!thread.isInterrupted()) {
                    if (i <= 0) {
                        if(mediaPlayer2.isPlaying())
                            mediaPlayer2.stop();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (timer.getAnimation() != null) {
                                    animation.cancel();
                                }
                                timer.setVisibility(View.INVISIBLE);
                                resetText.setVisibility(View.VISIBLE);
                                scoreText.setVisibility(View.INVISIBLE);
                                if(!activity.isFinishing()) {
                                    buildAlert("점수 : " + score);
                                }
                            }
                        });


                    } else if (i <= 5 && i > 0) {
                        mediaPlayer2.start();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (timer.getAnimation() == null) {
                                    timer.startAnimation(animation);
                                }
                                timer.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }


            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }

            for (int i = 0; i < threadList.size(); i++) {
                threadList.get(i).interrupt();
                Message t = new Message();
                t.arg1 = i;
                hideHandler.sendMessage(t);
                img_array[i].setOnClickListener(null);
            }

        }


    }

    private void buildAlert(String text) {
        View view = getLayoutInflater().inflate(R.layout.layout_custom_dialog, null);
        view.setBackgroundResource(R.drawable.dialog1);
        ((TextView) view.findViewById(R.id.dialog_confirm_msg)).setText(text);

        resultDialog = new Dialog(this);
        resultDialog.setContentView(view);
        resultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultDialog.isShowing()) {
                    resultDialog.dismiss();
                }
            }
        });
        resultDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        if (thread != null) {
            thread.interrupt();
        }

        if (resultDialog != null) {
            Log.d("TEST", "종료전진입~");
            if (resultDialog.isShowing()) {
                Log.d("TEST", "종료전진입22~");
                resultDialog.dismiss();
            }
        }

        if(mediaPlayer2!=null) {
            if(mediaPlayer2.isPlaying()) {
                mediaPlayer2.stop();
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