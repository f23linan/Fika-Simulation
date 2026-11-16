package assignment3;

public class Fika {

	private static final int simulationDuration = 20000; // Sets duration of simulation to 20 seconds
	private static final double speed = 1.0; // Speed of simulation can be doubled by inputting 2.0
	private static volatile boolean simulationRunning = true; // Sets the simulation to running from start

	private Thread[] workerThreads; // To be able to interrupt worker threads by iterating through them

	public static void main(String[] args) {
		Fika fika = new Fika();
		fika.startSimulation();
	}

	public void startSimulation() {
		String[] workerNames = { "Lina", "Wilma", "Ebba", "Hampus" }; // More workers can be added to the array manually

		workerThreads = new Thread[workerNames.length]; // For creating one thread/workers name

		// Handling the thread for the breakroom
		BreakRoom breakRoom = new BreakRoom();
		Thread threadBreakRoom = new Thread(breakRoom);
		threadBreakRoom.start();

		// Handling thread for the coffeemachine
		CoffeeMachine coffeeMachine = new CoffeeMachine();
		breakRoom.setCoffeeMachine(coffeeMachine);
		Thread threadCoffeeMachine = new Thread(coffeeMachine);
		threadCoffeeMachine.start();

		// Iterating through all workers names to create a thread per worker
		for (int i = 0; i < workerNames.length; i++) {
			Worker worker = new Worker(workerNames[i], breakRoom);
			workerThreads[i] = new Thread(worker);
			workerThreads[i].start();
		}

		// For stopping simulation according to simulationDuration
		try {
			Thread.sleep(simulationDuration); // Threads sleep after the duration has passed
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.out.println("Simulation was interrupted.");
		}

		simulationRunning = false; // Changes the simulationRunning to false
		
		// Interrupts all threads that are active
		threadBreakRoom.interrupt();
		threadCoffeeMachine.interrupt();
		// Iterates through the workers threads and interrupts them in the for-loop
		for (Thread workerThread : workerThreads) {
			workerThread.interrupt();
		}

		System.out.println("\n---SIMULATION FINISHED---"); 
	}

	// For passing the speed-variable to other classes
	public static double getSpeed() {
		return speed;
	}

	// For passing the simulationrunning-variable to other classes
	public static boolean simulationRunning() {
		return simulationRunning;
	}
}
