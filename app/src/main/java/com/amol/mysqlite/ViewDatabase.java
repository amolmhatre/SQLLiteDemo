package com.amol.mysqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.amol.mysqlite.util.DatabaseHelper;

public class ViewDatabase extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";
    private static DatabaseHelper databaseHelper;
    private static TextView tvTableData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);
        databaseHelper = new DatabaseHelper(this);
        tvTableData = (TextView) findViewById(R.id.tvTableData);
        viewTableData();
    }

    public void viewTableData() {
        Cursor cursor = databaseHelper.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(ViewDatabase.this, "Data is empty", Toast.LENGTH_SHORT).show();
//            showMessage("Error", "No data found");
            tvTableData.setText("No data found");

            return;
        } else {
            String items = databaseHelper.getNumberOfItems();
            Toast.makeText(ViewDatabase.this, items + " items in the cart", Toast.LENGTH_SHORT).show();
            StringBuffer stringBuffer = new StringBuffer();
            while (cursor.moveToNext()) {
                /**cursor is just pointer to read table*/
                /**cursor.getString(0) denotes zeroth column*/
//                stringBuffer.append("\nVendor Id: "+cursor.getString(0)+"\n");
                stringBuffer.append("\nVendor ID: " + cursor.getString(0));
                stringBuffer.append("\n" + cursor.getString(1) + " : ");
                stringBuffer.append(cursor.getString(2) + "\n");
                stringBuffer.append("Item: " + cursor.getString(3) + " x ");
                stringBuffer.append("₹" + cursor.getString(4) + " = ");
                stringBuffer.append("₹" + cursor.getString(5) + "\n");
            }//end of while
            tvTableData.setText(stringBuffer);
//            showMessage("Data",stringBuffer.toString() );
        }//end of if else
    }//viewTableData

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}//ViewDatabase class