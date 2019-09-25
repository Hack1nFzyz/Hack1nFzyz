package net.fzyz.jerryc05.fzyz_app.ui.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import net.fzyz.jerryc05.fzyz_app.core.utils.EArrayMap;
import net.fzyz.jerryc05.fzyz_app.core.utils.EOkHttp3Cookie;
import net.fzyz.jerryc05.fzyz_app.core.utils.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Dns;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

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
  private       OkHttpClient       okHttpClient;

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //noinspection deprecation | We knew this is deprecated, but we need it.
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
    initThreadPoolExecutor();

    Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> {
      Log.e(TAG, "uncaughtException: [" + paramThrowable
              + "] @ [" + paramThread + "]");
      Log.e(TAG, "uncaughtException: ", paramThrowable);
//      new NotificationUtils(getApplicationContext(),
//              CHANNEL_ID_ERROR_HANDLING, R.drawable.ic_launcher_fzyz_foreground,
//              NOTIFICATION_TITLE_FATAL_ERROR).show();

      if (Thread.getDefaultUncaughtExceptionHandler() != null)
        Thread.getDefaultUncaughtExceptionHandler()
                .uncaughtException(paramThread, paramThrowable);
      else
        System.exit(1);
    });
  }

  @Override
  protected void onDestroy() {
    Log.w(TAG, "onDestroy() called");

    final List<Runnable> tasks = threadPoolExecutor.shutdownNow();
    threadPoolExecutor = null;

    Log.w(TAG, "onDestroy(): Unfinished tasks = " + tasks);
    super.onDestroy();
  }

  public OkHttpClient getOkHttpClient() {
    if (okHttpClient != null) return okHttpClient;

    okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
              final int MAX_ATTEMPT = 3;

              for (int attemptCountdown = MAX_ATTEMPT; ; )
                try {
                  return chain.proceed(chain.request());
                } catch (final SocketTimeoutException e) {
                  attemptCountdown--;
                  Log.w(TAG, "intercept: " + e.toString()
                          + "Retrying... attemptCountdown = " + attemptCountdown);
                  if (attemptCountdown <= 1)
                    return chain.proceed(chain.request());
                }
            })
            .addNetworkInterceptor(chain ->
                    chain.proceed(chain.request().newBuilder()
                            .removeHeader("User-Agent")
                            .removeHeader("Connection")
                            .build()))
            .cookieJar(new CookieJar() {
              private static final String COOKIES_FILENAME = "sys_network_cookies.deflate";
              private final Deflater mDeflater = new Deflater(Deflater.BEST_COMPRESSION);

              @Override
              public void saveFromResponse(@NonNull final HttpUrl url,
                                           @NonNull final List<Cookie> cookies) {
                if (cookies.isEmpty()) return;

                final EArrayMap<String, EArrayMap
                        <String, EOkHttp3Cookie>> allCookiesArrayMap =
                        new EArrayMap<>(1);

                final EArrayMap<String, EOkHttp3Cookie>
                        existingExternalizableOkHttp3Cookies = allCookiesArrayMap.get(url.host());
                final EArrayMap<String, EOkHttp3Cookie>
                        cookiesExternalizableArrayMap =
                        existingExternalizableOkHttp3Cookies != null
                                ? existingExternalizableOkHttp3Cookies
                                : new EArrayMap<>(cookies.size());

                for (Cookie cookie : cookies)
                  cookiesExternalizableArrayMap.put(
                          cookie.name(), EOkHttp3Cookie.of(cookie));
                allCookiesArrayMap.put(url.host(), cookiesExternalizableArrayMap);

                Log.w(TAG, "saveFromResponse: " + Arrays.toString(Objects.requireNonNull(
                        allCookiesArrayMap.get(url.host())).values().toArray(new EOkHttp3Cookie[0])));

                try (final FileOutputStream fileOS = getApplicationContext()
                        .openFileOutput(COOKIES_FILENAME, MODE_PRIVATE);
                     final DeflaterOutputStream deflaterOS =
                             new DeflaterOutputStream(fileOS, mDeflater);
                     final ObjectOutputStream objectOS = new ObjectOutputStream(deflaterOS)) {

                  objectOS.writeObject(allCookiesArrayMap);
                  Log.w(TAG, "saveFromResponse: Cookies wrote to file!");

                } catch (final Exception e) {
                  Log.e(TAG, "saveFromResponse: ", e);
                  ToastUtils.showText(_BaseActivity.this, e.toString(), Toast.LENGTH_LONG);
                }
              }

              @NonNull
              @Override
              public List<Cookie> loadForRequest(@NonNull final HttpUrl url) {
                final File                                                 cookiesFile = new File(getFilesDir(), COOKIES_FILENAME);
                final EArrayMap<String, EArrayMap<String, EOkHttp3Cookie>> allCookiesArrayMap;

                try (final FileInputStream fileIS = getApplicationContext()
                        .openFileInput(COOKIES_FILENAME);
                     final DeflaterInputStream deflaterIS = new DeflaterInputStream(
                             fileIS, mDeflater);
                     final ObjectInputStream objectIS = new ObjectInputStream(deflaterIS)) {

                  //noinspection unchecked
                  allCookiesArrayMap = (EArrayMap) objectIS.readObject();
                  Log.w(TAG, "loadForRequest: Cookies loaded from file!");

                } catch (final Exception e) {
                  Log.w(TAG, "loadForRequest: ", e);
                  if (!(e instanceof FileNotFoundException))
                    ToastUtils.showText(_BaseActivity.this,
                            e.toString(), Toast.LENGTH_LONG);
                  return emptyList();
                }
                final EArrayMap<String, EOkHttp3Cookie> cookiesArrayMap =
                        allCookiesArrayMap.get(url.host());
                if (cookiesArrayMap == null) return emptyList();

                Log.w(TAG, "loadForRequest: " + Arrays.toString(Objects.requireNonNull(
                        allCookiesArrayMap.get(url.host())).values()
                        .toArray(new EOkHttp3Cookie[0])));
                final ArrayList<Cookie> cookieList = new ArrayList<>(cookiesArrayMap.size());
                for (EOkHttp3Cookie eCookie : cookiesArrayMap.values())
                  cookieList.add(eCookie.toOkHttp3Cookie());
                return cookieList;
              }
            })
            .dns(hostname -> {
              if (hostname.contains("fzyz.net")) {
                final List<InetAddress> sysResult =
                        Dns.SYSTEM.lookup("fzyz.net");
                final ArrayList<InetAddress> inetAddresses =
                        new ArrayList<>(sysResult.size() + 1);
                inetAddresses.add(InetAddress.getByName("110.90.118.123"));
                return inetAddresses;

              } else
                return Dns.SYSTEM.lookup(hostname);
            })
            .readTimeout(2, SECONDS)
            .eventListener(new EventListener() {
              @Override
              public void connectEnd(Call call, InetSocketAddress inetSocketAddress,
                                     Proxy proxy, Protocol protocol) {
                Log.w(TAG, "connectEnd() called with: inetSocketAddress = ["
                        + inetSocketAddress + "], proxy = [" + proxy
                        + "], protocol = [" + protocol + "]");
              }
            })
            .build();
    return okHttpClient;
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
