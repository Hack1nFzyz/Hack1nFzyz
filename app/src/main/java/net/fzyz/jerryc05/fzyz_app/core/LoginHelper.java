package net.fzyz.jerryc05.fzyz_app.core;


import android.util.Log;

import androidx.annotation.WorkerThread;

import java.io.IOException;

@WorkerThread
public class LoginHelper {

  private static final String TAG = LoginHelper.class.getName();

  private LoginHelper() {
    throw new UnsupportedOperationException("Cannot create new instance of "
            + LoginHelper.class.getName());
  }

  private static void loginHelper(String username, String password,
                                  String loginURL) {
    try {
      URLConnectionBuilder.post(loginURL).connect().close();
    } catch (IOException e) {
      Log.e(TAG, "loginHelper: ", e);
    }
  }

//  public static void loginAsTeacher(String username, String password) {
//    loginHelper(username, password, WebsiteCollection.of(
//            WebsiteCollection.URL_TEACHER_LOGIN));
//  }

//  public static void loginAsPublic(String username, String password) {
//    loginHelper(username, password, WebsiteCollection.of(
//            WebsiteCollection.URL_PUBLIC_LOGIN));
//  }

//  /**
//   * Idk why this shit exists, but I found it inside the js code.
//   */
//  public static void loginAsOther(String username, String password) {
//    loginHelper(username, password, WebsiteCollection.of(
//            WebsiteCollection.URL_OTHER_LOGIN));
//  }

  public static void loginAsStudent(String username, String password) {
    loginHelper(username, password, WebsiteCollection.of(
            WebsiteCollection.URL_STUDENT_LOGIN));
  }
}
// todo add greyed out register/public_login button.