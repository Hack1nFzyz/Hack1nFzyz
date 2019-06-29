package net.fzyz.jerryc05.fzyz_app.core;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.WorkerThread;

@SuppressWarnings({"SpellCheckingInspection", "unused", "WeakerAccess"})
@WorkerThread
public class WebsiteCollection {

  private static final String TAG = WebsiteCollection.class.getName();

  private static final String
          URL_GET_NEWS_PREFIX       = "L2Rvb25lL2hvbWUvcG9ydGFsL2dldE5ld3Muc2h0bWw/",
          URL_SCHOOL_NEWS_PREFIX_1  = URL_GET_NEWS_PREFIX
                  + "bmV3c19sZXZlbHRhZz0wJmlzX291dHNpZGU9MSZuZXdudW1zPTEwJnR5cGVzPT",
          URL_SCHOOL_NEWS_PREFIX_2  = URL_GET_NEWS_PREFIX
                  + "bmV3c19sZXZlbHRhZz0wJmlzX291dHNpZGU9MSZuZXdudW1zPTExJnR5cGVzPT",
          URL_INDEX_RES_LIST_PREFIX =
                  "L2Rvb25lL2tub3dsZWRnZS9pbmZvL2luZGV4cmVzbGlzdC5zaHRtbD9rdHlwZWlkPT";

  public static final String
          URL_BASE                              = "aHR0cDovL3d3dy5menl6Lm5ldA==",
          URL_ROLLING_NEWS_GunDongXinWen        =
                  "L25ld3MvbmV3c21nci9nZXRNb3ZlSW1nLnNodG1s",
          URL_OFFICE_ANNOUNCEMENT_ChuShiGongGao = URL_GET_NEWS_PREFIX
                  + "bmV3bnVtcz0zJnR5cGVzPTEsNSw2LDcmaXNfb3V0c2lkZT0x",
          URL_SCHOOL_AFFAIRS_XiaoWuGongKai      = URL_GET_NEWS_PREFIX
                  + "bmV3bnVtcz01JnR5cGVzPTE5MSZpc19vdXRzaWRlPTE=",
          URL_HEADLINE_NEWS_ZuiXinXinWenNeiRong =
                  "L2Rvb25lL2hvbWUvcG9ydGFsL2dldE5ld3NDb250ZW50LnNodG1sP2lzX291"
                          + "dHNpZGU9MSZuZXdzX2xldmVsdGFnPTEmdF9uZXdzX2xldmVsPTI=",
          URL_LATEST_NEWS_ZuiXinXinWen          = URL_GET_NEWS_PREFIX
                  + "aXNfb3V0c2lkZT0xJm5ld251bXM9OCZpc0ZpcnN0PWlzRmlyc3QmbmV3c19sZXZlbHRhZz0x",
          URL_SCHOOL_NEWS_MIXED_XiaoYuanXinWen  = URL_SCHOOL_NEWS_PREFIX_1 + "EyLDEw",
          URL_SCHOOL_NEWS_HIGH_GaoZhongXinWen   = URL_SCHOOL_NEWS_PREFIX_1 + "Ey",
          URL_SCHOOL_NEWS_MIDDLE_ChuZhongXinWen = URL_SCHOOL_NEWS_PREFIX_1 + "Ew",
          URL_EDUCATION_NEWS_JiaoYuDongTai      = URL_SCHOOL_NEWS_PREFIX_2 + "Q=",
          URL_ENTRANCE_EXAMS_ZhongKaoGaoKao     = URL_SCHOOL_NEWS_PREFIX_1 + "M=",
          URL_ENTRANCE_HIGH_GaoKaoDongTai       = URL_SCHOOL_NEWS_PREFIX_1 + "M4",
          URL_ENTRANCE_MIDDLE_ZhongKaoDongTai   = URL_SCHOOL_NEWS_PREFIX_1 + "M5",
          URL_STUDENTS_CHANNEL_XueShengPinDao   = URL_SCHOOL_NEWS_PREFIX_2 + "k=",
          URL_ALUMNI_CHANNEL_XiaoYouPinDao      = URL_SCHOOL_NEWS_PREFIX_2 + "Qw",
          URL_SPECIAL_TOPIC_NEWS_XinWenZhuanTi  =
                  "L2Rvb25lL2hvbWUvcG9ydGFsL2dldFRvcGljcy5zaHRtbD9uZXdudW1zPTU",
          URL_EDUCATION_BLOG_JiaoYuBoKe         =
                  "L2Rvb25lL2hvbWUvcG9ydGFsL2dldEJsb2dzLnNodG1s",
          URL_CAMPUS_PICTURE_XiaoYuanChuanZhen  =
                  "L2Rvb25lL2hvbWUvcG9ydGFsL2dldFRvcFBpY3Muc2h0bWw/cGljX3R5cGU9Mw==",
          URL_YEARS_PASSED_SuiYueLiuYing        =
                  "L2Rvb25lL2hvbWUvcG9ydGFsL2dldFRvcFBpY3Muc2h0bWw/cGljX3R5cGU9NA==",
          URL_PRINCIPAL_FORUM_XiaoZhangLunTan   =
                  "L2Rvb25lLWVkdS9zY2hvb2wvaG9tZS9zY2hvb2xGb3J1bUluZGV4QWN0aW9uLnNodG1s",
          URL_TUO_FENG_ONLINE_TuoFengZaiXian    =
                  URL_INDEX_RES_LIST_PREFIX + "EyNDMmdHlwZV9pZD0x",
          URL_3M_FATE_SanMuYuan                 =
                  URL_INDEX_RES_LIST_PREFIX + "EyNDQmdHlwZV9pZD0y",
          URL_WINDOW_OF_HEART_XinYuZhiChuang    =
                  URL_INDEX_RES_LIST_PREFIX + "IyNjEmdHlwZV9pZD0z",
          URL_CALENDAR_DETAIL                   =
                  "L3NjaG9vbC9jYWxlbmRhci9nZXRTY2hvb2xDYWxlbmRhci5zaHRtbD9jYWwuQ0FMRU5EQVJfREFZPQ==";

