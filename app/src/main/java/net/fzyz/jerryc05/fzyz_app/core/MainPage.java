package net.fzyz.jerryc05.fzyz_app.core;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import net.fzyz.jerryc05.fzyz_app.R;

import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.threadPoolExecutor;

@WorkerThread
public class MainPage {

  static final String TAG = MainPage.class.getName();

  public static void test(Activity activity, String url, @NonNull final Context context) {
    threadPoolExecutor.execute(() -> {
      try {
        String uurl = (String) WebsiteCollection.class
                .getDeclaredField(url).get(null);
        assert uurl != null;
        String   new_url = URLConnectionBuilder.decodeURL(uurl);
        TextView tv      = activity.findViewById(R.id.frag_home_textView);

        activity.runOnUiThread(() -> tv.setText(new_url + "\n"));

        try (URLConnectionBuilder req = URLConnectionBuilder.get(new_url)) {
          String result = req.connect(context).getResult("gbk");

          activity.runOnUiThread(() -> tv.setText(tv.getText() + result));
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

