package scs.ubb.mpp.entity;

public class Reservation extends Entity<Integer> {
    private String userName;
    private Integer tripId;
    private Integer numberOfSeats;

    public Reservation(Integer id, String userName, Integer tripId, Integer numberOfSeats) {
        super(id);
        this.userName = userName;
        this.tripId = tripId;
        this.numberOfSeats = numberOfSeats;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getTripId() {
        return tripId;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }
}
