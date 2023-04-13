package com.pro.movie.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.pro.movie.R;
import com.pro.movie.prefs.DataStoreManager;
import com.pro.movie.utils.GlobalFuntion;
import com.pro.movie.utils.StringUtil;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(this::goToActivity, 2000);
    }

    private void goToActivity() {
        if (DataStoreManager.getUser() != null && !StringUtil.isEmpty(DataStoreManager.getUser().getEmail())) {
            GlobalFuntion.startActivity(this, MainActivity.class);
        } else {
            GlobalFuntion.startActivity(this, SignInActivity.class);
        }
        finish();
    }
}
