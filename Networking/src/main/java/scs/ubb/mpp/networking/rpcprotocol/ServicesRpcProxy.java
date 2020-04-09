package scs.ubb.mpp.networking.rpcprotocol;


import scs.ubb.mpp.entity.Reservation;
import scs.ubb.mpp.entity.Trip;
import scs.ubb.mpp.entity.User;
import scs.ubb.mpp.services.Services;
import scs.ubb.mpp.services.TripObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesRpcProxy implements Services {
    private String host;
    private Integer port;

    private TripObserver client;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;

    private BlockingQueue<Response> responses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.responses = new LinkedBlockingQueue<>();
    }

    @Override
    public void login(User user, TripObserver tripObserver) throws Exception {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type().equals(ResponseType.ERROR)) {
            closeConnection();
            throw new Exception("Invalid user");
        } else {
            client = tripObserver;
        }
    }

    @Override
    public void logout(User user) throws Exception {
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type().equals(ResponseType.OK)) {
            closeConnection();
            client = null;
        } else {
            throw new Exception("Error");
        }
    }

    @Override
    public List<Trip> getTrips() throws Exception {
        Request request = new Request.Builder().type(RequestType.TRIPS_GATHERING).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type().equals(ResponseType.ERROR)) {
            closeConnection();
            throw new Exception("Error");
        }

        return (List<Trip>) response.data();
    }

    @Override
    public void book(Reservation reservation, TripObserver observer) throws Exception{
        Request request = new Request.Builder().type(RequestType.BOOKING).data(reservation).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type().equals(ResponseType.ERROR)) {
            throw new Exception("Invalid user");
        }
    }

    @Override
    public List<Reservation> getReservations() throws Exception {
        Request request = new Request.Builder().type(RequestType.BOOKINGS_GATHERING).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type().equals(ResponseType.ERROR)) {
            closeConnection();
            throw new Exception("Error");
        }

        return (List<Reservation>) response.data();
    }

    private void closeConnection() {
        finished=true;
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = responses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void sendRequest(Request request) {
        try {
            System.out.println("TRIMIT request" + request.type());
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeConnection() {
        try {
            this.socket = new Socket(host, port);
            this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
            outputStream.flush();
            this.inputStream = new ObjectInputStream(this.socket.getInputStream());
            this.finished = false;
            startRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRead() {
        Thread thread = new Thread(new ReaderThread());
        thread.start();
    }

    private boolean isUpdate(Response response) {
        return response.type().equals(ResponseType.BOOKING_MADE);
    }

    private void handelUpdate(Response response) {
        if (response.type().equals(ResponseType.BOOKING_MADE)) {
            client.tripsUpdate((Reservation) response.data());
        }
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    Object response = inputStream.readObject();
                    System.out.println("PRIMESC response" + ((Response) response).type());
                    if (isUpdate((Response) response)) {
                        handelUpdate((Response) response);
                    } else {
                        responses.put((Response) response);
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
