package com.example.afinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.util.RegexHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText user_id, user_pw, email, name, nickname;
    TextView domain;
    Button back, confirm1, confirm2, input;

    TextView checkingId, checkingNick;

    String[] serverURL ={
            "http://118.128.215.54:8080/projectLogin/member/member_userIdCheck.do",
            "http://118.128.215.54:8080/projectLogin/member/member_nickCheck.do",
            "http://118.128.215.54:8080/projectLogin/member/member_write.do"
    };
    AsyncHttpClient client;
    AsyncHttpResponseHandler response;
    RegexHelper regexHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        client = new AsyncHttpClient();
        regexHelper = RegexHelper.getInstance();

        user_id = findViewById(R.id.user_id);
        user_pw = findViewById(R.id.user_pw);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        nickname = findViewById(R.id.nickname);
        back = findViewById(R.id.back);
        confirm1 = findViewById(R.id.confirm1);
        confirm2 = findViewById(R.id.confirm2);
        domain = findViewById(R.id.domain);
        input = findViewById(R.id.input);

        checkingId = findViewById(R.id.checkingId);
        checkingNick = findViewById(R.id.checkingNick);

        back.setOnClickListener(this);
        confirm1.setOnClickListener(this);
        confirm2.setOnClickListener(this);
        domain.setOnClickListener(this);
        input.setOnClickListener(this);

        checkingId.setVisibility(View.GONE);
        checkingNick.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                //setResult(RESULT_CANCELED);
                finish();
                break;

            // 아이디 중복확인
            case R.id.confirm1:
                String user_id_str = user_id.getText().toString().trim();
                if (user_id_str != null && !user_id_str.equals("")) {
                    response = new ModooResponse(1);
                    RequestParams params = new RequestParams();
                    params.put("user_id", user_id_str);
                    client.post(serverURL[0], params, response);
                } else {
                    Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                break;

            // 별명 중복확인
            case R.id.confirm2:
                String nickname_str = nickname.getText().toString().trim();
                if (nickname_str != null && !nickname_str.equals("")) {
                    response = new ModooResponse(2);
                    RequestParams params = new RequestParams();
                    params.put("nickname", nickname_str);
                    client.post(serverURL[1], params, response);
                }
                break;

            case R.id.input:
                String uid = user_id.getText().toString().trim();
                String nick = nickname.getText().toString().trim();
                String upw = user_pw.getText().toString().trim();
                String nam = name.getText().toString().trim();
                String mail = email.getText().toString().trim();

                if(!regexHelper.isValue(uid)) {
                    Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(checkingId.getVisibility() == View.VISIBLE)) {
                    Toast.makeText(this, "아이디 중복검사를 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(!regexHelper.isValue(nick)) {
                    Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(checkingNick.getVisibility() == View.VISIBLE)) {
                    Toast.makeText(this, "닉네임 중복검사를 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!regexHelper.isValue(upw)) {
                    Toast.makeText(this, "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!regexHelper.isValue(nam)) {
                    Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!regexHelper.isValue(mail)) {
                    Toast.makeText(this, "메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                String totalEmail = mail + "@"+domain.getText().toString().trim();
                Log.d("TEST", totalEmail);

                response = new ModooResponse(3);
                RequestParams params = new RequestParams();
                params.put("user_id", uid);
                params.put("user_pw", upw);
                params.put("nickname", nick);
                params.put("name", nam);
                params.put("email", totalEmail);
                client.post(serverURL[2], params, response);

                break;

            // 이메일 도메인
            case R.id.domain:
                final AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.dialog_email_domain, null);
                final TextView naver, daum, gmail, nate;
                naver = view.findViewById(R.id.naver);
                daum = view.findViewById(R.id.daum);
                gmail = view.findViewById(R.id.gmail);
                nate = view.findViewById(R.id.nate);


                builder.setView(view);
                dialog = builder.create();
                dialog.show();
                naver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        domain.setText(naver.getText());
                        dialog.dismiss();
                    }
                });
                daum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        domain.setText(daum.getText());
                        dialog.dismiss();
                    }
                });
                gmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        domain.setText(gmail.getText());
                        dialog.dismiss();
                    }
                });
                nate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        domain.setText(nate.getText());
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    class ModooResponse extends AsyncHttpResponseHandler {

        int dbNum;

        public ModooResponse(int dbNum) {
            this.dbNum = dbNum;
        }

        @Override
        public void onStart() {
            super.onStart();
            Log.d("TEST", "진입");
            Log.d("TEST", "URL : " + getRequestURI());
        }

        @Override
        public void onSuccess(int responseCode, Header[] headers, byte[] bytes) {
            String content = null;
            try {
                content = new String(bytes, "utf-8");
                Log.d("TEST", "반환 content : "+content);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (content != null) {
                try {
                    JSONObject json = new JSONObject(content);
                    String rt = json.getString("rt");
                    if(dbNum == 1) {
                        // 아이디 중복
                        if (rt.equals("OK")) {
                            user_id.setText("");
                            user_id.setHint("사용불가");
                            user_id.setHintTextColor(Color.RED);
                            checkingId.setVisibility(View.VISIBLE);
                            checkingId.setBackgroundResource(R.drawable.check_x);
                        } else {
                            checkingId.setVisibility(View.VISIBLE);
                            checkingId.setBackgroundResource(R.drawable.check_y);
                        }

                    } else if(dbNum==2) {
                        // 별명 중복
                        if (rt.equals("OK")) {
                            nickname.setText("");
                            nickname.setHint("사용불가");
                            nickname.setHintTextColor(Color.RED);
                            checkingNick.setVisibility(View.VISIBLE);
                            checkingNick.setBackgroundResource(R.drawable.check_x);
                        } else {
                            checkingNick.setVisibility(View.VISIBLE);
                            checkingNick.setBackgroundResource(R.drawable.check_y);
                        }
                    } else if(dbNum==3) {
                        // 정상
                        if (rt.equals("OK")) {
                            finish();
                        } else {
                            finish();
                            Toast.makeText(RegisterActivity.this, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

        @Override
        public void onFailure(int responseCode, Header[] headers, byte[] bytes, Throwable throwable) {

        }
    }
}


