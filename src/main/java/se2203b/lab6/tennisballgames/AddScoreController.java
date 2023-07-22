package se2203b.lab6.tennisballgames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddScoreController implements Initializable {

    @FXML
    Button c;
    @FXML
    Button s;
    @FXML
    TextField hScores;
    @FXML
    TextField vScores;
    @FXML
    ComboBox <String> mCombo;
    final ObservableList<String> data = FXCollections.observableArrayList();
    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;
    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        matchesAdapter = match;
        teamsAdapter = team;
        DisplayCombo();
    }
    public void DisplayCombo() {
        try {
            data.addAll(matchesAdapter.getMatchesNamesList());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
    }
    @FXML
    public void cancelBtn() {
        Stage stage = (Stage) c.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void saveBtn() {
        // Do some work here
        try {
            String [] selected = (mCombo.getValue()).split("-");
            String home = selected[1];
            String visit = selected[2];
            String m = selected[0];
            matchesAdapter.setTeamsScore(Integer.parseInt(m),Integer.parseInt(hScores.getText()),Integer.parseInt(vScores.getText()));
            teamsAdapter.setStatus(home, visit,Integer.parseInt(hScores.getText()),Integer.parseInt(vScores.getText()));


        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
        Stage stage = (Stage) c.getScene().getWindow();
        stage.close();
    }
    private void displayAlert(String msg) {
        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/se2203b/lab6/tennisballgames/WesternLogo.png"));
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mCombo.setItems(data);
    }
}
