package com.example.afinal.gameInform;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.afinal.R;
import com.example.afinal.gameModel.Starter_ladder_Model;
import com.example.afinal.gameModel.Starter_pb_Model;

import java.util.ArrayList;
import java.util.List;

import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;

public class GameInform extends View {

    // 초기화
    Context context;
    Activity activity;
    Dialog dialog;
    int menuNum;

    LayoutInflater inflater;

    // 게임 설명 다이얼로그 셋팅
    ConstraintLayout informLayout;
    TextView title, next, backtag;
    AutoScrollViewPager autoScrollViewPager;
    GameInformAdapter adapter;
    List<Integer> viewPagerList;
    String gameTitle;


    // 게임 입력 다이얼로그 셋팅
    LinearLayout startLayout, settingLayout, scrollLayout;
    TextView personCount, bombCount, touchText, backText, tipText;
    ImageButton button1, button2, button3, button4;
    List<ViewGroup> list;
    List<Starter_pb_Model> deliveredList;
    List<Starter_ladder_Model> ladderList;

    private int personCnt, bombCnt, personMinCnt;

    public GameInform(Context context, String gameTitle, List<Integer> viewPagerList, int menuNum) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.gameTitle = gameTitle;
        this.viewPagerList = viewPagerList;
        this.menuNum = menuNum;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setDialog();
    }

    public GameInform(Context context, String gameTitle, List<Integer> viewPagerList, int menuNum, int personMinCnt) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.gameTitle = gameTitle;
        this.viewPagerList = viewPagerList;
        this.menuNum = menuNum;
        this.personMinCnt = personMinCnt;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setDialog();
    }

    public Dialog setDialog() {
        dialog = new Dialog(context);
        View view = inflater.inflate(R.layout.inform_game, null);

        informLayout = view.findViewById(R.id.informLayout);
        title = view.findViewById(R.id.title);
        next = view.findViewById(R.id.next);
        backtag = view.findViewById(R.id.backtag);

        title.setText(gameTitle);

        autoScrollViewPager = view.findViewById(R.id.autoScrollViewPager);

        adapter = new GameInformAdapter(context, viewPagerList);

        autoScrollViewPager.setAdapter(adapter);
        autoScrollViewPager.startAutoScroll(4000);


        if (menuNum == 0) next.setText("시작");

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuNum == 0) {
                    dialog.dismiss();
                } else if (menuNum == 1) {
                    View view = inflater.inflate(R.layout.game_starter_pb, null);
                    view.setBackgroundColor(Color.WHITE);

                    dialog.setContentView(view);

                    personCount = view.findViewById(R.id.personCount);
                    bombCount = view.findViewById(R.id.bombCount);

                    button1 = view.findViewById(R.id.button1);
                    button2 = view.findViewById(R.id.button2);
                    button3 = view.findViewById(R.id.button3);
                    button4 = view.findViewById(R.id.button4);

                    startLayout = view.findViewById(R.id.startLayout);
                    settingLayout = view.findViewById(R.id.settingLayout);

                    button1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            bombCnt = Integer.parseInt(bombCount.getText().toString().trim());

                            if (personCnt > 1) {
                                personCnt--;
                                //setPlayer();
                                //addData();
                                //touchText.setVisibility(GONE);
                                //useThread();
                            }
                            personCount.setText(String.valueOf(personCnt));
                            if (personCnt < bombCnt) {
                                bombCnt = personCnt;
                                bombCount.setText(String.valueOf(bombCnt));
                            }
                        }
                    });

                    button2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            bombCnt = Integer.parseInt(bombCount.getText().toString().trim());
                            if (personCnt < 8) {
                                personCnt++;
                                //setPlayer();
                                //addData();
                                //touchText.setVisibility(GONE);
                                //useThread();
                            }
                            personCount.setText(String.valueOf(personCnt));
                        }
                    });
                    button3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            bombCnt = Integer.parseInt(bombCount.getText().toString().trim());
                            if (bombCnt > 1) {
                                bombCnt--;
                            }
                            bombCount.setText(String.valueOf(bombCnt));
                        }
                    });
                    button4.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            bombCnt = Integer.parseInt(bombCount.getText().toString().trim());
                            if (bombCnt < personCnt) {
                                bombCnt++;
                            }
                            bombCount.setText(String.valueOf(bombCnt));
                        }
                    });

                    touchText = view.findViewById(R.id.resetText);
                    backText = view.findViewById(R.id.backText);
                    touchText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //Log.d("TEST", "진입~");
                        }
                    });

                    backText.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            activity.setResult(Activity.RESULT_CANCELED);
                            activity.finish();
                        }
                    });


                    list = new ArrayList<>();
                    personCnt = 1;
                    bombCnt = 1;

                    //setPlayer();
                    //addData();
                } else if (menuNum == 2) {
                    View view = inflater.inflate(R.layout.game_starter_pt, null);
                    view.setBackgroundColor(Color.WHITE);

                    dialog.setContentView(view);

                    personCount = view.findViewById(R.id.personCount);

                    button1 = view.findViewById(R.id.button1);
                    button2 = view.findViewById(R.id.button2);

                    startLayout = view.findViewById(R.id.startLayout);
                    settingLayout = view.findViewById(R.id.settingLayout);
                    scrollLayout = view.findViewById(R.id.scrollLayout);

                    personCount.setText(String.valueOf(personMinCnt));

                    button1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());

                            if (personCnt > personMinCnt) {
                                personCnt--;
                                setPlayer();
                                addData();
                            }
                            personCount.setText(String.valueOf(personCnt));
                        }
                    });

                    button2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            if (personCnt < 8) {
                                personCnt++;
                                setPlayer();
                                addData();
                            }
                            personCount.setText(String.valueOf(personCnt));
                        }
                    });


                    touchText = view.findViewById(R.id.resetText);
                    backText = view.findViewById(R.id.backText);
                    tipText = view.findViewById(R.id.tipText);

                    touchText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < list.size(); i++) {
                                deliveredList.add(new Starter_pb_Model(i + 1, ((EditText) (list.get(i).getChildAt(2))).getText().toString().trim(), getResources().getIdentifier("card" + String.valueOf(i + 1), "drawable", context.getPackageName())));
                            }
                            dialog.dismiss();
                        }
                    });

                    backText.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            activity.setResult(Activity.RESULT_CANCELED);
                            activity.finish();
                        }
                    });


                    list = new ArrayList<>();
                    deliveredList = new ArrayList<>();
                    personCnt = personMinCnt;

                    setPlayer();
                    addData();

                } else if (menuNum == 3) {
                    View view = inflater.inflate(R.layout.game_starter_timer, null);
                    view.setBackgroundColor(Color.WHITE);

                    dialog.setContentView(view);

                    personCount = view.findViewById(R.id.personCount);

                    startLayout = view.findViewById(R.id.startLayout);
                    settingLayout = view.findViewById(R.id.settingLayout);


                    touchText = view.findViewById(R.id.resetText);
                    backText = view.findViewById(R.id.backText);

                    touchText.setFocusable(false);
                    backText.setFocusable(false);

                    touchText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            if(personCnt < 10) {
                                personCnt = 10;
                            }
                            dialog.dismiss();
                        }
                    });

                    backText.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            activity.setResult(Activity.RESULT_CANCELED);
                            activity.finish();
                        }
                    });

                } else if (menuNum == 4) {
                    View view = inflater.inflate(R.layout.game_starter_pb_plus, null);
                    view.setBackgroundColor(Color.WHITE);

                    dialog.setContentView(view);

                    personCount = view.findViewById(R.id.personCount);
                    bombCount = view.findViewById(R.id.bombCount);

                    button1 = view.findViewById(R.id.button1);
                    button2 = view.findViewById(R.id.button2);
                    button3 = view.findViewById(R.id.button3);
                    button4 = view.findViewById(R.id.button4);

                    startLayout = view.findViewById(R.id.startLayout);
                    settingLayout = view.findViewById(R.id.settingLayout);
                    scrollLayout = view.findViewById(R.id.scrollLayout);

                    button1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());

                            if (personCnt > 3) {
                                personCnt--;
                            }
                            personCount.setText(String.valueOf(personCnt));
                        }
                    });

                    button2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            if (personCnt < 8) {
                                personCnt++;
                            }
                            personCount.setText(String.valueOf(personCnt));
                        }
                    });

                    button3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            bombCnt = Integer.parseInt(bombCount.getText().toString().trim());
                            if (bombCnt > 1) {
                                bombCnt--;
                                setBomb();
                                addData();
                            }
                            bombCount.setText(String.valueOf(bombCnt));
                        }
                    });
                    button4.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            bombCnt = Integer.parseInt(bombCount.getText().toString().trim());
                            if (bombCnt < personCnt) {
                                bombCnt++;
                                setBomb();
                                addData();
                            }
                            bombCount.setText(String.valueOf(bombCnt));
                        }
                    });

                    touchText = view.findViewById(R.id.resetText);
                    backText = view.findViewById(R.id.backText);
                    tipText = view.findViewById(R.id.tipText);

                    touchText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < list.size(); i++) {
                                ladderList.add(new Starter_ladder_Model(i, ((EditText) (list.get(i).getChildAt(2))).getText().toString().trim()));
                            }
                            personCnt = Integer.parseInt(personCount.getText().toString().trim());
                            dialog.dismiss();
                        }
                    });

                    backText.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.setResult(Activity.RESULT_CANCELED);
                            activity.finish();
                        }
                    });


                    list = new ArrayList<>();
                    ladderList = new ArrayList<>();
                    bombCnt = 1;

                    setBomb();
                    addData();
                }
            }
        });

        backtag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity)context).finish();
            }
        });
        dialog.setContentView(view);

        return dialog;
    }

    public List<Starter_pb_Model> getDeliveredList() {
        return deliveredList;
    }

    public int getPersonCnt() {     // 리스트갯수 = 사람수
        return personCnt;
    }

    public int getBombCnt() {
        return bombCnt;
    }

    public List<Starter_ladder_Model> getLadderList() {
        return ladderList;
    }

    private void setPlayer() {
        list.clear();

        for (int i = 0; i < personCnt; i++) {
            ViewGroup item = (ViewGroup) inflater.inflate(R.layout.game_starter_player, null);
            ImageView imageView = item.findViewById(R.id.imageView);
            EditText editText = item.findViewById(R.id.editText);

            int cardId = activity.getResources().getIdentifier("card" + String.valueOf(i + 1), "drawable", activity.getPackageName());
            imageView.setImageResource(cardId);
            editText.setHint("이름 입력");
            editText.setText("");

            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        settingLayout.setVisibility(GONE);
                        backText.setVisibility(GONE);
                        tipText.setVisibility(GONE);
                    }
                }
            });
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    checkingData();
                    return false;
                }
            });
            list.add(i, item);
        }
    }

    private void setBomb() {
        list.clear();

        for (int i = 0; i < bombCnt; i++) {
            ViewGroup item = (ViewGroup) inflater.inflate(R.layout.game_starter_bomb, null);
            TextView timerText = item.findViewById(R.id.timerText);
            EditText editText = item.findViewById(R.id.editText);

            timerText.setText(String.valueOf(i+1));
            editText.setHint("내기 입력");
            editText.setText("");

            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        settingLayout.setVisibility(GONE);
                        backText.setVisibility(GONE);
                        tipText.setVisibility(GONE);
                    }
                }
            });
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    checkingData();
                    return false;
                }
            });
            list.add(i, item);
        }
    }

    private void addData() {
        scrollLayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            scrollLayout.addView(list.get(i));
        }
    }

    private void checkingData() {
        for (int i = 0; i < list.size(); i++) {
            ViewGroup view = list.get(i);
            EditText checking = (EditText) view.getChildAt(2);
            String name = checking.getText().toString().trim();

            if (name.equals("") || name == null) {
                touchText.setVisibility(GONE);
                tipText.setVisibility(VISIBLE);
                return;
            } else {
                touchText.setVisibility(VISIBLE);
                backText.setVisibility(VISIBLE);
                settingLayout.setVisibility(VISIBLE);
            }
        }
    }

}
