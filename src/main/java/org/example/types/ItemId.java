package org.example.types;

public enum ItemId {
	DICE, GEM, KEY, PUMPKIN, ROCK, SHINY, SKULL, SWORD, EGG, ROSE, TULIP, CHERRY_BLOSSOM;

	public String key() {
		return name().toLowerCase();
	}
}
