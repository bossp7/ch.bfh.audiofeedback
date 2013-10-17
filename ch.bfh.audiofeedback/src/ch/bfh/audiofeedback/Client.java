package ch.bfh.audiofeedback;

/**
 * 
 * @author pascalboss
 *
 */

//Helper class to encapsulate Author information programmatically
public class Client {
	String firstname;
	String name;
	String dob;
	String gender;

	public Client(String cfirstname, String clastname, String cgender, String cdob) {
		firstname = cfirstname;
		name = clastname;
		dob = cdob;
		gender = cgender;

	}
}
