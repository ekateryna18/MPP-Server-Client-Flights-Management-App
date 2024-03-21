package agency.client;

import agency.client.gui.LoginController;
import agency.client.gui.MainWindowController;
import agency.network.protobuffprotocol.ProtoProxy;
import agency.network.rpcprotocol.ServicesRpcProxy;
import agency.services.IAgencyServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartProtobufClient extends Application {
    private Stage primaryStage;
    private static int defaulPort = 55555;
    private static String defaultServer = "localhost";
    public void start(Stage primaryStage) throws Exception{
        System.out.println("In start");
        Properties clientProps = new Properties();
        try{
            clientProps.load(StartRpcClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("agency.server.host", defaultServer);
        int serverPort = defaulPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("agency.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaulPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IAgencyServices server = new ProtoProxy(serverIP,serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LoginView.fxml"));
        Parent root=loader.load();
        LoginController ctrl = loader.<LoginController>getController();
        ctrl.setServer(server);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindowView.fxml"));
        Parent mainroot=mainLoader.load();
        MainWindowController mainCtrl = mainLoader.<MainWindowController>getController();
        //mainCtrl.setServer(server, "", "");

        ctrl.setMainWindowController(mainCtrl);
        ctrl.setParent(mainroot);

        primaryStage.setTitle("MPP app");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
