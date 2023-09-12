package program.files;

import java.sql.*;
import java.util.Scanner;

public class SwipeApplication {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter id (or 0 to close application): ");

        String input = sc.nextLine();

        while (!input.equals("0")) {

            String id = "";
            // parse value for id
            int idLength = 0;
            boolean idFound = false;
            for (int i = 0; i < input.length() && idLength < 9; i++) {
                if (input.charAt(i) == '0' || input.charAt(i) == '1' || input.charAt(i) == '2'
                    || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) =='5'
                        || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) =='8'
                        || input.charAt(i) == '9')
                {
                    id += input.charAt(i);
                    idLength++;
                    idFound = true;
                }
                else if (idFound) {
                    i = input.length();
                }
            }

            if (!idFound) {
                System.out.println("Please enter in a valid id");
            }
            else {

                // once input is parsed for the id,
                // check if user is allowed to gain access
                String driverName = "com.mysql.jdbc.Driver";

                String serverName = "localhost";
                String mydatabase = "sun_lab_access";
                String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

                Connection con = null;
                try {
                    con = DriverManager.getConnection(url, "root", "abc123#@!Z");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                String query = "SELECT status FROM users WHERE id = " + id;
                String status = "";

                try {
                    Statement queryStmt = con.createStatement();
                    ResultSet rs = queryStmt.executeQuery(query);
                    if (rs.next()) status = rs.getString(1);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (status.isEmpty()) {
                    System.out.println("id not found");
                }
                // if user is allowed access, log the action
                else if (status.equalsIgnoreCase("active")) {
                    String update = "INSERT INTO access_log(id) VALUES(" + id + ")";
                    try {
                        Statement updateStmt = con.createStatement();
                        updateStmt.executeUpdate(update);

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("ACCESS GRANTED");
                } else {
                    System.out.println("ACCESS DENIED");
                }

                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println("\nPlease enter id (or 0 to close application): ");
            input = sc.nextLine();

        }

    }

}
