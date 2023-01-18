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

public class EditActivity extends AppCompatActivity
        implements View.OnClickListener {

    TextView output;
    EditText input_name, input_money;
    Button btn_dayPick, btn_add , btn_back;
    RadioButton isCost, isIncome;

    String s, dateTime, name;
    Integer money, position, cost, income;

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
        setContentView(R.layout.activity_edit);

        output = (TextView) findViewById(R.id.txv_dayPicker_edit);
        input_name = (EditText) findViewById(R.id.input_detail_edit);
        input_money = (EditText) findViewById(R.id.input_money_edit);

        btn_add = (Button) findViewById(R.id.button_editConfirm);
        btn_back = (Button) findViewById(R.id.button_editCancel);
        btn_dayPick = (Button) findViewById(R.id.button_dayPicker_edit);

        isCost = (RadioButton) findViewById(R.id.radioButton_cost_edit);
        isIncome = (RadioButton) findViewById(R.id.radioButton_income_edit);

        btn_dayPick.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_back.setOnClickListener(this);


        Bundle bundle = this.getIntent().getExtras();
        if (bundle!= null) {
            s = bundle.getString("POSITION");
            position = Integer.parseInt(s);
        }

        db = openOrCreateDatabase(DB_NAME,  Context.MODE_PRIVATE, null);
        cur = db.rawQuery("SELECT * FROM "+ TB_NAME, null);
        cur.moveToPosition(position);

        calendar = Calendar.getInstance();
        dateTime = cur.getString(1);
        output.setText(dateTime);
        input_name.setText(cur.getString(2));

        s = cur.getString(3);
        income = Integer.parseInt(s);
        s = cur.getString(4);
        cost = Integer.parseInt(s);

        if (income>cost) {
            money = income;
            isIncome.setChecked(true);

        } else {
            money = cost;
            isCost.setChecked(true);
        }
        input_money.setText(money.toString());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_dayPicker_edit:
                dlg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        dateTime = year + "/" + month+1 + "/" + day;
                        output.setText(dateTime);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dlg.show();
                break;

            case R.id.button_editCancel:
                finish();
                break;

            case R.id.button_editConfirm:
                name = input_name.getText().toString();
                s = input_money.getText().toString();
                if ( name.isEmpty() ) {
                    Toast.makeText(EditActivity.this, "請輸入備註", Toast.LENGTH_SHORT).show();
                } else if (s.isEmpty()) {
                    Toast.makeText(EditActivity.this, "請輸入金額", Toast.LENGTH_SHORT).show();
                } else {
                    name = input_name.getText().toString();
                    s = input_money.getText().toString();
                    money = Integer.parseInt(s);

                    ContentValues cv=new ContentValues(4);
                    cv.put(FROM[0], dateTime);
                    cv.put(FROM[1], name);
                    if (isIncome.isChecked()) {
                        cv.put(FROM[2], money);
                        cv.put(FROM[3], 0);
                    } else {
                        cv.put(FROM[2], 0);
                        cv.put(FROM[3], money);
                    }

                    db.update(TB_NAME, cv, "_id="+cur.getInt(0), null);

                    Toast.makeText(EditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
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