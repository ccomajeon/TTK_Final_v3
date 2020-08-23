package com.example.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.Totheking;
import com.example.activity.TTKMainActivity;
import com.example.graduate.ConnectPHP;
import com.example.graduate.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("로그인");
        setContentView(R.layout.activity_login);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText id = (EditText) findViewById(R.id.idText);
        final EditText password1 = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView registerButton = (TextView) findViewById(R.id.regist);
        Totheking ttk = (Totheking) getApplication();
        if(ttk.isLogin()){
            ttk.logout();
            Toast.makeText(this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
            Intent registerIntent = new Intent(LoginActivity.this, TTKMainActivity.class);
            LoginActivity.this.startActivity(registerIntent);
            finish();
            return;
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String userID = id.getText().toString();
                final String userPassword = password1.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);
                            boolean success = data.getBoolean("result");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 성공했습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .create()
                                        .show();
                                Totheking ttk = (Totheking) getApplication();
                                ttk.setLogin(data.getInt("index"), userID, data.getString("nickname"));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하였습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                Map<String, String> para = new HashMap<>();
                para.put("id", userID);
                para.put("pw", userPassword);
                ConnectPHP php = new ConnectPHP("http://gebalnote.pe.kr/member_login.php", 1, para, responseListener, LoginActivity.this);
            }
        });
    }
    @Override
    public void onBackPressed() {
        //startActivity(getSupportParentActivityIntent());
        finish();
    }
}
