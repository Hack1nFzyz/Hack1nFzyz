package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.URL_SAFE;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiFzyz.URL_CALENDAR_DETAIL;
import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiFzyz.URL_FZYZ_HOST;

@SuppressWarnings("unused")
public abstract class _BaseActivity extends AppCompatActivity {

  private static final String TAG = "_BaseActivity";

  public static ThreadPoolExecutor threadPoolExecutor;

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //noinspection deprecation | We knew this is deprecated, but we need it.
    AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
    initThreadPoolExecutor();
  }

  @Override
  protected void onDestroy() {
    Log.w(TAG, "onDestroy() called");

    final List<Runnable> tasks = threadPoolExecutor.shutdownNow();
    threadPoolExecutor = null;

    Log.w(TAG, "onDestroy(): Unfinished tasks = " + tasks);
    super.onDestroy();
  }

  public static final class OkHttpClientLazyLoader {
    private static final Interceptor  removeHeadersInterceptor = chain ->
            chain.proceed(chain.request().newBuilder()
                    .removeHeader("User-Agent")
                    .removeHeader("Connection")
                    .build());
    private static final CookieJar    cookieJar                = new CookieJar() {
      private ArrayMap<String, ArrayMap<String, Cookie>> allCookiesArrayMap;

      @Override
      public void saveFromResponse(@NonNull final HttpUrl url,
                                   @NonNull final List<Cookie> cookies) {
        if (!cookies.isEmpty()) {
          if (allCookiesArrayMap == null)
            allCookiesArrayMap = new ArrayMap<>(1);

          final ArrayMap<String, Cookie> existingCookies = allCookiesArrayMap.get(url.host());
          final ArrayMap<String, Cookie> cookiesArrayMap = existingCookies != null
                  ? existingCookies : new ArrayMap<>(cookies.size());
          for (Cookie cookie : cookies)
            cookiesArrayMap.put(cookie.name(), cookie);
          allCookiesArrayMap.put(url.host(), cookiesArrayMap);

          Log.w(TAG, "saveFromResponse: " + Arrays.toString(Objects.requireNonNull(
                  allCookiesArrayMap.get(url.host())).values().toArray(new Cookie[0])));
        }
      }

      @NonNull
      @Override
      public List<Cookie> loadForRequest(@NonNull final HttpUrl url) {
        if (allCookiesArrayMap == null)
          return emptyList();

        final ArrayMap<String, Cookie> cookiesArrayMap = allCookiesArrayMap.get(url.host());
        Log.w(TAG, "loadForRequest: " + (cookiesArrayMap != null
                ? Arrays.toString(Objects.requireNonNull(
                allCookiesArrayMap.get(url.host())).values().toArray(new Cookie[0]))
                : "null"));
        return cookiesArrayMap == null ? emptyList()
                : new ArrayList<>(cookiesArrayMap.values());
      }
    };
    public static final  OkHttpClient okHttpClient             = new OkHttpClient.Builder()
            .addNetworkInterceptor(removeHeadersInterceptor)
            .cookieJar(cookieJar)
            .build();
  }

  private static void initThreadPoolExecutor() {
    Log.w(TAG, "initThreadPoolExecutor() called");

    if (threadPoolExecutor != null)
      return;

    final int processorCount = Runtime.getRuntime().availableProcessors();
    threadPoolExecutor = new ThreadPoolExecutor(processorCount,
            2 * processorCount, 5, SECONDS,
            new LinkedBlockingQueue<>(1));
    threadPoolExecutor.allowCoreThreadTimeOut(true);
  }

  public final boolean isActiveNetworkMetered() {
    return isActiveNetworkMetered(getApplicationContext());
  }

  public static boolean isActiveNetworkMetered(@NonNull final Context context) {
    Log.w(TAG, "isActiveNetworkMetered() called with: context = [" + context + "]");

    return Objects.requireNonNull((ConnectivityManager)
                    context.getSystemService(CONNECTIVITY_SERVICE),
            "isActiveNetworkMetered: connectivityManager is null!")
            .isActiveNetworkMetered();
  }

  /**
   * @param url Encoded url
   * @return Decoded url.
   */
  public static String decodeURL(@NonNull final String url) {
    Log.w(TAG, "decodeURL() called with: url = [" + url + "]");

    final String decoded = (url.equals(URL_FZYZ_HOST)
            ? "" : new String(Base64.decode(URL_FZYZ_HOST, URL_SAFE)))
            + new String(Base64.decode(url, DEFAULT));

    Log.w(TAG, "decodeURL() returned: " + decoded);
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

    Log.w(TAG, "decodeCalendarDateURL() returned: " + decoded);
    return decoded;
    // http://www.fzyz.net/school/calendar/getSchoolCalendar.shtml?cal.CALENDAR_DAY=2019-06-15
  }
}
