package scs.ubb.mpp.networking.utils;

import java.net.Socket;


public abstract class AbstractConcurrentSever extends AbstractServer {

    public AbstractConcurrentSever(int port) {
        super(port);
         System.out.println("Concurrent AbstractServer");
    }

    protected void processRequest(Socket client) {
        Thread tw=createWorker(client);
        tw.start();
    }

    protected abstract Thread createWorker(Socket client) ;


}
