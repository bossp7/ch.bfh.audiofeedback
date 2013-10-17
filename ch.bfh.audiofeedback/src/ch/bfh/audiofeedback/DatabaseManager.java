package ch.bfh.audiofeedback;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author pascalboss
 *
 */
public class DatabaseManager {
	private final String TAG = "DatabaseManager";
	SQLiteDatabase clientDB;
	
	// String to create client table
	private static final String CREATE_CLIENT_TABLE = 
			"CREATE TABLE IF NOT EXISTS client_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"firstname TEXT," +
			"name TEXT, " +
			"gender TEXT, " +
			"birthdate TEXT)";	
	
	public DatabaseManager(Context context) {
		SQLiteDatabase db = null;
		Log.d(TAG, "trying to create or open DB");
        db = context.openOrCreateDatabase(
        		"AudioFeedbackClients.db",
        		SQLiteDatabase.CREATE_IF_NECESSARY,
        		null);
        Log.d(TAG, "opening the DB suceeded, trying to create the tables");
        db.execSQL(CREATE_CLIENT_TABLE);
        Log.d(TAG, "creation of the tables suceeded");
        clientDB = db;
	}
	
	public int putClient(Client client){
		boolean exists = false;
		int clientID = -1;
		
		Log.d(TAG, "Running PutClient...");
		
		// get some data from the table, matching the same name as the new one
		Cursor cursor = clientDB.query("client_table", null, "name", null, null, null, null, null);
		if (cursor.getCount()==1) {
			Log.d(TAG, "no data found... DB is empty");
		}
		
		// check if yet a exact entry exists
		else while (cursor.moveToNext()){
			if (client.name.equals(cursor.getString(2))){
				Log.d(TAG, "the name exists, checking the dob");
				if ((client.dob == cursor.getString(4))){
					Log.d(TAG, "the name with same dob exists, checking the prename");
					if (client.firstname.equals(cursor.getString(1))){
						exists = true;
						Log.d(TAG, "this client yet exists! ");	
						clientID = cursor.getInt(0);
					}
				}
			}
		}
		
		// if no entry exists, add the new entry to the table
		if (!exists) {
			Log.d(TAG, "no entry with this client exists... entry will be made.");
			ContentValues values = new ContentValues();
			values.put("name",  client.name);
			values.put("firstname", client.firstname);
			values.put("gender", client.gender);
			values.put("birthdate", client.dob);
			try {
				clientID = (int) clientDB.insertOrThrow("client_table", null, values);
			}
			catch(SQLException e){
				Log.e(TAG, "adding client didn't work");
			}
			
		} //if	
		
		return clientID;
	} // putChart()
	
	

	
	public Cursor getAllClients(){
		Log.d(TAG, "get some clients...");
		Cursor cursor = clientDB.query("client_table", null, null, null, "name", null, "LOWER(name) DESC, name DESC", null);
		Log.d(TAG, cursor.getCount() + " rows read");
		return cursor;
	}
	
	
	public boolean deleteItem(long id){
		return clientDB.delete("client_table", "_id" + "=" + id, null) > 0;
	}
	
	
	


}
