package net.fzyz.jerryc05.fzyz_app.core.utils;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;


public final class ToastUtils {
  @IntDef({LENGTH_SHORT, LENGTH_LONG})
  @Retention(RetentionPolicy.SOURCE)
  @interface Duration {
  }

  public static void showText(@NonNull final Activity activity,
                              @NonNull final CharSequence text, @Duration int duration) {
    activity.runOnUiThread(() -> Toast.makeText(
            activity.getApplicationContext(), text, duration).show());
  }
}
