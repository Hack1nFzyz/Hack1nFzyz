package net.fzyz.jerryc05.fzyz_app.core;

@SuppressWarnings({"SpellCheckingInspection", "unused", "WeakerAccess"})
public class Websites {

  private static final String
          URL_GET_NEWS_PREFIX      = "L2Rvb25lL2hvbWUvcG9ydGFsL2dldE5ld3Muc2h0bWw/",
          URL_SCHOOL_NEWS_PREFIX_1 = URL_GET_NEWS_PREFIX
                  + "bmV3c19sZXZlbHRhZz0wJmlzX291dHNpZGU9MSZuZXdudW1zPTEwJnR5cGVzPT",
          URL_SCHOOL_NEWS_PREFIX_2 = URL_SCHOOL_NEWS_PREFIX_1.replace(
                  "Ew", "Ex");

  public static final String
          URL_BASE                              = "aHR0cDovL3d3dy5menl6Lm5ldA==",
          URL_ROLLING_NEWS_GunDongXinWen        = "L25ld3MvbmV3c21nci9nZXRNb3ZlSW1nLnNodG1s",
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
                  URL_CAMPUS_PICTURE_XiaoYuanChuanZhen.replace("Mw", "NA");
}
