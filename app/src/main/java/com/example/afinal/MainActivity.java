package com.example.afinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.database.DatabaseHelper;
import com.example.afinal.util.RegexHelper;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.Utility;
import com.kakao.util.helper.log.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout mainLayout;
    FrameLayout loginLayout, firstLayout;

    // 광고
    private RewardedAd rewardedAd;
    Activity activity;

    // firstLayout

    // loginLayout
    EditText user_id, user_pw;
    TextView register, searchID, searchPW;
    Button naver, login;

   // DB 초기화
    private String pref_data = "pref_data";
    DatabaseHelper dbHelper;


    // kakao 로그인
    private SessionCallback callback;
    UserAccount kakaoAccount;
    AsyncHttpClient client;
    HttpResponse  response;
    String nickname;
    String email;

    String serverURL = "http://118.128.215.54:8080/projectLogin/member/member_kakao.do";
    String loginURL = "http://118.128.215.54:8080/projectLogin/member/member_login.do";

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
        Log.d("TEST","ㅇ틸리티 키 : "+Utility.getKeyHash(this));


        // 풀 스크린 설정 + 세로 화면 고정
        fullScreen();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mainLayout = findViewById(R.id.mainLayout);
        loginLayout = findViewById(R.id.loginLayout);
        //imageView = findViewById(R.id.imageView);

        loginLayout.setVisibility(View.GONE);

        startTest();
        dbInit();


