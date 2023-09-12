package program.files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {

    private boolean status;
    private String username;
    private String password;
    public DatabaseConnection(String usrnm, String pswrd) {

        String driverName = "com.mysql.cj.jdbc.Driver";

        String url = "jdbc:mysql://localhost:3306/sun_lab_access?autoReconnect=true&useSSL=false";

        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(url, usrnm, pswrd);
            status = con.isValid(10);
            con.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (status) {
            username = usrnm;
            password = pswrd;
        }
        else {
            username = "";
            password = "";
        }

    }

    public boolean login(String usrnm, String pswrd) {

        String driverName = "com.mysql.jdbc.Driver";

        String serverName = "localhost";
        String mydatabase = "sun_lab_access";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

        Connection con = null;
        try {
            con = DriverManager.getConnection(url, usrnm, pswrd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            status = con.isValid(10);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (status) {
            username = usrnm;
            password = pswrd;
        }
        else {
            username = "";
            password = "";
        }

        return status;
    }

    public boolean getStatus() {

        return status;

    }

    public static ArrayList<AccessLogRow> queryAccess_log(String query) throws SQLException {

        String driverName = "com.mysql.jdbc.Driver";

        String serverName = "localhost";
        String mydatabase = "sun_lab_access";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

        // establish connection
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, "root", "abc123#@!Z");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ResultSet rs = null;

        // execute query and catch results
        try {
            Statement queryStmt = con.createStatement();
            rs = queryStmt.executeQuery(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // store results in an array of ObservableList<String>
        ArrayList<AccessLogRow> rows = new ArrayList<>();
        while (rs.next()) {
            // get values from result set
            String id = rs.getString("id");
            String time = rs.getString("time");
            // create row for tableview
            AccessLogRow row = new AccessLogRow(id, time);
            // store row in array
            rows.add(row);
        }

        // close connection
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rows;

    }

    public static void addUser(String id, String name, String position) {

        String driverName = "com.mysql.jdbc.Driver";

        String serverName = "localhost";
        String mydatabase = "sun_lab_access";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

        // establish connection
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, "root", "abc123#@!Z");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // insert the new user
        String insert = "INSERT INTO users VALUES( " + id + ", '" + name + "', '" + position + "', 'active')";

        try {
            Statement insertStmt = con.createStatement();
            insertStmt.executeUpdate(insert);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean changeUserStatus(String id, boolean status) {

        String driverName = "com.mysql.jdbc.Driver";

        String serverName = "localhost";
        String mydatabase = "sun_lab_access";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

        // establish connection
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, "root", "abc123#@!Z");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // update status field for user
        String update;
        if (status)
            update = "UPDATE users SET status = 'active' WHERE id = " + id;
        else
            update = "UPDATE users SET status = 'disabled' WHERE id = " + id;

        int numOfRowsUpdated = 0;
        try {
            Statement updateStmt = con.createStatement();
            numOfRowsUpdated = updateStmt.executeUpdate(update);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return numOfRowsUpdated != 0;

    }

    public static boolean isValidID(String id) {

        // if id is empty
        if (id.isEmpty()) return false;

        // id must be 9 digits long
        if (id.length() != 9) return false;

        // id must be an integer
        try {
            Integer.parseInt(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

}
