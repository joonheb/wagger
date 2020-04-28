package com.example.afinal.gameZip;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.gameInform.GameInform;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game2_FlowerBomb extends AppCompatActivity implements View.OnClickListener {

    // 객체 선언
    EditText editText;
    ImageView bombImage;
    TextView backText, touchText;
    // MediaPlayer 객체생성
    MediaPlayer mediaPlayer, mediaPlayer2;
    ObjectAnimator animator;
    Vibrator vibrator;

    private int count = 0;     // 시간 초기화
    private int backupCount = 0;
    private static final int MILLISINFUTURE = R.id.editText;       // "editText"초 카운팅을 하겠다.
    private static final int COUNT_DOWN_INTERVAL = 1000;      // 1초씩 깎인다.

    private CountDownTimer countDownTimer;

    Dialog dialog;
    List<Integer> viewPagerList;
    String gameTitle = "꽃 폭탄 돌리기";
    LinearLayout mainLyaout;
    Random rc;
    int randomNum;

    Activity activity;
    boolean isStart = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb);

        // 객체 초기화
        activity = this;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        editText = findViewById(R.id.editText);
        bombImage = findViewById(R.id.bombImage);
        backText = findViewById(R.id.backText);
        touchText = findViewById(R.id.resetText);
        mainLyaout = findViewById(R.id.mainLayout);

        touchText.setVisibility(View.INVISIBLE);
        // 이벤트 설정
        backText.setOnClickListener(this);
        touchText.setOnClickListener(this);
        // 화면 초기화
        //imageView1.setImageResource(R.drawable.bomb21);

        rc = new Random();
        mediaPlayer2 = MediaPlayer.create(Game2_FlowerBomb.this, R.raw.countdown);

        mainLyaout.setVisibility(View.GONE);
        informGameDialog();

    }


    private void informGameDialog() {

        viewPagerList = new ArrayList<>();

        // 게임 설명 스크린샷 3장 첨부 필수.
        viewPagerList.add(R.raw.flowerbomb_explain1);
        viewPagerList.add(R.raw.flowerbomb_explain2);
        viewPagerList.add(R.raw.flowerbomb_explain3);


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
            public void onDismiss(DialogInterface dialog) {
                if(!activity.isFinishing()) {
                    mainLyaout.setVisibility(View.VISIBLE);
                    count = gameInform.getPersonCnt();
                    backupCount = count;
                    randomNum = rc.nextInt(30)+5;
                    // 타이머 관련 이벤트 초기화
                    countDownTimer();
                    anim();
                    countDownTimer.start();
                }
            }
        });

        dialog.show();
    }

    private void countDownTimer() {
        isStart =  true;
        countDownTimer = new CountDownTimer(count*1000, COUNT_DOWN_INTERVAL) {

            public void onTick(long millisUntilFinished) {
                if(!mediaPlayer2.isPlaying()) {
                    mediaPlayer2.start();
                }
                 if(count <= 100 && count > randomNum) {
                    if(animator.getDuration()!=2500)
                        animator.setDuration(2500);
                } else if(count <= randomNum) {
                    if(animator.getDuration()!=500) {
                        animator.setDuration(500);
                        animator.setFloatValues(-30, 30);
                    }
                } else {
                    if(animator.getDuration()!=5000) {
                        animator.setDuration(5000);
                    }
                }
                count--;
                if(count==0) {
                    mediaPlayer = MediaPlayer.create(Game2_FlowerBomb.this, R.raw.bs);
                    mediaPlayer.start();
                    if(mediaPlayer2.isPlaying())
                        mediaPlayer2.stop();
                }
            }

            public void onFinish() {
                bombImage.setImageResource(R.drawable.card3_break);
                vibrator.vibrate(2000);
                // 카드 쪼개진거
                //bombImage.setImageResource();

                new ParticleSystem (Game2_FlowerBomb.this, 200, R.drawable.flower1,3000)
                        .setSpeedRange ( 0.1f , 0.8f )
                        .oneShot (bombImage, 1000);
                new ParticleSystem (Game2_FlowerBomb.this, 200, R.drawable.flower2,3000)
                        .setSpeedRange ( 0.1f , 0.8f )
                        .oneShot (bombImage, 1000);

                animator.end();
                touchText.setVisibility(View.VISIBLE);
                mediaPlayer2 = null;
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            countDownTimer.cancel();
        } catch (Exception e) {
            countDownTimer = null;
        }

        if (dialog != null) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        if(mediaPlayer2!=null) {
            if(mediaPlayer2.isPlaying()) {
                mediaPlayer2.stop();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backText:
                if(isStart) {
                    setResult(RESULT_OK);
                }
                finish();
                break;

            case R.id.resetText:
                reset();
                anim();
                break;
        }
    }

    private void reset() {
        countDownTimer.cancel();
        bombImage.setImageResource(R.drawable.card3);
        count = backupCount;
        touchText.setVisibility(View.INVISIBLE);
        randomNum = rc.nextInt(15)+3;
        countDownTimer.start();
        mediaPlayer2 = MediaPlayer.create(Game2_FlowerBomb.this, R.raw.countdown);
    }

    private void anim()  {
        animator = ObjectAnimator.ofFloat(bombImage, "rotationY", -10, 10);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
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
