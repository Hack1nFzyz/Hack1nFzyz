package net.fzyz.jerryc05.fzyz_app.core;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings({"WeakerAccess", "unused"})
public class MyURLRequestBuilder {

  private final static String
          TAG            = "MyURLRequestBuilder";
  public final static  String
          METHOD_GET     = "GET",
          METHOD_POST    = "POST",
          METHOD_HEAD    = "HEAD",
          METHOD_OPTIONS = "OPTIONS",
          METHOD_PUT     = "PUT",
          METHOD_DELETE  = "DELETE",
          METHOD_TRACE   = "TRACE";

  @StringDef({METHOD_GET, METHOD_POST, METHOD_HEAD, METHOD_OPTIONS,
          METHOD_PUT, METHOD_DELETE, METHOD_TRACE})
  @Retention(RetentionPolicy.SOURCE)
  public @interface RequestMethods {
  }

  @NonNull
  private String
          baseURL,
          requestMethod = METHOD_GET;
  @Nullable
  private String
          result;
  private int
          connectTimeout = 5 * 1000,
          readTimeout    = 5 * 1000;
  private boolean
          useCache = true,
          isHTTP;
  private ArrayMap<String, String>
          requestPropertyMap;
  private URLConnection
          urlConnection;


  public MyURLRequestBuilder(@NonNull String _baseURL) {
    _baseURL = _baseURL.trim();
    if (!_baseURL.startsWith("http://") && !_baseURL.startsWith("https://"))
      throw new UnsupportedOperationException(
              "${baseURL} prefix not recognized: " + _baseURL);

    baseURL = _baseURL;
  }

  public MyURLRequestBuilder buildRequest() throws IOException {
    try {
      URL url = new URL(baseURL);
      urlConnection = url.openConnection();
      urlConnection.setConnectTimeout(connectTimeout);
      urlConnection.setReadTimeout(readTimeout);
      urlConnection.setUseCaches(useCache);

      {
        final char ch = baseURL.charAt(4);
        if (ch == ':')
          isHTTP = true;
        else if (ch != 's') throw new UnsupportedOperationException(
                "Cannot determine request type of " + baseURL);
      }

      (isHTTP ? (HttpURLConnection) urlConnection
              : (HttpsURLConnection) urlConnection)
              .setRequestMethod(requestMethod);
      if (requestPropertyMap != null)
        for (Map.Entry<String, String> entry : requestPropertyMap.entrySet())
          urlConnection.setRequestProperty(
                  entry.getKey(), entry.getValue());
      return this;

    } catch (Exception e) {
      Log.e(TAG, "buildRequest: ", e);
      throw e;
    }
  }

  public String connect() throws IOException {
    checkNullUrlConnection("run");
    try {
      urlConnection.connect();
      {
        InputStream           inputStream  = urlConnection.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[]                buffer       = new byte[1024];
        int                   length;

        while ((length = inputStream.read(buffer)) != -1)
          outputStream.write(buffer, 0, length);

        result = outputStream.toString(StandardCharsets.UTF_8.name());
        inputStream.close();
      }

      Log.d(TAG, "connect: Response Code " + (isHTTP
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

  public void disconnect() {
    checkNullUrlConnection("disconnect");
    (isHTTP ? (HttpURLConnection) urlConnection
            : (HttpsURLConnection) urlConnection)
            .disconnect();
    urlConnection = null;
    Log.d(TAG, "disconnect: " + baseURL + " disconnected!");
  }

  public MyURLRequestBuilder setBaseURL(String _baseURL) {
    baseURL = _baseURL;
    return this;
  }

  public MyURLRequestBuilder setRequestMethod(
          @RequestMethods String _requestMethod) {
    requestMethod = _requestMethod;
    return this;
  }

  public MyURLRequestBuilder setConnectTimeout(
          @IntRange(from = 0) int _connectTimeout) {
    connectTimeout = _connectTimeout;
    return this;
  }

  public MyURLRequestBuilder setReadTimeout(
          @IntRange(from = 0) int _readTimeout) {
    readTimeout = _readTimeout;
    return this;
  }

  public MyURLRequestBuilder setUseCache(boolean _useCache) {
    useCache = _useCache;
    return this;
  }

  public MyURLRequestBuilder putRequestPropertyMap(String key, String value) {
    if (requestPropertyMap == null)
      requestPropertyMap = new ArrayMap<>();
    requestPropertyMap.put(key, value);
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

  @Nullable
  public String getResult() {
    return result;
  }

  private void checkNullUrlConnection(@NonNull String action) {
    if (urlConnection == null)
      throw new UnsupportedOperationException(
              "Cannot " + action + " null instance of ${urlConnection}!");
  }
}
