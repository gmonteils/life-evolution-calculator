package fr.gouv.dgefp.model;

import fr.gouv.dgefp.Constants;

public class LivingCell {

	private int x;
	
	private int y;
	
	private int lifetime;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
	
	public String getCoordinates() {
		return x + Constants.COORDINATE_SEPARATOR + y;
		
	}
}
