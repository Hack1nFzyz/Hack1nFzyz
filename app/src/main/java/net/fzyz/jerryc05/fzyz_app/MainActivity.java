package net.fzyz.jerryc05.fzyz_app;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.legacy.app.ActionBarDrawerToggle;

import android.app.Fragment;
import android.view.Gravity;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.fzyz.jerryc05.fzyz_app.bottom_nav_bar.DashboardFragment;
import net.fzyz.jerryc05.fzyz_app.bottom_nav_bar.HomeFragment;
import net.fzyz.jerryc05.fzyz_app.bottom_nav_bar.ProfileFragment;


public class MainActivity extends Activity {
  private DrawerLayout drawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setFragment(new HomeFragment());
    setBottomNavView();
    setToolBarAndDrawer();
  }

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START))
      drawerLayout.closeDrawer(GravityCompat.START);
    else
      super.onBackPressed();
  }

  private void setToolBarAndDrawer() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setActionBar(toolbar);

    drawerLayout = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawerLayout, R.drawable.ic_dashboard_black_24dp,
            R.string.app_name, R.string.appbar_scrolling_view_behavior);
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState();
  }

  private void setBottomNavView() {
    BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view);
    bottomNavView.setOnNavigationItemSelectedListener(item -> {

      Fragment fragment;
      switch (item.getItemId()) {
        case R.id.nav_home:
          fragment = new HomeFragment();
          break;
        case R.id.nav_dashboard:
          fragment = new DashboardFragment();
          break;
        case R.id.nav_profile:
        default:
          fragment = new ProfileFragment();
      }
      setFragment(fragment);
      return true;
    });
  }

  private void setFragment(@NonNull Fragment fragment) {
    getFragmentManager().beginTransaction().replace(
            R.id.frame_layout, fragment).commit();
  }
}
