package com.pro.movie.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.movie.R;
import com.pro.movie.model.User;
import com.pro.movie.prefs.DataStoreManager;
import com.pro.movie.utils.GlobalFuntion;
import com.pro.movie.utils.StringUtil;

public class SignInActivity extends BaseActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnSignIn;
    private TextView tvForgotPassword;
    private LinearLayout layoutSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUi();
        initListener();
    }

    private void initUi() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        layoutSignUp = findViewById(R.id.layout_sign_up);
    }

    private void initListener() {
        layoutSignUp.setOnClickListener(
                v -> GlobalFuntion.startActivity(SignInActivity.this, SignUpActivity.class));

        btnSignIn.setOnClickListener(v -> onClickValidateSignIn());
        tvForgotPassword.setOnClickListener(v -> onClickForgotPassword());
    }

    private void onClickForgotPassword() {
        GlobalFuntion.startActivity(this, ForgotPasswordActivity.class);
    }

    private void onClickValidateSignIn() {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            signInUser(strEmail, strPassword);
        }
    }

    private void signInUser(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            DataStoreManager.setUser(userObject);
                            GlobalFuntion.startActivity(SignInActivity.this, MainActivity.class);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, getString(R.string.msg_sign_in_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}