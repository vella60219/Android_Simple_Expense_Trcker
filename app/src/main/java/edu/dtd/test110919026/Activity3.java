package edu.dtd.test110919026;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Activity3 extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener {

    static final String DB_NAME = "MoneyDB";
    static final String TB_NAME = "moneylist";
    static final String[] FROM = new String[] {"date","name","income","cost"};
//    static final String ORDER = "ORDER BY column_1 DESC";
    SQLiteDatabase db;
    Cursor cur;
    SimpleCursorAdapter adapter;

    TextView output_money, output_cost, output_income;
    String s = "";

    Integer money, cost, income, position;
    Button btn_add;
    ListView lv;

    AlertDialog.Builder builder;
    String[] options = {"編輯", "刪除" };
    String alertMessage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        btn_add = (Button) findViewById(R.id.button_add);
        lv = (ListView)findViewById(R.id.lv_data);
        output_money = (TextView) findViewById(R.id.txv_countMoney);
        output_income = (TextView) findViewById(R.id.txv_countIncome);
        output_cost = (TextView) findViewById(R.id.txv_countCost);

        btn_add.setOnClickListener(this);

        builder = new AlertDialog.Builder(this);

        dbInit();
        dbListInit();
        countData();
    }

    private void setAlertDialog(){
        builder .setTitle(alertMessage)
                .setItems(options, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        editPack();
                        break;
                    case 1:
                        db.delete(TB_NAME, "_id="+cur.getInt(0),null);
                        refresh();
                        break;
                }
            }
        })
                .setNegativeButton("取消", null);
    }

    private void dbInit(){
        db = openOrCreateDatabase(DB_NAME,  Context.MODE_PRIVATE, null);

        String createTable = "CREATE TABLE IF NOT EXISTS " + TB_NAME +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT not null, " +
                "name TEXT not null, " +
                "income INTEGER not null, " +
                "cost INTEGER not null )";
        db.execSQL(createTable);

        cur = db.rawQuery("SELECT * FROM "+ TB_NAME, null);
        if (cur.getCount() == 0){
            addData("2020/12/30","午餐", 0, 100);
            addData("2023/10/31","爸爸給零用錢", 1000, 0);
        }
        cur = db.rawQuery("SELECT * FROM "+ TB_NAME, null);
    }

    private void dbListInit(){
        adapter = new SimpleCursorAdapter(this,
                  R.layout.item, cur,
                  FROM,
                  new int[] {R.id.txv_item_date,R.id.txv_item_detail,R.id.txv_item_income,R.id.txv_item_cost}, 0);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    private void addData(String date, String name, int income, int cost) {
        ContentValues cv=new ContentValues(4);
        cv.put(FROM[0], date);
        cv.put(FROM[1], name);
        cv.put(FROM[2], income);
        cv.put(FROM[3], cost);

        db.insert(TB_NAME, null, cv);
    }

    private void countData() {
        money = cost = income = 0;
        Integer i;
        cur.moveToFirst();
        do {
            i = cur.getColumnIndex(FROM[2]);
            income += cur.getInt(i);
            i = cur.getColumnIndex(FROM[3]);
            cost += cur.getInt(i);
            cur.moveToNext();
        } while (!cur.isLast());
        money = income - cost;
        output_money.setText("$" + money);
        output_income.setText("$" + income);
        output_cost.setText("$" + cost);
        if (money>0) output_money.setTextColor(getResources().getColor(R.color.green));
        else if (money<0) output_money.setTextColor(getResources().getColor(R.color.red));
        else output_money.setTextColor(getResources().getColor(R.color.black));
    }

    private void editPack() {
        Intent intent = new Intent(this, EditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("POSITION", position.toString());
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add:
                startActivityForResult(new Intent(this, AddActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data!= null) {
                        refresh();
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        position = i;
        cur.moveToPosition(position);
        alertMessage = cur.getString(1) + " " + cur.getString(2);
        setAlertDialog();
        builder.show();
    }



    private void refresh(){
        finish();
        startActivity(new Intent(this, Activity3.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_LONG).show();
        db.close();
    }
}