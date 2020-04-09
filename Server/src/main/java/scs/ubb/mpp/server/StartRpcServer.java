package scs.ubb.mpp.server;

import scs.ubb.mpp.networking.utils.AbstractServer;
import scs.ubb.mpp.networking.utils.ChatRpcConcurrentServer;
import scs.ubb.mpp.repository.ReservationRepository;
import scs.ubb.mpp.repository.TripRepository;
import scs.ubb.mpp.repository.UserRepository;
import scs.ubb.mpp.server.services.ServiceImpl;
import scs.ubb.mpp.services.Services;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/chatserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }

        UserRepository userRepository = new UserRepository();
        TripRepository tripRepository = new TripRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        Services services =new ServiceImpl(userRepository, tripRepository, reservationRepository);

        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new ChatRpcConcurrentServer(chatServerPort, services);

        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(Exception e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
