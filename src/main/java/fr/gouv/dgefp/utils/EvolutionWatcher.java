package fr.gouv.dgefp.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class EvolutionWatcher {

	private List<EvolutionData> evolutionDatas = new ArrayList<EvolutionData>();

	private EvolutionData currentEvolutionData = null;

	int defaultEvolutionNameIndex = 0;

	public void startEvolution(String evolutionName, int sourceCellCount) {

		if (currentEvolutionData != null) {
			currentEvolutionData.setStopTimestamp(System.currentTimeMillis());
		}

		defaultEvolutionNameIndex++;
		EvolutionData evolutionData = new EvolutionData();
		evolutionData.setEvolutionName(StringUtils.isBlank(evolutionName) ? "Iteration N°" + defaultEvolutionNameIndex : evolutionName);
		evolutionData.setSourceCellCount(sourceCellCount);
		evolutionData.setStartTimestamp(System.currentTimeMillis());

		evolutionDatas.add(evolutionData);
		currentEvolutionData = evolutionData;
	}

	public void stopEvolution(int cellCount) {
		if (currentEvolutionData != null) {
			currentEvolutionData.setCellCount(cellCount);
			currentEvolutionData.setStopTimestamp(System.currentTimeMillis());
			currentEvolutionData = null;
		}
	}

	public void setCalculatedCoordinatesCount(int calculatedCoordinatesCount) {
		if (currentEvolutionData != null) {
			currentEvolutionData.setCalculatedCoordinatesCount(calculatedCoordinatesCount);
		}
	}

	public void prettyPrint() {

		System.out.println("\n\n");
		for (EvolutionData evolutionData : evolutionDatas) {
			if (evolutionData.getCellCount() != null
					&& evolutionData.getStopTimestamp() != null) {
				System.out.println("--------------------------------------------------------------");
				System.out.println("Statistics for " + evolutionData.getEvolutionName());
				System.out.println("--------------------------------------------------------------");
				System.out.println("Living cells count goes from " + evolutionData.getSourceCellCount() + " to " + evolutionData.getCellCount());
				System.out.println("Cell evolution calculation lasted " + (evolutionData.getStopTimestamp() - evolutionData.getStartTimestamp()) + "ms.");
				if (evolutionData.getCalculatedCoordinatesCount() != null) {
					System.out.println("Cell evolution calculation compute living status for " + evolutionData.getCalculatedCoordinatesCount() + " coordinates.");
				}
			}
		}
	}
}
