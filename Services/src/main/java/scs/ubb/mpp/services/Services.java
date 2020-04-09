package scs.ubb.mpp.services;

import scs.ubb.mpp.entity.Reservation;
import scs.ubb.mpp.entity.Trip;
import scs.ubb.mpp.entity.User;

import java.util.List;

public interface Services {
    void login(User user, TripObserver tripObserver) throws Exception;
    void logout(User user) throws Exception;
    List<Trip> getTrips() throws Exception;
    void book(Reservation reservation, TripObserver sender) throws Exception;
    List<Reservation> getReservations() throws Exception;
}
