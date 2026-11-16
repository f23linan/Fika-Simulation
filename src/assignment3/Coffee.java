package assignment3;

import java.util.Random;

public class Coffee {

	private String drinkName;
	private int drinkEnergy;

	// Initializing the coffee with a name (type) and a random energy value
	public Coffee(String drinkName, int minEnergy, int maxEnergy) {
		this.drinkName = drinkName;
		this.drinkEnergy = randomEnergyGenerator(minEnergy, maxEnergy);
	}

	// ((max - min) + 1) + min calculates the difference between the values, and
	// adds 1 so it can reach the actual max value (otherwise stops at the value
	// before). The final +min makes it start counting from the minimum value
	// instead of 0.
	private int randomEnergyGenerator(int min, int max) {
		Random randomEnergy = new Random();
		return randomEnergy.nextInt((max - min) + 1) + min;
	}

	// Getter to retrieve the name of the coffee 
	public String getName() {
		return drinkName;
	}

	// Getter to retrieve the energy of the coffee
	public int getEnergy() {
		return drinkEnergy;
	}
}
