/**
 * Class AddNoteactivity
 * @author P. Boss
 * @version 0.1, 10/05/13
 * 
 */


package ch.bfh.audiofeedback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * <H2>Overview</H2>
 * Class AddNoteActivity enables the user to do the following:
 * <ul>
 * 	<li>check the entries of name, firstname, gender and date of birth
 *  <li>adding additional comment to the chosen graphic
 * 	<li>reset the comment
 * 	<li>save the graphic as .pdf file in file system
 * </ul>
 * 
 * @author P. Boss
 * @version 0.1 10/05/13
 *
 */
public class AddNoteActivity extends Activity{
	private final static String TAG = "AddNoteActivity";
	private static float GRAPH_WIDTH = 500.0f;
	private static float GRAPH_HIGHT = 300.0f;
	
	TextView name, firstname, dob, gender;
	EditText note;
	boolean pdfCreated = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_note);
		
		name = 			(TextView) findViewById(R.id.AddNote_TextView2);
		firstname = 	(TextView) findViewById(R.id.AddNote_TextView4);
		dob = 			(TextView) findViewById(R.id.AddNote_TextView6);
		gender = 		(TextView) findViewById(R.id.AddNote_TextView8);
		note =			(EditText) findViewById(R.id.AddNote_EditText1);

		Log.d(TAG, "open the add note form");
		
		String[] client = getIntent().getStringArrayExtra("ch.bfh.audiofeedback.client");
		
		if (client != null){
			for (int i=0; i < client.length;i++){
				Log.d(TAG, client[i]);
			}
			Log.d(TAG, Integer.toString(client.length));
			firstname.setText(client[0]);
			name.setText(client[1]);
			dob.setText(client[2]);
			gender.setText(client[3]);				
		}
		else {
			Log.d(TAG, "no data in str");
		}
		
		Button okButton = (Button)findViewById(R.id.AddNote_Button1);	
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				// create the .pdf file
				String filename = createPDF();
				Log.d(TAG, "pdf created successfully");
				// tell the calling activity whether creating the file was succesful or not
				setResult(Activity.RESULT_OK);			
				
				// open the created file
				openFile(filename);
				finish();
				
				
//				// show a dialog that the creation succeeded
//				AlertDialog.Builder builder1 = new AlertDialog.Builder(getBaseContext());
//	            builder1.setMessage("pdf - file created successfully");
//	            builder1.setCancelable(true);
//	            builder1.setPositiveButton("OK",
//	                    new DialogInterface.OnClickListener() {
//	                public void onClick(DialogInterface dialog, int id) {
//	                    dialog.cancel();
//	                }
//	            });
//	           
//	            AlertDialog alert11 = builder1.create();
//	            alert11.show();
	
				
			}
		});		
	}
	
	/**
	 * Crates a .pdf file and saves it to the external storage under a generated name.
	 * The data is being caught from the class fields name, firstname, dob, gender and note.
	 * @throws DocumentException
	 */
	private String createPDF(){// throws DocumentException{
		
		Log.d(TAG, "starting up createPDF");		
		// define the method data
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
    	Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
    	Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    	Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    	
    	// set up a tab
    	Chunk tab1 = new Chunk(new VerticalPositionMark(), 100, true);
    	
    	// get actual date
    	Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());
		
		// get the actual system time
		Long tsLong = System.currentTimeMillis()/1000;
		String ts = tsLong.toString();
    	  	
		// generate the filename out of name, firstname, and actual date
		String filename = formattedDate + "_" + firstname.getText().toString() + "_" 
										+ name.getText().toString() 
										+ "_"
										+ ts
										+ ".pdf";
		
		// generate the File object
        File file = new File(Environment.getExternalStorageDirectory().getPath().toString() + "/" + filename);
        
        try {
            file.createNewFile();
            
            // create a .pdf object and link it to the file
            PdfWriter pdfwriter = PdfWriter.getInstance(document, new FileOutputStream(file));         
            pdfwriter.close();
	        
	        // Set the Document Title
	        Paragraph title = new Paragraph("Feedback spectrum", titleFont);
	        Chapter titleChapter = new Chapter(title, 1);
	        titleChapter.setNumberDepth(0);
	        
	        Paragraph actualDate = new Paragraph("Date of measure:", normalFont);
	        actualDate.setLeading(50f);
	        actualDate.add(tab1);
	        actualDate.add(formattedDate);
	        titleChapter.add(actualDate);
	        
	        // Set the first chapter
	        Paragraph patientChapter = new Paragraph("Patient data", subFont);
	        patientChapter.setLeading(60f);
	        Section patientSection = titleChapter.addSection(patientChapter);
	        patientSection.setNumberDepth(0);
	        
	        Paragraph namePar = new Paragraph("Last name:", normalFont);
	        namePar.setLeading(20f);
	        namePar.add(tab1);
	        namePar.add(name.getText().toString());
	        patientSection .add(namePar);
	        
	        Paragraph firstnamePar = new Paragraph("First name:", normalFont);
	        firstnamePar.add(tab1);
	        firstnamePar.add(firstname.getText().toString());
	        patientSection.add(firstnamePar);
	        
	        Paragraph dobPar = new Paragraph("Date of birth:", normalFont);
	        dobPar.add(tab1);
	        dobPar.add(dob.getText().toString());
	        patientSection .add(dobPar);
	                
	        Paragraph genderPar = new Paragraph("Gender:", normalFont);
	        genderPar.add(tab1);
	        genderPar.add(gender.getText().toString());
	        patientSection .add(genderPar);
	        	        
	        // Set the note chapter
	        Paragraph noteChapter = new Paragraph("Notes", subFont);
	        noteChapter.setLeading(60f);
	        Section noteSection = titleChapter.addSection(noteChapter);
	        noteSection.setNumberDepth(0);
	        
	        Paragraph notePar = new Paragraph(note.getText().toString());
	        noteSection.add(notePar);
	        
	     // Set the graph chapter
	        Paragraph graphChapter = new Paragraph("Spectrum", subFont);
	        graphChapter.setLeading(60f);
	        Section graphSection = titleChapter.addSection(graphChapter);
	        graphSection.setNumberDepth(0);
	        
	        // get the image path and name from intent
	        String imagename = getIntent().getStringExtra("ch.bfh.audiofeedback.imagename2");
	        Log.d(TAG, imagename);
	        // create the image object
	        Image graph = Image.getInstance(imagename);
	        // change the image size to fit the page
	        graph.scaleToFit(GRAPH_WIDTH, GRAPH_HIGHT);
	        //  and put it to the document
	        graphSection.add(graph);           
	      
	        // Now add all this to the document
	        document.open();
	        document.add(titleChapter);
	        document.close();
	        
	        // delete the image file
	        File image = new File(imagename);	        
	        if(image.delete()){
	        	Log.d(TAG, "Image deleted" );
	        }
	        	        
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (DocumentException e2){
        	e2.printStackTrace();
        }
        
        return filename;
        
	 }// createPDF()
	
	private void openFile(String filename){
		
		File file = new File(Environment.getExternalStorageDirectory().getPath().toString() + "/" + filename);
		Uri uri  = Uri.fromFile(file);
		
		Log.d(TAG, filename);
		Log.d(TAG, uri.toString());

		try
		{
		    Intent intentUrl = new Intent(Intent.ACTION_VIEW);
		    intentUrl.setDataAndType(uri, "application/pdf");
		    intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    startActivity(intentUrl);
		}
		catch (ActivityNotFoundException e)
		{
		    Toast.makeText(this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
		}
		
		
	} // openFile
	
}// AddNoteActivity
