package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Patient;


public class PatientController {

    @FXML
    private TableView<Patient> tablePatients;

    @FXML
    private TableColumn<Patient, Boolean> selectColumn;

    @FXML
    private TableColumn<Patient, String> colNom;

    @FXML
    private TableColumn<Patient, String> colPrenom;

    @FXML
    private TableColumn<Patient, String> colNaissance;

    @FXML
    private TableColumn<Patient, String> colSexe;

    @FXML
    private void initialize() {
        // Set up selection checkbox
        selectColumn.setCellValueFactory(param -> new SimpleBooleanProperty(false));
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        // Set up patient columns
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));

        // Sample data (you can replace this with real DB data)
        ObservableList<Patient> data = FXCollections.observableArrayList(
            new Patient("Khadija", "Bennani", "1990-02-15", "Femme"),
            new Patient("Ali", "Ouazani", "1985-07-22", "Homme")
        );

        tablePatients.setItems(data);
    }

}
