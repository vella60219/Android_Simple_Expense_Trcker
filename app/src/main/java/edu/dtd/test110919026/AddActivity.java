package edu.dtd.test110919026;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity
    implements View.OnClickListener {

    TextView output;
    EditText input_name, input_money;
    Button btn_dayPick, btn_add , btn_back;
    RadioButton isCost, isIncome;

    String s, dateTime, name;
    int money;

    DatePickerDialog dlg;
    Calendar calendar;


    static final String DB_NAME = "MoneyDB";
    static final String TB_NAME = "moneylist";
    static final String[] FROM = new String[] {"date","name","income","cost"};
    SQLiteDatabase db;
    Cursor cur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        output = (TextView) findViewById(R.id.txv_dayPicker);
        input_name = (EditText) findViewById(R.id.input_detail);
        input_money = (EditText) findViewById(R.id.input_money);

        btn_add = (Button) findViewById(R.id.button_addConfirm);
        btn_back = (Button) findViewById(R.id.button_addCancel);
        btn_dayPick = (Button) findViewById(R.id.button_dayPicker);

        isCost = (RadioButton) findViewById(R.id.radioButton_cost);
        isIncome = (RadioButton) findViewById(R.id.radioButton_income);

        btn_dayPick.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        calendar = Calendar.getInstance();
        dateTime = calendar.get(Calendar.YEAR) + "/" +
                   calendar.get(Calendar.MONTH)+1 + "/" +
                   calendar.get(Calendar.DAY_OF_MONTH);
        output.setText(dateTime);

        db = openOrCreateDatabase(DB_NAME,  Context.MODE_PRIVATE, null);
        cur = db.rawQuery("SELECT * FROM "+ TB_NAME, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_dayPicker:
                dlg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        dateTime = year + "/" + month+1 + "/" + day;
                        output.setText(dateTime);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dlg.show();
                break;

            case R.id.button_addCancel:
                finish();
                break;

            case R.id.button_addConfirm:
                name = input_name.getText().toString();
                s = input_money.getText().toString();
                if ( name.isEmpty() ) {
                    Toast.makeText(AddActivity.this, "請輸入備註", Toast.LENGTH_SHORT).show();
                } else if (s.isEmpty()) {
                    Toast.makeText(AddActivity.this, "請輸入金額", Toast.LENGTH_SHORT).show();
                } else {
                    name = input_name.getText().toString();
                    s = input_money.getText().toString();
                    money = Integer.parseInt(s);
                    if (isIncome.isChecked()) {
                        addData(dateTime, name, money, 0);
                    } else {
                        addData(dateTime, name, 0, money);
                    }
                    Toast.makeText(AddActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
                    Intent rIntent = new Intent();
                    Bundle rbundle = new Bundle();
                    rbundle.putString("RESULT", "1");
                    rIntent.putExtras(rbundle);
                    setResult(RESULT_OK, rIntent);
                    finish();
                }

                break;
        }
    }

    private void addData(String date, String name, int income, int cost) {
        ContentValues cv=new ContentValues(4);
        cv.put(FROM[0], date);
        cv.put(FROM[1], name);
        cv.put(FROM[2], income);
        cv.put(FROM[3], cost);

        db.insert(TB_NAME, null, cv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_LONG).show();
        db.close();
    }
}