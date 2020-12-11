package com.example.assignment2_part2;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ToDoDetailActivity extends Activity {
    private Spinner designation, Province;
    private EditText firstName, LastName, Address, Country, Postal_Code;
    private Uri todoUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.address_edit);

        designation = (Spinner) findViewById(R.id.designation);
        Province = (Spinner) findViewById(R.id.province);
        firstName = (EditText) findViewById(R.id.firstName);
        LastName = (EditText) findViewById(R.id.lastName);
        Address = (EditText) findViewById(R.id.address);
        Country = (EditText) findViewById(R.id.country);
        Postal_Code = (EditText) findViewById(R.id.postalcode);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        Bundle extras = getIntent().getExtras();

        // Check from the saved Instance
        todoUri = (bundle == null) ? null : (Uri) bundle.getParcelable(MyToDoContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            todoUri = extras.getParcelable(MyToDoContentProvider.CONTENT_ITEM_TYPE);
            fillData(todoUri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(firstName.getText().toString())
                        || TextUtils.isEmpty(LastName.getText().toString())
                        || TextUtils.isEmpty(Address.getText().toString())
                        || TextUtils.isEmpty(Country.getText().toString())
                        || TextUtils.isEmpty(Postal_Code.getText().toString()) ) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }

    private void fillData(Uri uri) {
        String[] projection = { ToDoTableHandler.COLUMN_DESIGNATION, ToDoTableHandler.COLUMN_FIRSTNAME, ToDoTableHandler.COLUMN_LASTNAME, ToDoTableHandler.COLUMN_ADDRESS, ToDoTableHandler.COLUMN_PROVINCE, ToDoTableHandler.COLUMN_COUNRTY, ToDoTableHandler.COLUMN_POSTALCODE };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String category = cursor.getString(cursor.getColumnIndexOrThrow(ToDoTableHandler.COLUMN_DESIGNATION));

            for (int i = 0; i < designation.getCount(); i++) {

                String s = (String) designation.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    designation.setSelection(i);
                }
            }
            String province = cursor.getString(cursor.getColumnIndexOrThrow(ToDoTableHandler.COLUMN_PROVINCE));

            for (int i = 0; i < Province.getCount(); i++) {

                String s = (String) Province.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    Province.setSelection(i);
                }
            }
            firstName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToDoTableHandler.COLUMN_FIRSTNAME)));
            LastName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToDoTableHandler.COLUMN_LASTNAME)));
            Address.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToDoTableHandler.COLUMN_ADDRESS)));
            Country.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToDoTableHandler.COLUMN_COUNRTY)));
            Postal_Code.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToDoTableHandler.COLUMN_POSTALCODE)));

            // Always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyToDoContentProvider.CONTENT_ITEM_TYPE, todoUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String category = (String) designation.getSelectedItem();
        String province = (String) Province.getSelectedItem();
        String fname = firstName.getText().toString();
        String lname = LastName.getText().toString();
        String address = Address.getText().toString();
        String country = Country.getText().toString();
        String postalcode = Postal_Code.getText().toString();

        // Only save if either summary or description
        // is available

        if (fname.length() == 0 && country.length() == 0 && postalcode.length() == 0 && address.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ToDoTableHandler.COLUMN_DESIGNATION, category);
        values.put(ToDoTableHandler.COLUMN_FIRSTNAME, fname);
        values.put(ToDoTableHandler.COLUMN_LASTNAME, lname);
        values.put(ToDoTableHandler.COLUMN_ADDRESS, address);
        values.put(ToDoTableHandler.COLUMN_PROVINCE, province);
        values.put(ToDoTableHandler.COLUMN_COUNRTY, country);
        values.put(ToDoTableHandler.COLUMN_POSTALCODE, postalcode);

        if (todoUri == null) {
            // New ToDo
            todoUri = getContentResolver().insert(MyToDoContentProvider.CONTENT_URI, values);
        } else {
            // Update ToDo
            getContentResolver().update(todoUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(ToDoDetailActivity.this, "Please enter all required fields",Toast.LENGTH_LONG).show();
    }
}
