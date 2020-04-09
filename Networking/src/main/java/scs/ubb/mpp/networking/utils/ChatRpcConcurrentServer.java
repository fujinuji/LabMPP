package scs.ubb.mpp.networking.utils;

import scs.ubb.mpp.networking.rpcprotocol.ClientRpcWorker;
import scs.ubb.mpp.services.Services;

import java.net.Socket;

public class ChatRpcConcurrentServer extends AbstractConcurrentSever {
    private Services chatServer;

    public ChatRpcConcurrentServer(int port, Services chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
       // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        ClientRpcWorker worker=new ClientRpcWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
