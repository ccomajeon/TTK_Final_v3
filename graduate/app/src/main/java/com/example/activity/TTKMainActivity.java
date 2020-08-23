package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.Totheking;
import com.example.account.LoginActivity;
import com.example.account.RegisterActivity;
import com.example.graduate.R;
import com.example.module.BackPressCloseHandler;

public class TTKMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public String title;
    public BackPressCloseHandler closeHandler;

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Totheking ttk = (Totheking) getApplication();
        if (ttk.isLogin()) {
            TextView welcome = findViewById(R.id.welcome);
            welcome.setText(ttk.getNickname() + "님 환영합니다.");
            navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_logout_icon_24dp);
            navigationView.getMenu().getItem(0).setTitle("로그아웃"); //네비게이션 메뉴 이름 바꾸는 소스
            navigationView.getMenu().getItem(1).setVisible(false);
        } else {
            navigationView.getMenu().getItem(0).setTitle("로그인"); //네비게이션 메뉴 이름 바꾸는 소스
            navigationView.getMenu().getItem(1).setVisible(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_main);

        closeHandler = new BackPressCloseHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Totheking ttk = (Totheking) getApplication();
        if (ttk.isLogin()) {
            navigationView.getMenu().getItem(0).setTitle("로그아웃"); //네비게이션 메뉴 이름 바꾸는 소스
            navigationView.getMenu().getItem(1).setVisible(false);
        } else {
            navigationView.getMenu().getItem(0).setTitle("로그인"); //네비게이션 메뉴 이름 바꾸는 소스
            navigationView.getMenu().getItem(1).setVisible(true);
        }
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        Button part = (Button) findViewById(R.id.bcpu);
        Button value = (Button) findViewById(R.id.value);
        Button usage = (Button) findViewById(R.id.usage);
        Button test = (Button) findViewById(R.id.test);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bcpu:
                        Intent intent = new Intent(
                                getApplicationContext(),
                                PartItemActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.value:
                        Intent intent2 = new Intent(
                                getApplicationContext(),
                                ValueItemActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.usage:
                        Intent intent3 = new Intent(
                                getApplicationContext(),
                                UsageItemActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.test:
                        Intent intent4 = new Intent(
                                getApplicationContext(),
                                EstimateListActivity.class);
                        startActivity(intent4);
                        break;
                }

            }
        };

        part.setOnClickListener(listener);
        value.setOnClickListener(listener);
        usage.setOnClickListener(listener);
        test.setOnClickListener(listener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            closeHandler.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        switch (item.getItemId()) {

            case R.id.nav_login:
                Intent intent = new Intent(TTKMainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_sign_in:
                Intent intent2 = new Intent(TTKMainActivity.this, RegisterActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_favorit_list:
                Intent intent3 = new Intent(TTKMainActivity.this, DibbsActivity.class);
                startActivity(intent3);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        item.setChecked(true);
        getSupportActionBar().setTitle(title);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
