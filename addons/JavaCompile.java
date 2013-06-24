/**
 * This class is one of the modular add-ons for the server which controls the virtual machines.
 */
 
import java.lang.Runtime;
import java.io.IOException;

public class JavaCompile {

	private static final String DIR = "T:\\Dropbox\\Programming\\";
	
	/**
	 * Compiles Java Code. The base directory is
	 * 
	 * Command name - javac
	 * 
	 * Arguments - Minimum of 1
	 * args[0]   - The java file to compile
     *
     * @param   args    arguments from the input, space delimited into an array
     * @return          an error code and description, the code is the first character of the string
	 **/
	public static String javac(String args[]) {
		if(args.length < 1) return "2 - Not Enough Arguments";
		
		try {
			Runtime.getRuntime().exec("javac "+args[0]);
		} catch(IOException ioe) {
			return "3 - IOException when running the command";
		}
		
		return "0 - Javac command successful";
	}
}