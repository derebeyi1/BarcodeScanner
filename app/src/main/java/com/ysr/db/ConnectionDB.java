package com.ysr.db;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionDB {
    // Your IP address must be static otherwise this will not work. You //can get your Ip address
	//From Network and security in Windows.
    //static String ip = "192.168.2.200:1453";
    public static String ip = "";
    // This is default if you are using JTDS driver.
    public static String classs = "net.sourceforge.jtds.jdbc.Driver";
    public static String instance = "";
    // Name Of your database.
    public static String db = "";
    // Userame and password are required for security. so Go to sql server and add username and password for your database.
    public static String un = "";
    public static String password = "";

    @SuppressLint("NewApi")
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL;
        //try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip
                    //+ ";instance=" + instance
                    + ";databaseName=" + db
                    + ";user=" + un
                    + ";password=" + password + ";";
            Log.i("connnnn str:::::","" + ConnURL);
            conn = DriverManager.getConnection(ConnURL);            
        return conn;
    }
}