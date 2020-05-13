package com.example.a2dpcontroller;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainFragment.FragmentChanger{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private String currentFragment = "Nothing";
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMenu();
    }

    private void setMenu(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeLayout = findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("MYAPP", "Start refreshing");
                if(currentFragment=="MainPage"){
                    setMainFragment();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeLayout.setRefreshing(false);
                        }
                    },1200);
                }else{
                    swipeLayout.setRefreshing(false);
                }
            }
        });

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setMainFragment();
    }

    private void setMainFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                new MainFragment(this, this)).commit();
        navigationView.setCheckedItem(R.id.MainPage);
        currentFragment = "MainPage";
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            if(currentFragment=="SBC" || currentFragment=="AAC" || currentFragment=="aptX" || currentFragment=="aptX_HD" || currentFragment=="LDAC" || currentFragment=="about"){
                setMainFragment();
            }else{
                super.onBackPressed();
            }
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
                        new MainFragment(this, this)).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                currentFragment="MainPage";
                navigationView.setCheckedItem(R.id.MainPage);
                break;
            case R.id.SBC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new SbcInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                currentFragment="SBC";
                navigationView.setCheckedItem(R.id.SBC);
                break;
            case R.id.AAC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new AacInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                currentFragment="AAC";
                navigationView.setCheckedItem(R.id.AAC);
                break;
            case R.id.aptX:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new aptXInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                currentFragment="aptX";
                navigationView.setCheckedItem(R.id.aptX);
                break;
            case R.id.aptX_HD:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new aptXHDInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                currentFragment="aptX_HD";
                navigationView.setCheckedItem(R.id.aptX_HD);
                break;
            case R.id.LDAC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new LdacInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                currentFragment="LDAC";
                navigationView.setCheckedItem(R.id.LDAC);
                break;
            case R.id.about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new AboutAppFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                currentFragment="about";
                navigationView.setCheckedItem(R.id.about);
                break;
        }
        return true;
    }

    @Override
    public void changeFragment(int codec) {
        switch (codec){
            case Codec.SOURCE_CODEC_TYPE_SBC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new SbcInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                navigationView.setCheckedItem(R.id.SBC);
                currentFragment="SBC";
                break;
            case Codec.SOURCE_CODEC_TYPE_AAC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new AacInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                navigationView.setCheckedItem(R.id.AAC);
                currentFragment="AAC";
                break;
            case Codec.SOURCE_CODEC_TYPE_APTX:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new aptXInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                navigationView.setCheckedItem(R.id.aptX);
                currentFragment="aptX";
                break;
            case Codec.SOURCE_CODEC_TYPE_APTX_HD:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new aptXHDInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                navigationView.setCheckedItem(R.id.aptX_HD);
                currentFragment="aptX_HD";
                break;
            case Codec.SOURCE_CODEC_TYPE_LDAC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new LdacInfoFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                navigationView.setCheckedItem(R.id.LDAC);
                currentFragment="LDAC";
                break;
        }
    }
}
