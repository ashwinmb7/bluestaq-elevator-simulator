package com.elevator;

/**
 * Main class to run the Elevator System simulation.
 * This class initializes the ElevatorController and simulates various elevator requests.
 * @author Ashwin Mahesh
 */
public class ElevatorSystem {
    
    public static void main(String[] args) {
        System.out.println("Starting Elevator Simulation...\n");

        // Configuration
        int numElevators = 3;
        int minFloor = 1;
        int maxFloor = 10;
        int elevatorCapacity = 8;

        // Create controller
        ElevatorController controller = new ElevatorController(
            numElevators, minFloor, maxFloor, elevatorCapacity
        );

        // Simulate various requests
        runSimulation(controller);
    }

    /**
     * Run a simulation with multiple elevator requests
     */
    private static void runSimulation(ElevatorController controller) {
        System.out.println("\n--- Starting Simulation Scenario ---\n");

        // Create various requests
        controller.requestElevator(1, 7);   // Ground to 7th floor
        controller.requestElevator(3, 8);   // 3rd to 8th floor
        controller.requestElevator(5, 1);   // 5th to ground
        controller.requestElevator(10, 2);  // 10th to 2nd floor
        controller.requestElevator(2, 9);   // 2nd to 9th floor
        controller.requestElevator(6, 4);   // 6th to 4th floor

        controller.printStatus();

        // Run simulation steps
        int step = 0;
        int maxSteps = 50;

        while (!controller.allElevatorsIdle() && step < maxSteps) {
            step++;
            System.out.printf("\n--- Step %d ---%n", step);
            
            controller.step();
            
            
            if (step == 5) {
                System.out.println("\n[New request added during operation]");
                controller.requestElevator(4, 9);
            }
            
            if (step == 10) {
                System.out.println("\n[New request added during operation]");
                controller.requestElevator(8, 1);
            }

            
            if (step % 5 == 0) {
                controller.printStatus();
            }

            
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("\n\n=== SIMULATION COMPLETE ===");
        controller.printStatus();
        System.out.printf("Total steps: %d%n", step);
        
        if (controller.allElevatorsIdle()) {
            System.out.println("All requests completed successfully!");
        } else {
            System.out.println("Simulation ended with pending work (max steps reached)");
        }
    }
}