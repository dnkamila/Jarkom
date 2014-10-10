import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ListFiles {
	public static void listFiles(String sharedFolder) {
		try {
			//belom handle kalau sudah ada log.txt
			File file = new File("log.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			listFilesHelper(bw, sharedFolder, sharedFolder);
			bw.close();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void listFilesHelper(BufferedWriter bw, String sharedFolder, String directoryName) throws IOException {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();

		for (File file : fList) {
			if (file.isFile()) {
				String absolute = file.getAbsolutePath();
				String relative = new File(sharedFolder).toURI().relativize(new File(absolute).toURI()).getPath();
				
				bw.write(relative + ";" + file.lastModified() + "\n");
			}
			
			else if (file.isDirectory()) {
				listFilesHelper(bw, sharedFolder, file.getAbsolutePath());
			}
		}
	}

	public static void main(String[] args) {
		String directoryWindows = "D:\\Portable_IDM";
		ListFiles.listFiles(directoryWindows);
	}
}
