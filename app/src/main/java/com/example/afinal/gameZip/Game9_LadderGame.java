package com.example.afinal.gameZip;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.gameInform.GameInform;
import com.example.afinal.gameModel.LadderPathColorModel;
import com.example.afinal.gameModel.LadderRoadModel;
import com.example.afinal.gameModel.LadderPathModel;
import com.example.afinal.gameModel.Starter_ladder_Model;
import com.example.afinal.gameModel.Starter_pb_Model;
import com.plattysoft.leonids.ParticleSystem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game9_LadderGame extends AppCompatActivity implements View.OnClickListener {

    private static Dialog resultDialog = null;
    private static String resultText = null;

    FrameLayout mainLayout;
    LinearLayout playerLayout;
    ViewGroup sadariLayout;
    ImageView[] players;
    ImageView[] totalPlayers;

    int playerCnt;

    List<LadderPathModel> pathList;                                                                 // 경로 리스트

    List<LadderRoadModel> roadList;                                                                 // 이미지뷰 길 여부 리스트
    List<List<LadderRoadModel>> viewList;                                                                 // 위의 roadList 리스트를 담는 통합 리스트

    List<LadderPathColorModel> colorList;

    private int initX = 15;
    private int initY = 0;
    private int width = 66;                                                                         // player imageview 가로 크기
    private int height = 63;

    private int layoutCnt = 19;                                                                     // 가지 개수(홀수추천)

    List<Integer> viewPagerList;
    Dialog dialog;
    String gameTitle = "사다리 타기";
    List<Starter_ladder_Model> deliveredList;

    TextView blindText, autoText, touchText, backText;
    ImageView blindImage;

    boolean isBlind = true;

    int[] colors = {Color.rgb(255, 84, 84),
            Color.rgb(255, 178, 245),
            Color.rgb(255, 193, 158),
            Color.rgb(255, 224, 140),
            Color.rgb(150, 237, 125),
            Color.rgb(178, 235, 244),
            Color.rgb(178, 204, 255),
            Color.rgb(165, 102, 255)
    };

    ObjectAnimator animator;
    Thread thread;

    int[] eventNum;
    String[] event;
    Activity activity;
    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladder_game);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int rwidth = metrics.widthPixels;
        int rheight = metrics.heightPixels;
        int dpi = metrics.densityDpi;
        float density = metrics.density;

        int widthCnt = rwidth / 360;

        int tempX = rwidth - ((int)(400 * density));
        if(tempX < 0) {
            initX = 0;
            width = 22*widthCnt;
            height = (int)((24* density)+0.5);
        } else {
            initX = tempX / 2;
            width = (int)((25 * density)+0.5);
            height = (int)((24* density)+0.5);
        }

        Log.d("XTEST", "화면 가로 세로 : "+rwidth +", "+rheight);
        Log.d("XTEST", "width  : " +width);
        Log.d("XTEST", "height  : " +height);
        Log.d("XTEST", "Dpi : "+dpi);
        Log.d("XTEST", "density : "+density);
        activity = this;
        playerCnt = 8;
        players = new ImageView[playerCnt];
        totalPlayers = new ImageView[15];

        sadariLayout = findViewById(R.id.sadariLayout);

        mainLayout = findViewById(R.id.mainLayout);

        pathList = new ArrayList<>();
        viewList = new ArrayList<>();

        blindText = findViewById(R.id.blindText);
        autoText = findViewById(R.id.autoText);
        touchText = findViewById(R.id.resetText);
        backText = findViewById(R.id.backText);

        blindImage = findViewById(R.id.blindimage);

        blindText.setOnClickListener(this);
        autoText.setOnClickListener(this);
        touchText.setOnClickListener(this);
        backText.setOnClickListener(this);

        mainLayout.setVisibility(View.INVISIBLE);
        touchText.setVisibility(View.INVISIBLE);
        sadariLayout.removeAllViews();

        informGameDialog();
    }

    private void informGameDialog() {

        viewPagerList = new ArrayList<>();

        // 게임 설명 스크린샷 3장 첨부 필수.
        viewPagerList.add(R.raw.laddergame_explain1);
        viewPagerList.add(R.raw.laddergame_explain2);
        viewPagerList.add(R.raw.laddergame_explain3);


        final GameInform gameInform = new GameInform(this, gameTitle, viewPagerList, 4);
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
                    deliveredList = gameInform.getLadderList();
                    int listSize = deliveredList.size();
                    playerCnt = gameInform.getPersonCnt();

                    if(listSize>0 && playerCnt > 2) {
                        initGame();
                    }
                }
            }
        });

        dialog.show();
    }

    private void initGame() {
        blindImage.setVisibility(View.INVISIBLE);

        for (int i = 0; i < 15; i++) {
            totalPlayers[i] = findViewById(getResources().getIdentifier("player" + (i + 1), "id", getPackageName()));
            if (i < (playerCnt * 2) - 1) {
                totalPlayers[i].setVisibility(View.VISIBLE);
            } else {
                totalPlayers[i].setVisibility(View.GONE);
            }
        }
        for (int i = 0; i < 8; i++) {
            players[i] = totalPlayers[i * 2];
        }

        for (int i = playerCnt; i < 8; i++) {
            players[i].setVisibility(View.GONE);
        }

        initLadder();
        resultDialog = null;
        for (int i = 0; i < playerCnt; i++) {

            pathInit(i);

            final int index = i;
            players[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TEST", view.getX()+"");
                    isStart = true;
                    pathInitPosition(pathList.get(index), view.getX(), view.getY());
                    startGame(view, index);
                    players[index].setOnClickListener(null);
                    autoText.setVisibility(View.INVISIBLE);
                }
            });
        }
    }


    private void initLadder() {

        Random rd = new Random();

        for (int q = 0; q < layoutCnt; q++) {
            ViewGroup childView = (ViewGroup) getLayoutInflater().inflate(R.layout.game_ladder_item, null);
            //Log.d("TEST", "q값 테스트 : " + q);
            sadariLayout.addView(childView);

            roadList = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                ImageView imageView = (ImageView) childView.getChildAt(i);
                imageView.setVisibility(View.GONE);
            }


            for (int i = 0; i < (playerCnt * 2) - 1; i++) {
                // 일자 라인은 기본 사다리 셋팅
                if (i % 2 == 0) {
                    ImageView imageView = (ImageView) childView.getChildAt(i);
                    imageView.setVisibility(View.VISIBLE);
                    //
                    imageView.setBackgroundColor(Color.WHITE);

                    LadderRoadModel loadModel = new LadderRoadModel();

                    // 객체에 넣기
                    loadModel.setImageView(imageView);
                    loadModel.setLoadNum(0);

                    roadList.add(loadModel);
                } else {
                    ImageView imageView = (ImageView) childView.getChildAt(i);
                    imageView.setVisibility(View.VISIBLE);
                    LadderRoadModel loadModel = new LadderRoadModel();

                    // 객체에 넣기
                    loadModel.setImageView(imageView);
                    loadModel.setLoadNum(-1);

                    roadList.add(loadModel);
                }
            }

            if (q % 2 != 0) {
                // 사다리 가지 갯수(8명이면 가지가 3개일지 4개일지 랜덤으로 지정)
                int rdCnt = (playerCnt / 2);

                if (playerCnt == 3) {
                    rdCnt = rd.nextInt(2);
                } else {
                    rdCnt = rd.nextInt(rdCnt) + 1;
                }

                // 사람인원수 -1  =  옆 사다리 배치 최대갯수
                // 4명일경우 사다리 3개 배치가능한데, 첫번째랑 3번째만 배치가능.


                // 가지 배열 생성
                int[] rdNum;
                if (rdCnt != 0) {
                    rdNum = new int[rdCnt];

                    // 짝수일때
                    if (playerCnt % 2 == 0) {
                        if (rdCnt == playerCnt / 2) {
                            for (int i = 0; i < rdCnt; i++) {
                                rdNum[i] = 4 * i + 1;
                            }
                        } else {
                            do {
                                rdNum[0] = rd.nextInt((playerCnt * 2) - 1);
                            } while (rdNum[0] % 2 != 1 || rdNum[0] == 1 || rdNum[0] == (playerCnt * 2) - 1);

                            for (int j = 1; j < rdCnt; j++) {
                                rdNum[j] = rd.nextInt((playerCnt * 2) - 1);

                                for (int k = 0; k < j; k++) {
                                    if (rdNum[j] == rdNum[k] || rdNum[j] % 2 != 1 || rdNum[j] - 2 == rdNum[k] || rdNum[j] + 2 == rdNum[k]) {
                                        j--;
                                    }
                                }
                            }
                        }
                    }

                    // 홀수일때
                    else {
                        //Log.d("TEST", "진입~~#~#~");
                        if(playerCnt == 3 ){
                            do {
                                rdNum[0] = rd.nextInt((playerCnt * 2) - 1);
                                //Log.d("TEST", "rdnum[0] : "+rdNum[0]);
                            } while (rdNum[0] % 2 != 1);
                        } else {
                            do {
                                rdNum[0] = rd.nextInt((playerCnt * 2) - 1);
                            } while (rdNum[0] % 2 != 1 || rdNum[0] == 1 || rdNum[0] == (playerCnt * 2) - 1);

                            //Log.d("TEST", "rdnum[0] 최종 : "+rdNum[0]);
                            for (int j = 1; j < rdCnt; j++) {
                                rdNum[j] = rd.nextInt((playerCnt * 2) - 1);

                                for (int k = 0; k < j; k++) {
                                    if (rdNum[j] == rdNum[k] || rdNum[j] % 2 != 1 || rdNum[j] - 2 == rdNum[k] || rdNum[j] + 2 == rdNum[k]) {
                                        j--;
                                    }
                                }
                            }
                        }
                    }
                    // 만들어진 가지배열에 길 그림 넣기
                    for (int i = 0; i < rdNum.length; i++) {
                        ImageView imageView = (ImageView) childView.getChildAt(rdNum[i]);
                        imageView.setBackgroundColor(Color.WHITE);
                        LadderRoadModel loadModel = new LadderRoadModel();

                        // 객체에 넣기
                        loadModel.setImageView(imageView);
                        loadModel.setLoadNum(1);

                        roadList.set(rdNum[i], loadModel);
                    }

                }
            }
            viewList.add(roadList);
        }

        ViewGroup childView = (ViewGroup) getLayoutInflater().inflate(R.layout.game_ladder_result, null);
        childView.setVisibility(View.VISIBLE);
        eventNum = new int[deliveredList.size()];
        event = new String[deliveredList.size()];
        if (deliveredList.size() > 0) {
            do {
                eventNum[0] = rd.nextInt((playerCnt * 2) - 1);
                event[0] = deliveredList.get(0).getBet();
            } while (eventNum[0] % 2 != 0);
        }

        for (int i = 1; i < deliveredList.size(); i++) {
            event[i] = deliveredList.get(i).getBet();
            eventNum[i] = rd.nextInt((playerCnt * 2) - 1);
            for (int j = 0; j < i; j++) {
                if (eventNum[i] == eventNum[j] || eventNum[i] % 2 == 1) {
                    i--;
                    break;
                }
            }
        }

        for (int i = 0; i < deliveredList.size(); i++) {
            //Log.d("TEST", "eventNum["+i+"] : "+eventNum[i]);
            ((TextView) childView.getChildAt(eventNum[i])).setText(event[i]);
            ((TextView) childView.getChildAt(eventNum[i])).setTextColor(Color.YELLOW);
        }

        for (int i = (playerCnt * 2) - 1; i < 15; i++) {
            childView.getChildAt(i).setVisibility(View.GONE);
        }
        sadariLayout.addView(childView);
        //Log.d("TEST", "viewList 카운트 : "+viewList.size());
    }

    private void startGame(View view, final int index) {
        LadderPathModel model = pathList.get(index);

        float x;

        
        //Log.d("TEST", "temp : "+temp);
        for (int i = 0; i < layoutCnt; i++) {
            List tempList = viewList.get(i);

            x = pathList.get(index).getX() - width * (8 - playerCnt);
            int temp = (int) (x / width);
            //Log.d("TEST", "x : "+x);
            //Log.d("TEST", "temp : " + temp);
            //Log.d("TEST", "템프 사이즈 : "+tempList.size());
            //Log.d("TEST", "인덱스 : "+index);

            if (temp > 0 && temp != (playerCnt - 1) * 2) {
                if (((LadderRoadModel) (tempList.get(temp + 1))).getLoadNum() == 1) {
                    moveRight(pathList.get(index));
                } else if (((LadderRoadModel) (tempList.get(temp - 1))).getLoadNum() == 1) {
                    //((LadderRoadModel) (tempList.get(temp - 1))).getImageView().setBackgroundColor(Color.RED);
                    moveLeft(pathList.get(index));
                } else {
                    moveDown(pathList.get(index));
                }

            } else if (temp == 0) {
                if (((LadderRoadModel) (tempList.get(temp + 1))).getLoadNum() == 1) {
                    moveRight(pathList.get(index));
                } else {
                    moveDown(pathList.get(index));
                }
            } else if (temp == (playerCnt - 1) * 2) {
                if (((LadderRoadModel) (tempList.get(temp - 1))).getLoadNum() == 1) {
                    moveLeft(pathList.get(index));
                } else {
                    moveDown(pathList.get(index));
                }
            }
        }

        moveDown(pathList.get(index));
        moveDown(pathList.get(index));


        animator = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator = ObjectAnimator.ofFloat(view, View.X, View.Y, model.getPath());
        }

        if (animator != null) {
            animator.setDuration(250 * model.getPathCnt());
            //Log.d("TEST", "model.x, model.y: "+model.getX()+", "+model.getY());
            animator.start();
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Thread.interrupted()) {
                    for (int i = 0; i < 150; i++) {
                        try {
                            Thread.sleep(40);

                            //int w = pathList.get(index).getX() - 66*(8-playerCnt);
                            final int w = (int) ((players[index].getX() - width * (8 - playerCnt)) / width);
                            final int h = (int) ((players[index].getY() - height) / height);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!Thread.interrupted()) {
                                        if (h < layoutCnt && h>=0) {
                                            ((LadderRoadModel) viewList.get(h).get(w)).getImageView().setBackgroundColor(colors[index]);
                                            ImageView aaa =((LadderRoadModel) viewList.get(h).get(w)).getImageView();
                                            Log.d("XTEST", "x, y 좌표 : "+aaa.getX() + ", " +aaa.getY());
                                        }

                                    }

                                }
                            });
                            //Log.d("TEST", "w, h " + w + ", " + h);
                        } catch (InterruptedException e) {
                            Log.d("TEST", "이너럽트 반생!!");
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int cnt = 0;
                        for(int i=0; i<playerCnt; i++) {
                            if(players[i].getY() > 900) {
                                cnt++;
                            }

                            if(cnt==playerCnt) {
                                touchText.setVisibility(View.VISIBLE);
                            } else {
                                touchText.setVisibility(View.INVISIBLE);
                            }

                        }
                    }
                });
            }
        });
        thread.start();


    }

    private void pathInit(int index) {
        Path path = new Path();
        int pathCnt = 0;
        float x = initX + ((8 - playerCnt) * index) + (2 * width) * index;
        float y = initY;
//        Log.d("TEST", "view.getX, getY : "+x +","+y);
//        Log.d("TEST", "pathCnt : "+pathCnt);

        pathList.add(new LadderPathModel(x, y, path, pathCnt));
    }


    private void pathInitPosition(LadderPathModel model, float x, float y) {
        Path path = model.getPath();
        path.moveTo(x + 0, y + 0);
        model.setX(x);
        model.setY(y);
        //Log.d("TEST", "view.getX, getY : " + x + "," + y);
    }


    // 밑으로 한칸 움직이기
    private void moveDown(LadderPathModel model) {
        Path path = model.getPath();
        int pathCnt = model.getPathCnt();
        float x = model.getX();
        float y = model.getY();

        path.lineTo(x, y + height);
        pathCnt++;

        model.setX(x);
        model.setY(y + height);
        model.setPath(path);
        model.setPathCnt(pathCnt);
    }

    // 우측으로 한칸 움직이기
    private void moveRight(LadderPathModel model) {
        Path path = model.getPath();
        int pathCnt = model.getPathCnt();
        float x = model.getX();
        float y = model.getY();

        path.lineTo(x, y + height);
        path.lineTo(x + (2 * width), y + height);


        pathCnt++;

        model.setX(x + (2 * width));
        model.setY(y + height);
        model.setPath(path);
        model.setPathCnt(pathCnt);
    }

    // 좌측으로 한칸 움직이기
    private void moveLeft(LadderPathModel model) {
        Path path = model.getPath();
        int pathCnt = model.getPathCnt();
        float x = model.getX();
        float y = model.getY();

        path.lineTo(x, y + height);
        path.lineTo(x - (2 * width), y + height);
        pathCnt++;

        model.setX(x - (2 * width));
        model.setY(y + height);
        model.setPath(path);
        model.setPathCnt(pathCnt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blindText:
                if (!isBlind) {
                    blindImage.setVisibility(View.VISIBLE);
                    isBlind = true;
                } else {
                    blindImage.setVisibility(View.INVISIBLE);
                    isBlind = false;
                }
                break;

            case R.id.autoText:
                isStart = true;
                touchText.setVisibility(View.INVISIBLE);
                for (int i = 0; i < pathList.size(); i++) {
                    pathInitPosition(pathList.get(i), initX + ((8 - playerCnt) * width) + (2 * width) * i, initY);
                    startGame(players[i], i);
                }

                for(int i=0; i<players.length; i++) {
                    players[i].setOnClickListener(null);
                }
                autoText.setVisibility(View.INVISIBLE);
                autoText.setOnClickListener(null);

                //Log.d("TEST", "viewList.size() : "+viewList.size());
                break;

            case R.id.backText:
                if(isStart) {
                    setResult(RESULT_OK);
                }
                finish();
                break;

            case R.id.resetText:
                if(thread!=null) thread.interrupt();
                if(animator!=null) animator.cancel();

                animator = null;
                thread = null;

                sadariLayout.removeAllViews();
                viewList.clear();
                pathList.clear();

                touchText.setVisibility(View.INVISIBLE);
                autoText.setVisibility(View.VISIBLE);
                autoText.setOnClickListener(this);

                initGame();
                //.d("TEST", "viewList.size() : "+viewList.size());

                for (int i = 0; i < playerCnt; i++) {
                    float x = initX + ((8 - playerCnt) * width) + ((2 * width) * i);
                    float y = initY;
                    players[i].setX(x);
                    players[i].setY(y);
                }

                for(int i=0; i<playerCnt; i++) {
                    pathInitPosition(pathList.get(i), initX + ((8 - playerCnt) * width) + (2 * width) * i, initY);
                }

                break;
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
