package com.teamcos.modularsystems.notification;

public enum GuiColor {
	BLACK(0),
	BLUE(1),
	GREEN(2),
	CYAN(3),
	RED(4),
	PURPLE(5),
	ORANGE(6),
	LIGHTGRAY(7),
	GRAY(8),
	LIGHTBLUE(9),
	LIME(10),
	TURQUISE(11),
	PINK(12),
	MAGENTA(13),
	YELLOW(14),
	WHITE(15);
	
	private int number;
	GuiColor(int number) {
		this.number = number;
	}
	
	@Override
	public String toString() {
		return "\u00a7" + Integer.toHexString(number);
	}
	
}
