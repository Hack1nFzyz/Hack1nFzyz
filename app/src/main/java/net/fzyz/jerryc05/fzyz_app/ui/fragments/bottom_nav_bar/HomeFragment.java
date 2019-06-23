package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;


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

public class HomeFragment extends Fragment {

  private Activity           activity;
  private SwipeRefreshLayout swipeRefreshLayout;
  private TextView           textView;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_home, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    //f:off
    if (swipeRefreshLayout == null)
        swipeRefreshLayout = view.findViewById(R.id.frag_home_swipeRefreshLayout);
    if (textView           == null)
        textView           = view.findViewById(R.id.frag_home_textView);
    while (activity        == null)
           activity        = getActivity();
    //f:on
    new Thread(this::setSwipeRefreshLayout).start();
  }

  @WorkerThread
  private void setSwipeRefreshLayout() {
    swipeRefreshLayout.setColorSchemeColors(ContextCompat
            .getColor(activity, R.color.colorPrimary));
    swipeRefreshLayout.setOnRefreshListener(() -> new Thread(() -> {

      activity.runOnUiThread(() ->
              textView.setText("Refreshing..."));
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      activity.runOnUiThread(() -> {
        textView.setText("You just refreshed!");
        swipeRefreshLayout.setRefreshing(false);
      });
    }).start());
  }
}
