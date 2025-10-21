package com.elevator;

import java.util.*;


/**
 * Class representing the Elevator Controller.
 * This class manages multiple elevators, processes incoming requests,
 * and assigns requests to elevators based on their current state and load.
 * @author Ashwin Mahesh
 */
public class ElevatorController {
    /** List of elevators managed by this controller */
    private final List<Elevator> elevators;
    /** Minimum floor number in the building */
    private final int minFloor;
    /** Maximum floor number in the building */
    private final int maxFloor;
    /** Queue of pending requests that could not be assigned immediately */
    private final Queue<Request> pendingRequests;

    /**
     * Constructor to initialize the ElevatorController with a specified number of elevators.
     * @param numElevators the number of elevators to manage
     * @param minFloor the minimum floor number in the building
     * @param maxFloor the maximum floor number in the building
     * @param elevatorCapacity the maximum passenger capacity for each elevator
     */
    public ElevatorController(int numElevators, int minFloor, int maxFloor, int elevatorCapacity){
        this.elevators = new ArrayList<>();
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.pendingRequests = new LinkedList<>();

        for(int i = 0; i < numElevators; i++){
            elevators.add(new Elevator(i + 1, elevatorCapacity, minFloor));
        }

        System.out.printf("Initialized ElevatorController with %d elevators%n", numElevators);
    }

    /**
     * Processes a new elevator request from a user.
     * @param fromFloor the floor where the request originates
     * @param toFloor the desired destination floor
     */
    public void requestElevator(int fromFloor, int toFloor) {
        if (!isValidFloor(fromFloor) || !isValidFloor(toFloor)) {
            System.err.printf("Invalid floor request: %d to %d%n", fromFloor, toFloor);
            return;
        }

        if (fromFloor == toFloor) {
            System.err.println("Cannot request elevator to same floor");
            return;
        }

        Request request = new Request(fromFloor, toFloor);
        System.out.printf("New request: Floor %d -> %d%n", fromFloor, toFloor);
        
        
        Elevator bestElevator = findBestElevator(request);
        
        if (bestElevator != null) {
            bestElevator.addRequest(request);
            System.out.printf("Assigned to Elevator %d%n", bestElevator.getId());
        } else {
            pendingRequests.offer(request);
            System.out.println("Request queued - all elevators busy");
        }
    }

    /**
     * Finds the best elevator to handle the given request based on cost and load.
     * @param request the elevator request to be assigned
     * @return the best Elevator instance to handle the request, or null if none available
     */
    private Elevator findBestElevator(Request request){
        Elevator bestElevator = null;
        int lowestCost = Integer.MAX_VALUE;

        for(Elevator elevator : elevators){
            if(!elevator.hasCapacity()){
                continue;
            }

            int cost = elevator.calculateCost(request);
        
            if(cost < lowestCost){
                lowestCost = cost;
                bestElevator = elevator;
            } else if(cost == lowestCost && bestElevator != null){
                if(elevator.getDestinationCount() < bestElevator.getDestinationCount()){
                    bestElevator = elevator;
                }
            }
        }

        return bestElevator;
    }

    /**
     * Advances the state of all elevators by one time step.
     * Processes pending requests and updates each elevator's position and state.
     */
    public void step(){
        processPendingRequests();

        for (Elevator elevator : elevators) {
            if (elevator.shouldStopCurrentFloor()) {
                elevator.openDoors();
                try {
                    Thread.sleep(500); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                elevator.closeDoors();
            }

            
            elevator.updateDirection();

            
            elevator.move();
        }

    }

    /**
     * Processes pending requests and attempts to assign them to available elevators.
     */
    private void processPendingRequests(){
        Queue<Request> remainingRequests = new LinkedList<>();

        while(!pendingRequests.isEmpty()){
            Request request = pendingRequests.poll();
            Elevator bestElevator = findBestElevator(request);

            if(bestElevator != null){
                bestElevator.addRequest(request);
                System.out.printf("Assigned queued request to Elevator %d%n", bestElevator.getId());
            }
            else{
                remainingRequests.offer(request);
            }
        }
    }

    /**
     * Checks if all elevators are idle (no destinations and IDLE direction).
     * @return true if all elevators are idle, false otherwise.
     */
    public boolean allElevatorsIdle(){
         return elevators.stream().allMatch(Elevator::isIdle) && pendingRequests.isEmpty();
    }

    /**
     * Prints the current status of all elevators and pending requests.
     */
    public void printStatus() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ELEVATOR SYSTEM STATUS");
        System.out.println("=".repeat(60));
        
        for (Elevator elevator : elevators) {
            System.out.println(elevator);
        }
        
        if (!pendingRequests.isEmpty()) {
            System.out.printf("Pending requests: %d%n", pendingRequests.size());
        }
        
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Validates if the given floor number is within the building's floor range.
     * @param floor the floor number to validate
     * @return true if the floor is valid, false otherwise
     */
    private boolean isValidFloor(int floor) {
        return floor >= minFloor && floor <= maxFloor;
    }

    /**
     * Gets the list of elevators managed by this controller.
     * @return list of elevators
     */
    public List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }

    /**
     * Gets the count of pending requests.
     * @return the number of pending requests
     */
    public int getPendingRequestCount() {
        return pendingRequests.size();
    }


}
