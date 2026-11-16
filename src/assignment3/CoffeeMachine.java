package assignment3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class CoffeeMachine implements Runnable {

	// Maximum amount of coffee that can be stored in the reserve
	private int maximumCoffeeStored = 20;

	// Object used for synchronization
	private final Object coffeeCreatingDelay = new Object();

	// Queue to hold the coffee in the reserve
	private Queue<Coffee> coffeeReserve = new LinkedList<>();

	// Array of the available types of coffee
	private Coffee[] coffeeTypes;

	// Initializes the coffee machine and defines the available coffee types
	public CoffeeMachine() {
		coffeeTypes = new Coffee[] { new BlackCoffee(), new Cappuccino(), new Latte() };
	}

	// Method for serving coffee to a worker
	public Coffee getCoffee() {
		synchronized (coffeeCreatingDelay) {

			// If the coffee reserve is empty: wait until coffee is created
			while (coffeeReserve.isEmpty()) {
				try {
					System.out.println("Machine empty please wait.....");
					coffeeCreatingDelay.wait(); // Waiting for coffee to be made and added to reserve
				} catch (InterruptedException e) {
					// Handles thread interruption
					Thread.currentThread().interrupt();
					System.out.println("Coffeemachine interrupted");
					return null; // Returns null if the coffee machine is interrupted
				}
			}

			// If there is coffee available, first coffee in the reserve is served to the
			// worker
			Coffee coffee = coffeeReserve.poll();
			//System.out.println("Serving: " + coffee.getName() + " | Remaining: " + coffeeReserve.size());
			coffeeCreatingDelay.notifyAll(); // Notify all waiting threads that coffee is available
			return coffee; // Returns the served coffee
		}
	}
	
	public boolean hasCoffee() {
		synchronized (coffeeCreatingDelay) {
			return !coffeeReserve.isEmpty();
		}
	}

	// Thread for the coffee machine's activities
	@Override
	public void run() {

		// As long as the simulation is running, coffee keeps being created and served
		while (Fika.simulationRunning()) {
			synchronized (coffeeCreatingDelay) {
				try {

					// Wait until there is space available if the coffee storage is full
					while (coffeeReserve.size() >= maximumCoffeeStored) {
						System.out.println("Coffee storage is full. Waiting..."); // For debugging
						coffeeCreatingDelay.wait();
					}

					// Simulates a delay for creating coffee, and can be affected by the simulation
					// speed
					coffeeCreatingDelay.wait((long) (2000 / Fika.getSpeed()));

					// If the simulation ends, exit the method
					if (!Fika.simulationRunning())
						return;

					// Randomly picks a coffee type to create
					Coffee newCoffee = coffeeTypes[new Random().nextInt(coffeeTypes.length)];
					coffeeReserve.add(newCoffee); // Adds the coffee type to the reserve

					System.out.println("Drink created. Coffee Machine has " + coffeeReserve.size() + " drinks in reserve."); 
					
					coffeeCreatingDelay.notifyAll(); // Notify all waiting threads that a coffee is available
				} catch (InterruptedException e) {
					// Handles thread interruption
					Thread.currentThread().interrupt();
					System.out.println("Coffee machine was interrupted."); // Unsure if this is important or??
					return; // Exit method if interrupted
				}
			}
		}

		// Message for when the simulation ends
		System.out.println("Coffee machine shutting down.");

	}
}
