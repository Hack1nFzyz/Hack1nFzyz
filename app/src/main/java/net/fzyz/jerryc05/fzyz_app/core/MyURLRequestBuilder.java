package net.fzyz.jerryc05.fzyz_app.core;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;


@SuppressWarnings({"WeakerAccess", "unused"})
public class MyURLRequestBuilder implements Runnable {

  private final static String TAG = "MyURLRequestBuilder";

  @NonNull
  private String
          baseURL,
          requestMethod = "GET";
  @Nullable
  public  String
          result;
  private int
          connectTimeout = 5 * 1000,
          readTimeout    = 5 * 1000;
  private boolean
          useCache = true,
          isHTTP;
  private HashMap<String, String>
          requestPropertyMap;
  private URLConnection
          urlConnection;


  public MyURLRequestBuilder(@NonNull String _baseURL) {
    baseURL = _baseURL.trim();
  }

  @Nullable
  public URLConnection buildConnection() {
    Log.d(TAG, "buildConnection: ");
    try {
      URL url = new URL(baseURL);
      urlConnection = url.openConnection();
      urlConnection.setConnectTimeout(connectTimeout);
      urlConnection.setReadTimeout(readTimeout);
      urlConnection.setUseCaches(useCache);

      final char ch = baseURL.charAt(4);
      if (ch == ':')
        isHTTP = true;
      else if (ch != 's') throw new UnsupportedOperationException(
              "Cannot determine request type of " + baseURL);

      (isHTTP
              ? (HttpURLConnection) urlConnection
              : (HttpsURLConnection) urlConnection)
              .setRequestMethod(requestMethod);
      if (requestPropertyMap != null)
        for (Map.Entry<String, String> entry : requestPropertyMap.entrySet())
          urlConnection.setRequestProperty(
                  entry.getKey(), entry.getValue());
      return urlConnection;

    } catch (UnsupportedOperationException e) {
      throw e;
    } catch (Exception e) {
      Log.w(TAG, "build: " + e.getMessage());
    }
    return null;
  }

  public MyURLRequestBuilder buildRequest() {
    buildConnection();
    return this;
  }

  @Override
  public void run() {
    if (urlConnection == null)
      throw new UnsupportedOperationException("urlConnection == null!");

    try {
      urlConnection.connect();

      Scanner scanner = new java.util.Scanner(
              urlConnection.getInputStream()).useDelimiter("\\A");
      result = scanner.hasNext() ? scanner.next() : null;

      Log.e(TAG, "run: Response Code "
              + (isHTTP
              ? (HttpURLConnection) urlConnection
              : (HttpsURLConnection) urlConnection).getResponseCode()
              + "\nRespond: " + result);

    } catch (Exception e) {
      Log.w(TAG, "run: " + e.getMessage());
    } finally {

      (isHTTP
              ? (HttpURLConnection) urlConnection
              : (HttpsURLConnection) urlConnection).disconnect();
    }
  }

  public MyURLRequestBuilder setBaseURL(String _baseURL) {
    baseURL = _baseURL;
    return this;
  }

  public MyURLRequestBuilder setRequestMethod(String _requestMethod) {
    requestMethod = _requestMethod;
    return this;
  }

  public MyURLRequestBuilder setConnectTimeout(int _connectTimeout) {
    connectTimeout = _connectTimeout;
    return this;
  }

  public MyURLRequestBuilder setReadTimeout(int _readTimeout) {
    readTimeout = _readTimeout;
    return this;
  }

  public MyURLRequestBuilder setUseCache(boolean _useCache) {
    useCache = _useCache;
    return this;
  }

  public MyURLRequestBuilder putRequestPropertyMap(String key, String value) {
    if (requestPropertyMap == null)
      requestPropertyMap = new HashMap<>();
    requestPropertyMap.put(key, value);
    return this;
  }

  @Nullable
  public URLConnection getUrlConnection() {
    return urlConnection;
  }
}
