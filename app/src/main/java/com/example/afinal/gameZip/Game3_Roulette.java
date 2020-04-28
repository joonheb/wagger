package com.example.afinal.gameZip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.dialog.CustomDialogFragment;
import com.example.afinal.gameInform.GameInform;
import com.example.afinal.gameModel.Starter_pb_Model;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game3_Roulette extends AppCompatActivity {
    CircleManager circleManager;
    RelativeLayout layoutRoulette;

    Button btnRotate;
    ArrayList<String> player;
    float initAngle = 0.0f;
    int num_roulette;

    List<Integer> viewPagerList;
    Dialog dialog;
    String gameTitle = "룰렛 돌리기";
    LinearLayout mainLayout;
    List<Starter_pb_Model> deliveredList;
    Typeface typeface;

    TextView backText;
    Dialog resultDialog = null;

    Activity activity;
    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        activity = this;
        mainLayout = findViewById(R.id.mainLayout);
        btnRotate = findViewById(R.id.btnRotate);

        layoutRoulette = findViewById(R.id.layoutRoulette);

        mainLayout.setVisibility(View.INVISIBLE);

        informGameDialog();
        typeface = Typeface.createFromAsset(getAssets(), "mhZplRGIhqXOm9DpPRipJ7nMX7U.ttf");

        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStart = true;
                rotateLayout(layoutRoulette, num_roulette);
                btnRotate.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void informGameDialog() {

        viewPagerList = new ArrayList<>();

        // 게임 설명 스크린샷 3장 첨부 필수.
        viewPagerList.add(R.raw.roulette_explain1);
        viewPagerList.add(R.raw.roulette_explain2);
        viewPagerList.add(R.raw.roulette_explain3);


        final GameInform gameInform = new GameInform(this, gameTitle, viewPagerList, 2, 2);
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
                    deliveredList = gameInform.getDeliveredList();

                    num_roulette = deliveredList.size();
                    player = setPlayer(num_roulette);
                    circleManager = new CircleManager(getApplicationContext(), num_roulette);
                    layoutRoulette.addView(circleManager);
                    backText = findViewById(R.id.backText);
                    backText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isStart) {
                                setResult(RESULT_OK);
                            }
                            finish();
                        }
                    });
                }
            }
        });

        dialog.show();

    }

    public void rotateLayout(final RelativeLayout layout, final int num) {
        final float fromAngle = getRandom(360) + 3600 + initAngle;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getResult(fromAngle, num); // start when animation complete
            }
        }, 3000);

        RotateAnimation rotateAnimation = new RotateAnimation(initAngle, fromAngle,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.accelerate_decelerate_interpolator));
        rotateAnimation.setDuration(3000);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        layout.startAnimation(rotateAnimation);
    }

    // Set numbers on roulette to random
    public ArrayList<String> setPlayer(int playerCnt) {
        ArrayList<String> temp = new ArrayList<>();

        for(int i=0; i<playerCnt; i++) {
            temp.add(deliveredList.get(i).getName());
        }
        return temp;
    }

    // get Angle to random
    private int getRandom(int maxNumber) {
        double r = Math.random();
        return (int)(r * maxNumber);
    }

    private void getResult(float angle, int num_roulette) {
        String text = "";
        angle = angle % 360;

        Log.d("roulette", "당첨자! : " + angle);

        if (num_roulette == 2) {
            if (angle > 270 || angle <= 90) { // 11
                text = player.get(1);
            } else if (angle > 90 && angle <= 270) { // 22
                text = player.get(0);
            }
        } else if (num_roulette == 3) {
            if (angle > 270 || angle <= 30) { // 22
                text = player.get(2);
            } else if (angle > 30 && angle <= 150) { // 11
                text = player.get(1);
            } else if (angle > 150 && angle <= 270) { // 333
                text = player.get(0);
            }
        } else if (num_roulette == 4) {
            if (angle > 270 || angle <= 0) { // 22
                text = player.get(3);
            } else if (angle > 0 && angle <= 90) { // 11
                text = player.get(2);
            } else if (angle > 90 && angle <= 180) { // 333
                text = player.get(1);
            } else if (angle > 180 && angle <= 270) { // 222
                text = player.get(0);
            }
        }  else if (num_roulette == 5) {
            if (angle > 342 || angle <= 54) { // 11   2
                text = player.get(3);
            } else if (angle > 54 && angle <= 126) { // 333   3
                text = player.get(2);
            } else if (angle > 126 && angle <= 198) { // 222   4
                text = player.get(1);
            } else if (angle > 198 && angle <= 270) { // 111    0
                text = player.get(0);
            } else if (angle > 270 && angle <= 342) { // 22     1
                text = player.get(4);
            }
        } else if (num_roulette == 6) {
            if (angle > 330 || angle <= 30) { // 22
                text = player.get(4);
            } else if (angle > 30 && angle <= 90) { // 11
                text = player.get(3);
            } else if (angle > 90 && angle <= 150) { // 333
                text = player.get(2);
            } else if (angle > 150 && angle <= 210) { // 222
                text = player.get(1);
            } else if (angle > 210 && angle <= 270) { // 111
                text = player.get(0);
            } else if (angle > 270 && angle <= 330) { // 3
                text = player.get(5);
            }
        } else if (num_roulette == 7) {
            if (angle > 320 || angle <= 11.42857) { // 51.42857
                text = player.get(5);
            } else if (angle > 11.42857 && angle <= 62.85714) { // 11
                text = player.get(4);
            } else if (angle > 62.85714 && angle <= 114.28571) { // 333
                text = player.get(3);
            } else if (angle > 114.28571 && angle <= 165.71428) { // 222
                text = player.get(2);
            } else if (angle > 165.71428 && angle <= 217.14285) { // 111
                text = player.get(1);
            } else if (angle > 217.14285 && angle <= 268.57142) { // 3
                text = player.get(0);
            } else if (angle > 268.57142 && angle <= 320) { // 3
                text = player.get(6);
            }
        } else if (num_roulette == 8) {
            if (angle > 315 || angle <= 0) { // 45
                text = player.get(6);
            } else if (angle > 0 && angle <= 45) { // 11
                text = player.get(5);
            } else if (angle > 45 && angle <= 90) { // 333
                text = player.get(4);
            } else if (angle > 90 && angle <= 135) { // 222
                text = player.get(3);
            } else if (angle > 135 && angle <= 180) { // 111
                text = player.get(2);
            } else if (angle > 180 && angle <= 225) { // 3
                text = player.get(1);
            } else if (angle > 225 && angle <= 270) { // 3
                text = player.get(0);
            } else if (angle > 270 && angle <= 315) { // 3
                text = player.get(7);
            }
        }
        buildAlert(text);

        btnRotate.setVisibility(View.VISIBLE);
    }

    // 결과 알림창 뜨기
    private void buildAlert(String text) {
        View view = getLayoutInflater().inflate(R.layout.layout_custom_dialog, null);

        ((TextView)view.findViewById(R.id.dialog_confirm_msg)).setText(text + " 당첨!!");

        resultDialog = new Dialog(this);
        resultDialog.setContentView(view);
        resultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resultDialog.isShowing()) {
                    resultDialog.dismiss();
                }
            }
        });
        resultDialog.show();
    }

    public class CircleManager extends View {
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Random rd = new Random();
        private int[] COLORS =
                {
                        Color.rgb(255, 167, 167),
                        Color.rgb(255, 193, 158),
                        Color.rgb(255, 224, 140),
                        Color.rgb(150,237,125),
                        Color.rgb(206,242,121),
                        Color.rgb(183,240,177),
                        Color.rgb(178,235,244),
                        Color.rgb(178, 204, 255),
                }; // 2개늘려야될것같고
        private int[] randomColor = new int[COLORS.length];
        private int num;

        public CircleManager(Context context, int num) {
            super(context);
            this.num = num;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = layoutRoulette.getWidth();
            int height = layoutRoulette.getHeight();
            float sweepAngle;
            if(num!=7) {
                sweepAngle = 360/num;
            } else {
                sweepAngle = 364/num;
            }

            RectF rectF = new RectF(0, 0, width, height);
            Rect rect = new Rect(0, 0, width, height);

            int centerX = ((rect.left + rect.right) / 2);
            int centerY = (rect.top + rect.bottom) / 2;
            int radius = (rect.right - rect.left) / 2;

            int temp = 0;

            for(int i=0; i<randomColor.length; i++) {
                randomColor[i] = rd.nextInt(8);
                for(int j=0; j<i; j++) {
                    if(randomColor[i] == randomColor[j])
                        i--;
                }
            }
            for (int i = 0; i < num; i++) {
                paint.setColor(COLORS[randomColor[i]]);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setAntiAlias(true);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawArc(rectF, temp, sweepAngle, true, paint);

                float medianAngle = (temp + (sweepAngle / 2f)) * (float) Math.PI / 180f;

                paint.setColor(Color.BLACK);
                paint.setTextSize(42);

                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setTypeface(typeface);

                float arcCenterX = (float) (centerX + (radius * Math.cos(medianAngle))); // Arc's center X
                float arcCenterY = (float) (centerY + (radius * Math.sin(medianAngle))); // Arc's center Y

                // put text at middle of Arc's center point and Circle's center point
                float textX = (centerX + arcCenterX) / 2;
                float textY = (centerY + arcCenterY) / 2;
                canvas.drawText(player.get(i), textX, textY, paint);
                temp += sweepAngle;
            }
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

        if(resultDialog!=null) {
            if(resultDialog.isShowing()) {
                resultDialog.dismiss();
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
