package assignment3;

public class Cappuccino extends Coffee {

	// Interval-variables for the energy of the coffee type
	private static final int minimumEnergy = 20;
	private static final int maximumEnergy = 30;

	public Cappuccino() {
		// Calls the superclass Coffee with these specific parameters
		super("Cappuccino", minimumEnergy, maximumEnergy);
	}

}
