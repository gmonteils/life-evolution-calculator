package fr.asys.tech.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.write-file-output", havingValue = "true", matchIfMissing = true)
public class FileLivingCellOutputWriter implements LivingCellOutputWriter {

	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

	private static final String OUTPUT_FILENAME_FORMAT = "output-%d.life";
	
	private final File workingDirectory;
	
	public FileLivingCellOutputWriter() {
		//Create a working directory to copy file
		File currentDirectory = Paths.get("").toAbsolutePath().toFile();
		workingDirectory = new File(currentDirectory, DATETIME_FORMATTER.format(LocalDateTime.now()));
		if (!workingDirectory.mkdirs()) {
			throw new IllegalArgumentException("Cannot write into software working directory.");
		}
	}
	
	@Override
	public void print(String multilineLivingCellString, int iteration) {
		
		// Copy configuration file to working directory
		File iterationOutputFile = new File(workingDirectory, String.format(OUTPUT_FILENAME_FORMAT, iteration));
		try (FileOutputStream fos = new FileOutputStream(iterationOutputFile)){
			fos.write(multilineLivingCellString.getBytes(StandardCharsets.UTF_8));
		} catch (IOException ioe) {
			throw new RuntimeException("Unexpected exception while writing file result : " + ioe.getMessage(), ioe);
		}
	}
}
