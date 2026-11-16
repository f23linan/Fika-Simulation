package assignment3;

import java.util.Random;

public class Worker implements Runnable {

	// Worker attributes
	private String name;
	private int startingEnergy;
	private int energyDecrease;

	// Interval-variables for the starting energy of a worker
	private static final int minimumStartingEnergy = 30;
	private static final int maximumStartingEnergy = 90;

	// Interval-variables for the decrease of energy over time
	private static final int minimumEnergyDecreaseInterval = 500;
	private static final int maximumEnergyDecreaseInterval = 1500;

	// Object used for synchronization
	private final Object waitingDelay = new Object();

	private BreakRoom breakRoom;

	// Initializing a worker
	public Worker(String name, BreakRoom breakRoom) {
		this.name = name;
		this.breakRoom = breakRoom;
		this.startingEnergy = startingEnergy(); // Starting energy for this specific worker
		this.energyDecrease = energyDecreaseInterval(); // Energy decrease for this specific worker
	}

	// Randomly generate a starting energy within the defined interval-range
	private int startingEnergy() {
		Random randomStartingEnergy = new Random();
		return randomStartingEnergy.nextInt((maximumStartingEnergy - minimumStartingEnergy) + 1)
				+ minimumStartingEnergy;
	}

	// Randomly generate an energy decrease within the defined interval-range
	private int energyDecreaseInterval() {
		Random randomEnergyDecreaseInterval = new Random();
		return randomEnergyDecreaseInterval.nextInt((maximumEnergyDecreaseInterval - minimumEnergyDecreaseInterval) + 1)
				+ minimumEnergyDecreaseInterval;
	}

	// Getter for a worker's name
	public String getWorkerNames() {
		return name;
	}

	// Getter for a worker's starting energy
	public int getStartingEnergy() {
		return startingEnergy;
	}

	// Getter for a worker's energy decrease-interval
	public int getEnergyDecreaseInterval() {
		return energyDecrease;
	}

	// Method to consume a drink and increase the worker's energy, without going
	// above 100
	public void consumeCoffee(Coffee coffee) {
		this.startingEnergy += coffee.getEnergy();
		if (this.startingEnergy > 100) {
			this.startingEnergy = 100;
		}
	}

	// Thread-handling for a worker's activities
	@Override
	public void run() {

		// Loop: a worker works until their energy runs out, or the simulation ends
		while (startingEnergy > 0 && Fika.simulationRunning()) {
			synchronized (waitingDelay) {
				try {

					// Waits for the decrease-interval before energy decreases by 1 point
					waitingDelay.wait((long) (energyDecrease / Fika.getSpeed()));
					startingEnergy--;
					System.out.println(name + " is working with energy level: " + startingEnergy);

					// A worker with energy below 30 joins the coffee line
					if (startingEnergy < 30 && !breakRoom.getCoffeeLine().contains(this)) {
						breakRoom.addWorkerToLine(this);
						System.out.println(name + " is taking a break with energy level " + startingEnergy);


						while (startingEnergy < 100) {

							// If the worker is not in line, they rejoin it
							if (!breakRoom.getCoffeeLine().contains(this)) {
								breakRoom.addWorkerToLine(this);
							}

							// Worker waits until they are notified after coffee or energy increase
							synchronized (this) {
								wait();
							}

							// If the worker's energy hits 100, they exit the loop and go back to work
							if (startingEnergy >= 100) {
								break;
							}
						}

						// Output that worker goes back to work
						if (startingEnergy == 100) {
							System.out.println(name + " goes back to work with energy " + startingEnergy);
						}

					}
				} catch (InterruptedException e) {
					// When thread is interrupted, the interruption is handled
					Thread.currentThread().interrupt();
					System.out.println(name + " was interrupted.");
					return; // Exits the run() method and stops the worker
				}
			}
		}
	}
}
