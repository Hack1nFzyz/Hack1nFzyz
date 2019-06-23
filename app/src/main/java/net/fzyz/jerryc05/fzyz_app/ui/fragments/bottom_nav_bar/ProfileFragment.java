package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class ProfileFragment extends Fragment implements View.OnClickListener {

  private static final String TAG = ProfileFragment.class.getName();

  public static boolean isLoggedIn;

  private MainActivity              activity;
  private CircleImageView           avatar;
  private TextInputLayout           usernameLayout;
  private TextInputEditText         usernameText;
  private TextInputLayout           passwordLayout;
  private TextInputEditText         passwordText;
  private MaterialButton            teacherLogin;
  private MaterialButton            studentLogin;
  private MaterialButton            publicLogin;
  private MaterialButton            register;
  private ContentLoadingProgressBar progressBar;
  private TextView                  loggingIn;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_profile, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    new Thread(() -> {
      //f:off
      if (avatar         == null)
          avatar         = view.findViewById(R.id.frag_profile_avatar);
      if (usernameLayout == null)
          usernameLayout = view.findViewById(R.id.frag_profile_username_textLayout);
      if (usernameText   == null)
          usernameText   = view.findViewById(R.id.Frag_profile_username_text);
      if (passwordLayout == null)
          passwordLayout = view.findViewById(R.id.frag_profile_password_textLayout);
      if (passwordText   == null)
          passwordText   = view.findViewById(R.id.Frag_profile_password_text);
      if (teacherLogin   == null)
          teacherLogin   = view.findViewById(R.id.frag_profile_teacher_login_button);
      if (studentLogin   == null)
          studentLogin   = view.findViewById(R.id.frag_profile_student_login_button);
      if ( publicLogin   == null)
           publicLogin   = view.findViewById(R.id.frag_profile_public_login_button);
      if (register       == null)
          register       = view.findViewById(R.id.frag_profile_register_button);
      if (progressBar    == null)
          progressBar    = view.findViewById(R.id.frag_profile_progressBar);
      if (loggingIn      == null)
          loggingIn      = view.findViewById(R.id.frag_profile_logging_in_textView);
      while (activity    == null)
             activity    = (MainActivity) getActivity();
      //f:on

      activity.runOnUiThread(() -> {
        new AlertDialog.Builder(activity)
                .setTitle("Just a note")
                .setMessage("Login process is still under construction. " +
                        "Typing in real credential makes no sense.")
                .setCancelable(false)
                .setPositiveButton("OKAY", null)
                .create()
                .show();
        //f:off
        avatar   .setOnClickListener(this);
        teacherLogin.setOnClickListener(this);
        studentLogin.setOnClickListener(this);
         publicLogin.setOnClickListener(this);
        register    .setOnClickListener(this);
        //f:on
      });
    }).start();
  }

  @Override
  public void onClick(View view) {
    new Thread(() -> {
      switch (view.getId()) {

        case R.id.frag_profile_avatar: {
          final int borderWidth = avatar.getBorderWidth();

          for (int i = 0; i < 6; i++) {
            final int j = i;
            activity.runOnUiThread(() ->
                    avatar.setBorderWidth(j % 2 == 0 ? 0 : borderWidth));
            try {
              Thread.sleep(500);
            } catch (InterruptedException e) {
              Log.e(TAG, "onClick: Sleeping 500ms on frag_profile_avatar. ", e);
            }
          }
          activity.runOnUiThread(() ->
                  avatar.setBorderWidth(borderWidth));
          break;
        }

        case R.id.frag_profile_teacher_login_button: {
          activity.runOnUiThread(() ->
                  teacherLogin.setBackgroundColor(ContextCompat
                          .getColor(activity, R.color.grey)));
          Snackbar.make(view, "Pending feature: Login as teacher.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }

        case R.id.frag_profile_student_login_button: {
          activity.runOnUiThread(() -> {
            //f:off
            usernameLayout    .setVisibility(View.INVISIBLE);
            passwordLayout    .setVisibility(View.INVISIBLE);
            teacherLogin.setVisibility(View.INVISIBLE);
            studentLogin.setVisibility(View.INVISIBLE);
             publicLogin.setVisibility(View.INVISIBLE);
            register    .setVisibility(View.INVISIBLE);
            progressBar .setVisibility(View.  VISIBLE);
            loggingIn   .setVisibility(View.  VISIBLE);
            avatar      .startAnimation(AnimationUtils.loadAnimation(
                    activity,R.anim.translate_down_100p_bounce));
            //f:on
          });
          loginSucceed(view);
          break;
        }

        case R.id.frag_profile_public_login_button: {
          activity.runOnUiThread(() ->
                  publicLogin.setTextColor(ContextCompat
                          .getColor(activity, R.color.grey)));
          Snackbar.make(view, "Feature not supported: Login as public.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }

        case R.id.frag_profile_register_button: {
          activity.runOnUiThread(() ->
                  register.setTextColor(ContextCompat
                          .getColor(activity, R.color.grey)));
          Snackbar.make(view, "Feature not supported: Register.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }
      }
    }).start();
  }

  private void loginSucceed(View view) {
    try {
      Thread.sleep(2500);
    } catch (InterruptedException e) {
      Log.e(TAG, "onClick: Sleeping 2500ms on frag_profile_student_login_button.", e);
    }
    Snackbar.make(view, "Okayyyyy, you are logged in.",
            Snackbar.LENGTH_LONG).show();

    final String username = "Hello " +
            (usernameText.getText() == null
                    || usernameText.getText().toString().isEmpty()
                    ? "[Unnamed]" : usernameText.getText().toString()) + "!";
    activity.runOnUiThread(() -> {
      //f:off
      avatar     .setVisibility(View.INVISIBLE);
      progressBar.setVisibility(View.INVISIBLE);
      loggingIn  .setVisibility(View.INVISIBLE);
      activity   .setFragment(new ProfileLoggedInFragment());
      //f:on
      ((TextView) activity.findViewById(R.id.nav_header_username))
              .setText(username);
    });
    ProfileFragment.isLoggedIn = true;
  }
}
