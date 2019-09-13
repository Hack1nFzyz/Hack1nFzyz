package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import net.fzyz.jerryc05.fzyz_app.BuildConfig;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public abstract class _BaseActivity extends AppCompatActivity {

  private static final String TAG = "_BaseActivity";

  public static ThreadPoolExecutor threadPoolExecutor;
  public static OkHttpClient       okHttpClient;

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // We knew this is deprecated, but we need it.
    //noinspection deprecation
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
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

  public static OkHttpClient getOkHttpClient() {
    if (okHttpClient == null)
      okHttpClient = new OkHttpClient();
    return okHttpClient;
  }
}
