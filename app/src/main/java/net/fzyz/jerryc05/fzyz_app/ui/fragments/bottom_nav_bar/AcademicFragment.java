package net.fzyz.jerryc05.fzyz_app.ui.fragments.bottom_nav_bar;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.fzyz.jerryc05.fzyz_app.R;

public final class AcademicFragment extends Fragment {

  @Keep
  public AcademicFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_academic, container, false);
  }
}
