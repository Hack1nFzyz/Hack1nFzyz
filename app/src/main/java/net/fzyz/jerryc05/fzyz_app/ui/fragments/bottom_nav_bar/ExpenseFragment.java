package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import net.fzyz.jerryc05.fzyz_app.R;
import net.fzyz.jerryc05.fzyz_app.core.utils.CryptoUtils;
import net.fzyz.jerryc05.fzyz_app.core.utils.ToastUtils;
import net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiYdyg.URL_MEMBER_LOGIN;
import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiYdyg.URL_YDYG_HOST;
import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.threadPoolExecutor;

public final class ExpenseFragment extends Fragment implements TextView.OnEditorActionListener {

  private static final String TAG = "ExpenseFragment";

  private _BaseActivity  activity;
  private TextView       textView;
  private MaterialButton login;

  @Nullable
  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_expense, container, false);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onViewCreated(@NonNull final View view,
                            @Nullable final Bundle savedInstanceState) {
    threadPoolExecutor.execute(() -> {
      final TextInputEditText username = view.findViewById(R.id.frag_expense_username_text);
      final TextInputEditText password = view.findViewById(R.id.frag_expense_password_text);
      password.setOnEditorActionListener(this);

      login = view.findViewById(R.id.frag_expense_login_button);
      login.setOnClickListener(v -> {
        textView.setText("Logging in ......");
        //noinspection ConstantConditions
        threadPoolExecutor.execute(() -> requestLogin(
                username.getText().toString(), password.getText().toString()));
      });
      textView = view.findViewById(R.id.text_expense);
    });
  }

  @Override
  public boolean onEditorAction(@NonNull final TextView textView,
                                int actionId, @NonNull final KeyEvent keyEvent) {
    if (textView.getId() == R.id.frag_expense_password_text
            && actionId == EditorInfo.IME_ACTION_DONE) {
      login.performClick();
      return true;
    }
    return false;
  }

  @WorkerThread
  private void requestLogin(@NonNull final String username, @NonNull final String password) {
    try (final Response response = getActivityOfFragment().getOkHttpClient().newCall(
            new Request.Builder()
                    .url(URL_YDYG_HOST + URL_MEMBER_LOGIN)
                    .post(idPwdOToFormBodyBuilder(username, password).build())
                    .build()
    ).execute()) {
      final String result = Objects.requireNonNull(response.body()).string();
      getActivityOfFragment().runOnUiThread(() -> textView.setText(result));

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
              .key("uuid").value(getUUID(getActivityOfFragment().getApplicationContext()))
              .endObject().toString();
      final String seq = new String(CryptoUtils.getRandomKeyOf16());
      final String req_params = CryptoUtils.encrypt(
              json.getBytes(), seq.getBytes(), CryptoUtils.ALGORITHM_AES);

      builder
              .add("req_params", req_params)
              .add("seq", seq);

    } catch (final JSONException e) {
      Log.e(TAG, "idPwdOToFormBodyBuilder: ", e);
      ToastUtils.showText(getActivityOfFragment(), e.toString(), Toast.LENGTH_LONG);
    }
    return builder;
  }

  private static String getUUID(@NonNull final Context context) {
    final SharedPreferences sp = context.getSharedPreferences(
            "com.kkzn.ydyg", Context.MODE_PRIVATE);
    String uuid = sp.getString("com.kkzn.ydyg:USER_PREFERENCE_NAME:UUID", "");

    if (TextUtils.isEmpty(uuid))
      uuid = UUID.randomUUID().toString();
    sp.edit().putString("com.kkzn.ydyg:USER_PREFERENCE_NAME:UUID", uuid).apply();

    return uuid;
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
