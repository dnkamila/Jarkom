import java.io.File;

public class ListFiles {
	public static void listFilesAndFilesSubDirectories(String sharedFolder,
			String directoryName) {

		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File file : fList) {
			if (file.isFile()) {
				String absolute = file.getAbsolutePath();

				String relative = new File(sharedFolder).toURI()
						.relativize(new File(absolute).toURI()).getPath();

				System.out.println(relative + ";" + file.lastModified());
			} else if (file.isDirectory()) {
				listFilesAndFilesSubDirectories(sharedFolder,
						file.getAbsolutePath());
			}
		}
	}

	public static void main(String[] args) {
		String directoryWindows = "D:\\Portable_IDM";
		System.out.println(directoryWindows);
		
		ListFiles.listFilesAndFilesSubDirectories(directoryWindows,
				directoryWindows);
	}
}
