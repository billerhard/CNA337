/* Imports Seattle,WA Public Access Lab Locations from csv to SQLite
Bill Erhard, wherhard@student.rtc.edu
CNA 336 Spring 2018 */

package com.company;
// http://www.sqlitetutorial.net/sqlite-java/
// http://www.ntu.edu.sg/home/ehchua/programming/java/jdbc_basic.html
// https://stackoverflow.com/questions/16151903/intellij-error-no-suitable-driver-found-for-jdbcmysql-127-0-0-13306-person
// https://docs.oracle.com/javase/tutorial/essential/io/

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class Main {

  public static void main(String[] args) {
    String filename = "Public_Computer_Access_Locations";
    String csv_file = "./" + filename + ".csv";
    String[] fields = {"Lab_name", "Phone", "Accessible", "Hours", "Tech_Support_Assisted", "Organization",
            "Location", "Web_address"};
    Connection db_connection;
    try {
      String url = "jdbc:sqlite:Public_Computer_Access_Locations.db";
      db_connection = DriverManager.getConnection(url);
      String sql = "CREATE TABLE IF NOT EXISTS locations(ID INTEGER PRIMARY KEY, ";
      for (int i = 0; i < fields.length - 1; i++) {
        sql += fields[i] + " TEXT, ";
      }
      sql += "Web_address);";
      Statement statement = db_connection.createStatement();
      statement.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    Charset charset = Charset.forName("US-ASCII");
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(csv_file), charset)) {
      String lines[] = new String[50];
      int current = 0;
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        if (current != 0) {
          if (line.contains("Seattle")) {
            String newline = lines[current - 1];
            current--;
            lines[current] = newline + " " + line;
            line = reader.readLine();
            lines[current] += " " + line;
          } else {
            lines[current] = line;
          }
        }
        current++;
      }
      try {
        String url = "jdbc:sqlite:Public_Computer_Access_Locations.db";
        db_connection = DriverManager.getConnection(url);
        for (String line : lines) {
          String sql = "INSERT INTO locations VALUES (";
          //PreparedStatement s;

          sql+=line; //doesn't work... needs more than 1 value.

          sql+=");";
          if(line!=null)
          System.out.println(sql);
          //s = db_connection.prepareStatement(sql);
          //s.execute(sql);
        }

        String sql = "SELECT * FROM locations;";
        Statement s = db_connection.createStatement();
        s.execute(sql);
        ResultSet r = s.getResultSet();
        String row = " ";
        while (r.next()) {
          for (String f : fields) {
            row += r.getString(f);
          }
          System.out.println(row);
        }
      } catch (Exception e) {
        System.err.format("IOException: %s%n", e);
      }
    } catch (Exception x) {
      System.err.format("IOException: %s%n", x);
    }
  }
}
