package net.fzyz.jerryc05.fzyz_app;

import net.fzyz.jerryc05.fzyz_app.core.URLConnectionBuilder;
import net.fzyz.jerryc05.fzyz_app.core.WebsiteCollection;

import org.junit.Test;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

  @Test
  public void addition_isCorrect() {
    System.out.println(new String(Base64.getDecoder().decode(
            WebsiteCollection.URL_STUDENT_LOGIN)));
  }
}