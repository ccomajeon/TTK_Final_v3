package com.example.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private boolean validate = false;

    boolean idb = false;
    boolean nickb = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("회원가입");
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText id = findViewById(R.id.idText);
        final EditText nick = findViewById(R.id.nick_name);
        final EditText password1 = findViewById(R.id.passwordText1);
        final EditText password2 = findViewById(R.id.passwordText2);

        final Button registerButton = findViewById(R.id.registButton);

        final Button idcheck = findViewById(R.id.idcheck);
        final Button nickcheck = findViewById(R.id.nick_name_check);

        //아이디 중복확인
        idcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = id.getText().toString();

                if (!checkID(userID)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("아이디 길이를 확인해주세요")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean result = jsonResponse.getBoolean("result");
                            if (result) {
                                idb = false;
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("이미 존재하는 아이디입니다")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                                return;
                            } else {
                                //idb = true;
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("사용할 수 있는 아이디입니다. 사용하시겠습니까?")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                idb = true;
                                                id.setEnabled(false);
                                                id.setBackgroundColor(Color.parseColor("#ADFF2F"));
                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                idb = false;
                                            }
                                        })
                                        .create()
                                        .show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Map<String, String> param = new HashMap<String, String>();
                param.put("type", "1");
                param.put("id", userID);
                ConnectPHP php = new ConnectPHP("http://gebalnote.pe.kr/member_regist.php", 1, param, responseListener, RegisterActivity.this);
            }
        });
        //닉네임 체크
        nickcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nickname = nick.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean result = jsonResponse.getBoolean("result");
                            if (result) {
                                nickb = false;
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("이미 존재하는 닉네임입니다")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                                return;
                            } else {
                                //nickb = true;
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("사용할 수 있는 닉네임입니다. 사용하시겠습니까?")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                nickb = true;
                                                nick.setEnabled(false);
                                                nick.setBackgroundColor(Color.parseColor("#ADFF2F"));
                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                nickb = false;
                                            }
                                        })
                                        .create()
                                        .show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Map<String, String> param = new HashMap<String, String>();
                param.put("type", "2");
                param.put("nickname", nickname);
                ConnectPHP php = new ConnectPHP("http://gebalnote.pe.kr/member_regist.php", 1, param, responseListener, RegisterActivity.this);
            }
        });

        // 비밀번호 일치 검사
        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = password1.getText().toString();
                String confirm = password2.getText().toString();

                if (password.equals(confirm)) {
                    password1.setBackgroundColor(Color.parseColor("#ADFF2F"));  //연두색
                    password2.setBackgroundColor(Color.parseColor("#ADFF2F"));
                } else {
                    password1.setBackgroundColor(Color.parseColor("#F08080"));  //빨간색
                    password2.setBackgroundColor(Color.parseColor("#F08080"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //회원가입
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = id.getText().toString();
                String userPassword = password1.getText().toString();
                String nickname = nick.getText().toString();

                if (!idb || !nickb) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("중복확인을 해주세요.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    return;
                }
                if (userID.equals("") || userPassword.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("빈 칸 없이 입력해주세요")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    return;
                }
                if (id.getText().toString().length() == 0) {
                    Toast.makeText(RegisterActivity.this, "아이디을 입력하세요!", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                    return;
                }
                if (password1.getText().toString().length() == 0) {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    password1.requestFocus();
                    return;
                }
                if (!password1.getText().toString().equals(password2.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    password1.setText("");
                    password2.setText("");
                    password1.requestFocus();
                    password1.setBackgroundColor(Color.parseColor("#00ff0000"));  //투명색
                    password2.setBackgroundColor(Color.parseColor("#00ff0000"));
                    return;
                }
                if (password1.getText().toString().length() < 6 && password2.getText().toString().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "비밀번호는 6자리 이상입니다.", Toast.LENGTH_SHORT).show();
                    password1.setText("");
                    password2.setText("");
                    password1.requestFocus();
                    password1.setBackgroundColor(Color.parseColor("#00ff0000"));  //투명색
                    password2.setBackgroundColor(Color.parseColor("#00ff0000"));
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        //I/System.out: (HTTPLog)-Static: isSBSettingEnabled false 에러
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean result = jsonResponse.getBoolean("result");
                            if (result) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원 가입에 성공하였습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .create()
                                        .show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원 가입에 실패하였습니다.. 에러 사유 : "+jsonResponse.getString("error"))
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Map<String, String> param = new HashMap<String, String>();
                param.put("type", "0");
                param.put("nickname", nickname);
                param.put("id", userID);
                param.put("pw", userPassword);
                ConnectPHP php = new ConnectPHP("http://gebalnote.pe.kr/member_regist.php", 1, param, responseListener, RegisterActivity.this);
            }
        });
    }

    public boolean checkID(String id) {
        if (id.length() >= 6 && id.length() <= 16)
            return true;
        return false;
    }

    public boolean checkPW(String pw1, String pw2) {
        if (pw1.equals(pw2)) {
            if (pw1.length() >= 8 && pw1.length() <= 20) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}