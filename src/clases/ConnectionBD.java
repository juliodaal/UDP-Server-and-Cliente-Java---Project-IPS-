package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {
    
    private static Connection conn;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "";
    private static final String url = "jdbc:mysql://127.0.0.1:3306/projeto";

    public ConnectionBD() {
        conn =  null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,password);
            if(conn != null){
                System.out.println("Connected..");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error to connect : " + e);
        }
    }
    
    public Connection getConnection(){
        return conn;
    }
    
    public void disconnect(){
        conn = null;
        if(conn == null){
            System.out.println("Disconnect..");
        }
    }
    
    
}
