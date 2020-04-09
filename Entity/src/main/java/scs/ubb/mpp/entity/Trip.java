package scs.ubb.mpp.entity;

import java.time.LocalTime;

public class Trip extends Entity<Integer> {
    private String departureTown;
    private String arrivalTown;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Integer availableSeats;

    public Trip(Integer id, String departureTown, String arrivalTown, LocalTime departureTime, LocalTime arrivalTime, Integer availableSeats) {
        super(id);
        this.departureTown = departureTown;
        this.arrivalTown = arrivalTown;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
    }

    public String getDepartureTown() {
        return departureTown;
    }

    public String getArrivalTown() {
        return arrivalTown;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void bookSeats(Integer nrOfSeats) {
        this.availableSeats -= nrOfSeats;
    }
}
