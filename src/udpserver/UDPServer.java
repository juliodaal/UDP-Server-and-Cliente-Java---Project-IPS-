/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpserver;

import clases.UdpServer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author julio
 */
public class UDPServer extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("OFF");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
        UdpServer udpServer = new UdpServer();
            @Override
            public void handle(ActionEvent event) {
                if("OFF".equals(btn.getText())){
                    btn.setText("ON");
                    udpServer.setValue(true);
                    udpServer.initServer();
                } else{
                    btn.setText("OFF");
                    udpServer.setValue(false);
                    udpServer.initServer();
                }
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("UDP Communication");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
