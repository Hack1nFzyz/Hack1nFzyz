package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
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
import net.fzyz.jerryc05.fzyz_app.core.utils.ydyg.AccountRecordPOJO;
import net.fzyz.jerryc05.fzyz_app.core.utils.ydyg.MemberInfoPOJO;
import net.fzyz.jerryc05.fzyz_app.core.utils.ydyg.MemberLoginPOJO;
import net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiYdyg.URL_MEMBER_ACCOUNT_RECORD;
import static net.fzyz.jerryc05.fzyz_app.core.apis.ApiYdyg.URL_MEMBER_INFO;
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
        threadPoolExecutor.execute(() -> fetchMemberLogin(
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

  private _BaseActivity getActivityOfFragment() {
    while (activity == null)
      activity = (_BaseActivity) getActivity();
    return activity;
  }

  @WorkerThread
  private void fetchMemberLogin(@NonNull final String username,
                                @NonNull final String password) {
    try (final Response response = getActivityOfFragment().getOkHttpClient().newCall(
            new Request.Builder()
                    .url(URL_YDYG_HOST + URL_MEMBER_LOGIN)
                    .post(idPwdOToFormBodyBuilder(username, password).build())
                    .build()).execute()) {
      final String result = Objects.requireNonNull(response.body(),
              "fetchMemberLogin: response.body() is null!").string();
      Log.d(TAG, "fetchMemberLogin: " + result);

      final MemberLoginPOJO memberLoginPOJO = MemberLoginPOJO.parseJson(result);
      if (!memberLoginPOJO.flag.equals("success")) {
        getActivityOfFragment().runOnUiThread(() -> textView.setText(memberLoginPOJO.rspDsc));
        return;
      }

      final String toShow = "Name: " + memberLoginPOJO.rspData.memName +
              "\nClass: " + memberLoginPOJO.rspData.company.dptName;
      getActivityOfFragment().runOnUiThread(() -> textView.setText(toShow));

      fetchMemberInfo(memberLoginPOJO.rspData.memberId,
              memberLoginPOJO.rspData.accessToken, 0, 10);

    } catch (final IOException e) {
      Log.e(TAG, "fetchMemberLogin: ", e);
      getActivityOfFragment().runOnUiThread(() -> textView.setText(e.toString()));
    }
  }

  @WorkerThread
  private void fetchMemberInfo(@NonNull final String recId,
                               @NonNull final String accessToken,
                               final int start,
                               final int limit) {
    try (final Response response = getActivityOfFragment().getOkHttpClient().newCall(
            new Request.Builder()
                    .url(URL_YDYG_HOST + URL_MEMBER_INFO)
                    .post(new FormBody.Builder()
                            .add("req_params", "{\"rec_id\":\"" + recId + "\"}")
                            .add("access_token", accessToken)
                            .build())
                    .build()).execute()) {
      final String result = Objects.requireNonNull(response.body(),
              "fetchMemberInfo: response.body() is null!").string();
      Log.d(TAG, "fetchMemberInfo: " + result);

      final MemberInfoPOJO memberInfoPOJO = MemberInfoPOJO.parseJson(result);
      if (!memberInfoPOJO.flag.equals("success")) {
        getActivityOfFragment().runOnUiThread(() -> textView.setText(memberInfoPOJO.rspDsc));
        return;
      }

      final String toShow = "\nBalance: RMB " + memberInfoPOJO.rspData.cashAccount + '\n';
      getActivityOfFragment().runOnUiThread(() -> textView.append(toShow));

      fetchAccountRecord(recId, "2019-08-22",
              Integer.toString(start), Integer.toString(limit),
              new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
                      .format(new Date()), accessToken);

    } catch (IOException e) {
      Log.e(TAG, "fetchMemberInfo: ", e);
      getActivityOfFragment().runOnUiThread(() -> textView.setText(e.toString()));
    }
  }

  @WorkerThread
  private void fetchAccountRecord(@NonNull final String memberId,
                                  @NonNull final String startTime,
                                  @NonNull final String start,
                                  @NonNull final String limit,
                                  @NonNull final String endTime,
                                  @NonNull final String accessToken) {
    try (final Response response = getActivityOfFragment().getOkHttpClient().newCall(
            new Request.Builder()
                    .url(URL_YDYG_HOST + URL_MEMBER_ACCOUNT_RECORD)
                    .post(new FormBody.Builder()
                            .add("req_params",
                                    "{\"member_id\":\"" + memberId
                                            + "\",\"starttime\":\"" + startTime
                                            + "\",\"start\":\"" + start
                                            + "\",\"limit\":\"" + limit
                                            + "\",\"endtime\":\"" + endTime + "\"}")
                            .add("access_token", accessToken)
                            .build())
                    .build()).execute()) {
      final String result = Objects.requireNonNull(response.body(),
              "fetchAccountRecord: response.body() is null!").string();
      Log.d(TAG, "fetchAccountRecord: " + result);

      final AccountRecordPOJO accountRecordPOJO = AccountRecordPOJO.parseJson(result);

      final StringBuilder stringBuilder = new StringBuilder(16);
      for (AccountRecordPOJO.RspDatum data : accountRecordPOJO.rspData)
        stringBuilder
                .append("\nRMB ").append(data.amount)
                .append(" as ").append(data.recStatusCh)
                .append("\non ").append(data.recUpdatetime)
                .append(data.detail != null ? "\n@ " + data.detail.shopName : "")
                .append('\n');

      getActivityOfFragment().runOnUiThread(() -> textView.append(stringBuilder.toString()));

//      if (!accountRecordPOJO.flag.equals("success")) return;

    } catch (IOException e) {
      Log.e(TAG, "fetchAccountRecord: ", e);
      getActivityOfFragment().runOnUiThread(() -> textView.setText(e.toString()));
    }
  }

  private FormBody.Builder idPwdOToFormBodyBuilder(@NonNull final String username,
                                                   @NonNull final String password) {
    final FormBody.Builder builder = new FormBody.Builder();
    try {
      final String json = new JSONStringer().object()
              .key("login_id").value(username)
              .key("password").value(byteArrayToHexString(CryptoUtils.digest(
                      password.getBytes(), CryptoUtils.ALGORITHM_MD5_DIGEST)))
              .key("uuid").value(getUUID(getActivityOfFragment().getApplicationContext()))
              .endObject().toString();
      final String seq = new String(CryptoUtils.generateRandomKeyFast(16));
      final String req_params = new String(Base64.encode(CryptoUtils.encrypt(
              json.getBytes(), seq.getBytes(), CryptoUtils.ALGORITHM_AES_ECB_PKCS5PADDING), Base64.NO_WRAP));

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
    String uuid = sp.getString("com.kkzn.ydyg:USER_PREFERENCE_NAME:UUID", null);

    if (TextUtils.isEmpty(uuid))
      uuid = UUID.randomUUID().toString();

    sp.edit().putString("com.kkzn.ydyg:USER_PREFERENCE_NAME:UUID", uuid).apply();
    return uuid;
  }

  private static String byteArrayToHexString(@NonNull final byte[] input) {
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
