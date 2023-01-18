package edu.dtd.test110919026;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener {

    EditText edt_account , edt_password;
    Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_account = (EditText) findViewById(R.id.input_account);
        edt_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.button_login);
        btn_register = (Button) findViewById(R.id.button_register);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode) {
            case 1:
                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_login:
                String input_account = edt_account.getText().toString();
                String input_password = edt_password.getText().toString();
                SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);

                if ( !preferences.getAll().isEmpty()
                        && input_account.equals(preferences.getString("user_account", ""))
                        && input_password.equals(preferences.getString("user_password", "")) ) {
                    Toast.makeText(MainActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Activity3.class));
                } else {
                    Toast.makeText(MainActivity.this, "帳號或密碼輸入錯誤", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_register:
                startActivity(new Intent(this, Activity2.class));
                break;
        }

    }
}