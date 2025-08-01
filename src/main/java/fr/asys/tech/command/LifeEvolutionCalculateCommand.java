package fr.asys.tech.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.asys.tech.model.LivingCell;
import fr.asys.tech.service.LivingCellService;
import fr.asys.tech.utils.EvolutionWatcher;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Component 
@Command(name = "lifeEvolutionCalculateCommand")
public class LifeEvolutionCalculateCommand implements Callable<Integer> {

	private final LivingCellService livingCellService;

	@Autowired
	public LifeEvolutionCalculateCommand(LivingCellService livingCellService) {
		this.livingCellService = livingCellService;
	}

	@Option(names = {"--iteration", "-i"}, description = "Evolution iteration", defaultValue = "5", showDefaultValue = Visibility.ALWAYS)
	private int iteration;
	
	@Option(names = {"--stats", "-s"}, description = "Print statistics ?", defaultValue = "false", showDefaultValue = Visibility.ALWAYS)
	private boolean statisticsEnabled;

	@Option(names = { "-h", "--help", "-?", "-help"}, usageHelp = true, description = "Display this help and exit")
	private boolean help;

	@Parameters(description = "input life file")
	private File lifeInputFile;

	public Integer call() throws Exception {

		EvolutionWatcher evolutionWatcher = new EvolutionWatcher();
		
		List<LivingCell> linvingCells = readLifeInputFile(lifeInputFile);

		int iterationCount = 0;

		livingCellService.printLivingCells(linvingCells, iterationCount);

		while (iterationCount < iteration) {
			iterationCount++;
			evolutionWatcher.startEvolution("Iteration N°" + iterationCount, linvingCells.size());
			linvingCells = livingCellService.calculateEvolution(linvingCells, evolutionWatcher);
			livingCellService.printLivingCells(linvingCells, iterationCount);
			evolutionWatcher.stopEvolution(linvingCells.size());
		}
		
		if (statisticsEnabled) {
			evolutionWatcher.prettyPrint();
		}

		return 0;
	}

	private List<LivingCell> readLifeInputFile(File lifeInputFile) throws FileNotFoundException, IOException {

		List<LivingCell> result = new ArrayList<LivingCell>();
		
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(lifeInputFile))) {
			
			int y = 0;
			String line = bufferedReader.readLine();

			while (line != null) {

				// Life file must contains only digit (for lifetime value) and space (living status)
				if (!line.matches("[0-9 ]*")){
					throw new IllegalArgumentException("Life file only have space and digit characters.");
				}

				// If character is a digit, the cell is living and the digit is the lifetime of the cell
				char[] lineCharacters = line.toCharArray();
				for (int x = 0; x < lineCharacters.length; x++) {
					if (Character.isDigit(lineCharacters[x])) {
						LivingCell livingCell = new LivingCell();
						livingCell.setX(x);
						livingCell.setY(y);
						livingCell.setLifetime(Integer.parseInt(String.valueOf(lineCharacters[x])));
						result.add(livingCell);
					}
				}

				line = bufferedReader.readLine();
				y++;
			}
		}

		return result;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}
}