package ch.bfh.audiofeedback;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * The Class ClientListActivity provides a List with clients. 
 * Clients can be added with touching on the soft options-menu button. 
 * Touching a client entry on the list selects the client and leads to the AddNoteActivity.
 * 
 * The clientdata on the list is stored in a SQLite database, provided by a DatabaseManager object.
 * 
 * @TODO 
 *  
 *  
 * @author pascalboss
 *
 */
public class ClientListActivity extends Activity{
	private static String TAG = "ClientListActivity";
	private static int CREATE_CLIENT_ACTIVITY = 0;
	private static int ADD_NOTE_ACTIVITY = 1;
	
	private Cursor cursor;
	private ListAdapter adapter;
	private ListView listview;
	
	private DatabaseManager clientDBManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get a Database Manager, which provides the methods to get and add data from/to database
		Log.d(TAG, "create  a DBManager");
		clientDBManager = new DatabaseManager(getApplicationContext());
		setUpListView();	    
	}// onCreate
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clientlist_optionmenu, menu);
		return true;
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		boolean result = true;
		// check, which item was selected and call the corresponding method
		switch (item.getItemId()) {
			case (R.id.clientlist_optionmenu_item1): 	createClient(); break;
			default:			result = false;
		}
		return result;
	}
	
	@Override
	public void onResume(){
		super.onResume();	
		setUpListView();	    
	}	   
	
	private void setUpListView(){
				
		// get a cursor, refering to the rows in the client_table
		cursor = clientDBManager.getAllClients();
		
		// set up the listview
		setContentView(R.layout.activity_client_list);
		ListView listview = (ListView) findViewById(R.id.clientList);
		
		// set up the fields for the cursor adapter
		Log.d(TAG, "Set up the adapter");	
		String[] columnNames = new String[] {"name", "firstname", "birthdate"};
		int[] viewIDs = new int[] {R.id.item_clientName, R.id.item_clientPrename, 
				R.id.item_clientDOB};
		
		// connect an adapter to the cursor and add the adapter to the listview
		adapter = new SimpleCursorAdapter(getApplicationContext(),
				R.layout.client_item,
				cursor,
				columnNames,
				viewIDs,
				0);
	    listview.setAdapter(adapter);
	    Log.d(TAG, listview.getCount() + " Items in adapter found");

	    // call method addNote if the user tabs (short) onto an list item
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                addNote(position);      
            }
        });
	    
	    // call method deleteItem if the user long-tabs onto an list item
	    listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long id) {
                deleteItem(position, id);
                return true;
            }
        });
	}

	private void createClient(){
		// start the new Activity		
		Intent intent = new Intent(this, CreateClientActivity.class);
		intent.putExtra("ch.bfh.audiofeedback.imagename3", getIntent().getStringExtra("ch.bfh.audiofeedback.imagename"));
		startActivityForResult(intent, CREATE_CLIENT_ACTIVITY);
	}
	
	private void addNote(int position){
		Intent intent = new Intent(this, AddNoteActivity.class);
		String name, firstname, dob, gender;
	
		cursor.moveToPosition(position);
		name = cursor.getString(cursor.getColumnIndex("name"));
		firstname = cursor.getString(cursor.getColumnIndex("firstname"));
		dob = cursor.getString(cursor.getColumnIndex("birthdate"));
		gender = cursor.getString(cursor.getColumnIndex("gender"));
		
		Log.d(TAG, name + ", " + firstname + ", " + dob + ", " + gender);
		String[] data = new String[]{name, firstname, dob, gender};
			
		for(int i = 0; i < data.length; i++){
			Log.d(TAG, data[i]);
		}
		
		// put the clientdata to the new intent
		intent.putExtra("ch.bfh.audiofeedback.client", data);
		
		// put the imagename from snapshot to the new intent
		Log.d(TAG, "Extra: " + getIntent().getStringExtra("ch.bfh.audiofeedback.imagename"));
		intent.putExtra("ch.bfh.audiofeedback.imagename2", getIntent().getStringExtra("ch.bfh.audiofeedback.imagename"));
			
		// start the new activity
		startActivityForResult(intent, ADD_NOTE_ACTIVITY);
	}
	
	@Override
	public void onCreateContextMenu(
			ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.clientlist_contextmenu, menu);
		Log.d(TAG, menuInfo.toString());
		Log.d(TAG, "Menu inflated");
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		boolean result = true;
		Log.d(TAG, "Item selected");
		switch (item.getItemId()) {
			case (R.id.clientlist_contextmenu_item1
					): 	 break;
			default:			result = false;
		}
		Log.d(TAG, Boolean.toString(result));
		return super.onContextItemSelected(item);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		// check, if result is valid
		Log.d(TAG, "Resultcode: " + requestCode);
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == ADD_NOTE_ACTIVITY){
				Log.d(TAG, "requestCode = ADD_NOTE_Activity");
				finish();
			}
			else if(requestCode == CREATE_CLIENT_ACTIVITY){
				Log.d(TAG, "requestCode = CreateClient_Activity");
				setUpListView();
			}
		}
	}
	
	private void deleteItem(int position, long id){
		Log.d(TAG, Long.toString(id));	
		clientDBManager.deleteItem(id);
		setUpListView();
	}

	
}
