package fr.asys.tech.repository;

import org.springframework.stereotype.Component;

@Component
public class ConsoleLivingCellOutputWriter implements LivingCellOutputWriter {

	@Override
	public void print(String multilineLivingCellString, int iteration) {
		System.out.println("\n\n");
		System.out.println("Iteration N°" + iteration);
		System.out.println("\n\n");
		System.out.println(multilineLivingCellString);
	}
}
