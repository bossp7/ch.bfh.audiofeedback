package ch.bfh.audiofeedback;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * 
 * @author pascalboss
 *
 */

public class CreateClientActivity extends Activity{
	private static String TAG = "CreateClientActivity";
	
	private int result = -1;
	EditText prename, name, dob, notes;
	Spinner gender;
	Button okButton, resetButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_client);
		Log.d(TAG, "open the input form");
		
		prename	= (EditText)findViewById(R.id.editText1);
		name 	= (EditText)findViewById(R.id.editText2);
		gender 	= (Spinner)findViewById(R.id.spinner1);
		dob	= (EditText)findViewById(R.id.editText4);	
		//notes	= (EditText)findViewById(R.id.editText5);
			
		
		
		okButton = (Button)findViewById(R.id.button2);	
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "create  a DBManager");
				DatabaseManager DBManager = new DatabaseManager(getApplicationContext());
				Log.d(TAG, "DBManager instantiated");
				// get the client data from the form input ant put it to the client database
				result = DBManager.putClient(
					new Client(
						name.getText().toString(),
						prename.getText().toString(),
						String.valueOf(gender.getSelectedItem()),
						dob.getText().toString())
					);
				// proceed to the addNote form
				addNote();
				finish();

				
				
			}// onClick()
		}); // setOnClickListener()
		
		resetButton = (Button)findViewById(R.id.button3);
		resetButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				prename.setText("");
				name.setText("");
				gender.invalidate();
				dob.setText("");
	//			notes.setText("");
			}
		});
		
	}
	
	/**
	 * Puts the client data as extra data to the intent and opens the AddNoteActivity.
	 */
	private void addNote(){
		Intent intent = new Intent(this, AddNoteActivity.class);
	
		// create the string array to put as extra data
		String[] data = new String[]{name.getText().toString(),
										prename.getText().toString(),
										String.valueOf(gender.getSelectedItem()),
										dob.getText().toString()};
		
		// put the extra data to the intent and start the intent
		intent.putExtra("ch.bfh.audiofeedback.client", data);
		intent.putExtra("ch.bfh.audiofeedback.imagename2", getIntent().getStringExtra("ch.bfh.audiofeedback.imagename3"));
		startActivity(intent);
		setResult(Activity.RESULT_OK);
		
		
	}
	

}// class CreateClientActivity
