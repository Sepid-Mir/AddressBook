package com.example.address_book;

        import android.app.ActionBar;
        import android.app.AlertDialog;
        import android.app.ListActivity;
        import android.app.LoaderManager;
        import android.content.CursorLoader;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.Loader;
        import android.database.Cursor;
        import android.net.Uri;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.ContextMenu;
        import android.view.ContextMenu.ContextMenuInfo;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.AdapterContextMenuInfo;
        import android.widget.ListView;
        import android.widget.SimpleCursorAdapter;
        import android.widget.Toolbar;

        import com.example.address_book.MyAddrContentProvider;
        import com.example.address_book.MyAddrTableHandler;

/*
 * ToDoOverviewActivity displays the existing ToDo items in a list
 *
 * You can create new ones via the ActionBar entry "Insert"
 * You can delete existing ones via a long press on the item
 */

public class AddressListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    // private Cursor cursor;
    private SimpleCursorAdapter adapter;
    String tag1="Sepideh Miralaei, Student ID: 125260190";
    String tag2="Android Studio 3.4.1, Student ID: 125260190";



    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_list);
        Log.i(tag1, "Application started");
        Log.i(tag2, "Application started");
        ActionBar actionBar = getActionBar();
        ((ActionBar) actionBar).setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.icon);

      //  getSupportActionBar().setLogo(R.drawable.mailbox);
        this.getListView().setDividerHeight(2);
        //Populate the ToDOlist when creating and rendering the ToDoList
        //for the first time and each time we re-render / re-create ToDoList

        fillData();
        //each Row or the List of Addresses will have a Context Menu
        //Long Press a Row --> call the toDoDetailsActivity

        registerForContextMenu(getListView());
    }

    // Create the menu based on the XML defintion
    //Create the options Manu and event handling of the
    //ActionBar Options Menu Item selection
    //creates a new Loader after the initLoader() call

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addr_menu, menu);
        return true;
    }

    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createAddr();
                return true;

            case R.id.about:
                new AlertDialog.Builder(AddressListActivity.this)
                        .setTitle("About Menu")
                        .setMessage("MyAddressPlus is a nice and simple Android" +
                                     "Application that allows a user to query/insert/" +
                                      "update/delete his home address. It is written for " +
                                      "android API 11 or newer. It supports Tablets" )

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAddr() {
        Intent i = new Intent(this, AddressDetailActivity.class);
        //to get smth back from the second activity
        //we will get back Summary to update List
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    //Context Menu on Long-press of a Row Item

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Uri uri = Uri.parse(MyAddrContentProvider.CONTENT_URI + "/" + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, AddressDetailActivity.class);
        Uri myaddrUri = Uri.parse(MyAddrContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(MyAddrContentProvider.CONTENT_ITEM_TYPE, myaddrUri);

        // Activity returns an result if called with startActivityForResult
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    // Called with the result of the other activity
    // requestCode was the origin request code send to the activity
    // resultCode is the return code, 0 is everything is ok
    // intent can be used to get data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void fillData() {
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { MyAddrTableHandler.COLUMN_FNAME };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.label };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.address_row, null, from, to, 0);

        //this.listview.adapter = adapter
        setListAdapter(adapter);
    }



    // Creates a new loader after the initLoader () call
    //@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { MyAddrTableHandler.COLUMN_ID, MyAddrTableHandler.COLUMN_FNAME };
        CursorLoader cursorLoader = new CursorLoader(this, MyAddrContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }
    //@Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    //@Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

}
