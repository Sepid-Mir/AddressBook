package com.example.address_book;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.address_book.MyAddrContentProvider;
import com.example.address_book.MyAddrTableHandler;

import java.util.ArrayList;
import java.util.List;

/*
 * AddressDetailActivity allows to enter a new Address
 * or to change an existing
 */
public class AddressDetailActivity extends Activity {


    private Spinner myTitle , myProv;
    private EditText myFirst, myLast, myAddr, myCntr,myPost;
    private Button confirmBtn;

    private Uri addUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.address_edit);
        ActionBar actionBar = getActionBar();
        ((ActionBar) actionBar).setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.icon);


        myTitle = (Spinner) findViewById(R.id.title);
        myFirst = (EditText) findViewById(R.id.fname);
        myLast = (EditText)findViewById(R.id.lname);
        myAddr = (EditText) findViewById(R.id.addrline);
        myProv = (Spinner) findViewById(R.id.prv);
        myCntr = (EditText) findViewById(R.id.cntr);
        myPost = (EditText) findViewById(R.id.pcode);
        confirmBtn = (Button) findViewById(R.id.addr_edit_btn);

        List<String> spinnerArray1 =  new ArrayList<String>();
        List<String> spinnerArray2 =  new ArrayList<String>();
        spinnerArray1.add("Dr");
        spinnerArray1.add("Mr");
        spinnerArray1.add("Mrs");
        spinnerArray1.add("Ms");

        spinnerArray2.add("Ontario");
        spinnerArray2.add("British Columbia");
        spinnerArray2.add("Alberta");
        spinnerArray2.add("Quebec");
        spinnerArray2.add("Manitoba");
        spinnerArray2.add("Saskatchewan");
        spinnerArray2.add("Yukon");
        spinnerArray2.add("Nunavut");
        spinnerArray2.add("Northwest Territories");
        spinnerArray2.add("New Brunswick");
        spinnerArray2.add("Nova Scotia");
        spinnerArray2.add("Prince-Edward Island");
        spinnerArray2.add("Newfoundland");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray2);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myTitle.setAdapter(adapter1);
        myProv.setAdapter(adapter2);


        Bundle extras = getIntent().getExtras();

        // Check from the saved Instance
        addUri = (bundle == null) ? null : (Uri) bundle.getParcelable(MyAddrContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            addUri = extras.getParcelable(MyAddrContentProvider.CONTENT_ITEM_TYPE);
            fillData(addUri);
        }


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(myFirst.getText().toString())) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish(); }
            }

        });
    }

    private void fillData(Uri uri) {
        String[] projection = { MyAddrTableHandler.COLUMN_TITLE, MyAddrTableHandler.COLUMN_FNAME,MyAddrTableHandler.COLUMN_LNAME,
                MyAddrTableHandler.COLUMN_ADDRESS,
                MyAddrTableHandler.COLUMN_PROVINCE,MyAddrTableHandler.COLUMN_COUNTRY,MyAddrTableHandler.COLUMN_POSTCODE};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {

            cursor.moveToFirst();
            String category = cursor.getString(cursor.getColumnIndexOrThrow(MyAddrTableHandler.COLUMN_TITLE));

            for (int i = 0; i < myTitle.getCount(); i++) {

                String s = (String) myTitle.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    myTitle.setSelection(i);
                }
            }

            myFirst.setText(cursor.getString(cursor.getColumnIndexOrThrow(MyAddrTableHandler.COLUMN_FNAME)));
            myLast.setText(cursor.getString(cursor.getColumnIndexOrThrow(MyAddrTableHandler.COLUMN_LNAME)));
            myAddr.setText(cursor.getString(cursor.getColumnIndexOrThrow(MyAddrTableHandler.COLUMN_ADDRESS)));
            myCntr.setText(cursor.getString(cursor.getColumnIndexOrThrow(MyAddrTableHandler.COLUMN_COUNTRY)));
            myPost.setText(cursor.getString(cursor.getColumnIndexOrThrow(MyAddrTableHandler.COLUMN_POSTCODE)));

            // Always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyAddrContentProvider.CONTENT_ITEM_TYPE, addUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String title = (String) myTitle.getSelectedItem();
        String fname = myFirst.getText().toString();
        String lname = myLast.getText().toString();
        String address = myAddr.getText().toString();
        String province = (String) myProv.getSelectedItem();
        String country = myCntr.getText().toString();
        String postcode = myPost.getText().toString();

        // Only save if either summary or description
        // is available

        if (fname.length() == 0 && lname.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MyAddrTableHandler.COLUMN_TITLE, title);
        values.put(MyAddrTableHandler.COLUMN_FNAME, fname);
        values.put(MyAddrTableHandler.COLUMN_LNAME, lname);
        values.put(MyAddrTableHandler.COLUMN_ADDRESS,address);
        values.put(MyAddrTableHandler.COLUMN_COUNTRY,country);
        values.put(MyAddrTableHandler.COLUMN_PROVINCE,province);
        values.put(MyAddrTableHandler.COLUMN_POSTCODE,postcode);

        if (addUri == null) {
            // New ToDo
            addUri = getContentResolver().insert(MyAddrContentProvider.CONTENT_URI, values);
        } else {
            // Update ToDo
            getContentResolver().update(addUri, values, null, null);
        }
    }


    private void makeToast() {
        Toast.makeText(AddressDetailActivity.this, "Please maintain a summary",Toast.LENGTH_LONG).show();
    }
}
/***************************************************************************************************************************/
