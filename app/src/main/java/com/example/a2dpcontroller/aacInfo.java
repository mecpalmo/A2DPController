package com.example.a2dpcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class aacInfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aac_info);
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
                Intent a = new Intent(aacInfo.this,MainActivity.class);
                startActivity(a);
                break;
            case R.id.SBC:
                Intent b = new Intent(aacInfo.this,sbcInfo.class);
                startActivity(b);
                break;
            case R.id.AAC:
                Intent c = new Intent(aacInfo.this,aacInfo.class);
                startActivity(c);
                break;
            case R.id.aptX:
                Intent d = new Intent(aacInfo.this,aptxInfo.class);
                startActivity(d);
                break;
            case R.id.aptX_HD:
                Intent e = new Intent(aacInfo.this,aptxhdInfo.class);
                startActivity(e);
                break;
            case R.id.LDAC:
                Intent f = new Intent(aacInfo.this,ldacInfo.class);
                startActivity(f);
                break;
            case R.id.about:
                Intent g = new Intent(aacInfo.this,about.class);
                startActivity(g);
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
