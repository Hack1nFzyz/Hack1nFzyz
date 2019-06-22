package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.button.MaterialButton;

import net.fzyz.jerryc05.fzyz_app.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

  private static final String TAG = ProfileFragment.class.getName();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_profile, container, false);
    FragmentActivity activity = Objects.requireNonNull(getActivity());

    CircleImageView imageView =
            view.findViewById(R.id.frag_profile_login_avatar);

    imageView.setOnClickListener(view_ -> new Thread(() -> {
      int borderWidth = imageView.getBorderWidth();

      for (int i = 0; i < 6; i++) {
        int j = i;

        activity.runOnUiThread(() ->
                imageView.setBorderWidth(j % 2 == 0 ? 0 : borderWidth));
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          Log.e(TAG, "onViewCreated: First sleep 1500. ", e);
        }
      }
    }).start());


    MaterialButton button_teacher =
            view.findViewById(R.id.frag_profile_teacher_login_button);

    button_teacher.setOnClickListener(view_ -> {
      button_teacher.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey));
      Toast.makeText(activity, "Login as teacher is not supported currently.",
              Toast.LENGTH_LONG).show();
    });


    MaterialButton button_student =
            view.findViewById(R.id.frag_profile_student_login_button);

    button_student.setOnClickListener(view_ -> {
      Toast.makeText(activity, "Okayyyyy, you are logged in.",
              Toast.LENGTH_LONG).show();
    });


    Button button_public =
            view.findViewById(R.id.frag_profile_public_login_button);

    button_public.setOnClickListener(view_ -> {
      Toast.makeText(activity,
              "Login as public is not supported here, and will not be supported in the near future.",
              Toast.LENGTH_LONG).show();
    });


    Button button_register =
            view.findViewById(R.id.frag_profile_register_button);

    button_register.setOnClickListener(view_ -> {
      Toast.makeText(activity,
              "Register is not supported here, and will not be supported in the near future.",
              Toast.LENGTH_LONG).show();
    });

    return view;
  }
}
