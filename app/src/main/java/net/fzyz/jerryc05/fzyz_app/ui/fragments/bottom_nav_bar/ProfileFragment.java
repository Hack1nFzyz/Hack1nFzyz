package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.fzyz.jerryc05.fzyz_app.R;
import net.fzyz.jerryc05.fzyz_app.ui.activities.MainActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static net.fzyz.jerryc05.fzyz_app.ui.activities._BaseActivity.threadPoolExecutor;

public final class ProfileFragment extends Fragment implements
        View.OnClickListener, TextView.OnEditorActionListener {

  static final String TAG = ProfileFragment.class.getName();

  public static boolean isLoggedIn;
  MainActivity              activity;
  CircleImageView           avatar;
  TextInputLayout           usernameLayout;
  TextInputEditText         usernameText;
  TextInputLayout           passwordLayout;
  TextInputEditText         passwordText;
  MaterialButton            teacherLogin;
  MaterialButton            studentLogin;
  MaterialButton            publicLogin;
  MaterialButton            register;
  ContentLoadingProgressBar progressBar;
  TextView                  loggingIn;

  @Nullable
  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_profile, container, false);
  }

  @Override
  public void onViewCreated(@NonNull final View view,
                            @Nullable final Bundle savedInstanceState) {
    threadPoolExecutor.execute(() -> {
      //f:off
      if (avatar          == null)
          avatar          = view.findViewById(R.id.frag_profile_avatar);
      if (usernameLayout  == null)
          usernameLayout  = view.findViewById(R.id.frag_profile_username_textLayout);
      if (usernameText    == null)
          usernameText    = view.findViewById(R.id.frag_profile_username_text);
      if (passwordLayout  == null)
          passwordLayout  = view.findViewById(R.id.frag_profile_password_textLayout);
      if (passwordText    == null)
          passwordText    = view.findViewById(R.id.frag_profile_password_text);
      if (teacherLogin    == null)
          teacherLogin    = view.findViewById(R.id.frag_profile_teacher_login_button);
      if (studentLogin    == null)
          studentLogin    = view.findViewById(R.id.frag_profile_student_login_button);
      if ( publicLogin    == null)
           publicLogin    = view.findViewById(R.id.frag_profile_public_login_button);
      if (register        == null)
          register        = view.findViewById(R.id.frag_profile_register_button);
      if (progressBar     == null)
          progressBar     = view.findViewById(R.id.frag_profile_progressBar);
      if (loggingIn       == null)
          loggingIn       = view.findViewById(R.id.frag_profile_logging_in_textView);
      avatar      .setOnClickListener(ProfileFragment.this);
      teacherLogin.setOnClickListener(ProfileFragment.this);
      studentLogin.setOnClickListener(ProfileFragment.this);
       publicLogin.setOnClickListener(ProfileFragment.this);
      register    .setOnClickListener(ProfileFragment.this);
      passwordText.setOnEditorActionListener(ProfileFragment.this);
      //f:on
      while (activity == null)
        activity = (MainActivity) getActivity();
    });
  }

  @WorkerThread
  @Override
  public void onClick(@NonNull final View view) {
    threadPoolExecutor.execute(() -> {

      switch (view.getId()) {
        case R.id.frag_profile_teacher_login_button: {
          activity.runOnUiThread(() -> teacherLogin.setBackgroundColor(
                  ContextCompat.getColor(activity, R.color.grey)));
          Snackbar.make(view, "Pending feature: Login as teacher.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }

        case R.id.frag_profile_student_login_button: {
          activity.runOnUiThread(() -> { //f:off
            usernameLayout.setVisibility(View.INVISIBLE);
            passwordLayout.setVisibility(View.INVISIBLE);
            teacherLogin  .setVisibility(View.INVISIBLE);
            studentLogin  .setVisibility(View.INVISIBLE);
             publicLogin  .setVisibility(View.INVISIBLE);
            register      .setVisibility(View.INVISIBLE);
            progressBar   .setVisibility(View.  VISIBLE);
            loggingIn     .setVisibility(View.  VISIBLE); //f:on
            avatar.startAnimation(AnimationUtils.loadAnimation(
                    activity, R.anim.translate_down_100p_bounce));
          });
          loginSucceed(view);
          break;
        }

        case R.id.frag_profile_public_login_button: {
          activity.runOnUiThread(() -> publicLogin.setTextColor(
                  ContextCompat.getColor(activity, R.color.grey)));
          Snackbar.make(view, "Feature not supported: Login as public.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }

        case R.id.frag_profile_register_button: {
          activity.runOnUiThread(() -> register.setTextColor(
                  ContextCompat.getColor(activity, R.color.grey)));
          Snackbar.make(view, "Feature not supported: Register.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }

        default:
      }
    });
  }

  @Override
  public boolean onEditorAction(@NonNull final TextView textView,
                                int actionId, @NonNull final KeyEvent keyEvent) {
    if (textView.getId() == R.id.frag_profile_password_text
            && actionId == EditorInfo.IME_ACTION_DONE) {
      studentLogin.performClick();
      return true;
    }
    return false;
  }

  void loginSucceed(@NonNull final View view) {
    try {
      Thread.sleep(500);
    } catch (final InterruptedException e) {
      Log.e(TAG, "onClick: Sleeping 2500ms on frag_profile_student_login_button.", e);
    }
    Snackbar.make(view, "Okayyyyy, you are logged in.",
            Snackbar.LENGTH_LONG).show();

    final String username = "Hello " +
            (usernameText.getText() == null
                    || usernameText.getText().toString().isEmpty()
                    ? "[Unnamed]" : usernameText.getText().toString()) + "!";
    activity.runOnUiThread(() -> { //f:off
      avatar     .setVisibility(View.INVISIBLE);
      progressBar.setVisibility(View.INVISIBLE);
      loggingIn  .setVisibility(View.INVISIBLE);
      activity   .setFragment(ProfileLoggedInFragment.class); //f:on

      ((TextView) activity.findViewById(R.id.nav_header_username))
              .setText(username);
    });
    ProfileFragment.isLoggedIn = true;
  }
}
