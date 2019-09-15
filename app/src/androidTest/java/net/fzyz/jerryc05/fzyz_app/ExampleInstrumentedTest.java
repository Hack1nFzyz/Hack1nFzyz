package net.fzyz.jerryc05.fzyz_app;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

  private static final String TAG = ExampleInstrumentedTest.class.getName();

  @Test
  public void useAppContext() {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    assertEquals("net.fzyz.jerryc05.fzyz_app", appContext.getPackageName());
  }

  @Test
  public void testURLConnection() {
    try {
      assertNotNull(URLConnectionBuilder.get("https://httpbin.org/get")
              .connect()
              .getResult());
    } catch (IOException e) {
      Log.e(TAG, "testURLConnection: ", e);
    }
  }
}
