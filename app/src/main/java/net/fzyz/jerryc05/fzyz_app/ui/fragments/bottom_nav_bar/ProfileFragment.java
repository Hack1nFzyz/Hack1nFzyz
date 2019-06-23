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
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import net.fzyz.jerryc05.fzyz_app.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

  private static final String TAG = ProfileFragment.class.getName();

  private FragmentActivity          activity;
  private CircleImageView           imageView;
  private TextInputLayout           username;
  private TextInputLayout           password;
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
      if (imageView    == null)
          imageView    = view.findViewById(R.id.frag_profile_login_avatar);
      if (username     == null)
          username     = view.findViewById(R.id.frag_profile_username_textLayout);
      if (password     == null)
          password     = view.findViewById(R.id.frag_profile_password_textLayout);
      if (teacherLogin == null)
          teacherLogin = view.findViewById(R.id.frag_profile_teacher_login_button);
      if (studentLogin == null)
          studentLogin = view.findViewById(R.id.frag_profile_student_login_button);
      if ( publicLogin == null)
           publicLogin = view.findViewById(R.id.frag_profile_public_login_button);
      if (register     == null)
          register     = view.findViewById(R.id.frag_profile_register_button);
      if (progressBar  == null)
          progressBar  = view.findViewById(R.id.frag_profile_progressBar);
      if (loggingIn    == null)
          loggingIn    = view.findViewById(R.id.frag_profile_logging_in_textView);
      while (activity  == null)
        activity       = getActivity();
      //f:on

      activity.runOnUiThread(() -> {
        //f:off
        imageView   .setOnClickListener(this);
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
        case R.id.frag_profile_login_avatar: {
          new Thread(() -> {
            int borderWidth = imageView.getBorderWidth();

            for (int i = 0; i < 6; i++) {
              imageView.setBorderWidth(i % 2 == 0 ? 0 : borderWidth);
              try {
                Thread.sleep(500);
              } catch (InterruptedException e) {
                Log.e(TAG, "onViewCreated: First sleep 500ms. ", e);
              }
            }
            imageView.setBorderWidth(borderWidth);
          }).start();
          break;
        }

        case R.id.frag_profile_teacher_login_button: {
          teacherLogin.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey));
          Snackbar.make(view, "Pending feature: Login as teacher.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }

        case R.id.frag_profile_student_login_button: {
          activity.runOnUiThread(() -> {
            //f:off
            username    .setVisibility(View.INVISIBLE);
            password    .setVisibility(View.INVISIBLE);
            teacherLogin.setVisibility(View.INVISIBLE);
            studentLogin.setVisibility(View.INVISIBLE);
             publicLogin.setVisibility(View.INVISIBLE);
            register    .setVisibility(View.INVISIBLE);
            progressBar .setVisibility(View.  VISIBLE);
            loggingIn   .setVisibility(View.  VISIBLE);
            imageView.startAnimation(AnimationUtils.loadAnimation(
                    activity,R.anim.frag_profile_image_view));
            //f:on
          });
          Snackbar.make(view, "Okayyyyy, you are logged in.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }

        case R.id.frag_profile_public_login_button: {
          ((MaterialButton) view.findViewById(R.id.frag_profile_public_login_button))
                  .setTextColor(ContextCompat.getColor(activity, R.color.grey));
          Snackbar.make(view, "Feature not supported: Login as public.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }

        case R.id.frag_profile_register_button: {
          ((MaterialButton) view.findViewById(R.id.frag_profile_register_button))
                  .setTextColor(ContextCompat.getColor(activity, R.color.grey));
          Snackbar.make(view, "Feature not supported: Register.",
                  Snackbar.LENGTH_LONG).show();
          break;
        }
      }
    }).start();
  }
}
