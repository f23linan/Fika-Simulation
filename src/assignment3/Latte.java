package assignment3;

public class Latte extends Coffee {

	// Interval-variables for the energy of the coffee type
	private static final int minimumEnergy = 25;
	private static final int maximumEnergy = 35;

	public Latte() {
		// Calls the superclass Coffee with these specific parameters
		super("Latte", minimumEnergy, maximumEnergy);
	}

}
