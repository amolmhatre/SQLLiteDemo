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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amol.mysqlite.util.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static DatabaseHelper databaseHelper;
    private static EditText etVendor_id, etProduct_id, etProduct_Name, etQuantity, etPrice, etTotal;
    private static Button btnInsert, btnUpdate, btnViewdata,
            btnDelete, btnClearTable, btnCheckProduct,
            btnTotalItems, btnTotalPrice, btnAdd,
            btnSubtract, btnAddToCart;
    private static TextView tvShowQuantity, tvResult;
    private static LinearLayout linearLayout;
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
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnViewdata = (Button) findViewById(R.id.btnViewdata);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnClearTable = (Button) findViewById(R.id.btnClearTable);
        btnCheckProduct = (Button) findViewById(R.id.btnCheckProduct);
        btnTotalItems = (Button) findViewById(R.id.btnTotalItems);
        btnTotalPrice = (Button) findViewById(R.id.btnTotalPrice);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSubtract = (Button) findViewById(R.id.btnSubtract);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
        tvShowQuantity = (TextView) findViewById(R.id.tvShowQuantity);
        tvResult = (TextView) findViewById(R.id.tvResult);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        /** Check if product_id exists and show/hide ADD button accordingly */

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vendor_id_temp = etVendor_id.getText().toString();
                Log.d(TAG, "vendor_id" + vendor_id + "," + vendor_id_temp);
//                Toast.makeText(MainActivity.this, "vendor_id" + vendor_id + "," + vendor_id_temp, Toast.LENGTH_SHORT).show();
                if (vendor_id.equals(vendor_id_temp) || vendor_id.equals("0")) {
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

        btnCheckProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product_id = etProduct_id.getText().toString();
                if (databaseHelper.findProductID(product_id)) {
                    tvResult.setText(product_id + " is found.");
                } else {
                    tvResult.setText(product_id + " is not found.");
                }
            }
        });

        btnViewdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewDatabase.class));
            }
        });

        btnTotalItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String total = databaseHelper.getNumberOfItems();
//                Toast.makeText(MainActivity.this, "Total Items: " + total, Toast.LENGTH_SHORT).show();
                tvResult.setText("Total Items: " + total);
            }
        });

        btnTotalPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String total = databaseHelper.getTotalPrice();
//                Toast.makeText(MainActivity.this, "Grand Total: ₹" + total, Toast.LENGTH_SHORT).show();
                tvResult.setText("Grand Total: ₹" + total);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We are assuming at lease one entry for product_id exists
                String product_id = "0";
                tvShowQuantity.setText(databaseHelper.addItem(product_id));
            }
        });

        btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we are assuming at least 1 entry exists
                String product_id = "0";
                String result = databaseHelper.subtractItem(product_id);
                if (Integer.parseInt(result) > 0) {
                    tvShowQuantity.setText(result);
                } else {
                    linearLayout.setVisibility(View.GONE);
                    btnAddToCart.setVisibility(View.VISIBLE);
                    databaseHelper.deleteData(product_id);
                }
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We are assuming no entry for product_id exist
                boolean result = databaseHelper.insertData("1",
                        "0",
                        "Test Product",
                        "1",
                        "10");
                if (result) {
                    tvShowQuantity.setText(databaseHelper.getQuantity("0"));
                    linearLayout.setVisibility(View.VISIBLE);
                    btnAddToCart.setVisibility(View.GONE);
                } else {
                    tvResult.setText("0 Exists");
                }
            }
        });

    }

    public boolean insertData() {
        int total = Integer.parseInt(etQuantity.getText().toString())
                * Integer.parseInt(etPrice.getText().toString());
        return databaseHelper.insertData(
                etVendor_id.getText().toString(),
                etProduct_id.getText().toString(),
                etProduct_Name.getText().toString(),
                etQuantity.getText().toString(),
                etPrice.getText().toString());
    }

    public boolean updateData() {
        int total = Integer.parseInt(etQuantity.getText().toString())
                * Integer.parseInt(etPrice.getText().toString());
        return databaseHelper.updateData(
                etVendor_id.getText().toString(),
                etProduct_id.getText().toString(),
                etProduct_Name.getText().toString(),
                etQuantity.getText().toString(),
                etPrice.getText().toString());
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
