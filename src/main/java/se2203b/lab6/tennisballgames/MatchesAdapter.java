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
public class MatchesAdapter {

    Connection connection;

    public MatchesAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
            // This will throw an exception if the tables do not exist
            stmt.execute("DROP TABLE Matches");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                // Create the table of Matches
                stmt.execute("CREATE TABLE Matches ("
                        + "MatchNumber INT NOT NULL PRIMARY KEY, "
                        + "HomeTeam CHAR(15) NOT NULL REFERENCES Teams (TeamName), "
                        + "VisitorTeam CHAR(15) NOT NULL REFERENCES Teams (TeamName), "
                        + "HomeTeamScore INT, "
                        + "VisitorTeamScore INT "
                        + ")");
                populateSamples();
            }
        }
    }
    
    private void populateSamples() throws SQLException{
            // Create a listing of the matches to be played
            this.insertMatch(1, "Astros", "Brewers");
            this.insertMatch(2, "Brewers", "Cubs");
            this.insertMatch(3, "Cubs", "Astros");
    }
        
    
    public int getMax() throws SQLException {
        int num = 0;

        Statement s =connection.createStatement();
        ResultSet r = s.executeQuery("SELECT MAX(MatchNumber) FROM Matches");
        while (r.next()){
            num=r.getInt(1)+1;
        }
        
        return num;
    }
    
    public void insertMatch(int num, String home, String visitor) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO Matches (MatchNumber, HomeTeam, VisitorTeam, HomeTeamScore, VisitorTeamScore) "
                + "VALUES (" + num + " , '" + home + "' , '" + visitor + "', 0, 0)");
    }
    
    // Get all Matches
    public ObservableList<Matches> getMatchesList() throws SQLException {
        ObservableList<Matches> matchesList = FXCollections.observableArrayList();
       
        // Add your code here for Task #2
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery("SELECT * FROM Matches");

        while (r.next()){
            matchesList.add(new Matches(
                    r.getInt("MatchNumber"),
                    r.getString("HomeTeam"),
                    r.getString("VisitorTeam"),
                    r.getInt("HomeTeamScore"),
                    r.getInt("VisitorTeamScore")));}


        return matchesList;
    }

    // Get a String list of matches to populate the ComboBox used in Task #4.
    public ObservableList<String> getMatchesNamesList() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        ResultSet rs;
        
        // Create a Statement object
       Statement s = connection.createStatement();

        // Create a string with a SELECT statement
        String sql = "SELECT MatchNumber, HomeTeam, VisitorTeam FROM Matches";

        // Execute the statement and return the result
        rs = s.executeQuery(sql);
        
        // Loop the entire rows of rs and set the string values of list
        while (rs.next()){
            list.add(String.format("%d-%s-%s",
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3)
                    ));
        }
        
        return list;
    }
    
    
    public void setTeamsScore(int matchNumber, int hScore, int vScore) throws SQLException
   {
        // Add your code here for Task #4
       Statement s = connection.createStatement();
       s.executeUpdate("UPDATE Matches SET HomeTeamScore = "+hScore+", VisitorTeamScore = "+vScore+" WHERE MatchNumber = "+matchNumber+"");
   }  
}
