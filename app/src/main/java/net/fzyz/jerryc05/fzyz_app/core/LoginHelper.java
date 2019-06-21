package net.fzyz.jerryc05.fzyz_app.core;


public class LoginHelper {

  private static void loginHelper(String username, String password,
                                  int loginRole, String loginURL) {
  }

  public static void loginAsTeacher(String username, String password) {
    loginHelper(username, password, 1,WebsiteCollection.of(
            WebsiteCollection.URL_TEACHER_LOGIN));
  }

  public static void loginAsStudent(String username, String password) {
    loginHelper(username, password, 2,WebsiteCollection.of(
            WebsiteCollection.URL_STUDENT_LOGIN));
  }

  public static void loginAsPublic(String username, String password) {
    loginHelper(username, password, 3,WebsiteCollection.of(
            WebsiteCollection.URL_PUBLIC_LOGIN));
  }

//  /**
//   * Idk why this shit exists, but I found it inside the js code.
//   */
//  public static void loginAsOther(String username, String password) {
//  //                                ↓ Any value except 1~3
//    loginHelper(username, password, 0, WebsiteCollection.of(
//            WebsiteCollection.URL_OTHER_LOGIN));
//  }

  private LoginHelper() {
  }
}
//todo add greyed out register/public_login button.