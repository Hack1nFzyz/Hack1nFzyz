package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import net.fzyz.jerryc05.fzyz_app.R;

import java.util.Objects;

import okhttp3.Response;

import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.OkHttpClientLazyLoader.okHttpClient;
import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.getMyOkHttpRequestBuilder;
import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.threadPoolExecutor;

abstract class _FeedBaseFragment extends Fragment
        implements OnRefreshListener {

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
  public void onResume() {
    super.onResume();

    threadPoolExecutor.execute(() -> {
      if (swipeRefreshLayout == null) {
        View view = getView();
        while (view == null)
          view = getView();
        if (textView == null)
          textView = view.findViewById(R.id.frag_feed_content_textView);
        swipeRefreshLayout = view.findViewById(
                R.id.frag_feed_content_swipeRefreshLayout);
      }
      initSwipeRefreshLayout();
    });
  }

  @WorkerThread
  private void initSwipeRefreshLayout() {
    swipeRefreshLayout.setOnRefreshListener(this);

    while (activity == null)
      activity = getActivity();
    swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(activity, R.color.colorPrimary));
    activity.runOnUiThread(this::onRefresh);
  }

  abstract String getDecodedURL();

  @UiThread
  @Override
  public void onRefresh() {
    swipeRefreshLayout.setRefreshing(true);

    threadPoolExecutor.execute(() -> {
      try (final Response response = okHttpClient.newCall(
              getMyOkHttpRequestBuilder(getDecodedURL()).build()).execute()) {
        final String result = Objects.requireNonNull(
                response.body()).string();
        activity.runOnUiThread(() -> textView.setText(result));

      } catch (Exception e) {
        activity.runOnUiThread(() -> Toast.makeText(activity,
                e.toString(), Toast.LENGTH_LONG).show());
      }

      activity.runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false));
    });
  }
}
