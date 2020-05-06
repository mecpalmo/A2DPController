package com.example.a2dpcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class ldacInfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ldac_info);
        setMenu();
    }

    private void setMenu(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.MainPage:
                Intent a = new Intent(ldacInfo.this,MainActivity.class);
                startActivity(a);
                break;
            case R.id.SBC:
                Intent b = new Intent(ldacInfo.this,sbcInfo.class);
                startActivity(b);
                break;
            case R.id.AAC:
                Intent c = new Intent(ldacInfo.this,aacInfo.class);
                startActivity(c);
                break;
            case R.id.aptX:
                Intent d = new Intent(ldacInfo.this,aptxInfo.class);
                startActivity(d);
                break;
            case R.id.aptX_HD:
                Intent e = new Intent(ldacInfo.this,aptxhdInfo.class);
                startActivity(e);
                break;
            case R.id.LDAC:
                Intent f = new Intent(ldacInfo.this,ldacInfo.class);
                startActivity(f);
                break;
            case R.id.about:
                Intent g = new Intent(ldacInfo.this,about.class);
                startActivity(g);
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
