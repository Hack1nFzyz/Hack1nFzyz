package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import net.fzyz.jerryc05.fzyz_app.R;

import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiFzyz.URL_SCHOOL_AFFAIRS_XiaoWuGongKai;
import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.decodeURL;

public final class FeedSchoolAffairsFragment extends _FeedBaseFragment
        implements OnRefreshListener {

  public static final int STRING_ID = R.string.school_affairs;

  @Override
  String getDecodedURL() {
    return decodeURL(URL_SCHOOL_AFFAIRS_XiaoWuGongKai);
  }
}
