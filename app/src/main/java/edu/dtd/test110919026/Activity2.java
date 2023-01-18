package edu.dtd.test110919026;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity2 extends AppCompatActivity
        implements View.OnClickListener {

    EditText edt_createAccount, edt_createPassword, edt_checkPassword;
    Button btn_backToLogin, btn_createaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_createAccount = (EditText) findViewById(R.id.input_createAccount);
        edt_createPassword = (EditText) findViewById(R.id.input_createPassword);
        edt_checkPassword = (EditText) findViewById(R.id.input_checkPassword);
        btn_backToLogin = (Button) findViewById(R.id.button_backToLogin);
        btn_createaccount = (Button) findViewById(R.id.button_creatAccount);

        btn_backToLogin.setOnClickListener(this);
        btn_createaccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_creatAccount:
                String input_account = edt_createAccount.getText().toString();
                String input_password = edt_createPassword.getText().toString();
                String input_check = edt_checkPassword.getText().toString();
                SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);

                if ( input_account.isEmpty() ) {
                    Toast.makeText(Activity2.this, "請輸入帳號", Toast.LENGTH_SHORT).show();
                } else if (input_password.isEmpty()) {
                    Toast.makeText(Activity2.this, "請輸入密碼", Toast.LENGTH_SHORT).show();
                } else if (input_check.isEmpty()) {
                    Toast.makeText(Activity2.this, "請確認密碼", Toast.LENGTH_SHORT).show();
                } else if (!input_check.equals(input_password)) {
                    Toast.makeText(Activity2.this, input_check+"密碼輸入不一致"+input_password, Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_account", input_account);
                    editor.putString("user_password", input_password);
                    editor.apply();
                    Toast.makeText(Activity2.this, "註冊成功！請重新登入。", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                }
                break;

            case R.id.button_backToLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }
}