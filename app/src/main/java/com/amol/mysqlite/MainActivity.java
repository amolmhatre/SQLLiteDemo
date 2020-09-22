package com.amol.mysqlite;

/**
 * Created by amolmhatre on 9/22/20
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amol.mysqlite.util.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static DatabaseHelper databaseHelper;
    private static EditText etVendor_id, etProduct_id, etProduct_Name, etQuantity, etPrice, etTotal;
    private static Button btnInsert, btnUpdate, btnViewdata, btnDelete, btnClearTable;
    private static String vendor_id = "0", quantity, price, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        vendor_id = databaseHelper.getVendorId();

        etVendor_id = (EditText) findViewById(R.id.etVendor_id);
        etProduct_id = (EditText) findViewById(R.id.etProduct_id);
        etProduct_Name = (EditText) findViewById(R.id.etProduct_Name);
        etQuantity = (EditText) findViewById(R.id.etQuantity);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etTotal = (EditText) findViewById(R.id.etTotal);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnViewdata = (Button) findViewById(R.id.btnViewdata);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnClearTable = (Button) findViewById(R.id.btnClearTable);


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vendor_id_temp = etVendor_id.getText().toString();
                Log.d(TAG, "vendor_id" + vendor_id + "," + vendor_id_temp);
                if (vendor_id.equals(vendor_id_temp) || vendor_id == "0") {
                    boolean isInsterted = insertData();
                    if (isInsterted) {
                        Toast.makeText(MainActivity.this, "Data is Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    switchVendorAlert(vendor_id, vendor_id_temp);
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdated = updateData();
                if (isUpdated) {
                    Toast.makeText(MainActivity.this, "Data is updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDeleted = deleteData();
                if (isDeleted) {
                    Toast.makeText(MainActivity.this, "Data is deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClearTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCleared = clearTable();
                if (isCleared) {
                    Toast.makeText(MainActivity.this, "Table is deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Table delete failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnViewdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewDatabase.class));
            }
        });
    }

    public boolean insertData() {
        return databaseHelper.insertData(
                etVendor_id.getText().toString(),
                etProduct_id.getText().toString(),
                etProduct_Name.getText().toString(),
                etQuantity.getText().toString(),
                etPrice.getText().toString(),
                etTotal.getText().toString());
    }

    public boolean updateData() {
        return databaseHelper.updateData(
                etVendor_id.getText().toString(),
                etProduct_id.getText().toString(),
                etProduct_Name.getText().toString(),
                etQuantity.getText().toString(),
                etPrice.getText().toString(),
                etTotal.getText().toString());
    }

    public boolean deleteData() {
        return databaseHelper.deleteData(etProduct_id.getText().toString());
    }

    public boolean clearTable() {
        return databaseHelper.deleteTable(etVendor_id.getText().toString());
    }

    public void switchVendorAlert(final String Vendor1, final String Vendor2) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.bombill);
        alertDialog.setCancelable(true);
        alertDialog.setTitle("Do you want to empty the cart?");
        alertDialog.setMessage("Your cart contain products from vendor#" + Vendor1 +
                ". Do you want to discard the selection and add products from vendor#" + Vendor2 + "?");
        alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHelper.deleteTable(Vendor1);
                dialogInterface.dismiss();
                vendor_id = Vendor2;
            }
        });
        alertDialog.show();
    }
}
