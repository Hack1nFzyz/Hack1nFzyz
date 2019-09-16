package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.AuthenticationCallback;
import androidx.biometric.BiometricPrompt.AuthenticationResult;
import androidx.biometric.BiometricPrompt.PromptInfo;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import net.fzyz.jerryc05.fzyz_app.R;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.AcademicFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.FeedFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.ProfileFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.ProfileLoggedInFragment;

import java.lang.ref.WeakReference;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public final class MainActivity extends _BaseActivity {

  private static final String TAG = "MainActivity";

  private DrawerLayout            drawerLayout;
  private Fragment                currentFragment;
  private FragmentManager         fragmentManager;
  static  WeakReference<Activity> activityWeakReference; // Synthetic accessor in AuthenticationCallback

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    threadPoolExecutor.execute(this::setToolBarAndDrawer);
    threadPoolExecutor.execute(this::setBottomNavView);
//    threadPoolExecutor.execute(() ->
    setFragment(FeedFragment.class)
//    )
    ;
  }

  @Override
  public boolean onCreateOptionsMenu(@NonNull final Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
    getBiometricPrompt().authenticate(getPromptInfo());
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START))
      drawerLayout.closeDrawer(GravityCompat.START);
    else
      threadPoolExecutor.execute(this::setExitDialog);
  }

  @WorkerThread
  private void setToolBarAndDrawer() {
    final MaterialToolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    drawerLayout = findViewById(R.id.drawer_layout);
    final ActionBarDrawerToggle actionBarDrawerToggle =
            new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.open_drawer, R.string.appbar_scrolling_view_behavior);
    actionBarDrawerToggle.syncState();
    runOnUiThread(() -> drawerLayout.addDrawerListener(actionBarDrawerToggle));
  }

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
                case R.id.nav_misc:
                  fragmentClass = ProfileFragment.class;
                  break;
                default:
                  return true;
              }
              setFragment(fragmentClass);
              return true;
            };

    runOnUiThread(() -> ((BottomNavigationView)
            findViewById(R.id.activity_main_bottomNavView))
            .setOnNavigationItemSelectedListener(onNavigationItemSelectedListener));
  }

  private BiometricPrompt getBiometricPrompt() {
    if (activityWeakReference == null)
      activityWeakReference = new WeakReference<>(this);

    final AuthenticationCallback authenticationCallback = new AuthenticationCallback() {
      @Override
      public void onAuthenticationError(int errorCode,
                                        @NonNull final CharSequence errString) {
        final Activity activity = activityWeakReference.get();
        if (activity != null)
          activity.runOnUiThread(() -> Toast.makeText(activity,
                  errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON
                          ? "NegativeButton" : errString, LENGTH_SHORT).show());
      }

      @Override
      public void onAuthenticationSucceeded(
              @NonNull final AuthenticationResult result) {
        final Activity activity = activityWeakReference.get();
        if (activity != null)
          activity.runOnUiThread(() -> Toast.makeText(activity,
                  "SUCCESS", LENGTH_SHORT).show());
//          biometricPrompt = null;
//          promptInfo = null; // Set both to null will crash on MIUI.
      }

      @Override
      public void onAuthenticationFailed() {
        final Activity activity = activityWeakReference.get();
        if (activity != null)
          activity.runOnUiThread(() -> Toast.makeText(activity,
                  "FAILED", LENGTH_SHORT).show());
      }
    };
    return new BiometricPrompt(this,
            threadPoolExecutor, authenticationCallback);
  }

  private static PromptInfo getPromptInfo() {
    return new PromptInfo.Builder()
            .setTitle("This is title")
            .setSubtitle("This is subtitle")
            .setDescription("This is description")
//              .setNegativeButtonText("NegativeButton")
            .setConfirmationRequired(true)
            .setDeviceCredentialAllowed(true)
            .build();
  }

  //  @WorkerThread
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
        Log.w(TAG, "setFragment: Removing  " + currentFragment.getTag());
        transaction.remove(currentFragment);

      } else if (currentFragment != null) {
        Log.w(TAG, "setFragment: Hiding    " + currentFragment.getTag());
        transaction.hide(currentFragment);
      }

      if (existingFragment != null) {
        transaction.show(existingFragment);
        currentFragment = existingFragment;
        Log.w(TAG, "setFragment: Reusing   " + existingFragment.getTag());

      } else {
        try {
          final Fragment newFragment = (Fragment) fragmentClass.newInstance();
          transaction.add(R.id.activity_main_frameLayout, newFragment,
                  fragmentClass.getSimpleName());
          currentFragment = newFragment;
          Log.w(TAG, "setFragment: Creating  "
                  + fragmentClass.getSimpleName());
        } catch (final Exception e) {
          throw new IllegalStateException("Cannot instantiate "
                  + fragmentClass.getSimpleName());
        }
      }
      transaction.commit();
    } else
      Log.w(TAG, "setFragment: Unchanged " + fragmentClass.getSimpleName());
  }

  private void setExitDialog() {
    final String[][] testBank = {
            {"校训", "植基立本", "成德达材"},
            {"校风", "勤奋、严谨", "竞取、活跃"},
            {"教风", "严、实", "细、活"},
            {"办学宗旨", "为天下人", "谋永福"},
            {"精神楷模前两句", "坚持真理", "嫉恶如仇"},
            {"精神楷模后两句", "铁骨铮铮", "宁折不弯"},
            {"育人目标前两句", "心术端正", "文行交修"},
            {"育人目标后两句", "博通时务", "讲求实用"},
            {"学子风范前两句", "基础扎实", "素质全面"},
            {"学子风范后两句", "志向高远", "风仪端朴"},
            {"育人八大支柱第一句", "国家责任", "独立人格"},
            {"育人八大支柱第二句", "学会学习", "健体怡情"},
            {"育人八大支柱第三句", "服务意识", "国际视野"},
            {"育人八大支柱第四句", "实践能力", "自力自治"}
    };
    final String[] test = testBank[
            (int) (System.currentTimeMillis() % testBank.length)];

    final MaterialAlertDialogBuilder alertDialogBuilder =
            new MaterialAlertDialogBuilder(this)
                    .setTitle("听说你想退出 App?")
                    .setIcon(R.mipmap.ic_launcher_fzyz_round)
                    .setMessage("福州一中的 " + test[0] + " 是什么？")
                    .setCancelable(false)
                    .setNeutralButton("不知道", (dialogInterface, i) ->
                            Snackbar.make(drawerLayout,
                                    test[0] + "：" + test[1]
                                            + (test[2].equals(testBank[1][2]) ? "" : "，")
                                            + test[2] + "。",
                                    Snackbar.LENGTH_LONG).show())
                    .setNegativeButton(test[1],
                            (dialogInterface, i) -> MainActivity.super.onBackPressed())
                    .setPositiveButton(test[2],
                            (dialogInterface, i) -> MainActivity.super.onBackPressed());
    runOnUiThread(alertDialogBuilder::show);
  }
}
