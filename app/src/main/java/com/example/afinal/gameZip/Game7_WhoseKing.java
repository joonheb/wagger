package com.example.afinal.gameZip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.database.DatabaseHelper;
import com.example.afinal.gameInform.GameInform;
import com.example.afinal.gameModel.Starter_pb_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game7_WhoseKing extends AppCompatActivity implements View.OnClickListener {
    LinearLayout mainLayout;
    TextView name1, name2, action;
    TextView backText, resetText, item, left, right;

    List<Integer> viewPagerList;
    String gameTitle = "내가 광이다";
    Dialog dialog;
    List<Starter_pb_Model> deliveredList;
    List<String> dataList;

    Random rc;
    Activity activity;
    DatabaseHelper dbHelper;

    String[] menuStr = { "술 게임", "후식 내기", "식사 내기", "벌칙 내기"};
    int menuNum = 1;
    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whose_king);


        activity = this;
        mainLayout = findViewById(R.id.mainLayout);
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        action = findViewById(R.id.action);

        backText = findViewById(R.id.backText);
        resetText = findViewById(R.id.resetText);
        item = findViewById(R.id.item);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        backText.setOnClickListener(this);
        resetText.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

        rc = new Random();

        mainLayout.setVisibility(View.INVISIBLE);
        informGameDialog();

    }

    private void informGameDialog() {

        viewPagerList = new ArrayList<>();

        // 게임 설명 스크린샷 3장 첨부 필수.
        viewPagerList.add(R.raw.whoseking_explain1);
        viewPagerList.add(R.raw.whoseking_explain2);
        viewPagerList.add(R.raw.whoseking_explain3);


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
                    mainLayout.setVisibility(View.VISIBLE);
                    dataList = new ArrayList<>();
                    deliveredList = gameInform.getDeliveredList();
                    resetText.setVisibility(View.VISIBLE);
                }
            }
        });

        dialog.show();
    }

    public void startGame() {
        isStart = true;
        dataList.clear();
        dbHelper = DatabaseHelper.getInstance();
        dataList = dbHelper.selectTable(menuNum);

        initThread(name1,10, true).start();
        initThread(name2,15, true).start();
        initThread(action,20, false).start();
    }

    public Thread initThread(final TextView textView, final int endNum, final boolean isPerson) {
        final Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < endNum; i++) {
                    try {
                        Thread.sleep(350);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final int temp = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.animate().translationY(300f).setDuration(300).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    if(temp>= endNum-1) {
                                        textView.setTranslationY(0f);
                                        if(!isPerson) {
                                            resetText.setVisibility(View.VISIBLE);
                                            left.setVisibility(View.VISIBLE);
                                            right.setVisibility(View.VISIBLE);
                                        }
                                    } else{
                                        textView.setTranslationY(-300f);
                                    }

                                    if(isPerson) {
                                        textView.setText(deliveredList.get(rc.nextInt(deliveredList.size())).getName());
                                    } else {
                                        textView.setText(dataList.get(rc.nextInt(dataList.size())));
                                    }
                                }
                            }).start();
                        }
                    });
                }
            }
        });
        return thread;
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
                left.setVisibility(View.INVISIBLE);
                right.setVisibility(View.INVISIBLE);
                resetText.setVisibility(View.INVISIBLE);
                break;
            case R.id.left:
                menuNum--;
                if(menuNum < 1) menuNum = 4;
                item.setText(menuStr[menuNum-1]);
                Log.d("TEST", menuNum+"");
                break;
            case R.id.right:
                menuNum++;
                if(menuNum > 4) menuNum = 1;
                item.setText(menuStr[menuNum-1]);
                Log.d("TEST", menuNum+"");
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
