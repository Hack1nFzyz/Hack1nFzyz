package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import net.fzyz.jerryc05.fzyz_app.R;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedHeadlineNewsFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedLatestNewsFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedOfficeAnnouncementFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedRollingNewsFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedSchoolAffairsFragment;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.threadPoolExecutor;

public class FeedFragment extends Fragment {

  private static final String TAG = "FeedFragment";

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private Activity  activity;

  @Nullable
  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_feed, container, false);
  }

  @Override
  public void onViewCreated(@NonNull final View view,
                            @Nullable final Bundle savedInstanceState) {
    threadPoolExecutor.execute(() -> { //f:off
    if (tabLayout == null)
        tabLayout =  view.findViewById(R.id.frag_feed_tabLayout);
    if (viewPager == null)
        viewPager =  view.findViewById(R.id.frag_feed_viewPager); //f:on
      viewPager.setOffscreenPageLimit(2);

      final FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(
              getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        final Class[] fragmentClasses = new Class[]{
                FeedRollingNewsFragment.class,
                FeedOfficeAnnouncementFragment.class,
                FeedSchoolAffairsFragment.class,
                FeedHeadlineNewsFragment.class,
                FeedLatestNewsFragment.class
        };
        final String[] fragmentNames = {
                getString(FeedRollingNewsFragment.STRING_ID),
                getString(FeedOfficeAnnouncementFragment.STRING_ID),
                getString(FeedSchoolAffairsFragment.STRING_ID),
                getString(FeedHeadlineNewsFragment.STRING_ID),
                getString(FeedLatestNewsFragment.STRING_ID),
        };

        @Override
        public CharSequence getPageTitle(int position) {
          return fragmentNames[position];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
          try {
            return (Fragment) fragmentClasses[position].newInstance();
          } catch (final Exception e) {
            Log.e(TAG, "getItem: ", e);
            throw new IllegalStateException(e);
          }
        }

        @Override
        public int getCount() {
          return fragmentClasses.length;
        }
      };
      viewPager.setAdapter(adapter);

      while (activity == null)
        activity = getActivity();
      activity.runOnUiThread(() -> tabLayout.setupWithViewPager(viewPager));
    });
  }
}
