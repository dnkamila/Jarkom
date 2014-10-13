import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleBoxClient implements Runnable {
	public static final String SERVER_NAME = "localhost";
	public static final int SERVER_PORT_NUMBER = 7736;
	static Socket clientSocket;
	static PrintStream os;
	static DataInputStream is;
	static boolean closed;

	public static void main(String[] args) {
		BufferedReader read = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			System.out.print("Masukkan path shared folder Anda : ");
			String sharedFolder = read.readLine();
			clientSocket = new Socket(SERVER_NAME, SERVER_PORT_NUMBER);

			os = new PrintStream(clientSocket.getOutputStream());
			is = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			System.out.println("Cannot established connection to server");
		}

		if (clientSocket != null && os != null && is != null) {
			new Thread(new SimpleBoxClient()).start();
			new Thread(new SimpleBoxClientCheck()).start();
		}

	}

	public void run() {
		String responseLine;
		try {
			while ((responseLine = is.readLine()) != null) {
				if (responseLine.equals("quit")) {
					os.close();
					is.close();
					clientSocket.close();
					System.exit(0);
					break;
				}
				if (responseLine.indexOf("request") != -1)
				{
					
				}
			}
		} catch (IOException e) {
			System.out.println("Oops, something's WRONG here");
		}
	}

}
