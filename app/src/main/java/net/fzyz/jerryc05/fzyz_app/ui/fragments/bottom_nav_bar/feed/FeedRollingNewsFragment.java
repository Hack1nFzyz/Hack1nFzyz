package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import net.fzyz.jerryc05.fzyz_app.R;
import net.fzyz.jerryc05.fzyz_app.core.URLConnectionBuilder;
import net.fzyz.jerryc05.fzyz_app.core.WebsiteCollection;

import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;

import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.getOkHttpClient;
import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.threadPoolExecutor;

public final class FeedRollingNewsFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {

  public final static  int    STRING_ID = R.string.rolling_news;
  private final static String TAG       = "FeedRollingNewsFragment";

  private TextView           textView;
  private SwipeRefreshLayout swipeRefreshLayout;
  private Activity           activity;

  @Nullable
  @Override

  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_feed_content, container, false);
  }

  @Override
  public void onViewCreated(@NonNull final View view,
                            @Nullable final Bundle savedInstanceState) {
    threadPoolExecutor.execute(() -> { //f:off
      if (textView           == null)
          textView           = view.findViewById(R.id.frag_feed_content_textView);
      if (swipeRefreshLayout == null)
          swipeRefreshLayout = view.findViewById(
                  R.id.frag_feed_content_swipeRefreshLayout);
      while (activity        == null)
             activity        = getActivity(); //f:on

      initSwipeRefreshLayout();
    });
  }

  private void initSwipeRefreshLayout() {
    swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(activity, R.color.colorPrimary));
    swipeRefreshLayout.setOnRefreshListener(this);
    onRefresh();
  }

  @WorkerThread
  @Override
  public void onRefresh() {
    swipeRefreshLayout.setRefreshing(true);

    threadPoolExecutor.execute(() -> {
      final String url = URLConnectionBuilder
              .decodeURL(WebsiteCollection.URL_ROLLING_NEWS_GunDongXinWen);

      final Request request = new Request.Builder()
              .url(url)
              .build();

      try (final Response response = getOkHttpClient().newCall(request).execute()) {
        final String result = Objects.requireNonNull(
                response.body()).string();
        activity.runOnUiThread(() -> textView.setText(result));

      } catch (Exception e) {
        throw new RuntimeException(e);
      }

      activity.runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false));
    });
  }
}
