package fr.asys.tech;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fr.asys.tech.command.LifeEvolutionCalculateCommand;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication
public class LifeEvolutionCalculator implements CommandLineRunner, ExitCodeGenerator {

	private final IFactory factory;

	private final LifeEvolutionCalculateCommand lifeEvolutionCalculateCommand;

	private int exitCode;

	@Autowired
	public LifeEvolutionCalculator(IFactory factory, 
			LifeEvolutionCalculateCommand lifeEvolutionCalculateCommand) {
		this.factory = factory;
		this.lifeEvolutionCalculateCommand = lifeEvolutionCalculateCommand;
	}

	@Override
	public void run(String... args) throws IOException {
		exitCode = new CommandLine(lifeEvolutionCalculateCommand, factory).execute(args);
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(LifeEvolutionCalculator.class, args)));
	}
}