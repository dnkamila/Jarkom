import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SimpleBoxClientCheck implements Runnable {

	public void run() {

		while (true) {
			try {
				ListFiles.listFiles("D:\\Adsis", "client");
				File logFile = new File("log_client.txt");
				BufferedReader readFile;
				readFile = new BufferedReader(new FileReader(logFile));
				SimpleBoxClient.os.println("check;" + readFile.readLine());
				Thread.sleep(5*1000);
				readFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
