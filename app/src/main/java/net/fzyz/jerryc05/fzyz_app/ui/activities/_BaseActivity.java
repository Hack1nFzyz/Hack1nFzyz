package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import net.fzyz.jerryc05.fzyz_app.BuildConfig;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class _BaseActivity extends AppCompatActivity {

  private static final String             TAG = "_BaseActivity";
  public static        ThreadPoolExecutor threadPoolExecutor;

  public static ThreadPoolExecutor getThreadPoolExecutor() {
    if (BuildConfig.DEBUG)
      Log.d(TAG, "getThreadPoolExecutor: ");

    if (threadPoolExecutor == null) {
      final int processorCount = Runtime.getRuntime().availableProcessors();
      threadPoolExecutor = new ThreadPoolExecutor(processorCount,
              2 * processorCount, 5, TimeUnit.SECONDS,
              new LinkedBlockingQueue<>(1));
      threadPoolExecutor.allowCoreThreadTimeOut(true);
    }
    return threadPoolExecutor;
  }

  @Override
  protected void onDestroy() {
    if (BuildConfig.DEBUG)
      Log.d(TAG, "onDestroy: ");

    super.onDestroy();

    if (threadPoolExecutor != null) {
      threadPoolExecutor.shutdownNow();
      threadPoolExecutor = null;
    }
  }
}