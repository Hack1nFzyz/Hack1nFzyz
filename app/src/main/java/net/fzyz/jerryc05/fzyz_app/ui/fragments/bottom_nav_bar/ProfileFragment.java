package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import net.fzyz.jerryc05.fzyz_app.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

  private static final String TAG = ProfileFragment.class.getName();

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
      view.findViewById(R.id.frag_profile_login_avatar).setOnClickListener(this);
      view.findViewById(R.id.frag_profile_teacher_login_button).setOnClickListener(this);
      view.findViewById(R.id.frag_profile_student_login_button).setOnClickListener(this);
      view.findViewById(R.id.frag_profile_public_login_button).setOnClickListener(this);
      view.findViewById(R.id.frag_profile_register_button).setOnClickListener(this);
    }).start();
  }

  @Override
  public void onClick(View view) {
    FragmentActivity activity = Objects.requireNonNull(getActivity());

    switch (view.getId()) {
      case R.id.frag_profile_login_avatar: {
        new Thread(() -> {
          CircleImageView imageView =
                  view.findViewById(R.id.frag_profile_login_avatar);
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
        view.findViewById(R.id.frag_profile_teacher_login_button)
                .setBackgroundColor(ContextCompat.getColor(activity, R.color.grey));
        Snackbar.make(view, "Pending feature: Login as teacher.",
                Snackbar.LENGTH_LONG).show();
        break;
      }

      case R.id.frag_profile_student_login_button: {
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
  }
}
