package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import net.fzyz.jerryc05.fzyz_app.R;
import net.fzyz.jerryc05.fzyz_app.core.MainPage;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.AcademicFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.FeedFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.ProfileFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.ProfileLoggedInFragment;

public final class MainActivity extends _BaseActivity {

  private final static String          TAG = "MainActivity";
  private              DrawerLayout    drawerLayout;
  private              Fragment        currentFragment;
  private              FragmentManager fragmentManager;

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    getThreadPoolExecutor().execute(this::setToolBarAndDrawer);
    threadPoolExecutor.execute(this::setBottomNavView);
    threadPoolExecutor.execute(() -> setFragment(FeedFragment.class));
  }

  @Override
  public boolean onCreateOptionsMenu(@NonNull final Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
    return true;
  }

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START))
      drawerLayout.closeDrawer(GravityCompat.START);
    else {
      Log.d(TAG, "onBackPressed: Ready to quit.");
      threadPoolExecutor.execute(this::setExitDialog);
    }
  }

  @WorkerThread
  private void setToolBarAndDrawer() {
    final MaterialToolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    drawerLayout = findViewById(R.id.drawer_layout);
    final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.app_name, R.string.appbar_scrolling_view_behavior);
    actionBarDrawerToggle.syncState();
    runOnUiThread(() -> drawerLayout.addDrawerListener(actionBarDrawerToggle));
  }

  @SuppressLint("WrongThread")
  @WorkerThread
  private void setBottomNavView() {
    final OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            item -> {
              Class fragmentClass;

              switch (item.getItemId()) {
                case R.id.nav_feed:
                  fragmentClass = FeedFragment.class;
                  break;
                case R.id.nav_academic:
                  fragmentClass = AcademicFragment.class;
                  break;
                case R.id.nav_activity:
                default:
                  fragmentClass = ProfileFragment.class;
                  break;
              }
              setFragment(fragmentClass);
              return true;
            };

    ((BottomNavigationView) findViewById(R.id.activity_main_bottomNavView))
            .setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
  }

  @WorkerThread
  public void setFragment(@NonNull Class fragmentClass) {
    if (fragmentManager == null)
      fragmentManager = getSupportFragmentManager();

    if (fragmentClass.equals(ProfileFragment.class))
      fragmentClass = ProfileFragment.isLoggedIn
              ? ProfileLoggedInFragment.class
              : fragmentClass;

    final Fragment existingFragment = fragmentManager.findFragmentByTag(
            fragmentClass.getSimpleName());

    if (existingFragment == null || !existingFragment.equals(currentFragment)) {
      final FragmentTransaction transaction = fragmentManager.beginTransaction();

      if (currentFragment instanceof ProfileFragment
              && fragmentClass.equals(ProfileLoggedInFragment.class)) {
        Log.d(TAG, "setFragment: Removing  " + currentFragment.getTag());
        transaction.remove(currentFragment);

      } else if (currentFragment != null) {
        Log.d(TAG, "setFragment: Hiding    " + currentFragment.getTag());
        transaction.hide(currentFragment);
      }

      if (existingFragment != null) {
        transaction.show(existingFragment);
        currentFragment = existingFragment;
        Log.d(TAG, "setFragment: Reusing   " + existingFragment.getTag());

      } else {
        try {
          final Fragment newFragment = (Fragment) fragmentClass.newInstance();
          transaction.add(R.id.activity_main_frameLayout, newFragment,
                  fragmentClass.getSimpleName());
          currentFragment = newFragment;
          Log.d(TAG, "setFragment: Creating  "
                  + fragmentClass.getSimpleName());
        } catch (final Exception e) {
          throw new IllegalStateException("Cannot instantiate "
                  + fragmentClass.getSimpleName());
        }
      }
      transaction.commit();
    } else
      Log.d(TAG, "setFragment: Unchanged " + fragmentClass.getSimpleName());
  }

  private void setExitDialog() {
    final String[][] testBank = {
            {"校训", "植基立本", "成德达材"},
            {"办学宗旨", "为天下人", "谋永福"},
            {"育人八大支柱第一句", "国家责任", "独立人格"},
            {"育人八大支柱第二句", "学会学习", "健体怡情"},
            {"育人八大支柱第三句", "服务意识", "国际视野"},
            {"育人八大支柱第四句", "实践能力", "自力自治"}
    };
    final String[] test = testBank[
            (int) (System.currentTimeMillis() % testBank.length)];

    final MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this)
            .setTitle("听说你想退出 App?")
            .setIcon(R.mipmap.ic_launcher_fzyz_round)
            .setMessage("福州一中的" + test[0] + "是什么？")
            .setCancelable(false)
            .setNeutralButton("不知道", (dialogInterface, i) ->
                    Snackbar.make(drawerLayout, test[0] + "：" + test[1] +
                            (test[2].equals(testBank[1][2]) ? "" : "，") +
                            test[2] + "。", Snackbar.LENGTH_LONG).show())
            .setNegativeButton(test[1],
                    (dialogInterface, i) -> MainActivity.super.onBackPressed())
            .setPositiveButton(test[2],
                    (dialogInterface, i) -> MainActivity.super.onBackPressed());
    runOnUiThread(alertDialogBuilder::show);
  }
}
