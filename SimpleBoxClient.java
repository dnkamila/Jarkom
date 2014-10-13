import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class SimpleBoxClient implements Runnable {
	public static final String SERVER_NAME = "localhost";
	public static final int SERVER_PORT_NUMBER = 7718;
	private static Socket clientSocket;
	private static PrintStream os;
	private static DataInputStream is;
	private static FileInputStream fis;
	private static BufferedInputStream bis;
	private static OutputStream bos;
	private static File listFileLog;
	private static boolean closed;
	private static String sharedFolder;

	public static void main(String[] args) {
		BufferedReader read = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			System.out.print("Masukkan path shared folder Anda : ");
			sharedFolder = read.readLine();
			clientSocket = new Socket(SERVER_NAME, SERVER_PORT_NUMBER);

			os = new PrintStream(clientSocket.getOutputStream());
			bos = clientSocket.getOutputStream();
			listFileLog = new File("log.txt");
			fis = new FileInputStream(listFileLog);
		} catch (IOException e) {
			System.out.println("Cannot established connection to server");
		}

		if (clientSocket != null && os != null && is != null) {
			new Thread(new SimpleBoxClient()).start();
			while (true) {
				try {
					byte[] byteArrayFileLog = new byte[(int) listFileLog.length()];
					bis = new BufferedInputStream(fis);
					bis.read(byteArrayFileLog, 0, byteArrayFileLog.length);
					bos.write(byteArrayFileLog);
					bos.flush();
				} catch (IOException e) {
					System.out.println("Sorry, we cannot send your log");
				}

			}
		}

	}

	public void run() {
		String responseLine;
		try {
			while ((responseLine = is.readLine()) != null) {
				System.out.println(responseLine);
				if (responseLine.equals("quit")) {
					os.close();
					fis.close();
					is.close();
					clientSocket.close();
					System.exit(0);
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Oops, something's WRONG here");
		}
	}

}
