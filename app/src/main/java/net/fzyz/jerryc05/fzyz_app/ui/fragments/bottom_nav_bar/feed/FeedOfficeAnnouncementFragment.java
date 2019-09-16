package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar.feed;

import androidx.annotation.Keep;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import net.fzyz.jerryc05.fzyz_app.R;

import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiFzyz.URL_OFFICE_ANNOUNCEMENT_ChuShiGongGao;
import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.decodeURL;

public final class FeedOfficeAnnouncementFragment extends _FeedBaseFragment
        implements OnRefreshListener {

  public static final int STRING_ID = R.string.office_announcement;

  @Keep
  public FeedOfficeAnnouncementFragment() {
  }

  @Override
  String getDecodedURL() {
    return decodeURL(URL_OFFICE_ANNOUNCEMENT_ChuShiGongGao);
  }
}
