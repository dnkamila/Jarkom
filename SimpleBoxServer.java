import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class SimpleBoxServer {
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static final int PORT_NUMBER = 7736;
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

	public void RequestFile(ArrayList<String> listOfFiles) {

		try {
			InputStream ris = clientSocket.getInputStream();
			for (int i = 0; i < listOfFiles.size(); i++) {
				os.println("request;" + listOfFiles.get(i));
				byte[] byteReceived = new byte[10000000];
				ris.read(byteReceived, 0, byteReceived.length);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void run() {
		ArrayList<ThreadClient> listThreads = this.listThreads;
		try {

			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());

			while (true) {
				String line = is.readLine();
				ListFiles.listFiles(".", "server");
				File logFile = new File("log_server.txt");
				BufferedReader readFile;
				readFile = new BufferedReader(new FileReader(logFile));
				String filesOnServer = readFile.readLine();
				ArrayList<String> listFilesOnServer = new ArrayList<String>(
						Arrays.asList(filesOnServer.split(";")));
				ArrayList<String> fileNotFoundOnClient = new ArrayList<String>();
				ArrayList<String> fileNotFoundOnServer = new ArrayList<String>();
				String requestToClient = "";
				String requestToServer = "";
				readFile.close();
				if (line.indexOf("check") != -1) {
					for (int i = 0; i < listThreads.size(); i++) {
						if (listThreads.get(i) != null) {
							if (listThreads.get(i) == this) {
								ArrayList<String> listOfFiles = new ArrayList<String>(
										Arrays.asList(line.split(";")));
								for (int j = 1; j < listOfFiles.size(); j++) {
									// jika file pada client tidak terdapat pada
									// server
									if (!listFilesOnServer.contains(listOfFiles
											.get(j))) {
										// ini nanti diadd
										fileNotFoundOnClient.add(listOfFiles
												.get(j).split("!")[0]);
										requestToClient += listOfFiles.get(j)
												.split("!")[0] + "!";
									}
								}
								for (int j = 0; j < listFilesOnServer.size(); j++) {
									// jika file pada client tidak terdapat pada
									// server
									if (!listOfFiles.contains(listFilesOnServer
											.get(j))) {
										// ini nanti dihapus
										fileNotFoundOnServer
												.add(listFilesOnServer.get(j)
														.split("!")[0]);
										// requestToClient +=
										// listFilesOnServer.get(j).split("!")[0]+"!";
									}
								}

								// SendFileToClient(fileNotFoundOnServer);
								RequestFile(fileNotFoundOnClient);
							}
						}
					}
				} else if (line.indexOf("quit") != -1) {
					for (int i = 0; i < listThreads.size(); i++) {
						if (listThreads.get(i) != null) {
							if (listThreads.get(i) == this) {
								os.println("quit");
							}
						}
					}
					System.out.println("Done quit");
					break;
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
		}
	}
}
