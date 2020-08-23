package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.graduate.R;
import com.example.graduate.ViewPagerAdapter;
import com.example.module.CategoryID;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private DrawerLayout drawer;
    public String title;
    SearchView searchView;


    private TabLayout tabLayout;

    public static int tab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        for (int i = 0; i < 8; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(CategoryID.values()[i].getCategoryName()));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("category", 0));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("검색...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //검색어 입력시 이벤트 제어
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.equals("")) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("검색어를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    return false;
                }
                Intent search = new Intent(MainActivity.this, SearchPartListActivity.class);
                search.putExtra("category", viewPager.getCurrentItem() + 1);
                search.putExtra("search", s);

                startActivity(search);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //startActivity(getSupportParentActivityIntent());
        finish();
    }
}
