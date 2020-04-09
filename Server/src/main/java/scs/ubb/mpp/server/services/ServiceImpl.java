package scs.ubb.mpp.server.services;

import scs.ubb.mpp.entity.Reservation;
import scs.ubb.mpp.entity.Trip;
import scs.ubb.mpp.entity.User;
import scs.ubb.mpp.repository.ReservationRepository;
import scs.ubb.mpp.repository.TripRepository;
import scs.ubb.mpp.repository.UserRepository;
import scs.ubb.mpp.services.Services;
import scs.ubb.mpp.services.TripObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImpl implements Services {
    private UserRepository userRepository;
    private TripRepository tripRepository;
    private ReservationRepository reservationRepository;
    private Map<String, TripObserver> loggedClients;
    //private List<TripObserver> tripObserverLi

    public ServiceImpl(UserRepository userRepository, TripRepository tripRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
        loggedClients = new HashMap<>();
    }

    @Override
    public synchronized void login(User user, TripObserver tripObserver) throws Exception {
        User userR=userRepository.loginUser(user.getUserName(),user.getPassword());
        if (userR!=null){/*
            if(loggedClients.get(user.getId())!=null)
                throw new ChatException("User already logged in.");
            loggedClients.put(user.getId(), client);
            notifyFriendsLoggedIn(user);*/
            loggedClients.put(user.getUserName(), tripObserver);
        } else {
            throw new Exception("Authentication failed.");
        }
    }

    @Override
    public void logout(User user) throws Exception {
        loggedClients.remove(user.getUserName());
    }

    @Override
    public synchronized List<Trip> getTrips() throws Exception {
        return tripRepository.getAll();
    }

    @Override
    public synchronized void book(Reservation reservation, TripObserver sender) {
        reservationRepository.save(reservation);
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (TripObserver observer : loggedClients.values()) {
            if (sender != observer) {
                executorService.execute(() -> observer.tripsUpdate(reservation));
            }
        }
        executorService.shutdown();
    }

    @Override
    public List<Reservation> getReservations() throws Exception {
        return reservationRepository.getAll();
    }
}
