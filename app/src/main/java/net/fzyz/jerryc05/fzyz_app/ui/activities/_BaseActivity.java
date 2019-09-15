package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import net.fzyz.jerryc05.fzyz_app.BuildConfig;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.OkHttpClient;

import static android.util.Base64.URL_SAFE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.fzyz.jerryc05.fzyz_app.core.WebsiteCollection.URL_CALENDAR_DETAIL;
import static net.fzyz.jerryc05.fzyz_app.core.WebsiteCollection.URL_FZYZ_HOST;

@SuppressWarnings("unused")
public abstract class _BaseActivity extends AppCompatActivity {

  private static final String TAG = "_BaseActivity";

  public static ThreadPoolExecutor threadPoolExecutor;

  private static class OkHttpClientLazyLoader {
    static final OkHttpClient okHttpClient = new OkHttpClient();
  }

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //noinspection deprecation | We knew this is deprecated, but we need it.
    AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
    newThreadPoolExecutor();
  }

  @Override
  protected void onDestroy() {
    if (BuildConfig.DEBUG)
      Log.d(TAG, "onDestroy: Called!");

    threadPoolExecutor.shutdownNow();
    super.onDestroy();
  }

  private static void newThreadPoolExecutor() {
    if (BuildConfig.DEBUG)
      Log.d(TAG, "newThreadPoolExecutor: Called!");

    final int processorCount = Runtime.getRuntime().availableProcessors();
    threadPoolExecutor = new ThreadPoolExecutor(processorCount,
            2 * processorCount, 5, SECONDS,
            new LinkedBlockingQueue<>(1));
    threadPoolExecutor.allowCoreThreadTimeOut(true);
  }

  public static OkHttpClient getOkHttpClient() {
    return OkHttpClientLazyLoader.okHttpClient;
  }

  public boolean isActiveNetworkMetered() {
    return isActiveNetworkMetered(this);
  }

  public static boolean isActiveNetworkMetered(@NonNull final Context context) {
    if (BuildConfig.DEBUG)
      Log.d(TAG, "isActiveNetworkMetered: Called!");

    final ConnectivityManager connectivityManager = Objects.requireNonNull(
            (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE),
            "isActiveNetworkMetered: connectivityManager is null!");
    return connectivityManager.isActiveNetworkMetered();
  }

  /**
   * @param url Encoded url
   * @return Decoded url.
   */
  public static String decodeURL(@NonNull final String url) {
    final String decoded = (url.equals(URL_FZYZ_HOST)
            ? "" : new String(Base64.decode(URL_FZYZ_HOST, URL_SAFE)))
            + new String(Base64.decode(url, URL_SAFE));
    if (BuildConfig.DEBUG)
      Log.d(TAG, "decodeURL: " + decoded);
    return decoded;
  }

  /**
   * Parse date string to its corresponding calendar url.
   *
   * @param date Date of format yyyy-mm-dd.
   * @return Decoded url.
   */
  public static String decodeCalendarDateURL(@NonNull final String date) {
    final String decoded = new String(Base64.decode(URL_FZYZ_HOST, URL_SAFE))
            + new String(Base64.decode(URL_CALENDAR_DETAIL, URL_SAFE))
            + date;
    if (BuildConfig.DEBUG)
      Log.d(TAG, "decodeURL: " + decoded);
    return decoded;
  }
}
