package fr.gouv.dgefp.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.gouv.dgefp.Constants;
import fr.gouv.dgefp.model.LivingCell;
import fr.gouv.dgefp.utils.EvolutionWatcher;

@Component
public class LivingCellService {

	private final int maxLifetime;
	
	public LivingCellService(@Value("${app.max-lifetime:3}") int maxLifetime){
		this.maxLifetime = maxLifetime;
	}
	
	public List<LivingCell> calculateEvolution(List<LivingCell> sourceLivingCells, EvolutionWatcher evolutionWatcher) {

		List<LivingCell> result = new ArrayList<LivingCell>();
		
		// Here are the main steps for evolution calculation
		// 1 - list coordinates that may have a living cell after evolution
		// 2 - calculate living cells for all coordinates that were previously listed
		// 3 - move living cells for strictly positive coordinates
		// 4 - return living cells
		
		Set<String> toBeCalculatedCoordinates = new HashSet<String>();
		
		// 1 - list coordinates that may have a living cell
		for (LivingCell sourceLivingCell : sourceLivingCells) {
			toBeCalculatedCoordinates.add(sourceLivingCell.getCoordinates());
			toBeCalculatedCoordinates.addAll(computeAdjacentCellsCoordinates(sourceLivingCell.getX(), sourceLivingCell.getY()));
		}
		
		// Ajout d'une donnée statistique
		evolutionWatcher.setCalculatedCoordinatesCount(toBeCalculatedCoordinates.size());
		
		// 2 - compute living status for all coordinates that were previously listed
		
		// Transform living cells into Map for performance improvement
		Map<String, LivingCell> sourceLivingCellByCoordonateMap = sourceLivingCells.stream().collect(Collectors.toMap(LivingCell::getCoordinates, Function.identity()));
		
		// While calculating living cells, we have to keep track of negative coordinate,
		// in order to update the coordinate to keep strictly positive coordinate.
		boolean isNegativeXCoordinatePresent = false;
		boolean isNegativeYCoordinatePresent = false;
		
		for (String coordinates : toBeCalculatedCoordinates) {
			int x = Integer.valueOf(coordinates.split(Constants.COORDINATE_SEPARATOR)[0]);
			int y = Integer.valueOf(coordinates.split(Constants.COORDINATE_SEPARATOR)[1]);
			LivingCell livingCellAtCoordinates = calculateLivingCellAtCoordinates(sourceLivingCellByCoordonateMap, x, y);
			
			// if a coordinate is negative, keep track of the coordinate for next step (3)
			if (livingCellAtCoordinates != null) {
				result.add(livingCellAtCoordinates);
				isNegativeXCoordinatePresent = isNegativeXCoordinatePresent || livingCellAtCoordinates.getX() < 0;
				isNegativeYCoordinatePresent = isNegativeYCoordinatePresent || livingCellAtCoordinates.getY() < 0;
			}
		}
		
		// 3 - move living cells for strictly positive coordinates
		
		if (isNegativeXCoordinatePresent) {
			for (LivingCell livingCell : result) {
				livingCell.setX(livingCell.getX() + 1);
			}
		}
		
		if (isNegativeYCoordinatePresent) {
			for (LivingCell livingCell : result) {
				livingCell.setY(livingCell.getY() + 1);
			}
		}
		
		// 4 - return living cells
		
		return result.stream().collect(Collectors.toList());
	}

	public void printLivingCells(List<LivingCell> linvingCells, int i) {
		System.out.println("\n\n");
		System.out.println("Iteration N°" + i);
		System.out.println("\n\n");
		System.out.println(convertLivingCellsToString(linvingCells));
	}
	
	/**
	 * Method that compute the living status of the cell at input coordinates.
	 * @return a living cell with adjusted lifetime if the cell is (still) alive
	 * @return null if the cell is not alive
	 */
	private LivingCell calculateLivingCellAtCoordinates(Map<String, LivingCell> sourceLivingCellByCoordonateMap, int x, int y) {

		LivingCell sourceLivingCell = sourceLivingCellByCoordonateMap.get(x + Constants.COORDINATE_SEPARATOR + y);
		
		if (sourceLivingCell != null && sourceLivingCell.getLifetime() == maxLifetime) {
			return null;
		}
		
		List<LivingCell> adjacentSourceLivingCells = computeAdjacentCellsCoordinates(x, y).stream()
				.filter(sourceLivingCellByCoordonateMap::containsKey)
				.map(sourceLivingCellByCoordonateMap::get)
				.collect(Collectors.toList());
		
		// if the cell has 3 adjacent living cells, it lives
		if ((adjacentSourceLivingCells.size() >= 3)
		// if the cell has 2 adjacent living cells, and is already alive, it lives
		|| (sourceLivingCell != null && adjacentSourceLivingCells.size() >= 2)
		// if the cell has a adjacent living cells that reach the max lifetime, it lives
	    || (adjacentSourceLivingCells.stream().anyMatch(livingCell -> livingCell.getLifetime() == maxLifetime))) {
			
			LivingCell livingCell = new LivingCell();
			livingCell.setX(x);
			livingCell.setY(y);
			livingCell.setLifetime(sourceLivingCell != null ? sourceLivingCell.getLifetime() + 1 : 0);
				
			return livingCell;
		}
		
		return null;
	}

	private Collection<? extends String> computeAdjacentCellsCoordinates(int x, int y) {
		
		Set<String> result = new HashSet<String>();
		
		result.add((x-1) + Constants.COORDINATE_SEPARATOR + (y-1));
		result.add((x-1) + Constants.COORDINATE_SEPARATOR + (y));
		result.add((x-1) + Constants.COORDINATE_SEPARATOR + (y+1));
		result.add((x) + Constants.COORDINATE_SEPARATOR + (y-1));
		result.add((x) + Constants.COORDINATE_SEPARATOR + (y+1));
		result.add((x+1) + Constants.COORDINATE_SEPARATOR + (y-1));
		result.add((x+1) + Constants.COORDINATE_SEPARATOR + (y));
		result.add((x+1) + Constants.COORDINATE_SEPARATOR + (y+1));
		
		return result;
	}
	
	private String convertLivingCellsToString(List<LivingCell> livingCells) {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		Collections.sort(livingCells, Comparator.comparingInt(LivingCell::getY)
		        .thenComparingInt(LivingCell::getX));
		
		int currentX = 0;
		int currentY = 0;
		
		for (LivingCell livingCell : livingCells) {
			
			// Check if we are on the right line,
			int offsetY = livingCell.getY() - currentY;
			// if not go to the line as many times as needed, and start at the beginning of the new line
			for (int i = 0; i < offsetY; i++) {
				stringBuilder.append("\n");
				currentY++;
				currentX=0;
			}
			
			// Check if we are on the right character (column),
			int offsetX = livingCell.getX() - currentX;
			// if not add space character as many times as needed
			for (int j = 0; j < offsetX; j++) {
				stringBuilder.append(" ");
				currentX++;
			}
			
			// write the cell lifetime
			stringBuilder.append(livingCell.getLifetime());
			currentX++;
		}
		
		return stringBuilder.toString();
	}
}
