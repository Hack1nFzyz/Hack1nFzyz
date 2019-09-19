package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed;

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
import net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;

import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.threadPoolExecutor;

abstract class _FeedBaseFragment extends Fragment
        implements OnRefreshListener {

  private TextView           textView;
  private SwipeRefreshLayout swipeRefreshLayout;
  private _BaseActivity      activity;

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
    while (activity == null)
      activity = (_BaseActivity) getActivity();
    activity.runOnUiThread(() -> {
      swipeRefreshLayout.setOnRefreshListener(this);
      swipeRefreshLayout.setColorSchemeColors(
              ContextCompat.getColor(activity, R.color.colorPrimary));
      onRefresh();
    });
  }

  abstract String getDecodedURL();

  @UiThread
  @Override
  public void onRefresh() {
    swipeRefreshLayout.setRefreshing(true);

    threadPoolExecutor.execute(() -> {
      try (final Response response = activity.getOkHttpClient().newCall(
              new Request.Builder().url(getDecodedURL()).build()).execute()) {
        final String result = Objects.requireNonNull(
                response.body()).string();
        activity.runOnUiThread(() -> textView.setText(result));

      } catch (final UnknownHostException e) {
        activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(),
                "Wait... Did you connect to the internet?", Toast.LENGTH_LONG).show());

      } catch (final SocketTimeoutException e) {
        activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(),
                "Wait... Do you have a stable internet connection?", Toast.LENGTH_LONG).show());

      } catch (final Exception e) {
        throw new IllegalStateException(e);
      }

      activity.runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false));
    });
  }
}
