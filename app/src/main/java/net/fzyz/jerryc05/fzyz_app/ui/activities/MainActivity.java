package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import net.fzyz.jerryc05.fzyz_app.R;
import net.fzyz.jerryc05.fzyz_app.core.MainPage;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.DashboardFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.HomeFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.ProfileFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.ProfileLoggedInFragment;


public class MainActivity extends AppCompatActivity {

  final static String TAG = MainActivity.class.getName();
  Drawer          drawer;
  //  DrawerLayout drawerLayout;
  Fragment        currentFragment;
  MaterialToolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    new Thread(new Runnable() {
      @Override
      public void run() {
        setToolBarAndDrawer();
        setFragment(new HomeFragment());
        setBottomNavView();
      }
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
//    if (drawerLayout.isDrawerOpen(GravityCompat.START))
//      drawerLayout.closeDrawer(GravityCompat.START);
    if (drawer.isDrawerOpen())
      drawer.closeDrawer();
    else {
      Log.d(TAG, "onBackPressed: Ready to quit.");
      setExitDialog();
    }
  }

  @WorkerThread
  void setToolBarAndDrawer() {
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final AccountHeader accountHeader = new AccountHeaderBuilder()
            .withActivity(MainActivity.this)
//            .withHeaderBackground(
//                    R.drawable.ic_launcher_fzyz_background)
            .addProfiles(new ProfileDrawerItem()
                    .withName("FZYZ Student")
                    .withEmail("31000000000")
                    .withIcon(getResources()
                            .getDrawable(
                                    R.drawable.ic_launcher_fzyz_background)))
//            .withOnAccountHeaderListener(
//                    new AccountHeader.OnAccountHeaderListener() {
//                      @Override
//                      public boolean onProfileChanged(
//                              View view,
//                              IProfile profile,
//                              boolean currentProfile) {
//                        return false;
//                      }
//                    })
            .build();

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        drawer = new DrawerBuilder()
                .withActivity(MainActivity.this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withIcon(R.drawable.ic_home_black_24dp)
                                .withName(R.string.app_name)
                                .withDescription(R.string.dashboard),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIdentifier(2)
                                .withName(R.string.nav_header_username),
                        new SecondaryDrawerItem()
                                .withName(R.string.i_am_a_student))
                .addStickyDrawerItems(new SecondaryDrawerItem()
                        .withIdentifier(3)
                        .withName(R.string.nav_header_username),
                        new SecondaryDrawerItem()
                        .withIdentifier(4)
                        .withName(R.string.nav_header_username))
                .build();
      }
    });

//    drawerLayout = findViewById(R.id.drawer_layout);
//    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//            this, drawerLayout, toolbar,
//            R.string.app_name, R.string.appbar_scrolling_view_behavior);
//    runOnUiThread(new Runnable() {
//      @Override
//      public void run() {
//        drawerLayout.addDrawerListener(toggle);
//      }
//    });
//
//    toggle.syncState();
  }

  @WorkerThread
  void setBottomNavView() {
    ((BottomNavigationView) findViewById(R.id.activity_main_bottomNavView))
            .setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                      @Override
                      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                      }
                    });
  }

  @WorkerThread
  public void setFragment(@NonNull Fragment fragment) {
    Fragment existingFragment;

    if (fragment instanceof ProfileFragment)
      fragment = ProfileFragment.isLoggedIn
              ? new ProfileLoggedInFragment()
              : fragment;

    if ((existingFragment = getSupportFragmentManager().findFragmentByTag(
            fragment.getClass().getName())) != null)
      fragment = existingFragment;

    if (!fragment.equals(currentFragment)) {
      FragmentTransaction transaction =
              getSupportFragmentManager().beginTransaction();

      if (currentFragment instanceof ProfileFragment
              && fragment instanceof ProfileLoggedInFragment) {
        Log.d(TAG, "setFragment: Removing  " + currentFragment.getTag());
        transaction.remove(currentFragment);
      } else if (currentFragment != null) {
        Log.d(TAG, "setFragment: Hiding    " + currentFragment.getTag());
        transaction.hide(currentFragment);
      }

      if (fragment.isAdded()) {
        transaction.show(fragment);
        Log.d(TAG, "setFragment: Reusing   " + fragment.getTag());
      } else {
        transaction.add(R.id.activity_main_frameLayout, fragment,
                fragment.getClass().getName());
        Log.d(TAG, "setFragment: Creating  " + fragment.getTag());
      }
      transaction.commit();
      currentFragment = fragment;
    } else
      Log.d(TAG, "setFragment: Unchanged " + fragment.getTag());
  }

  private void setExitDialog() {
    final String[][] testBank = {//f:off
            {"校训",            "植基立本", "成德达材"},
            {"办学宗旨",         "为天下人", "谋永福"},
            {"育人八大支柱第一句", "国家责任", "独立人格"},
            {"育人八大支柱第二句", "学会学习", "健体怡情"},
            {"育人八大支柱第三句", "服务意识", "国际视野"},
            {"育人八大支柱第四句", "实践能力", "自力自治"}
    };//f:on
    final String[] test = testBank[
            (int) (System.currentTimeMillis() % testBank.length)];

    new AlertDialog.Builder(this)
            .setTitle("听说你想退出 App?")
            .setIcon(R.mipmap.ic_launcher_fzyz_round)
            .setMessage("福州一中的" + test[0] + "是什么？")
            .setCancelable(false)
            .setNeutralButton("不知道",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(drawer.getDrawerLayout(),
                                test[0] + "：" + test[1] +
                                        (test[2].equals(testBank[1][2]) ? "" : "，") +
                                        test[2] + "。", Snackbar.LENGTH_LONG).show();
                      }
                    })
            .setNegativeButton(test[1],
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                      }
                    })
            .setPositiveButton(test[2],
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                      }
                    })
            .create()
            .show();
  }
}
