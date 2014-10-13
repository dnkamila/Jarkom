import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SimpleBoxServer {
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static final int PORT_NUMBER = 7711;
	private static final ArrayList<ThreadClient> listThreads = new ArrayList<ThreadClient>();

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
		} catch (IOException e) {
			System.out.println("Ooops, we can't use port number : "
					+ PORT_NUMBER);
		}

		while (true) {
			try {
				clientSocket = serverSocket.accept();
				ThreadClient currentThreadClient = new ThreadClient(
						clientSocket, listThreads);
				listThreads.add(currentThreadClient);
				currentThreadClient.start();
			} catch (IOException e) {
				System.out.println("Oops, our server has some problems");
			}

		}
	}
}

class ThreadClient extends Thread {
	private ArrayList<ThreadClient> listThreads;
	private String clientName;
	private String sharedFolderPath;
	private Socket clientSocket;
	private DataInputStream is;
	private PrintStream os;

	public ThreadClient(Socket clientSocket, ArrayList<ThreadClient> listThreads) {
		this.listThreads = listThreads;
		this.clientSocket = clientSocket;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		ArrayList<ThreadClient> listThreads = this.listThreads;
		try {
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());

			String name;
			while (true) {
				os.println("Please enter your shared folder path.");
				name = is.readLine().trim();
				if (name != null && !name.equals("")) {
					break;
				} else {
					os.println("You should type your shared folder path");
				}
			}
			
			synchronized (this) {
				for (int i = 0; i < listThreads.size(); i++) {
					if (listThreads.get(i) != null) {
						if (listThreads.get(i) == this) {
							sharedFolderPath = name;
							break;
						}
					}
				}
			}
			
			while (true) {
				String line = is.readLine();
				if (line.equals("quit")) {
					break;
				}
				for (int i = 0; i < listThreads.size(); i++) {
					if (listThreads.get(i) != null) {
						if (listThreads.get(i) != this) {
							listThreads.get(i).os.println("Username " + name
									+ " send " + line);
						} else {
							listThreads.get(i).os.println(name + " : " + line);
						}
					}
				}
				this.sleep(2500);
			}
			
			for (int i = 0; i < listThreads.size(); i++) {
				if (listThreads.get(i) != null
						&& listThreads.get(i) != this) {
					listThreads.get(i).os.println("Username " + name
							+ " is offline");
				}
			}
			
			for (int i = 0; i < listThreads.size(); i++) {
				if (listThreads.get(i) == this) {
					listThreads.set(i, null);
				}
			}
			
			is.close();
			os.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Oops, cannot established connection to : "
					+ clientSocket.getRemoteSocketAddress());
		} catch (InterruptedException e) {
		}

	}
}
