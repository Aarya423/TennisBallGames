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
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddMatchController implements Initializable {
    Connection connection;
    @FXML
    Button c;

    @FXML
    Button s;

    @FXML
    ComboBox <String> hCombo;

    @FXML
    ComboBox <String> vCombo;

    final ObservableList<String> data = FXCollections.observableArrayList();
    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;
    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        matchesAdapter = match;
        teamsAdapter = team;
        buildComboBoxData();
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
            matchesAdapter.insertMatch(matchesAdapter.getMax(),hCombo.getValue(),
                    vCombo.getValue());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
        Stage stage = (Stage) c.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void buildComboBoxData() {
        try {
            data.clear();
            data.addAll(teamsAdapter.getTeamsNames());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hCombo.setItems(data);
        vCombo.setItems(data);
    }

}
