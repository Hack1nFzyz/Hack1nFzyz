package net.fzyz.jerryc05.fzyz_app.core;

import android.Manifest;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.StringDef;
import androidx.annotation.WorkerThread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings({"WeakerAccess", "unused"})
@WorkerThread
public class URLConnectionBuilder implements AutoCloseable {

  private final static String
          TAG = URLConnectionBuilder.class.getName();

  public final static String
          METHOD_GET     = "GET",
          METHOD_POST    = "POST",
          METHOD_HEAD    = "HEAD",
          METHOD_OPTIONS = "OPTIONS",
          METHOD_PUT     = "PUT",
          METHOD_DELETE  = "DELETE",
          METHOD_TRACE   = "TRACE";

  @Override
  public void close() {
    disconnect();
  }

  @StringDef({METHOD_GET, METHOD_POST, METHOD_HEAD, METHOD_OPTIONS,
          METHOD_PUT, METHOD_DELETE, METHOD_TRACE})
  @Retention(RetentionPolicy.SOURCE)
  public @interface RequestMethods {
  }

  private boolean       isHTTP;
  private URLConnection urlConnection;

  @RequiresPermission(Manifest.permission.INTERNET)
  private URLConnectionBuilder(@NonNull String _baseURL) throws IOException {
    _baseURL = _baseURL.trim();
    if (!_baseURL.startsWith("http://") && !_baseURL.startsWith("https://"))
      throw new UnsupportedOperationException(
              "${baseURL} prefix not recognized: " + _baseURL);

    urlConnection = new URL(_baseURL).openConnection();
    urlConnection.setConnectTimeout(5 * 1000);
    urlConnection.setReadTimeout(5 * 1000);
    isHTTP = _baseURL.charAt(4) == ':';
  }

  public static URLConnectionBuilder get(String _baseURL) throws IOException {
    return new URLConnectionBuilder(_baseURL);
  }

  public static URLConnectionBuilder post(String _baseURL) throws IOException {
    return new URLConnectionBuilder(_baseURL).setRequestMethod(METHOD_POST);
  }

  public URLConnectionBuilder connect() throws IOException {
    checkNullUrlConnection("run");
    urlConnection.connect();
    return this;
  }

  @Nullable
  public String getResult(@NonNull String charset) throws IOException {
    try {
      String result;
      {
        InputStream           inputStream  = urlConnection.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[]                buffer       = new byte[1024];
        int                   length;

        while ((length = inputStream.read(buffer)) != -1)
          outputStream.write(buffer, 0, length);

        result = outputStream.toString(charset);
        inputStream.close();
      }

      Log.d(TAG, "connect: Response code = " + (isHTTP
              ? (HttpURLConnection) urlConnection
              : (HttpsURLConnection) urlConnection)
              .getResponseCode()
              + "\n================ Respond content ================\n"
              + result);

      return result;

    } catch (Exception e) {
      Log.e(TAG, "connect: ", e);
      throw e;
    }
  }

  @Nullable
  public String getResult() throws IOException {
    return getResult(StandardCharsets.UTF_8.name());
  }

  public void disconnect() {
    checkNullUrlConnection("disconnect");
    (isHTTP ? (HttpURLConnection) urlConnection
            : (HttpsURLConnection) urlConnection)
            .disconnect();

    Log.d(TAG, "disconnect: " + urlConnection.getURL());
  }

  public URLConnectionBuilder setRequestMethod(
          @RequestMethods String _requestMethod) {
    try {
      (isHTTP ? (HttpURLConnection) urlConnection
              : (HttpsURLConnection) urlConnection)
              .setRequestMethod(_requestMethod);
    } catch (java.net.ProtocolException e) {
      Log.e(TAG, "setRequestMethod: ", e);
    }
    return this;
  }

  public URLConnectionBuilder setConnectTimeout(
          @IntRange(from = 0) int _connectTimeout) {
    urlConnection.setConnectTimeout(_connectTimeout);
    return this;
  }

  public URLConnectionBuilder setReadTimeout(
          @IntRange(from = 0) int _readTimeout) {
    urlConnection.setReadTimeout(_readTimeout);
    return this;
  }

  public URLConnectionBuilder setUseCache(boolean _useCache) {
    urlConnection.setUseCaches(_useCache);
    return this;
  }

  public URLConnectionBuilder setRequestProperty(
          @NonNull String key, @NonNull String value) {
    urlConnection.setRequestProperty(key, value);
    return this;
  }

  public URLConnection getUrlConnection() {
    checkNullUrlConnection("get");
    return urlConnection;
  }

  @Nullable
  public URLConnection _getUrlConnection() {
    return urlConnection;
  }

  private void checkNullUrlConnection(@NonNull String action) {
    if (urlConnection == null)
      throw new UnsupportedOperationException(
              "Cannot " + action + " null instance of ${urlConnection}!");
  }
}
