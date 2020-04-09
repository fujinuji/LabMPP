package scs.ubb.mpp.services;

import scs.ubb.mpp.entity.Reservation;

public interface TripObserver {
    void tripsUpdate(Reservation reservation);
}
