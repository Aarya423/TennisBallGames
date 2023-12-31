package se2203b.lab6.tennisballgames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Abdelkader Ouda
 */
public class TeamsAdapter {

    Connection connection;

    public TeamsAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                // We drop Matches first because it references the table Teams
                stmt.execute("DROP TABLE Matches");
                stmt.execute("DROP TABLE Teams");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                // Create the table of teams
                stmt.execute("CREATE TABLE Teams ("
                        + "TeamName CHAR(15) NOT NULL PRIMARY KEY, "
                        + "Wins INT, Losses INT, Ties INT)");
                populateSampls();
            }
        }
    }

    private void populateSampls() throws SQLException {
        // Add some teams
        this.insertTeam("Astros");
        this.insertTeam("Marlins");
        this.insertTeam("Brewers");
        this.insertTeam("Cubs");
    }

    public void insertTeam(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO Teams (TeamName, Wins, Losses, Ties) VALUES ('" + name + "', 0, 0, 0)");
    }

    // Get all teams Data
    public ObservableList<Teams> getTeamsList() throws SQLException {
        ObservableList<Teams> list = FXCollections.observableArrayList();
        ResultSet rs;

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String sqlStatement = "SELECT * FROM Teams";

        // Execute the statement and return the result
        rs = stmt.executeQuery(sqlStatement);

        while (rs.next()) {
            list.add(new Teams(rs.getString("TeamName"),
                    rs.getInt("Wins"),
                    rs.getInt("Losses"),
                    rs.getInt("Ties")));
        }
        return list;
    }

    // Get all teams names to populate the ComboBoxs used in Task #3.
    public ObservableList<String> getTeamsNames() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        ResultSet rs;

        // Create a Statement object
        Statement s = connection.createStatement();

        // Create a string with a SELECT statement
        String sqlStatement= "SELECT TeamName FROM Teams";

        // Execute the statement and return the result
        rs = s.executeQuery(sqlStatement);
        // loop for the all rs rows and update list
        while(rs.next()){
            list.add(rs.getString("teamName"));
        }
        
        return list;
    }

    public void setStatus(String hTeam, String vTeam, int hScore, int vScore) throws SQLException {
        // Create a Statement object
        Statement stmt = connection.createStatement();
        ResultSet rs;
        int hW=0,hL=0,hT=0,vW=0,vL=0,vT=0;
        // Write your code here for Task #4
//        rs = stmt.executeQuery("SELECT Wins, Losses, Ties FROM Teams WHERE TeamName='"+hTeam+"'");
//
//            hW=rs.getInt("Wins");
//            hL=rs.getInt("Losses");
//            hT=rs.getInt("Ties");
//        rs = stmt.executeQuery("SELECT Wins, Losses, Ties FROM Teams WHERE TeamName='"+vTeam+"'");
//        vW=rs.getInt("Wins");
//        vL=rs.getInt("Losses");
//        vT=rs.getInt("Ties");

        if (hScore==vScore){
            rs = stmt.executeQuery("SELECT Ties FROM Teams WHERE TeamName='"+hTeam+"'");

            while (rs.next()){
                hT=rs.getInt("Ties")+1;
                }
            stmt.executeUpdate("UPDATE Teams Set Ties="+hT+" WHERE TeamName='"+hTeam+"'");

            rs = stmt.executeQuery("SELECT Ties FROM Teams WHERE TeamName='"+vTeam+"'");
            while (rs.next()) {
                vT = rs.getInt("Ties") + 1;
            }
            stmt.executeUpdate("UPDATE Teams Set Ties="+vT+" WHERE TeamName='"+vTeam+"'");
        }
        else if (hScore<vScore){
            rs = stmt.executeQuery("SELECT Wins FROM Teams WHERE TeamName='"+vTeam+"'");

            while (rs.next()){
                vW = rs.getInt("Wins") + 1;
            }
            stmt.executeUpdate("UPDATE Teams Set Wins="+vW+" WHERE TeamName='"+vTeam+"'");

            rs = stmt.executeQuery("SELECT Losses FROM Teams WHERE TeamName='"+hTeam+"'");

            while (rs.next()){
                hL = rs.getInt("Losses") + 1;
            }
            stmt.executeUpdate("UPDATE Teams Set Losses="+hL+" WHERE TeamName='"+hTeam+"'");



        }
        else{

            rs = stmt.executeQuery("SELECT Wins FROM Teams WHERE TeamName='"+hTeam+"'");

            while (rs.next()){
                hW = rs.getInt("Wins") + 1;
            }
            stmt.executeUpdate("UPDATE Teams Set Wins="+hW+" WHERE TeamName='"+hTeam+"'");

            rs = stmt.executeQuery("SELECT Losses FROM Teams WHERE TeamName='"+vTeam+"'");

            while (rs.next()){
                vL = rs.getInt("Losses") + 1;
            }
            stmt.executeUpdate("UPDATE Teams Set Losses="+vL+" WHERE TeamName='"+vTeam+"'");
        }


    }

}
