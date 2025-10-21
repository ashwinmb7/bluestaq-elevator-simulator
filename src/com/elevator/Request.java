package com.elevator;

import com.elevator.Direction;

/**
 * Class representing an elevator request.
 * This class encapsulates the details of a request made by a user,
 * including the origin floor, destination floor, direction, and timestamp.
 * @author Ashwin Mahesh
 */
public class Request {
    /** The floor where the request originates */
    private final int fromFloor;
    /** The desired destination floor */
    private final int toFloor;
    /** The direction of the request (UP or DOWN) */
    private final Direction direction;
    /** The timestamp when the request was created */
    private final long timestamp;

    /** 
     * Constructor to create a new Request.
     * @param fromFloor the floor where the request originates
     * @param toFloor the desired destination floor
     */
    public Request(int fromFloor, int toFloor) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.direction = (toFloor > fromFloor) ? Direction.UP : Direction.DOWN;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Gets the origin floor of the request.
     * @return the from floor
     */
    public int getFromFloor() {
        return fromFloor;
    }

    /**
     * Gets the destination floor of the request.
     * @return the to floor
     */
    public int getToFloor() {
        return toFloor;
    }

    /**
     * Gets the direction of the request.
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Gets the timestamp when the request was created.
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a string representation of the request.
     * @return string representation of the request
     */
    @Override
    public String toString() {
        return String.format("Request{from=%d, to=%d, direction=%s}", 
                           fromFloor, toFloor, direction);
    }
}