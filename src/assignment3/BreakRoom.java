package assignment3;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BreakRoom implements Runnable {

	// Queue to hold the workers in the coffee line
	private ConcurrentLinkedQueue<Worker> coffeeLine = new ConcurrentLinkedQueue<>();

	private CoffeeMachine coffeeMachine;

	// Object used for synchronization
	private final Object coffeeLock = new Object();

	public BreakRoom() {
		this.setCoffeeLine(coffeeLine);
	}

	// Setting up the coffeeMachine
	public void setCoffeeMachine(CoffeeMachine coffeeMachine) {
		this.coffeeMachine = coffeeMachine;
	}

	// Method for adding workers to the coffee line
	public void addWorkerToLine(Worker worker) {
		synchronized (coffeeLock) {
			getCoffeeLine().add(worker); // Adds the worker to the line
			coffeeLock.notifyAll(); // Notify all waiting threads that a worker has joined the line
			// System.out.println(worker.getWorkerNames() + " joins the coffee line");
		}
	}

	// Thread for the BreakRoom's activities
	@Override
	public void run() {

		// Loop for continuing to serve coffee as long as the simulation is running
		while (Fika.simulationRunning()) {
			synchronized (coffeeLock) {

				// If the line is empty, waiting for workers to join
				while (getCoffeeLine().isEmpty()) {
					try {
						coffeeLock.wait();
					} catch (InterruptedException e) {
						// Handling thread-interruption
						Thread.currentThread().interrupt();
						System.out.println("Breakroom interrupted");
						return;
					}
				}

				// If the simulation is not running, exit the loop
				if (!Fika.simulationRunning())
					return;

				// Serves coffee to the worker in the front of the line, FIFO
				Worker worker = getCoffeeLine().poll();

				// Get coffee from the coffee machine
				Coffee coffee = coffeeMachine.getCoffee();

				// If the coffee machine contains coffee, it serves it to the worker
				if (coffee != null) {
					worker.consumeCoffee(coffee);

					// Notify worker that their energy is increased
					synchronized (worker) {
						worker.notify();
					}

					System.out.println(worker.getWorkerNames() + " enjoyed a " + coffee.getName() + " with "
							+ coffee.getEnergy() + " energy");
				}

				// Simulating a 1 second delay for serving workers
				try {
					synchronized (coffeeMachine) {
						while (coffeeMachine.hasCoffee()) {
							coffeeMachine.wait(1000);
						}
					}
				} catch (InterruptedException e) {
					// Handles thread-interruption
					Thread.currentThread().interrupt();
					System.out.println("BreakRoom thread interrupted.");
					return; // Exits method if interrupted
				}
			}
		}
	}

	public ConcurrentLinkedQueue<Worker> getCoffeeLine() {
		return coffeeLine;
	}

	public void setCoffeeLine(ConcurrentLinkedQueue<Worker> coffeeLine) {
		this.coffeeLine = coffeeLine;
	}
}
