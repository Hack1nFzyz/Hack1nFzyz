package net.fzyz.jerryc05.fzyz_app.core;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import net.fzyz.jerryc05.fzyz_app.R;

public class MainPage {
  private static final String TAG = MainPage.class.getName();

  public static void test(Activity activity) {
    new Thread(() -> {
      final String url =
              Websites.of(Websites.URL_EDUCATION_NEWS_JiaoYuDongTai);
      try (URLConnectionBuilder req = URLConnectionBuilder.get(url)) {
        String result = req.connect().getResult("gbk");

        activity.runOnUiThread(() -> {
          Snackbar.make(activity.findViewById(R.id.container_main_activity),
                  url, Snackbar.LENGTH_LONG).show();
          ((TextView) activity.findViewById(R.id.text_home))
                  .setText(result);
        });
      } catch (Exception e) {
        Log.e(TAG, "test: ", e);
      }
    }).start();
  }
}

/*
<form action="/sys/login.shtml" method="post" name="form1" id="form1">
    <span>
        用户名：<input name="staffCode" size="15" type="text"/>
        密码：<input size="15" name="password" type="password"/>
        身份：
        <select id="loginRole" name="loginRole">
            <option selected="selected" value="1">教师</option>
            <option value="2">学生</option>
            <option value="3">社会人士</option>
        </select>
        <input type="checkbox" name="cookietype"/>
        免登录<input type="submit" value="登 录"/>
        <input type="button" onclick="javascript:myregist();" value="注 册"/>
    </span>
</form>
 */

/*
//进入我的主页
function online(){
  var loginRole = '';
  var url ='';
  if(loginRole == '1'){
     url = '/sys/main.shtml';
  }else if(loginRole == '2'){
     url = '/doone-edu/student/stuinfo/loadStuInfoAction.shtml';
  }else if(loginRole == '3'){
     url = '/doone-edu/family/familyinfo/comfindFamilyInfoAction.shtml';
  }else{
     url = '/home/family/familyInfo_list.jsp';
  }
  location.href= url;
}
 */

// http://www.fzyz.net/school/calendar/getSchoolCalendar.shtml?cal.CALENDAR_DAY=2019-06-15

