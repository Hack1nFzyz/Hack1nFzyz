package net.fzyz.jerryc05.fzyz_app.core.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class NotificationUtils {

  private final Notification.Builder mBuilder;
  private final Context              mContext;
  private final int                  mNotificationId;

  public static final String CHANNEL_ID_ERROR_HANDLING = "Error Handling";

  @StringDef({CHANNEL_ID_ERROR_HANDLING})
  @Retention(RetentionPolicy.SOURCE)
  @interface ChannelId {
  }

  public static final String NOTIFICATION_TITLE_FATAL_ERROR = "Fatal Error";

  @StringDef({NOTIFICATION_TITLE_FATAL_ERROR})
  @Retention(RetentionPolicy.SOURCE)
  @interface NotificationTitle {
  }

  public NotificationUtils(@NonNull final Context context,
                           @NonNull @ChannelId final String CHANNEL_ID,
                           @DrawableRes final int SMALL_ICON,
                           @NonNull @NotificationTitle final String TITLE) {
    mBuilder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            ? new Notification.Builder(context, CHANNEL_ID)
            : new Notification.Builder(context);
    mBuilder.setSmallIcon(SMALL_ICON);

    mContext = context;
    mBuilder.setContentTitle(TITLE);
    mNotificationId = TITLE.hashCode();
  }

  public void show() {
    //noinspection ConstantConditions
    ((NotificationManager) mContext.getSystemService(
            Context.NOTIFICATION_SERVICE)).notify(mNotificationId, mBuilder.build());
  }
}
