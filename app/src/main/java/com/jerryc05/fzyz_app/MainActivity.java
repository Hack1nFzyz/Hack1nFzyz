package com.jerryc05.fzyz_app;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

/*
    BottomNavigationView navView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    AppBarConfiguration appBarConfiguration =
            new AppBarConfiguration.Builder(
                    R.id.navigation_home,
                    R.id.navigation_dashboard,
                    R.id.navigation_notifications)
                    .build();
    NavController navController = Navigation.findNavController(
            this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(
            this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navView, navController);*/
  }

}
