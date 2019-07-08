package net.fzyz.jerryc05.fzyz_app.core;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import net.fzyz.jerryc05.fzyz_app.R;

@WorkerThread
public class MainPage {

  static final String TAG = MainPage.class.getName();

  public static void test(AppCompatActivity activity, String url) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          String uurl = (String) WebsiteCollection.class
                  .getDeclaredField(url).get(null);
          assert uurl != null;
          String   new_url = URLConnectionBuilder.decodeURL(uurl);
          TextView tv      = activity.findViewById(R.id.frag_home_textView);

          activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              tv.setText(new_url + "\n");
            }
          });

          try (URLConnectionBuilder req = URLConnectionBuilder.get(new_url)) {
            String result = req.connect().getResult("gbk");

            activity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                tv.setText(tv.getText() + result);
              }
            });
          } catch (Exception e) {
            Log.e(TAG, "test: ", e);
          }
        } catch (Exception e) {
          Log.e(TAG, "test: ", e);
        }
      }
    }).start();
  }
}

// http://www.fzyz.net/school/calendar/getSchoolCalendar.shtml?cal.CALENDAR_DAY=2019-06-15