//        resId = new int[33];
//        for (int i = 1; i <= resId.length; i++) {
//            resId[i - 1] = getResources().getIdentifier("start" + i, "drawable", getPackageName());
//        }
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
//                startActivity(intent);
//            }
//        });


    }

    private void dbInit() {
        dbHelper = DatabaseHelper.getInstance();
        dbHelper.openDatabase(activity);
        dbHelper.createTable();
        dbHelper.insertTable(activity);


    }

    private void startTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        firstLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.layout_firstscreen, null);
                        mainLayout.addView(firstLayout);
                    }
                });

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        firstLayout.setVisibility(View.INVISIBLE);
                        loginLayout.setVisibility(View.VISIBLE);
                        loginAct();
                    }
                });


            }
        }).start();

    }

    private void loginAct() {
        if(loginLayout.getVisibility() == View.VISIBLE) {
            mediaPlayer = MediaPlayer.create(this, R.raw.modoo);
            mediaPlayer.start();
            user_id = findViewById(R.id.user_id);
            user_pw = findViewById(R.id.user_pw);
            register = findViewById(R.id.register);
            searchID = findViewById(R.id.searchID);
            searchPW = findViewById(R.id.searchPW);
            //kakao = findViewById(R.id.kakao);
            naver = findViewById(R.id.naver);
            login = findViewById(R.id.login);

            register.setOnClickListener(this);
            searchID.setOnClickListener(this);
            searchPW.setOnClickListener(this);
            //kakao.setOnClickListener(this);
            naver.setOnClickListener(this);
            login.setOnClickListener(this);

            client = new AsyncHttpClient();
        }
    }

    private void fullScreen() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()){
            case R.id.register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

                // 미구현
            case R.id.searchID:
                break;


                // 미구현
            case R.id.searchPW:
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                break;


                // 미구현
            case R.id.naver:
                break;

            case R.id.login:
                String id = user_id.getText().toString().trim();
                String pw = user_pw.getText().toString().trim();

                RegexHelper regexHelper = RegexHelper.getInstance();
                if(!regexHelper.isValue(id)) {
                    Toast.makeText(activity, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!regexHelper.isValue(pw)) {
                    Toast.makeText(activity, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                response = new HttpResponse(this, 2);

                RequestParams params = new RequestParams();
                params.put("user_id", id);
                params.put("user_pw", pw);

                client.get(loginURL, params, response);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper!= null) {
            dbHelper.freeInstance();
            dbHelper = null;
        }
        Session.getCurrentSession().removeCallback(callback);

        if(mediaPlayer!=null) {
            mediaPlayer = null;
        }
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.d("TEST", Session.getCurrentSession().getTokenInfo().getAccessToken());
            //redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.d("TEST", "에러진입");
                //Log.d("TEST", Session.getCurrentSession().getTokenInfo().getAccessToken());
            }
        }
    }


    protected void redirectSignupActivity() {
        Log.d("TEST", Session.getCurrentSession().getAccessToken());
        requestMe();
        //log.d("TEST", Session.getCurrentSession().ge);
    }



    private void requestMe() {

        Log.d("TEST", "진입~11");
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                activityFailChange();
            }

            @Override
            public void onSuccess(MeV2Response response) {
                kakaoAccount = response.getKakaoAccount();
                Log.d("TEST" , "user id : "+response.getId());

                if (kakaoAccount != null) {
                    handleScopeError(kakaoAccount);

                    Profile profile = kakaoAccount.getProfile();

                    if (profile != null) {
                        Log.d("TESTNickname", profile.getNickname());
                        nickname = profile.getNickname();
                        email = kakaoAccount.getEmail();
                        if(profile != null) {
                            sendData();
                        } else {
                            activitySuccessChange();
                        }
                    } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                        // 동의 요청 후 프로필 정보 획득 가능
                    } else {
                        // 프로필 획득 불가
                    }
                }
            }
        });
    }

    private void activityFailChange() {
        /*
        final Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);

         */
    }

    private void activitySuccessChange() {
        final Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("nickname", nickname);
        finish();
        startActivity(intent);
    }

    private void handleScopeError(UserAccount account) {
        List<String> neededScopes = new ArrayList<>();

        if (account.emailNeedsAgreement() == OptionalBoolean.TRUE) {
            Log.d("TEST", "이메일~");
            neededScopes.add("account_email");
        }



        Session.getCurrentSession().updateScopes(this, neededScopes, new AccessTokenCallback() {
            @Override
            public void onAccessTokenReceived(AccessToken accessToken) {
                //유저에게 성공적으로 동의를 받음. 토큰을 재발급 받게 됨.
//                String email = kakaoAccount.getEmail();
//                if (email != null) {
//                    Log.d("TEST", email);
//                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
//
//                } else {
//
//                }

            }
            @Override
            public void onAccessTokenFailure(ErrorResult errorResult) {
                //동의 얻기 실패}
            }
        });
    }

    private void sendData() {
        response = new HttpResponse(this, 1);

        Log.d("sendDataTEST","진입TEST");
        RequestParams params = new RequestParams();
        params.put("nickname", nickname);
        params.put("email", email);

        client.get(serverURL, params, response);
    }

    class HttpResponse extends AsyncHttpResponseHandler{
        Activity activity;
        int menuNum;
        public HttpResponse(Activity activity, int menuNum) {
            this.activity = activity;
            this.menuNum = menuNum;
        }

        @Override
        public void onStart() {
            super.onStart();
            Log.d("TEST", "URI : "+getRequestURI());
        }

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            String str = new String(bytes);
            Log.d("TEST", "str = "+str);
            try {
                JSONObject json = new JSONObject(str);
                String rt= json.getString("rt");
                if(menuNum==1) {
                    if (rt.equals("OK")) {
                        activitySuccessChange();
                    } else {
                    }
                } else if(menuNum==2) {
                    if (rt.equals("OK")) {
                       JSONObject member = json.getJSONObject("member");
                       String nickname = member.getString("nickname");
                       //int token = member.getInt("token");
                       Log.d("TEST", "닉네임 : "+nickname);
                       Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                       if(nickname!=null) {
                           intent.putExtra("nickname" , nickname);
                       }
                       //intent.putExtra("token",token);
                       startActivity(intent);


                    } else {
                        Toast.makeText(activity, "아이디 또는 비밀번호가 틀립니다", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show();
            Log.d("[TEST]", "i = " + i);
            Log.d("[TEST]", "m = " + throwable.getLocalizedMessage());
        }
    }

}

