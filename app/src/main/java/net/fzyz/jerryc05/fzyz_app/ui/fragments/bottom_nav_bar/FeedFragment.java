package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;

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
import net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedHeadlineNewsFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedLatestNewsFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedOfficeAnnouncementFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedRollingNewsFragment;
import net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed.FeedSchoolAffairsFragment;

import java.util.Arrays;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public final class FeedFragment extends Fragment {

  private final static String    TAG = "FeedFragment";
  private              TabLayout tabLayout;
  private              ViewPager viewPager;

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
    _BaseActivity.threadPoolExecutor.execute(() -> { //f:off
    if (tabLayout == null)
        tabLayout =  view.findViewById(R.id.frag_feed_tabLayout);
    if (viewPager == null)
        viewPager =  view.findViewById(R.id.frag_feed_viewPager); //f:on

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
      tabLayout.setupWithViewPager(viewPager);
    });
  }
}
