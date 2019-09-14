package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import net.fzyz.jerryc05.fzyz_app.BuildConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public abstract class _BaseActivity extends AppCompatActivity {

  private static final String TAG = "_BaseActivity";

  public static  ThreadPoolExecutor threadPoolExecutor;
  private static OkHttpClient       okHttpClient;

  private static final int
          NOT_CONNECTED = -1,
          CELLULAR      = 0,
          WIFI          = 1;

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({NOT_CONNECTED, CELLULAR, WIFI})
  private @interface ConnectionType {
  }

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //noinspection deprecation | We knew this is deprecated, but we need it.
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
  }

  @Override
  protected void onDestroy() {
    if (BuildConfig.DEBUG)
      Log.d(TAG, "onDestroy: Called!");

    if (threadPoolExecutor != null) {
      threadPoolExecutor.shutdownNow();
      threadPoolExecutor = null;
    }
    super.onDestroy();
  }

  public static ThreadPoolExecutor getThreadPoolExecutor() {
    if (BuildConfig.DEBUG)
      Log.d(TAG, "getThreadPoolExecutor: Called!");

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

  @SuppressWarnings("unused")
  @ConnectionType
  public int isWifi() {
    return isWifi(this);
  }


  @SuppressWarnings("unused")
  @ConnectionType
  public static int isWifi(@NonNull final Activity activity) {
    if(BuildConfig.DEBUG)
      Log.d(TAG, "isWifi: Called!");

    final ConnectivityManager connectivityManager = Objects.requireNonNull(
            (ConnectivityManager)
                    activity.getSystemService(Context.CONNECTIVITY_SERVICE),
            "isWifi: connectivityManager is null");
    // We knew these are deprecated, but we need it.
    final NetworkInfo wifiInfo = Objects.requireNonNull(
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI),
            "isWifi: wifiInfo is null");
    final NetworkInfo mobileInfo = Objects.requireNonNull(
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE),
            "isWifi: mobileInfo is null");

    if (mobileInfo.isConnectedOrConnecting())
      return CELLULAR;
    else if (wifiInfo.isConnectedOrConnecting())
      return WIFI;
    else
      return NOT_CONNECTED;
  }
}
