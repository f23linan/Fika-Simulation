package assignment3;

public class BlackCoffee extends Coffee {

	// Interval-variables for the energy of the coffee type
	private static final int minimumEnergy = 15;
	private static final int maximumEnergy = 20;

	public BlackCoffee() {
		// Calls the superclass Coffee with these specific parameters
		super("Black Coffee", minimumEnergy, maximumEnergy);
	}

}
