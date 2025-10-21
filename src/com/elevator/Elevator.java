package com.elevator;
import java.util.*;

public class Elevator {
    private final int id;
    private int currentFloor;
    private Direction direction;
    private final int capacity;
    private int currentLoad;
    private final Set<Integer> destinationFloors;
    private final Queue<Request> requestQueue;
    private boolean doorsOpen;


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
    
    public void addDestination(int floor) {
        destinationFloors.add(floor);
    }

    public boolean shouldStopCurrentFloor() {
        return destinationFloors.contains(currentFloor);
    }

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

    public void closeDoors(){
        doorsOpen = false;
        System.out.printf("Elevator %d doors closed at floor %d%n", id, currentFloor);
    }

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

    public void addRequest(Request request){
        requestQueue.offer(request);
        addDestination(request.getFromFloor());
        addDestination(request.getToFloor());
    }

    public boolean hasCapacity(){
        return currentLoad < capacity;
    }

    public int calculateCost(Request request){
        int distance = Math.abs(currentFloor - request.getFromFloor());

        // IDLE elevators are most efficient - they can go anywhere
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

    public int getId(){
        return id;
    }

    public int getCurrentFloor(){
        return currentFloor;
    }

    public Direction getDirection(){
        return direction;
    }

    public int getCurrentLoad(){
        return currentLoad;
    }

    public int getCapacity(){
        return capacity;
    }

    public boolean isIdle(){
        return direction == Direction.IDLE && destinationFloors.isEmpty();
    }
    
    public boolean hasDestination(){
        return !destinationFloors.isEmpty();
    }

    public int getDestinationCount(){
        return destinationFloors.size();
    }

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
