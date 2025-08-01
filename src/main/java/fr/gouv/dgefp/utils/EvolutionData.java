package fr.gouv.dgefp.utils;

class EvolutionData {
	
	private String evolutionName;
	
	private int sourceCellCount = 0;
	
	private Integer cellCount = null;
	
	private Integer calculatedCoordinatesCount = null;
	
	private long startTimestamp = 0;
	
	private Long stopTimestamp = null;

	public String getEvolutionName() {
		return evolutionName;
	}

	public void setEvolutionName(String evolutionName) {
		this.evolutionName = evolutionName;
	}

	public int getSourceCellCount() {
		return sourceCellCount;
	}

	public void setSourceCellCount(int sourceCellCount) {
		this.sourceCellCount = sourceCellCount;
	}

	public Integer getCellCount() {
		return cellCount;
	}

	public void setCellCount(Integer cellCount) {
		this.cellCount = cellCount;
	}

	public Integer getCalculatedCoordinatesCount() {
		return calculatedCoordinatesCount;
	}

	public void setCalculatedCoordinatesCount(Integer calculatedCoordinatesCount) {
		this.calculatedCoordinatesCount = calculatedCoordinatesCount;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Long getStopTimestamp() {
		return stopTimestamp;
	}

	public void setStopTimestamp(Long stopTimestamp) {
		this.stopTimestamp = stopTimestamp;
	}
}
