package by.creepid.docgeneration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

	public static byte[] getBytes(String pathToFile) {
		byte[] data = new byte[0];
		Path path = Paths.get(pathToFile);
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

}
