package scs.ubb.mpp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scs.ubb.mpp.controller.LoginController;
import scs.ubb.mpp.networking.rpcprotocol.ServicesRpcProxy;
import scs.ubb.mpp.services.Services;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();
        try {
            clientProps.load(this.getClass().getResourceAsStream("/chatclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        Services server = new ServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("loginView.fxml"));
        Parent root=loader.load();

        LoginController ctrl = loader.getController();
        ctrl.setService(server);
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }
}
