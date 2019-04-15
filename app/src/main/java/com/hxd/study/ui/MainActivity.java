package com.hxd.study.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hxd.study.AdMobActivity;
import com.hxd.study.FullscreenActivity;
import com.hxd.study.ItemListActivity;
import com.hxd.study.R;
import com.hxd.study.ScrollingActivity;
import com.hxd.study.SettingsActivity;
import com.hxd.study.TabbedActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = new Intent();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    intent.setClass(MainActivity.this, ItemListActivity.class);
//                    intent.setClass(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    intent.setClass(MainActivity.this, FullscreenActivity.class);
                    MainActivity.this.startActivity(intent);
                    return true;
                case R.id.navigation_information:
                    mTextMessage.setText(R.string.title_information);
                    intent.setClass(MainActivity.this, SettingsActivity.class);
//                    intent.setClass(MainActivity.this, AdMobActivity.class);
                    MainActivity.this.startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    intent.setClass(MainActivity.this, ScrollingActivity.class);
                    MainActivity.this.startActivity(intent);
                    return true;
                case R.id.navigation_cyclopedia:
                    mTextMessage.setText(R.string.title_cyclopedia);
                    intent.setClass(MainActivity.this, TabbedActivity.class);
                    MainActivity.this.startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}
