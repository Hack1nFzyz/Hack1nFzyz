package net.fzyz.jerryc05.fzyz_app.core.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.app.Notification.PRIORITY_DEFAULT;
import static android.app.Notification.PRIORITY_LOW;
import static android.app.Notification.PRIORITY_MAX;
import static android.app.Notification.PRIORITY_MIN;
import static android.os.Build.VERSION_CODES.O;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class NotificationUtils {
  private static final String TAG = "NotificationUtils";

  public static final String CHANNEL_ID_ERROR_HANDLING = "Error Handling";

  @StringDef({CHANNEL_ID_ERROR_HANDLING})
  @Retention(RetentionPolicy.SOURCE)
  @interface ChannelIdAndName {
  }

  public static final String NOTIFICATION_TITLE_FATAL_ERROR = "Fatal Error";

  @StringDef({NOTIFICATION_TITLE_FATAL_ERROR})
  @Retention(RetentionPolicy.SOURCE)
  @interface NotificationTitle {
  }

  private final Notification.Builder mBuilder;
  private final int                  mNotificationId;

  private final NotificationManager mNotificationManager;
  private final NotificationChannel mNotificationChannel;

  public NotificationUtils(@NonNull final Context context,
                           @NonNull @ChannelIdAndName final String CHANNEL_ID_AND_NAME,
                           @DrawableRes final int SMALL_ICON,
                           @NonNull @NotificationTitle final String TITLE,
                           @NonNull final String TEXT) {
    mBuilder = Build.VERSION.SDK_INT >= O
            ? new Notification.Builder(context, CHANNEL_ID_AND_NAME)
            : new Notification.Builder(context);
    mBuilder.setSmallIcon(SMALL_ICON);

    mNotificationId = TITLE.hashCode();
    mNotificationManager = ((NotificationManager) context.getSystemService(
            Context.NOTIFICATION_SERVICE));
    mNotificationChannel = Build.VERSION.SDK_INT >= O
            ? new NotificationChannel(CHANNEL_ID_AND_NAME, CHANNEL_ID_AND_NAME,
            NotificationManager.IMPORTANCE_DEFAULT)
            : null;
    mBuilder.setContentTitle(TITLE).setContentText(TEXT);
  }

  public NotificationUtils withAutoCancel(final boolean AUTO_CANCEL) {
    mBuilder.setAutoCancel(AUTO_CANCEL);
    return this;
  }

  public static final int
          IMPORTANCE_NONE    = 0,
          IMPORTANCE_MIN     = 1,
          IMPORTANCE_LOW     = 2,
          IMPORTANCE_DEFAULT = 3,
          IMPORTANCE_HIGH    = 4;

  @IntDef({IMPORTANCE_NONE, IMPORTANCE_MIN, IMPORTANCE_LOW, IMPORTANCE_DEFAULT, IMPORTANCE_HIGH})
  @Retention(RetentionPolicy.SOURCE)
  public @interface ImportanceOrPriority {
  }

  public NotificationUtils withImportanceOrPriority(final int IMPORTANCE_OR_PRIORITY) {
    Log.i(TAG, "withImportanceOrPriority() called with: IMPORTANCE_OR_PRIORITY = ["
            + IMPORTANCE_OR_PRIORITY + "]");
    if (Build.VERSION.SDK_INT >= O) {
      mNotificationChannel.setImportance(IMPORTANCE_OR_PRIORITY);
      if (IMPORTANCE_OR_PRIORITY >= IMPORTANCE_DEFAULT)
        mNotificationChannel.enableVibration(true);
    } else {
      switch (IMPORTANCE_OR_PRIORITY) {
        case IMPORTANCE_NONE:
        case IMPORTANCE_MIN:
          mBuilder.setPriority(PRIORITY_MIN);
          break;
        case IMPORTANCE_LOW:
          mBuilder.setPriority(PRIORITY_LOW);
          break;
        case IMPORTANCE_DEFAULT:
          mBuilder.setPriority(PRIORITY_DEFAULT).setVibrate(new long[0]);
          break;
        case IMPORTANCE_HIGH:
//                  mBuilder.setPriority(PRIORITY_HIGH);
          mBuilder.setPriority(PRIORITY_MAX).setVibrate(new long[0]);
          break;
      }
    }
    return this;
  }

  public NotificationUtils withAction(@DrawableRes final int ICON,
                                      @NonNull final CharSequence TITLE,
                                      @NonNull final PendingIntent INTENT) {
    mBuilder.addAction(ICON, TITLE, INTENT);
    return this;
  }

  public void show() {
    mNotificationManager.notify(mNotificationId, mBuilder.build());
  }
}
