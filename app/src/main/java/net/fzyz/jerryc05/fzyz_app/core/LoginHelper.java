package net.fzyz.jerryc05.fzyz_app.core;


import android.util.Log;

import androidx.annotation.WorkerThread;

import java.io.IOException;

/**
 * A wrapper class for logging to fzyz internal.
 *
 * @author \
 * \           d88b d88888b d8888b. d8888b. db    db  .o88b.  .d88b.    ooooo
 * \           `8P' 88'     88  `8D 88  `8D `8b  d8' d8P  Y8 .8P  88.  8P~~~~
 * \            88  88ooooo 88oobY' 88oobY'  `8bd8'  8P      88  d'88 dP
 * \            88  88~~~~~ 88`8b   88`8b      88    8b      88 d' 88 V8888b.
 * \        db. 88  88.     88 `88. 88 `88.    88    Y8b  d8 `88  d8'     `8D
 * \        Y8888P  Y88888P 88   YD 88   YD    YP     `Y88P'  `Y88P'  88oobY'
 * @see net.fzyz.jerryc05.fzyz_app.core.URLConnectionBuilder
 */
@WorkerThread
public class LoginHelper {

  private static final String TAG = LoginHelper.class.getName();

  private LoginHelper() {
    throw new UnsupportedOperationException("Cannot create new instance decodeURL "
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
//    loginHelper(username, password, WebsiteCollection.decodeURL(
//            WebsiteCollection.URL_TEACHER_LOGIN));
//  }

//  public static void loginAsPublic(String username, String password) {
//    loginHelper(username, password, WebsiteCollection.decodeURL(
//            WebsiteCollection.URL_PUBLIC_LOGIN));
//  }

//  /**
//   * Idk why this shit exists, but I found it inside the js code.
//   */
//  public static void loginAsOther(String username, String password) {
//    loginHelper(username, password, WebsiteCollection.decodeURL(
//            WebsiteCollection.URL_OTHER_LOGIN));
//  }

  public static void loginAsStudent(String username, String password) {
    loginHelper(username, password, URLConnectionBuilder.decodeURL(
            WebsiteCollection.URL_STUDENT_LOGIN));
  }
}
// todo add greyed out register/public_login button.