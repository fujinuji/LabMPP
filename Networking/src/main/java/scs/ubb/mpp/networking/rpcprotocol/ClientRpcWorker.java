package scs.ubb.mpp.networking.rpcprotocol;

import scs.ubb.mpp.entity.Reservation;
import scs.ubb.mpp.entity.User;
import scs.ubb.mpp.services.Services;
import scs.ubb.mpp.services.TripObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;


//TODO Class should implements observable interface too
public class ClientRpcWorker implements Runnable, TripObserver {
    private Services services;
    private Socket socket;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private volatile boolean connected;

    public ClientRpcWorker(Services services, Socket socket) {
        this.services = services;
        this.socket = socket;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = inputStream.readObject();
                System.out.println("PRIMESC request" + ((Request) request).type());
                Response response = handleRequest((Request) request);

                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private void sendResponse(Response response) {
        try {

            System.out.println("TRIMIT response" + ((Response) response).type());
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;

        try {
            Method method = this.getClass().getDeclaredMethod("handle" + request.type(), Request.class);
            response = (Response) method.invoke(this, request);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return response;
    }

    private Response handleLOGIN(Request request){
        try {
            User user = (User) request.data();
            services.login(user, this);
            return new Response.Builder().type(ResponseType.OK).build();
        } catch (Exception e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage() + "reflection").build();
        }
    }

    private Response handleTRIPS_GATHERING(Request request) {
        try {
            return new Response.Builder().data(
                    services.getTrips()
            ).type(ResponseType.OK).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).build();
        }
    }

    private Response handleBOOKING(Request request) {
        try {
            services.book((Reservation) request.data(), this);
            return new Response.Builder().type(ResponseType.OK).data(request.data()).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).build();
        }
    }

    private Response handleBOOKINGS_GATHERING(Request request) {
        try {
            return new Response.Builder().data(
                    services.getReservations()
            ).type(ResponseType.OK).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).build();
        }
    }

    private Response handleLOGOUT(Request request) {
        try {
            User user = (User) request.data();
            services.logout(user);
            connected = false;
            return new Response.Builder().type(ResponseType.OK).build();
        } catch (Exception e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage() + "reflection").build();
        }
    }

    @Override
    public void tripsUpdate(Reservation reservation) {
        Response response = new Response.Builder().data(reservation).type(ResponseType.BOOKING_MADE).build();
        try {
            sendResponse(response);
        } catch (Exception ignored) {

        }
    }
}
