package net.fzyz.jerryc05.fzyz_app.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import net.fzyz.jerryc05.fzyz_app.R;

import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;

import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.getOkHttpClient;
import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.threadPoolExecutor;

@WorkerThread
public class MainPage {

  private static final String TAG = MainPage.class.getName();

  @SuppressLint("SetTextI18n")
  public static void test(@NonNull final Activity activity,
                          @NonNull final String title) {
    threadPoolExecutor.execute(() -> {
      try {
        final String encodedURL = (String) WebsiteCollection.class
                .getDeclaredField(title).get(null);
        assert encodedURL != null;
        final String url = URLConnectionBuilder
                .decodeURL(encodedURL);
        final TextView textView = activity.findViewById(
                R.id.frag_feed_content_textView);

        activity.runOnUiThread(() -> textView.setText(url + "\n"));

        /*






         */
        final Request request = new Request.Builder()
                .url(url)
                .build();

        try (final Response response = getOkHttpClient().newCall(request).execute()) {
          final String result = Objects.requireNonNull(
                  response.body()).string();
//        }

          /*






           */
//        try (URLConnectionBuilder req = URLConnectionBuilder.get(url)) {
//          String result = req.connect(context).getResult("gbk");

          activity.runOnUiThread(() -> textView.setText(textView.getText() + result));
        } catch (Exception e) {
          Log.e(TAG, "test: ", e);
        }
      } catch (Exception e) {
        Log.e(TAG, "test: ", e);
      }
    });
  }
}

// http://www.fzyz.net/school/calendar/getSchoolCalendar.shtml?cal.CALENDAR_DAY=2019-06-15

