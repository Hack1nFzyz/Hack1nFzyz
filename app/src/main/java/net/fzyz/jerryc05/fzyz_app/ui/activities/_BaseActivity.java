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
import androidx.collection.ArraySet;

import net.fzyz.jerryc05.fzyz_app.core.utils.EArrayMap;
import net.fzyz.jerryc05.fzyz_app.core.utils.EOkHttp3Cookie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Dns;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
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

  public static  ThreadPoolExecutor threadPoolExecutor;
  private static Interceptor        removeHeadersInterceptor;
  private        OkHttpClient       okHttpClient;

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

  private static Interceptor getRemoveHeadersInterceptor() {
    if (removeHeadersInterceptor != null) return removeHeadersInterceptor;

    removeHeadersInterceptor = chain ->
            chain.proceed(chain.request().newBuilder()
                    .removeHeader("User-Agent")
                    .removeHeader("Connection")
                    .build());
    return removeHeadersInterceptor;
  }

  public OkHttpClient getOkHttpClient() {
    if (okHttpClient != null) return okHttpClient;

    okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(getRemoveHeadersInterceptor())
            .cookieJar(new CookieJar() {
              private static final String COOKIES_FILENAME = "sys_network_cookies.gz";

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
                     final GZIPOutputStream gzipOS = new GZIPOutputStream(fileOS);
                     final ObjectOutputStream objectOS = new ObjectOutputStream(gzipOS)) {

                  objectOS.writeObject(allCookiesArrayMap);
                  Log.w(TAG, "saveFromResponse: Wrote to file!");

                } catch (final Exception e) {
                  Log.e(TAG, "saveFromResponse: ", e);
                  runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                          e.toString(), Toast.LENGTH_LONG).show());
                }
              }

              @NonNull
              @Override
              public List<Cookie> loadForRequest(@NonNull final HttpUrl url) {
                final File                                                 cookiesFile = new File(getFilesDir(), COOKIES_FILENAME);
                final EArrayMap<String, EArrayMap<String, EOkHttp3Cookie>> allCookiesArrayMap;

                try (final FileInputStream fileIS = getApplicationContext()
                        .openFileInput(COOKIES_FILENAME);
                     final GZIPInputStream gzipIS = new GZIPInputStream(fileIS);
                     final ObjectInputStream objectIS = new ObjectInputStream(gzipIS)) {

                  //noinspection unchecked
                  allCookiesArrayMap = (EArrayMap) objectIS.readObject();
                  Log.w(TAG, "loadForRequest: Loaded from file!");

                } catch (final Exception e) {
                  Log.w(TAG, "loadForRequest: ", e);
                  if (!(e instanceof FileNotFoundException))
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                            e.toString(), Toast.LENGTH_LONG).show());
                  return emptyList();
                }
                final EArrayMap<String, EOkHttp3Cookie> cookiesArrayMap =
                        allCookiesArrayMap.get(url.host());
                if (cookiesArrayMap == null) return emptyList();

                Log.w(TAG, "loadForRequest: " + Arrays.toString(Objects.requireNonNull(
                        allCookiesArrayMap.get(url.host())).values().toArray(new EOkHttp3Cookie[0])));
                ArrayList<Cookie> cookieList = new ArrayList<>(cookiesArrayMap.size());
                for (EOkHttp3Cookie eCookie : cookiesArrayMap.values())
                  cookieList.add(eCookie.toOkHttp3Cookie());
                return cookieList;
              }
            })
            .dns(hostname -> {
              if (!hostname.contains("fzyz.net")) return Dns.SYSTEM.lookup(hostname);

              final ArraySet<InetAddress> set = new ArraySet<>(3);
              set.add(InetAddress.getByName("172.0.0.15"));
              set.add(InetAddress.getByName("110.90.118.123"));
              set.addAll(Dns.SYSTEM.lookup("fzyz.net"));
              return new ArrayList<>(set);
            }).eventListener(new EventListener() {
              @Override
              public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
                Log.w(TAG, "connectStart() called with: call = [" + call + "], inetSocketAddress = [" + inetSocketAddress + "], proxy = [" + proxy + "]");
              }

              @Override
              public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, @Nullable Protocol protocol) {
                Log.w(TAG, "connectEnd() called with: call = [" + call + "], inetSocketAddress = [" + inetSocketAddress + "], proxy = [" + proxy + "], protocol = [" + protocol + "]");
              }

              @Override
              public void connectionAcquired(Call call, Connection connection) {
                Log.w(TAG, "connectionAcquired() called with: call = [" + call + "], connection = [" + connection + "]");
              }

              @Override
              public void connectionReleased(Call call, Connection connection) {
                super.connectionReleased(call, connection);
              }

              @Override
              public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, @Nullable Protocol protocol, IOException ioe) {
                Log.w(TAG, "connectFailed() called with: call = [" + call + "], inetSocketAddress = [" + inetSocketAddress + "], proxy = [" + proxy + "], protocol = [" + protocol + "], ioe = [" + ioe + "]");
              }

              @Override
              public void requestFailed(Call call, IOException ioe) {
                Log.w(TAG, "requestFailed() called with: call = [" + call + "], ioe = [" + ioe + "]");
              }

              @Override
              public void responseFailed(Call call, IOException ioe) {
                Log.w(TAG, "responseFailed() called with: call = [" + call + "], ioe = [" + ioe + "]");
              }

              @Override
              public void callEnd(Call call) {
                Log.w(TAG, "callEnd() called with: call = [" + call + "]");
              }

              @Override
              public void callFailed(Call call, IOException ioe) {
                Log.w(TAG, "callFailed() called with: call = [" + call + "], ioe = [" + ioe + "]");
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
