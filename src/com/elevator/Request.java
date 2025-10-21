package com.elevator;

import com.elevator.Direction;

public class Request {
    private final int fromFloor;
    private final int toFloor;
    private final Direction direction;
    private final long timestamp;

    public Request(int fromFloor, int toFloor) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.direction = (toFloor > fromFloor) ? Direction.UP : Direction.DOWN;
        this.timestamp = System.currentTimeMillis();
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Request{from=%d, to=%d, direction=%s}", 
                           fromFloor, toFloor, direction);
    }
}