# Elevator System Simulation

## Overview
A Java-based elevator control system that simulates multiple elevators servicing requests in a multi-floor building.

## Author
Ashwin Mahesh

## How to Run
```bash
javac -d bin src/com/elevator/*.java
java -cp bin com.elevator.ElevatorSystem
```

## System Architecture
- **ElevatorController**: Manages multiple elevators and assigns requests
- **Elevator**: Represents individual elevator with its own state and behavior
- **Request**: Encapsulates user requests with origin and destination floors
- **Direction**: Enum for elevator movement direction (UP, DOWN, IDLE)

## Assumptions Made

### 1. Building Configuration
- Building has floors numbered from a minimum to maximum (default: 1-10)
- All elevators start at the minimum floor (ground floor)

### 2. Passenger Behavior
- Passengers load/unload in fixed batches (2 at a time) for simulation purposes
- Actual passenger count per request is abstracted (each request = some passengers)
- Passengers are unloaded at ground floor and above (currentFloor > 0 check)

### 3. Elevator Scheduling
- Uses a cost-based algorithm to assign requests to elevators
- Cost factors: distance to pickup floor, direction alignment, current load
- Elevators prefer to continue in their current direction before reversing
- Tie-breaking uses elevator with fewer destinations

### 4. Timing and Movement
- One floor per time step in the step() method
- Door operations have a 500ms delay for realism
- No acceleration/deceleration modeling (constant speed)

### 5. Capacity Management
- Elevator capacity is enforced (default: 8 passengers)
- Requests are queued if no elevator has capacity
- No request prioritization (FIFO processing)

### 6. Request Handling
- Requests cannot be for the same floor (fromFloor == toFloor)
- Both pickup and destination floors are added to elevator's destination set
- Pending requests are reprocessed each step

## Features Not Implemented

### 1. Advanced Scheduling Algorithms
- **SCAN/LOOK algorithms**: Current implementation uses basic cost calculation
- **Destination dispatch systems**: Pre-assigns elevators based on destination before pickup
- **Dynamic load balancing**: More sophisticated distribution of requests across elevators

### 2. Real-World Constraints
- **Door open/close time**: Fixed 500ms; doesn't account for obstructions or safety sensors
- **Acceleration/deceleration**: Elevators move at constant speed (1 floor per step)
- **Maximum travel distance**: No limits on continuous operation
- **Maintenance mode**: No ability to take elevators out of service
- **Emergency handling**: No fire service, emergency stops, or priority modes

### 3. Passenger Management
- **Individual passenger tracking**: System tracks aggregate load, not individual passengers
- **Passenger destinations inside elevator**: Passengers aren't explicitly tracked after boarding
- **Maximum wait time guarantees**: No SLA or timeout for pending requests
- **Request cancellation**: Users cannot cancel requests once made

### 4. Energy Optimization
- **Idle positioning**: Elevators don't strategically position themselves during idle time
- **Power-saving modes**: No sleep mode for unused elevators
- **Group control optimization**: Elevators operate independently, not as a coordinated group

### 5. User Interface
- **Visual display**: Console output only, no GUI
- **Real-time monitoring**: No dashboard or status display beyond printStatus()
- **Call buttons**: No simulation of up/down call buttons per floor
- **Floor indicators**: No display of elevator position to waiting passengers

### 6. Safety and Reliability
- **Weight limits**: Capacity is passenger count, not weight-based
- **Door sensors**: No obstruction detection
- **Redundancy**: No failover if an elevator malfunctions
- **Error recovery**: Basic error handling only

### 7. Data and Analytics
- **Performance metrics**: No tracking of average wait time, service time, etc.
- **Usage patterns**: No learning or adaptation based on traffic patterns
- **Logging**: Console output only, no persistent logs or audit trail

### 8. Scalability Features
- **Distributed control**: Single controller, not distributed across multiple controllers
- **Building zones**: No express elevators or zone-based service
- **Sky lobbies**: No support for multi-stage elevator systems in tall buildings

## Potential Improvements

1. **Implement SCAN algorithm**: More efficient direction-based scheduling
2. **Add performance metrics**: Track and report average wait times, throughput
3. **Passenger object model**: Create explicit Passenger class for detailed tracking
4. **Configuration file**: Externalize building parameters (floors, elevator count, capacity)
5. **Unit tests**: Add comprehensive test coverage for edge cases
6. **Visualization**: Add real-time graphical representation of elevator positions
7. **Peak hour handling**: Special algorithms for rush hour traffic patterns
8. **Request prioritization**: Add urgency levels or VIP handling

## Design Decisions

### Why TreeSet for destinations?
- Maintains sorted order for efficient next-destination lookup
- Automatically handles duplicates

### Why cost-based assignment?
- Balances proximity with direction alignment
- More flexible than pure distance or pure direction-based algorithms

### Why separate Request class?
- Encapsulates request logic and timestamp
- Easier to extend with priority, user ID, etc.

## Testing Scenarios Covered

The simulation includes:
- Multiple simultaneous requests
- Requests in both directions (up and down)
- Mid-simulation request additions (steps 5 and 10)
- Full capacity handling
- Request queuing when elevators are busy

## Known Limitations

1. **Simplified passenger model**: Fixed batch sizes don't reflect real-world variability
2. **No request merging**: Multiple passengers going to same floor create separate requests
3. **Synchronous operation**: All elevators move in lockstep (same time steps)
4. **Console-only interface**: No interactive request generation

## Future Enhancements

- Web-based UI for real-time monitoring
- Machine learning for predictive elevator positioning
- Multi-building support
- API endpoints for external integration
- Database persistence for analytics