package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;

import net.fzyz.jerryc05.fzyz_app.R;
import net.fzyz.jerryc05.fzyz_app.core.utils.Crypto;
import net.fzyz.jerryc05.fzyz_app.core.utils.ydyg.UserManager;
import net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiYdyg.URL_MEMBER_LOGIN;
import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiYdyg.URL_YDYG_HOST;

public final class ExpenseFragment extends Fragment {

  private static final String TAG = "ExpenseFragment";

  private _BaseActivity activity;

  @Nullable
  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_expense, container, false);
  }

  @WorkerThread
  public void requestLogin(@NonNull final String username, @NonNull final String password) {
    try (final Response response = getActivityOfFragment().getOkHttpClient().newCall(
            new Request.Builder()
                    .url(URL_YDYG_HOST + URL_MEMBER_LOGIN)
                    .post(idPwdOToFormBodyBuilder(username, password).build())
                    .build()
    ).execute()) {
      final String result = Objects.requireNonNull(response.body()).string();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private _BaseActivity getActivityOfFragment() {
    while (activity == null)
      activity = (_BaseActivity) getActivity();
    return activity;
  }

  private FormBody.Builder idPwdOToFormBodyBuilder(@NonNull final String username,
                                                   @NonNull final String password) {
    final FormBody.Builder builder = new FormBody.Builder();
    try {
      final String json = new JSONStringer().object()
              .key("login_id").value(username)
              .key("password").value(byteArrayToMD5(password.getBytes()))
              .key("uuid").value(UserManager.getInstance().getUUID())
              .endObject().toString();
      final String seq = new String(Crypto.getRandomKeyOf16());
      final String req_params = Crypto.encrypt(
              json.getBytes(), seq.getBytes(), Crypto.ALGORITHM_AES);

      builder
              .add("req_params", req_params)
              .add("seq", seq);

    } catch (final JSONException e) {
      Log.e(TAG, "idPwdOToFormBodyBuilder: ", e);
      getActivityOfFragment().runOnUiThread(() -> Toast.makeText(
              activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show());
    }
    return builder;
  }

  private static String byteArrayToMD5(@NonNull final byte[] input) {
    try {
      final MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(input);
      return byteArrayToHex(md5.digest());

    } catch (final NoSuchAlgorithmException e) {
      Log.e(TAG, "MD5: ", e);
      throw new IllegalStateException(e);
    }
  }

  private static String byteArrayToHex(@NonNull final byte[] input) {
    final char[] hexDigits =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    final char[] resultCharArray = new char[(input.length << 1)];

    int index = 0;
    for (final byte b : input) {
      resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
      resultCharArray[index++] = hexDigits[b & 0xf];
    }
    return new String(resultCharArray);
  }
}
