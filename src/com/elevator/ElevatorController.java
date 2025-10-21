package com.elevator;

import java.util.*;

public class ElevatorController {
    private final List<Elevator> elevators;
    private final int minFloor;
    private final int maxFloor;
    private final Queue<Request> pendingRequests;

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
            }
        }

        return bestElevator;
    }

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

    public boolean allElevatorsIdle(){
         return elevators.stream().allMatch(Elevator::isIdle) && pendingRequests.isEmpty();
    }

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

    private boolean isValidFloor(int floor) {
        return floor >= minFloor && floor <= maxFloor;
    }

    public List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }

    public int getPendingRequestCount() {
        return pendingRequests.size();
    }


}
