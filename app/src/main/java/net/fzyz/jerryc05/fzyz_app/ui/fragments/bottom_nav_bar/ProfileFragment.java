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
import androidx.annotation.WorkerThread;
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

@SuppressWarnings("WeakerAccess")
public class ProfileFragment extends Fragment implements View.OnClickListener {

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
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_profile, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view,
                            @Nullable Bundle savedInstanceState) {
    new Thread(new Runnable() {
      @Override
      public void run() { //f:off
        if (avatar          == null)
            avatar          = view.findViewById(R.id.frag_profile_avatar);
        if (usernameLayout  == null)
            usernameLayout  = view.findViewById(R.id.frag_profile_username_textLayout);
        if (usernameText    == null)
            usernameText    = view.findViewById(R.id.Frag_profile_username_text);
        if (passwordLayout  == null)
            passwordLayout  = view.findViewById(R.id.frag_profile_password_textLayout);
        if (passwordText    == null)
            passwordText    = view.findViewById(R.id.Frag_profile_password_text);
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
        while (activity     == null)
               activity     = (MainActivity) getActivity(); //f:on

        activity.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            new AlertDialog.Builder(activity)
                    .setTitle("Just a note")
                    .setMessage("Login process is still under construction. " +
                            "Typing in real credential makes no sense.")
                    .setCancelable(false)
                    .setPositiveButton("OKAY", null)
                    .create()
                    .show();
            //f:off
            avatar      .setOnClickListener(ProfileFragment.this);
            teacherLogin.setOnClickListener(ProfileFragment.this);
            studentLogin.setOnClickListener(ProfileFragment.this);
             publicLogin.setOnClickListener(ProfileFragment.this);
            register    .setOnClickListener(ProfileFragment.this);
            //f:on
          }
        });
      }
    }).start();
  }

  @WorkerThread
  @Override
  public void onClick(View view) {
    new Thread(new Runnable() {
      @Override
      public void run() {

        switch (view.getId()) {
          case R.id.frag_profile_teacher_login_button: {
            activity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                teacherLogin.setBackgroundColor(ContextCompat
                        .getColor(activity, R.color.grey));
              }
            });
            Snackbar.make(view, "Pending feature: Login as teacher.",
                    Snackbar.LENGTH_LONG).show();
            break;
          }

          case R.id.frag_profile_student_login_button: {
            activity.runOnUiThread(new Runnable() {
              @Override
              public void run() { //f:off
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
              }
            });
            loginSucceed(view);
            break;
          }

          case R.id.frag_profile_public_login_button: {
            activity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                publicLogin.setTextColor(ContextCompat
                        .getColor(activity, R.color.grey));
              }
            });
            Snackbar.make(view, "Feature not supported: Login as public.",
                    Snackbar.LENGTH_LONG).show();
            break;
          }

          case R.id.frag_profile_register_button: {
            activity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                register.setTextColor(ContextCompat
                        .getColor(activity, R.color.grey));
              }
            });
            Snackbar.make(view, "Feature not supported: Register.",
                    Snackbar.LENGTH_LONG).show();
            break;
          }
        }
      }
    }).start();
  }

  void loginSucceed(View view) {
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
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() { //f:off
        avatar     .setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        loggingIn  .setVisibility(View.INVISIBLE);
        activity   .setFragment(new ProfileLoggedInFragment()); //f:on

        ((TextView) activity.findViewById(R.id.nav_header_username))
                .setText(username);
      }
    });
    ProfileFragment.isLoggedIn = true;
  }
}
