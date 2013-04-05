import java.lang.Runtime;
import java.io.IOException;

public class MsgMe {
	public static String msgMe(String args[]) {
		String s="";
		for(String st:args) s+=" "+st;
		s=s.substring(1);
		try {
			Runtime.getRuntime().exec("msg * "+s);
		} catch(IOException ioe) {
			return "1 - IOexception when running msgMe in MsgMe";
		}
		return "0 - msgMe in MsgMe ran successfully";
	}
}
