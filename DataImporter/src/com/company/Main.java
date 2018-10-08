/* Imports Seattle,WA Public Access Lab Locations from csv to SQLite
Bill Erhard, wherhard@student.rtc.edu
CNA 336 Spring 2018 */

package com.company;
// This is pretty simple, just need csv and sqlite3 functionality

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
      // Our filename, sans path and file extension
      String filename = "Public_Computer_Access_Locations";

      // Relative paths for each file
      String csv_file = "./" + filename + ".csv";
      String db_file = "./" + filename + ".db";

      // Fields, copy-pasted from the first row of CSV
      String[] fields = {"Lab_name", "Phone", "Accessible", "Hours", "Tech_Support_Assisted", "Organization",
              "Location", "Web_address"};

      // http://www.sqlitetutorial.net/sqlite-java/
      // http://www.ntu.edu.sg/home/ehchua/programming/java/jdbc_basic.html
      // https://stackoverflow.com/questions/16151903/intellij-error-no-suitable-driver-found-for-jdbcmysql-127-0-0-13306-person

      // Make a database connection and grab a cursor
      //db_connection = sqlite3.connect(db_file);
      //db_cursor = db_connection.cursor();

      // Create the table
      Connection db_connection;
      try {
        // db parameters
        String url = "jdbc:sqlite:Public_Computer_Access_Locations.db";
        // create a connection to the database
        db_connection = DriverManager.getConnection(url);

        //System.out.println("Connection to SQLite has been established.");
        String sql = "CREATE TABLE IF NOT EXISTS locations(ID INTEGER PRIMARY KEY, ";
        for (int i = 0; i<fields.length-1;i++){
          //System.out.println("i is: " + i + " field is: " + fields[i]);
          sql += fields[i] + " TEXT, ";
          //System.out.println("sql is: " + sql);

          //doublecup
        }
        sql += "Web_address);";

        Statement statement = db_connection.createStatement();
        statement.execute(sql);

      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }

      // cf hackyness beyond this point, csvs with random commas and newlines suck.

        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(csv_file), charset)) {
          String lines[] = new String[50];
          int current = 0;
          for (String line = reader.readLine(); line != null; line = reader.readLine()) {
          // while there are lines to read, read each line to lines[]. Ignore first case.

          if (current != 0) {
            if (line.contains("Seattle")) {
              String newline = lines[current-1];
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

        for (int i= 0;i<lines.length;i++){
          System.out.println(lines[i]);
        }
      } catch (Exception x) {
        System.err.format("IOException: %s%n", x);
      }




//  https://docs.oracle.com/javase/tutorial/essential/io/
//// Get ready to process data
//        with open(csv_file, "r") as file:
//        reader = csv.reader(file, delimiter=',', quotechar='"')
//        r = 0
//    // Ugly, but works. Ignore first line...
//        for row in reader:
//        if r > 0:
//            // For each item in each row, update the database; first item inserts
//        for i in range(len(row)):
//        if i == 0:
//        sql = 'INSERT INTO locations(ID,lab_name) VALUES(?, ?)'
//        db_cursor.execute(sql, (r, row[i]))
//                else:
//        sql = f'UPDATE locations SET {fields[i]} = ? WHERE ID = ?'
//        db_cursor.execute(sql, (row[i], r))
//        r += 1
//
//// Remember, db_cursor is connected to our db file, so this shows things went
//// into our db correctly.
//        db_cursor.execute("SELECT * FROM locations")
//        rows = db_cursor.fetchall()
//        for x in rows:
//        print(x)

    }
}
