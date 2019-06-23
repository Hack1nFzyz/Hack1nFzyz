package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.fzyz.jerryc05.fzyz_app.R;
import net.fzyz.jerryc05.fzyz_app.core.MainPage;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.DashboardFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.HomeFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.ProfileFragment;


public class MainActivity extends AppCompatActivity {

  private final static String       TAG = MainActivity.class.getName();
  private              DrawerLayout drawerLayout;
  private              Fragment     currentFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    new Thread(() -> {
      setToolBarAndDrawer();
      setFragment(new HomeFragment());
      setBottomNavView();
    }).start();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    MainPage.test(this, item.getTitle().toString());
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
    ((BottomNavigationView) findViewById(R.id.bottom_nav_view))
            .setOnNavigationItemSelectedListener(item -> {
              Fragment fragment;

              switch (item.getItemId()) {
                case R.id.nav_home:
                  fragment = new HomeFragment();
                  break;
                case R.id.nav_dashboard:
                  fragment = new DashboardFragment();
                  break;
                case R.id.nav_profile:
                  fragment = new ProfileFragment();
                  break;
                default:
                  return false;
              }
              setFragment(fragment);
              return true;
            });
  }

  private void setFragment(@NonNull Fragment fragment) {
    Fragment existingFragment;

    if ((existingFragment = getSupportFragmentManager().findFragmentByTag(
            fragment.getClass().getName())) != null)
      fragment = existingFragment;

    if (!fragment.equals(currentFragment)) {
      FragmentTransaction transaction =
              getSupportFragmentManager().beginTransaction();

      if (currentFragment != null)
        transaction.hide(currentFragment);

      if (fragment.isAdded()) {
        transaction.show(fragment);
        Log.d(TAG, "setFragment: Reusing   " + fragment.getTag());
      } else {
        transaction.add(R.id.frame_layout, fragment, fragment.getClass().getName());
        Log.d(TAG, "setFragment: Creating  " + fragment.getTag());
      }
      transaction.commit();
      currentFragment = fragment;
    } else
      Log.d(TAG, "setFragment: Unchanged " + fragment.getTag());
  }
}
