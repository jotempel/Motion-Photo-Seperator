import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Seperator {

	public static void main(String[] args) {

		if (args == null || args.length < 1) {
			System.out.println("Missing path to the directory with the Motion Photos.");
			return;
		}
		
		File[] files = getDirectoryFiles(args[0]);
		
		if (files == null) {
			System.out.println("Invalid directory path!");
			return;
		}
		
		if (files.length < 1) {
			System.out.println("No files found (.jpg)");
			return;
		}
		
		for (int i = 0; i < files.length; i++) {
			System.out.println("File " + i + ": " +files[i].getAbsolutePath());
			seperateMotionPhotoV1(files[i].getAbsolutePath());
		}
	}

	private static File[] getDirectoryFiles(String path) {
		File directory = new File(path);
		File[] files = null;
		if (directory.exists() && directory.isDirectory() && directory.canRead() && directory.canWrite()) {
			files = directory.listFiles(new FileFilter() {

				@Override
				public boolean accept(File arg0) {
					if (arg0.isFile() && arg0.getName().toLowerCase().endsWith(".jpg")) {
						return true;
					}
					return false;
				}
			});
		}
		return files;
	}

	public static void seperateMotionPhotoV1(String filePath) {

		String allString = readAllString(filePath);

		int index = allString.indexOf("MotionPhoto_Data");
		
		if (index != -1) {
			System.out.println("Motion Photo found!");
			byte[] allFile = readAllBytesJava7(filePath);
			try {
				byte[] mpgFile = Arrays.copyOfRange(allFile, index + 16, allFile.length - 1);
				String mp4FilePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".mp4";
				FileOutputStream fos = new FileOutputStream(mp4FilePath);
				fos.write(mpgFile);
				fos.close();

				byte[] jpgFile = Arrays.copyOfRange(allFile, 0, index - 1);
				fos = new FileOutputStream(filePath);
				fos.write(jpgFile);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Not a Motion Photo.");
		}
	}

	private static byte[] readAllBytesJava7(String filePath) {
		byte[] content = null;

		try {
			content = Files.readAllBytes(Paths.get(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	private static String readAllString(String filePath) {
		String content = null;

		try {
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

}
