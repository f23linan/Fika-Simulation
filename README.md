# Java Fika Simulation

## Description
A workplace simulation developed in Java using multithreading. The program simulates employees working, losing energy and taking coffee breaks (fika) to restore their energy using a shared coffee machine.

## Features
- **Multithreading:** Each employee and the coffee machine operate on separate threads.
- **Energy Management:** Employees gradually lose energy while working and waiting for coffee.
- **Coffee Breaks:** Employees enter a coffee queue when their energy drops below 30.
- **Coffee Machine:** Produces and serves three types of drinks with different energy values.
- **FIFO Queue:** Employees are served in the order they enter the queue.
- **Thread Synchronization:** Manages shared resources and interactions between concurrent threads.
- **Simulation Settings:** Supports adjusting the number of employees, simulation duration and speed.

## Coffee Types
- **Black Coffee:** Restores 15 to 20 energy points.
- **Cappuccino:** Restores 20 to 30 energy points.
- **Latte:** Restores 25 to 35 energy points.

## Technologies
- Java
- Object-Oriented Programming
- Multithreading
- Thread Synchronization
- Concurrent Programming
