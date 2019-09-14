package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.fzyz.jerryc05.fzyz_app.R;

public final class FeedLatestNewsFragment extends Fragment {

  public static final int STRING_ID = R.string.latest_news;

  @Nullable
  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_feed_content, container, false);
  }
}
