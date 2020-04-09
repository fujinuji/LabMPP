package scs.ubb.mpp.controller;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import scs.ubb.mpp.entity.Reservation;
import scs.ubb.mpp.entity.Trip;
import scs.ubb.mpp.entity.User;
import scs.ubb.mpp.repository.utils.Utils;
import scs.ubb.mpp.services.Services;
import scs.ubb.mpp.services.TripObserver;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TripController implements TripObserver {
    private ObservableList<Trip> model = FXCollections.observableArrayList();
    private ObservableList<Reservation> reservationModel = FXCollections.observableArrayList();
    private Services services;

    @FXML
    private TableView tripTable;
    @FXML
    private TableColumn<Trip, String> departureTownColumn;
    @FXML
    private TableColumn<Trip, String> arrivalTownColumn;
    @FXML
    private TableColumn<Trip, String> departureTimeColumn;
    @FXML
    private TableColumn<Trip, String> arrivalTimeColumn;
    @FXML
    private TableColumn<Trip, Integer> availableSeatsColumn;
    @FXML
    private TextField departureTownTextField;
    @FXML
    private TextField arrivalTownTextField;
    @FXML
    private TextField departureTimeTextField;
    @FXML
    private TextField arrivalTimeTextField;
    @FXML
    private TextField availableSeatsTextField;
    @FXML
    private TextField travelerNameTextField;
    @FXML
    private TextField seatsTextField;
    @FXML
    private TableView reservationsTable;
    @FXML
    private TableColumn<Reservation, String> travelerNameColumn;
    @FXML
    private TableColumn<Reservation, String> departureReservationTownColumn;
    @FXML
    private TableColumn<Reservation, String> arrivalReservationTownColumn;
    @FXML
    private TableColumn<Reservation, Integer> bookedSeatsColumn;
    @FXML
    private TextField searchTextField;

    private User user;

    @FXML
    public void initialize() {
        departureTownColumn.setCellValueFactory(new PropertyValueFactory<>("departureTown"));
        arrivalTownColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTown"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTown"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        travelerNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        departureReservationTownColumn.setCellValueFactory(new PropertyValueFactory<>("tripId"));
//        arrivalReservationTownColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTown"));
        bookedSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("noOfSeats"));

        tripTable.setItems(model);
        reservationsTable.setItems(reservationModel);
    }

    private void updateModel() throws Exception {
        model.setAll(services.getTrips());
        reservationModel.setAll(services.getReservations());
        /*for(Reservation reservation : reservationService.getAll()) {
            Trip trip = tripService.getById(reservation.getTripId());
            reservationModel.add(new ReservationDTO(reservation.getUserName(), trip.getDepartureTown(), trip.getArrivalTown(), reservation.getNumberOfSeats()));
        }*/
    }

    private void setReservation(Reservation booking) {
        List<Trip> trips = new ArrayList<>();
        model.forEach(trip -> {
            if (trip.getId().equals(booking.getTripId())) {
                trip.bookSeats(booking.getNumberOfSeats());
            }
            trips.add(trip);
        });

        model.setAll(trips);
        reservationModel.add(booking);
    }

    public void setItems(Services services, User user) {
        this.services = services;
        this.user = user;
        try {
            updateModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTripOnAction(ActionEvent event) {
        Trip trip = new Trip(
                null,
                departureTownTextField.getText(),
                arrivalTownTextField.getText(),
                LocalTime.parse(departureTimeTextField.getText(), Utils.formatter),
                LocalTime.parse(arrivalTimeTextField.getText(), Utils.formatter),
                Integer.parseInt(availableSeatsTextField.getText())
        );
    }

    public void reserveOnAction(ActionEvent event) {
        Trip trip = (Trip) tripTable.getSelectionModel().getSelectedItem();

        if (trip == null) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.initOwner(null);
            message.setTitle("Mesaj eroare");
            message.setContentText("Select a trip first");
            message.showAndWait();
            return;
        }


        Integer noOfSeats = Integer.parseInt(seatsTextField.getText());
        String traveler = travelerNameTextField.getText();
        if (noOfSeats > trip.getAvailableSeats()) {

            Alert message = new Alert(Alert.AlertType.ERROR);
            message.initOwner(null);
            message.setTitle("Mesaj eroare");
            message.setContentText("Select few seats");
            message.showAndWait();
            return;
        }

        Reservation reservation = new Reservation(null, traveler, trip.getId(), noOfSeats);
        try {
            services.book(reservation, this);
            tripsUpdate(reservation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //reservationService.save(reservation);
        //updateModel();
    }

    public void searchButtonOnAction(ActionEvent event) {
//        List<ReservationDTO> dtoList = new ArrayList<>();
//
//        for(Reservation reservation : reservationService.getAll()) {
//            Trip trip = tripService.getById(reservation.getTripId());
//            if (searchTextField.getText().contains(trip.getDepartureTown())) {
//                dtoList.add(new ReservationDTO(reservation.getUserName(), trip.getDepartureTown(), trip.getArrivalTown(), reservation.getNumberOfSeats()));
//            }
//        }
//        reservationModel.setAll(dtoList);
    }

    @Override
    public void tripsUpdate(Reservation reservation) {
        setReservation(reservation);
    }

    public void handleLogout(ActionEvent event) {
        try {
            services.logout(user);
            Platform.exit();
        } catch(Exception e) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.initOwner(null);
            message.setTitle("Mesaj eroare");
            message.setContentText(e.getMessage());
            message.showAndWait();
        }
    }
}
