package ch.bfh.audiofeedback;

//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//
//public class SettingsActivity extends Activity {
//	
//	@Override
//	public void onCreate(final Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.setting);	
//		
//        CheckBox chbx1Activity = (CheckBox)findViewById(R.id.checkBox1);
//
//        CheckBox chbx2Activity = (CheckBox)findViewById(R.id.checkBox2);
//
//        chbx1Activity.setOnClickListener(new View.OnClickListener()
//        {            	
//        	@Override
//        	public void onClick(View v) 
//        	{
//        		if (((CheckBox) v).isChecked()) {
//        			ScopeActivity.keepScreenOn = false;
//        		} else{
//
//        			ScopeActivity.keepScreenOn = true;
//        		}
//        	}
//        });
//        
//        chbx2Activity.setOnClickListener(new View.OnClickListener()
//        {           	
//        	@Override
//        	public void onClick(View v) 
//        	{
//        		if (((CheckBox) v).isChecked()) {
//        			ScopeActivity.orientatLandscape = false;
//        		} else{
//
//        			ScopeActivity.orientatLandscape = true;
//        		}
//        	}
//        });
//	}
//	
//}

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(SettingsActivity.this, R.xml.preferences, false);
	}
}