  public static final String
          URL_INDEPENDENT_RECRUITMENT_PUBLICITY_ZiZhuZhaoShengGongShi =
          URL_GET_NEWS_PREFIX + "bmV3bnVtcz02JnR5cGVzPTU4MiZpc19vdXRzaWRlPTE=";

  public static final String
          URL_STUDENT_LOGIN = "L2Rvb25lLWVkdS9zdHVkZW50L3N0dWluZm8vbG9hZFN0dUluZm9BY3Rpb24uc2h0bWw=";
//          URL_TEACHER_LOGIN = "L3N5cy9tYWluLnNodG1s",
//          URL_PUBLIC_LOGIN  =
//                  "L2Rvb25lLWVkdS9mYW1pbHkvZmFtaWx5aW5mby9jb21maW5kRmFtaWx5SW5mb0FjdGlvbi5zaHRtbA==";
//          URL_OTHER_LOGIN   = "L2hvbWUvZmFtaWx5L2ZhbWlseUluZm9fbGlzdC5qc3A=";

  /**
   * Return decoded url and appended to base url.
   *
   * @param url Encoded url
   */
  public static String of(String url) {
    String decoded = (url.equals(URL_BASE)
            ? "" : new String(Base64.decode(URL_BASE, Base64.DEFAULT)))
            + new String(Base64.decode(url, Base64.DEFAULT));
    Log.d(TAG, "of: " + decoded);
    return decoded;
  }

  /**
   * Parse data to formatted calendar url.
   *
   * @param date Date of form yyyy-mm-dd
   */
  public static String ofCalendarDate(String date) {
    String decoded = new String(Base64.decode(URL_BASE, Base64.DEFAULT))
            + new String(Base64.decode(URL_CALENDAR_DETAIL, Base64.DEFAULT))
            + date;
    Log.d(TAG, "of: " + decoded);
    return decoded;
  }

  private WebsiteCollection() {
    throw new UnsupportedOperationException("Cannot create new instance of "
            + WebsiteCollection.class.getName());
  }
}
