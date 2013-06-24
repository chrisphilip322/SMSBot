import com.techventus.server.voice.Voice;
import java.io.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

public class SMSBot {

	private static Voice voice;
	private static Map<String, Method> 		addOnMap;
	private static Map<Integer, String>		messageHashMap;
	private static boolean verbose;

    public static void main(String[] args) throws Exception {

		init(args);

		while(true);
		
    }

	private static String makeAddOnMap() throws Exception {
		System.out.println("\n-----------------------------------------\n");
		File folder=new File("addons");
		File fileList[]=folder.listFiles();
		ClassLoader cl=new URLClassLoader(new URL[] {folder.toURI().toURL()});
		for(File f:fileList) {
			String name=f.getName();
			if(!name.endsWith(".class")) continue;
			name=name.substring(0,name.lastIndexOf('.'));
			Class c=cl.loadClass(name);
			Method methods[]=c.getDeclaredMethods();
			for(Method m:methods) {
				name=m.getName();
				System.out.println(name);
				if(addOnMap.put(name,m)!=null) return "1 - command overload";
			}
		}
		System.out.println("\n-----------------------------------------\n");
		return "0 - success";
	}

	private static String executeCommand(String arg) throws Exception {
		String split[]=arg.split(" ");
		String args[]=Arrays.copyOfRange(split,1,split.length);
		Method m=addOnMap.get(split[0]);
		return (String)(m.invoke(null,(Object)(args)));
	}
	
	private static void init(String args[]) throws Exception{
		System.setOut(new PrintStream("console.log"));
	
		for(String s:args) {
			if(s.equals("--verbose")) System.setOut(System.out);
		}
	
		messageHashMap = new HashMap<Integer,String>();
		addOnMap = new HashMap<String,Method>();

		String username="",password="";
		int tickrate=3000;
		FileInputStream fstream=new FileInputStream("config.ini");
		DataInputStream in=new DataInputStream(fstream);
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		String strLine;
		while((strLine = br.readLine()) != null) {
			String split[]=strLine.split("=");
			if(split[0].trim().equals("username")) username=split[1].trim();
			else if(split[0].trim().equals("password")) password=split[1].trim();
			else if(split[0].trim().equals("tickrate")) tickrate=Integer.parseInt(split[1].trim());
		}
		
		voice = new Voice(username,password);
		voice.login();
		
		readNewSMS(true);

		System.out.println(makeAddOnMap());
		
		new Timer(tickrate, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					readNewSMS(false);
				} catch(Exception e) {}
				System.out.println("Running...");
			}
		}).start();
	}
    
	private static void readNewSMS(boolean firstRun) throws Exception{
		String messages[]=voice.getSMS().split("gc-message-sms-row");
	
		for(int i=1;i<messages.length;i++) {
			String tempMessage[]=messages[i].split("\n",14);
			String messageCode=tempMessage[2].substring(7,tempMessage[2].length()-1)+"\t"
							  +tempMessage[4].substring(37,tempMessage[4].length()-7)+"\t"
							  +tempMessage[6].substring(4);
			int hash=messageCode.hashCode();
			while(true) {
				if(messageHashMap.get(hash)==null) {
					messageHashMap.put(hash,messageCode);
					if(!firstRun) {
						System.out.println("Found one! - "+messageCode.split("\t")[1]);
						System.out.println(executeCommand(messageCode.split("\t")[1]));
					}
					break;
				}
				else if(messageHashMap.get(hash).equals(messageCode)) {
					break;
				}
				hash=(hash*3+1);
			}
		}
	}
}
