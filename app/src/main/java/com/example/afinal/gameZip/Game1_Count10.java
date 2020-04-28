package com.example.afinal.gameZip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.afinal.R;
import com.example.afinal.gameInform.GameInform;
import com.example.afinal.gameModel.Starter_pb_Model;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.List;


public class Game1_Count10 extends AppCompatActivity {

    LinearLayout informLayout;
    TextView title, tag, tip;
    Thread thread;

    ConstraintLayout count10Layout;
    LinearLayout scoreLayout;

    TextView textView, backText, resultText;
    ImageView imageView;

    Dialog dialog;
    int num = 10;

    Animation animation;
    boolean isTouch = false;
    double sec;

    List<Starter_pb_Model> deliveredList;
    List<TextView> scoreList;

    String gameTitle = "10초 카운트";
    String gameTag = "손은 눈보다 빠르다";

    int orderCount = 0;
    ParticleSystem particleSystem;

    List<Integer> viewPagerList;
    Dialog resultDialog = null;

    Activity activity;
    Vibrator vibrator;
    boolean isStart = false;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_count10);

        activity = this;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        textView = findViewById(R.id.timerText);
        backText = findViewById(R.id.backText);
        resultText = findViewById(R.id.resultText);
        imageView = findViewById(R.id.imageView);
        count10Layout = findViewById(R.id.count10Layout);
        scoreLayout = findViewById(R.id.scoreLayout);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wave);
        resultText.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        count10Layout.setVisibility(View.INVISIBLE);


        informGameDialog();
    }

    private void informGameDialog() {

        viewPagerList = new ArrayList<>();

        // 게임 설명 스크린샷 3장 첨부 필수.
        viewPagerList.add(R.raw.count10_explain1);
        viewPagerList.add(R.raw.count10_explain2);
        viewPagerList.add(R.raw.count10_explain3);


        final GameInform gameInform = new GameInform(this, gameTitle, viewPagerList, 2, 1);
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
                    count10Layout.setVisibility(View.VISIBLE);
                    deliveredList = gameInform.getDeliveredList();
                    scoreList = new ArrayList<>();
                    int listSize = deliveredList.size();
                    int layoutCount = (int) ((listSize + 1) / 2);

                    if (listSize > 0) {
                        for (int i = 0; i < layoutCount; i++) {
                            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            linearLayout.setLayoutParams(params1);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            linearLayout.setGravity(Gravity.CENTER | Gravity.LEFT);
                            if (i == layoutCount - 1) {
                                if (listSize % 2 == 1) {
                                    LinearLayout itemLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.game_count10_player, null);
                                    LinearLayout.LayoutParams iparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                                    itemLayout.setLayoutParams(iparam);
                                    itemLayout.setPadding(40, 5, 40, 5);
                                    TextView score = itemLayout.findViewById(R.id.score);
                                    TextView name = itemLayout.findViewById(R.id.name);
                                    scoreList.add(score);

                                    int temp = listSize - 1;
                                    Starter_pb_Model model = deliveredList.get(temp);
                                    name.setText(model.getName());

                                    linearLayout.addView(itemLayout);
                                } else {
                                    for (int j = 0; j < 2; j++) {
                                        LinearLayout itemLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.game_count10_player, null);
                                        LinearLayout.LayoutParams iparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                                        itemLayout.setLayoutParams(iparam);
                                        itemLayout.setPadding(40, 5, 40, 5);
                                        TextView score = itemLayout.findViewById(R.id.score);
                                        TextView name = itemLayout.findViewById(R.id.name);
                                        scoreList.add(score);

                                        int temp = (2 * i) + (j);
                                        Starter_pb_Model model = deliveredList.get(temp);
                                        name.setText(model.getName());
                                        linearLayout.addView(itemLayout);
                                    }
                                }

                            } else {
                                for (int j = 0; j < 2; j++) {
                                    LinearLayout itemLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.game_count10_player, null);
                                    LinearLayout.LayoutParams iparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                                    itemLayout.setLayoutParams(iparam);
                                    itemLayout.setPadding(40, 5, 40, 5);
                                    TextView score = itemLayout.findViewById(R.id.score);
                                    TextView name = itemLayout.findViewById(R.id.name);
                                    scoreList.add(score);
                                    int temp = (2 * i) + (j);

                                    Starter_pb_Model model = deliveredList.get(temp);
                                    name.setText(model.getName());
                                    linearLayout.addView(itemLayout);


                                }
                            }
                            scoreLayout.addView(linearLayout);

                        }

                        startThread();
                        isStart = true;
                    }
                }
        }
        });

        dialog.show();

    }

    public void startThread() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.countdown);
        backText.setVisibility(View.GONE);
        sec = 0;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isTouch) {
                    for (int i = 3; i >= 0; i--) {
                        final int re_time = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setVisibility(View.VISIBLE);
                                if (re_time != 0) {
                                    textView.setText(re_time + "초 후에 시작합니다. ");
                                } else {
                                    textView.setText(deliveredList.get(orderCount).getName() + " 시작!!");
                                }

                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            count10Layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    thread.interrupt();

                                    imageView.clearAnimation();

                                    String score_str = String.format("%.2f", sec);
                                    double score = Double.parseDouble(score_str);
                                    if (score < 0) score = 0;
                                    scoreList.get(orderCount).setText(String.valueOf(score));

                                    orderCount++;
                                    if (orderCount < deliveredList.size()) {
                                        isTouch = false;
                                        textView.setVisibility(View.VISIBLE);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                count10Layout.setOnClickListener(null);
                                            }
                                        });
                                        startThread();
                                    } else {
                                        onClickEvent();
                                    }
                                }
                            });
                        }
                    });

                    vibrator.vibrate(500);
                    imageView.startAnimation(animation);
                    sec = 10;
                    while (!isTouch && sec > 0) {
                        mediaPlayer.start();
                        try {
                            Thread.sleep(7);

                            if (!isTouch && sec > 0) sec -= 0.01;

                            if (sec > 5) {
                                final String strSec = String.format("%.2f", sec);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText(strSec);
                                    }
                                });
                            } else if (sec > 0 && sec < 5) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                });
                            } else if (sec <= 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setVisibility(View.VISIBLE);
                                        imageView.clearAnimation();
                                        textView.setText("시간 초과!!!");
                                        vibrator.vibrate(500);
                                        if(mediaPlayer.isPlaying())
                                            mediaPlayer.stop();
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            if(mediaPlayer.isPlaying())
                                 mediaPlayer.stop();
                            return;
                        }
                    }

                }
            }

        });
        thread.start();
    }

    private void onClickEvent() {
        //Log.d("TEST", "마지막 진입");
        isTouch = false;
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        double[] temp = new double[scoreList.size()];
        double max = 0;
        List<Integer> index = new ArrayList<>();

        for (int i = 0; i < temp.length; i++) {
            temp[i] = Double.parseDouble(scoreList.get(i).getText().toString().trim());
        }

        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == 0) {
                max = temp[i];
                break;
            } else {
                if (max < temp[i]) {
                    max = temp[i];
                }
            }
        }

        //Log.d("TEST", "max 값 : "+max);

        for (int i = 0; i < temp.length; i++) {
            if (max == temp[i]) {
                //Log.d("TEST", "데이터 담기는 인덱스값 : "+i);
                index.add(i);
            }
        }

        String result = "";
        int resultSize = 0;
        for (int i = 0; i < index.size(); i++) {
            result += deliveredList.get(index.get(i)).getName();
            resultSize++;
            scoreList.get(index.get(i)).setBackgroundColor(Color.YELLOW);
            if (i != index.size() - 1) {
                result += "\n";
            }
        }
        //Log.d("TEST", "result : "+result);
        backText.setVisibility(View.VISIBLE);

        buildAlert(result, resultSize);

        particleSystem = new ParticleSystem(this, 100, R.drawable.money, 10000);
        particleSystem.setSpeedModuleAndAngleRange(0f, 0.2f, 0, 150);

        particleSystem.setRotationSpeed(144);
        particleSystem.setAcceleration(0.00005f, 90);

        particleSystem.emit(250, -500, 8);
        count10Layout.setOnClickListener(null);
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                particleSystem.stopEmitting();
                if(isStart) {
                    setResult(RESULT_OK);
                }
                finish();
            }
        });

    }

    private List<Integer> calcMinScoreIndex(List<TextView> scoreList) {
        double[] temp = new double[scoreList.size()];
        double max = 0;
        List<Integer> index = new ArrayList<>();

        for (int i = 0; i < temp.length; i++) {
            temp[i] = Double.parseDouble(scoreList.get(i).getText().toString().trim());
            if (temp[i] == 0) {
                max = temp[i];
            } else {
                if (max < temp[i]) {
                    max = temp[i];
                }
            }
        }

        for (int i = 0; i < temp.length; i++) {
            if (max == temp[i]) {
                index.add(i);
                //Log.d("TEST", temp[i]+" 작은수");
            }
        }


        return index;
    }

    private void buildAlert(String text, int resultSize) {
        View view = getLayoutInflater().inflate(R.layout.layout_custom_dialog, null);
        if(resultSize > 3) {
            view.setBackgroundResource(R.drawable.dialog_square);
        } else {
            view.setBackgroundResource(R.drawable.dialog3);
        }
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
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        if (resultDialog != null) {
            if (resultDialog.isShowing()) {
                resultDialog.dismiss();
            }
        }

        if(mediaPlayer!=null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
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

