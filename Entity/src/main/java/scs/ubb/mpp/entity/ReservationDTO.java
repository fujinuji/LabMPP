package scs.ubb.mpp.entity;

public class ReservationDTO {
    private String travelerName;
    private String departureTown;
    private String arrivalTown;
    private Integer bookedSeats;

    public ReservationDTO(String travelerName, String departureTown, String arrivalTown, Integer bookedSeats) {
        this.travelerName = travelerName;
        this.departureTown = departureTown;
        this.arrivalTown = arrivalTown;
        this.bookedSeats = bookedSeats;
    }

    public String getTravelerName() {
        return travelerName;
    }

    public String getDepartureTown() {
        return departureTown;
    }

    public String getArrivalTown() {
        return arrivalTown;
    }

    public Integer getBookedSeats() {
        return bookedSeats;
    }
}
