/**
 * This class is one of the modular add-ons for the server which controls the virtual machines.
 */

import java.lang.Runtime;
import java.io.*;

public class VMManage {
	
	/**
	 * Manages VirtualBox. It can start, restart, or stop the server.
	 *
	 * Command Name - vm
	 *
	 * Arguments - Minimum of 2, more than 3 are ignored
	 * args[0]   - The sub command; start, restart, save, or stop.
	 * args[1]   - The VM name; must be one word.
	 * args[2]   - Starts the VM in headless mode if it equals 'headless'; only applies to the start subcommand.
	 *
	 * @param	args	arguments from the input, space delimited into an array
	 * @return			an error code and description, the code is the first character of the string
	 **/
	public static String vm(String args[]) {
		if(args.length<2) return "2 - Not Enough Arguments";

		String command="\"C:\\Program Files\\Oracle\\VirtualBox\\VBoxManage.exe\"";

		if(args[0].equals("start")) {
			command+=" startvm "+args[1];
			if(args.length>=3 && args[2].equals("headless")) {
				command+=" --type headless";
			}
		} else if(args[0].equals("restart")) {
			command+=" controlvm "+args[1]+" reset";
		} else if(args[0].equals("save")) {
			command+=" controlvm "+args[1]+" savestate";
		} else if(args[0].equals("stop")) {
			command+=" controlvm "+args[1]+" poweroff";
		} else {
			return "1 - Invalid VM command";
		}
		try {
			System.out.println(command);
			Runtime.getRuntime().exec(command);
		} catch(IOException ioe) {
			return "3 - IOException when running the command";
		}
		return "0 - VM command successful";
	}
}
