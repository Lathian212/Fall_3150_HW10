import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import java.util.Scanner;
public class JavaShell {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String line = null;
		String [] commandArg = null;
		String [] tempArg = null;
		int commandLen =0;
		ProcessBuilder procb = null;
		Process proc = null; 
		File stdout = null;
		boolean backgroundProc = false;
		boolean redirect = false;
		
		
		do{
			System.out.print("Please enter a command line argument or enter to exit: ");
			line = input.nextLine();
			//System.out.println(line);
			if (line.matches("^[a-zA-Z0-9][a-zA-Z0-9]*&")) {
				backgroundProc = true;
			}
			else {
				backgroundProc = false;
			}
			if (line.matches(".* > [a-zA-Z].*")) {
				redirect = true;
			}
			else {
				redirect = false;
			}
			commandArg = line.split("  *");
			commandLen = commandArg.length;
			//Parse commandArg & background process
			if (backgroundProc){
				//Below so don't get error when try to build background process remove &
				commandArg[0] = commandArg[0].substring(0, commandArg[0].length()-1);
			}
			if (redirect) {
				stdout = new File(commandArg[commandLen-1]);
				//System.out.println(stdout);
				//Chop off trailing ">" "FILENAME"
				tempArg = new String[commandLen-2];
				for (int i = 0 ; i < commandLen-2; i++) {
						tempArg[i] = commandArg[i];
				}
				//Having some bugs want to see if this fixes it.
				commandArg = tempArg;
				
			}

			procb = new ProcessBuilder(commandArg);
			//procb.directory(new File("/home/lathian/workspace/OOP_HW10/"));
			//Below allows me to use my Eclipse terminal
			procb.inheritIO();
			//Put standardOut in right file if redirection was called for.
			if(redirect) {
				procb.redirectOutput(Redirect.appendTo(stdout));
				//System.out.println(procb.directory());
			}
			if (backgroundProc) {
				System.out.println("Subprocess will terminate on it's own, not waiting.");
				try {
					procb.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Waiting for latest subprocess to return");
				try {
					proc = procb.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					proc.waitFor();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}while (line.length() !=0);

	}

}
