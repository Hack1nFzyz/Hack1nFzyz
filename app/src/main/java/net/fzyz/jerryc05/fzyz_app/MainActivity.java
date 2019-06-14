package net.fzyz.jerryc05.fzyz_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import net.fzyz.jerryc05.fzyz_app.bottom_nav_bar.DashboardFragment;
import net.fzyz.jerryc05.fzyz_app.bottom_nav_bar.HomeFragment;
import net.fzyz.jerryc05.fzyz_app.bottom_nav_bar.ProfileFragment;


public class MainActivity extends AppCompatActivity {
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
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    Snackbar.make(item.getActionView(),"????",Snackbar.LENGTH_INDEFINITE).show();
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START))
      drawerLayout.closeDrawer(GravityCompat.START);
    else
      super.onBackPressed();
  }

  private void setToolBarAndDrawer() {
    MaterialToolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    drawerLayout = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
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
