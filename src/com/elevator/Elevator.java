package com.elevator;
import java.util.*;

/**
 * Class representing an Elevator in the system.
 * This class manages the state and behavior of an individual elevator, including
 * its current position, movement direction, passenger load, and destination queue.
 * It implements a basic elevator scheduling algorithm that processes requests and
 * moves between floors efficiently.
 * @author Ashwin Mahesh
 */
public class Elevator {
    /** Unique identifier for this elevator */
    private final int id;
     /** The current floor position of the elevator */
    private int currentFloor;
    /** The current movement direction of the elevator */
    private Direction direction;
    /** Maximum passenger capacity of the elevator */
    private final int capacity;
    /** Current number of passengers in the elevator */
    private int currentLoad;
    /** Set of destination floors the elevator needs to visit */
    private final Set<Integer> destinationFloors;
    /** Queue of requests assigned to this elevator */
    private final Queue<Request> requestQueue;
    /** Flag indicating whether the elevator doors are open */
    private boolean doorsOpen;


    /**
     * Constructor to initialize an Elevator instance.
     * @param id the unique identifier for the elevator
     * @param capacity the maximum passenger capacity of the elevator
     * @param startFloor the floor where the elevator starts
     */
    public Elevator(int id, int capacity, int startFloor) {
        this.id = id;
        this.currentFloor = startFloor;
        this.direction = Direction.IDLE;
        this.capacity = capacity;
        this.currentLoad = 0;
        this.destinationFloors = new TreeSet<>();
        this.requestQueue = new LinkedList<>();
        this.doorsOpen = false;
    }

    /**
     * Moves the elevator one floor in its current direction.
     * If the elevator is IDLE, it does not move, else it increments or decrements
     * the current floor based on the direction.
     */
    public void move(){

        if(direction == Direction.IDLE){
            return;
        }

        if(direction == Direction.UP){
            currentFloor++;
        }

        else if(direction == Direction.DOWN){
            currentFloor--;
        }

        System.out.printf("Elevator %d moved to floor %d%n", id, currentFloor);
    }
    
    /**
     * Adds a destination floor to the elevator's set of destinations.
     * @param floor the floor to add as a destination
     */
    public void addDestination(int floor) {
        destinationFloors.add(floor);
    }

    /**
     * Determines if the elevator should stop at the current floor.
     * @return true if the current floor is a destination, false otherwise.
     */
    public boolean shouldStopCurrentFloor() {
        return destinationFloors.contains(currentFloor);
    }

    /**
     * Opens the elevator doors at the current floor.
     * Unloads passengers destined for this floor and loads new passengers
     * from the request queue if there is capacity.
     */
    public void openDoors(){
        doorsOpen = true;
        System.out.printf("Elevator %d doors opened at floor %d%n", id, currentFloor);

        destinationFloors.remove(currentFloor);

        if(currentFloor > 0){
            int unloading = Math.min(currentLoad, 2);
            currentLoad -= unloading;
            System.out.printf("Elevator %d unloaded %d passengers at floor %d%n", id, unloading, currentFloor);

        }

        if (currentLoad < capacity && !requestQueue.isEmpty()) {
            int loading = Math.min(capacity - currentLoad, 2);
            currentLoad += loading;
            System.out.printf("Elevator %d: %d passengers entered%n", id, loading);
        }
    }

    /**
     * Closes the elevator doors.
     */
    public void closeDoors(){
        doorsOpen = false;
        System.out.printf("Elevator %d doors closed at floor %d%n", id, currentFloor);
    }

    /**
     * Updates the elevator's direction based on its next destination.
     * If there are no destinations, the elevator becomes IDLE.
     */
    public void updateDirection(){
        if(destinationFloors.isEmpty()){
            direction = Direction.IDLE;
            return;
        }

        int nextFloor = getNextDestination();

        if(nextFloor > currentFloor){
            direction = Direction.UP;
        }

        else if(nextFloor < currentFloor){
            direction = Direction.DOWN;
        }

        else{
            direction = Direction.IDLE;
        }
    }

    /**
     * Gets the next destination floor based on the current direction.
     * @return the next floor to visit
     */
    private int getNextDestination(){
        if(destinationFloors.isEmpty()){
            return currentFloor;
        }

        if(direction == Direction.UP || direction == Direction.IDLE){
            for(int floor : destinationFloors){
                if(floor >= currentFloor){
                    return floor;
                }
            }
        }

        if(direction == Direction.DOWN || direction == Direction.IDLE){
            int lastFloor = currentFloor;

            for(int floor : destinationFloors){
                if(floor <= currentFloor){
                    lastFloor = floor;
                }
            }

            if(lastFloor != currentFloor){
                return lastFloor;
            }
        }

        return destinationFloors.iterator().next();
    }

    /**
     * Adds a request to the elevator's queue and updates destinations.
     * @param request the request to add
     */
    public void addRequest(Request request){
        requestQueue.offer(request);
        addDestination(request.getFromFloor());
        addDestination(request.getToFloor());
    }

    /**
     * Checks if the elevator has capacity for more passengers.
     * @return true if there is capacity, false otherwise.
     */
    public boolean hasCapacity(){
        return currentLoad < capacity;
    }

    /**
     * Calculates the cost of servicing a request based on current state.
     * @param request the request to evaluate
     * @return the calculated cost
     */
    public int calculateCost(Request request){
        int distance = Math.abs(currentFloor - request.getFromFloor());

        
        if (direction == Direction.IDLE) {
            return distance;
        }

        // If moving in same direction and request is on the way
        if (direction == request.getDirection() && 
            ((direction == Direction.UP && request.getFromFloor() >= currentFloor) ||
             (direction == Direction.DOWN && request.getFromFloor() <= currentFloor))) {
            return distance;
        }

        // Otherwise, elevator needs to complete current direction first
        return distance + 10;
    }

    /**
     * Gets the unique identifier of the elevator.
     * @return the elevator ID
     */
    public int getId(){
        return id;
    }

    /**
     * Gets the current floor of the elevator.
     * @return the current floor
     */
    public int getCurrentFloor(){
        return currentFloor;
    }

    /**
     * Gets the current direction of the elevator.
     * @return the current direction
     */
    public Direction getDirection(){
        return direction;
    }

    /**
     * Gets the current load of the elevator.
     * @return the current load
     */
    public int getCurrentLoad(){
        return currentLoad;
    }

    /**
     * Gets the capacity of the elevator.
     * @return the capacity
     */
    public int getCapacity(){
        return capacity;
    }

    /**
     * Checks if the elevator is idle (no destinations and IDLE direction).
     * @return  true if idle, false otherwise.
     */
    public boolean isIdle(){
        return direction == Direction.IDLE && destinationFloors.isEmpty();
    }
    
    /**
     * Checks if the elevator has any destination floors.
     * @return true if there are destination floors, false otherwise.
     */
    public boolean hasDestination(){
        return !destinationFloors.isEmpty();
    }

    /**
     * Gets the count of destination floors.
     * @return the number of destination floors
     */
    public int getDestinationCount(){
        return destinationFloors.size();
    }

    /**
     * Returns a string representation of the elevator's state.
     * @return string representation of the elevator
     */
    @Override
    public String toString(){
        return "Elevator{" +
                "id=" + id +
                ", currentFloor=" + currentFloor +
                ", direction=" + direction +
                ", capacity=" + capacity +
                ", currentLoad=" + currentLoad +
                ", destinationFloors=" + destinationFloors +
                ", doorsOpen=" + doorsOpen +
                '}';
    }


}
