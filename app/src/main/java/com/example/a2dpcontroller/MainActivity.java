package com.example.a2dpcontroller;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMenu();
    }

    private void setMenu(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                new MainFragment(this)).commit();
        navigationView.setCheckedItem(R.id.MainPage);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.MainPage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new MainFragment(this)).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.SBC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new SbcInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.AAC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new AacInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.aptX:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new aptXInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.aptX_HD:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new aptXHDInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.LDAC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new LdacInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new AboutAppFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
