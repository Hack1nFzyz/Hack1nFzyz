package net.fzyz.jerryc05.fzyz_app.core;

public class MainPage {

  public static void test() {
    MyURLRequestBuilder req = new MyURLRequestBuilder(
            "http://www.fzyz.net")
            .buildRequest();

    Thread t = new Thread(req);
    t.start();
  }
}