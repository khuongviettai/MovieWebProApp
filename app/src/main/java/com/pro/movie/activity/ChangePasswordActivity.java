package com.pro.movie.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.movie.R;
import com.pro.movie.model.User;
import com.pro.movie.prefs.DataStoreManager;
import com.pro.movie.utils.StringUtil;

public class ChangePasswordActivity extends BaseActivity {

    private EditText edtOldPassword;
    private EditText edtNewPassword;
    private EditText edtConfirmPassword;
    private Button btnChangePassword;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initUi();
        initListener();
    }

    private void initUi() {
        edtOldPassword = findViewById(R.id.edt_old_password);
        edtNewPassword = findViewById(R.id.edt_new_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnChangePassword = findViewById(R.id.btn_change_password);
        imgBack = findViewById(R.id.img_back);
    }

    private void initListener() {
        imgBack.setOnClickListener(v -> onBackPressed());
        btnChangePassword.setOnClickListener(v -> onClickValidateChangePassword());
    }

    private void onClickValidateChangePassword() {
        String strOldPassword = edtOldPassword.getText().toString().trim();
        String strNewPassword = edtNewPassword.getText().toString().trim();
        String strConfirmPassword = edtConfirmPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strOldPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_old_password_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_new_password_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strConfirmPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_confirm_password_require), Toast.LENGTH_SHORT).show();
        } else if (!DataStoreManager.getUser().getPassword().equals(strOldPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_old_password_invalid), Toast.LENGTH_SHORT).show();
        } else if (!strNewPassword.equals(strConfirmPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_confirm_password_invalid), Toast.LENGTH_SHORT).show();
        } else if (strOldPassword.equals(strNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_new_password_invalid), Toast.LENGTH_SHORT).show();
        } else {
            changePassword(strNewPassword);
        }
    }

    private void changePassword(String newPassword) {
        showProgressDialog(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_change_password_successfully), Toast.LENGTH_SHORT).show();
                        User userLogin = DataStoreManager.getUser();
                        userLogin.setPassword(newPassword);
                        DataStoreManager.setUser(userLogin);
                        edtOldPassword.setText("");
                        edtNewPassword.setText("");
                        edtConfirmPassword.setText("");
                    }
                });
    }
}