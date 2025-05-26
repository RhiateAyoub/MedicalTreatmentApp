/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Patient;
import utils.AlertMessage;
import utils.Database;
import utils.SceneManager;

/**
 * Contrôleur pour la gestion des patients dans l'application MediConnect.
 * Permet de créer, lire, mettre à jour et supprimer les données patients.
 * 
 * @author pc
 */

public class PatientController {
    
    // ============================================================
    // ================ COMPOSANTS INTERFACE FXML ================
    // ============================================================
    
    // --- Navigation
    @FXML private Button btnAccueil;
    @FXML private Button btnPatients;
    @FXML private Button btnTraitements;
    @FXML private Button btnRendezVous;
    @FXML private Button btnStatistiques;
    @FXML private Button btnParametres;
    @FXML private Button btnAide;
    
    // --- Actions principales
    @FXML private Button btnExporter;
    @FXML private Button btnAjouterPatient;
    @FXML private Button btnSupprimerPatients;

    // --- Organisation des vues
    @FXML private VBox patientsListView;
    @FXML private VBox patientsAddView;
    @FXML private VBox patientsEditView;

    // --- Tableau de patients
    @FXML private TableView<Patient> tablePatients;
    @FXML private TableColumn<Patient, Boolean> checkboxColumn;
    @FXML private TableColumn<Patient, String> nomColumn;
    @FXML private TableColumn<Patient, String> prenomColumn;
    @FXML private TableColumn<Patient, String> dateNaissanceColumn;
    @FXML private TableColumn<Patient, String> sexeColumn;
    @FXML private TableColumn<Patient, Void> actionsColumn;

    // --- Bar de recherche
    @FXML private TextField searchField;
    
    // --- Select all checkbox
    @FXML private CheckBox selectAllCheckbox;

    // --- Formulaire d'ajout
    @FXML private TextField inputNom;
    @FXML private TextField inputPrenom;
    @FXML private TextField inputSecuriteSociale;
    @FXML private TextField inputTelephone;
    @FXML private TextField inputDateNaissance;
    @FXML private RadioButton radioHomme;
    @FXML private RadioButton radioFemme;

    // --- Formulaire d'édition
    @FXML private TextField editNom;
    @FXML private TextField editPrenom;
    @FXML private TextField editSecuriteSociale;
    @FXML private TextField editTelephone;
    @FXML private TextField editDateNaissance;
    @FXML private RadioButton editRadioHomme;
    @FXML private RadioButton editRadioFemme;

    // ============================================================
    // ============== ATTRIBUTS ET STRUCTURES DE DONNÉES ==========
    // ============================================================
    // --- Propriété booléenne indiquant si au moins un patient est actuellement sélectionné.
    private BooleanProperty anyPatientSelected = new SimpleBooleanProperty(false);
    
    // --- Référence au patient sélectionné pour une opération de modification.
    private Patient patientToEdit;
    
    // --- List contenant les patients
    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    
    // --- List contenant les patients correspondant à la recherche.
    private FilteredList<Patient> filteredPatients;

    // ==============================================================================================================================
    // ================= MÉTHODE D’INITIALISATION (Méthode appelée automatiquement après le chargement du fichier FXML) =============
    // ==============================================================================================================================
    @FXML
    public void initialize() {
        // Configuration des vues
        patientsListView.setVisible(true);
        patientsAddView.setVisible(false);
        patientsEditView.setVisible(false);
        
        // Charge les patients depuis la base de données
        loadPatientsFromDatabase();

        // Configure la recherche avec filtrage dynamique
        setupSearch();

        // Configure les éléments du tableau
        tablePatients.setItems(filteredPatients);
        tablePatients.setEditable(true);

        configureCheckboxColumn();
        configureDataColumns();
        configureActionsColumn();

        // Configure les interactions entre éléments de l'interface
        btnSupprimerPatients.disableProperty().bind(anyPatientSelected.not());

        tablePatients.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateDeleteButtonState();
        });
    }
    
    // =============================================================================
    // ============== ACTIONS SUR LE MENU LATÉRAL (Navigation entre les vues) ======
    // =============================================================================
    @FXML
    private void handleAccueilClick(ActionEvent event) {
        SceneManager.switchScene(btnAccueil, "/view/AcceuilView.fxml", "MediConnect - Acceuil", 1500, 750);
        System.out.println("Navigation vers la vue d'accueil");
    }

    @FXML
    private void handlePatientsClick(ActionEvent event) {
        System.out.println("Page patients actuelle");
    }

    @FXML
    private void handleTraitementsClick(ActionEvent event) {
        SceneManager.switchScene(btnTraitements, "/view/TraitementView.fxml", "MediConnect - Traitments", 1500, 750);
        System.out.println("Navigation vers la vue Traitements");
    }

    @FXML
    private void handleRendezVousClick(ActionEvent event) {
        SceneManager.switchScene(btnRendezVous, "/view/RendezVousView.fxml", "MediConnect - RendezVous", 1500, 750);
        System.out.println("Navigation vers la vue Rendez-vous");
    }

    @FXML
    private void handleStatistiquesClick(ActionEvent event) {
        SceneManager.switchScene(btnStatistiques, "/view/StatistiquesView.fxml", "MediConnect - Statistiques", 1500, 750);
        System.out.println("Navigation vers la vue Statistiques");
    }

    @FXML
    private void handleParametresClick(ActionEvent event) {
        SceneManager.switchScene(btnParametres, "/view/SettingsView.fxml", "MediConnect - Settings", 1500, 750);
        System.out.println("Navigation vers la vue Paramètres");
    }

    @FXML
    private void handleAideClick(ActionEvent event) {
        SceneManager.switchScene(btnAide, "/view/AideView.fxml", "MediConnect - Aide", 1500, 750);
        System.out.println("Navigation vers la vue Aide");
    }

    // ============================================================
    // ========== ACTIONS SUR LA VUE PRINCIPALE ===================
    // ============================================================
    
    // --- Gestion de select all
    @FXML
    public void handleSelectAllCheckbox() {
        boolean selectAll = selectAllCheckbox.isSelected();

        for (Patient patient : patients) {
            patient.setSelected(selectAll);
        }

        updateDeleteButtonState();
    }

    // --- Navigation vers le formulaire d'ajout
    @FXML
    public void handleAddPatient() {
        resetAddPatientForm();
        patientsListView.setVisible(false);
        patientsAddView.setVisible(true);
        patientsEditView.setVisible(false);
    }
    
    // --- GESTION DE LA SUPPRESSION (événements @FXML) ========
    @FXML
    public void handleDeletePatients() {
        ObservableList<Patient> patientsToRemove = FXCollections.observableArrayList();

        for (Patient patient : patients) {
            if (patient.isSelected()) {
                patientsToRemove.add(patient);
            }
        }

        if (!patientsToRemove.isEmpty()) {
            boolean confirmed = AlertMessage.showConfirmationAlert(
                "Confirmation", 
                "Suppression de patient(s)", 
                "Êtes-vous sûr de vouloir supprimer " + patientsToRemove.size() + " patient(s) ?"
            );

            if (confirmed) {
                for (Patient p : patientsToRemove) {
                    supprimerPatientDeDB(p); // <== suppression de la base de données
                }
                patients.removeAll(patientsToRemove);
                selectAllCheckbox.setSelected(false);
                updateDeleteButtonState();
                
                AlertMessage.showInfoAlert(
                    "Succès", 
                    "Suppression réussie", 
                    "Patient(s) supprimé(s) avec succès !"
                );
            }
        } else {
            AlertMessage.showErrorAlert(
                "Erreur", 
                "Aucune sélection", 
                "Aucun patient sélectionné."
            );
        }
    }
    
    // Methode pour supprimer les patients de la DB
    private void supprimerPatientDeDB(Patient patient) {
        String sql = "DELETE FROM patient WHERE nom = ? AND prenom = ?";

        try (Connection connect = Database.connectDB();
             PreparedStatement pstmt = connect.prepareStatement(sql)) {

            pstmt.setString(1, patient.getNom());
            pstmt.setString(2, patient.getPrenom());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // =======================================================================
    // ========== ACTIONS SUR LA VUE DU FORMULAIRE D'AJOUT ===================
    // =======================================================================

    @FXML
    private void handleConfirmerAjoutClick() {
        if (!validatePatientInputs(inputNom, inputPrenom, inputDateNaissance)) {
            return;
        }

        String nom = inputNom.getText().trim();
        String prenom = inputPrenom.getText().trim();
        String dateNaissance = inputDateNaissance.getText().trim();
        String tele = inputTelephone.getText().trim();
        String secu = inputSecuriteSociale.getText().trim();
        String sexe = radioHomme.isSelected() ? "Homme" : (radioFemme.isSelected() ? "Femme" : "");

        Patient newPatient = new Patient(nom, prenom, dateNaissance, sexe);
        patients.add(newPatient);
        ajouterPatientDansDB(newPatient, tele, secu);

        patientsListView.setVisible(true);
        patientsAddView.setVisible(false);

        AlertMessage.showInfoAlert(
            "Succès", 
            "Ajout réussi", 
            "Patient ajouté avec succès !"
        );
    }

    @FXML
    private void handleAnnulerAjoutClick() {
        resetAddPatientForm();
        patientsListView.setVisible(true);
        patientsAddView.setVisible(false);
    }
    
    private void ajouterPatientDansDB(Patient patient, String tele, String secu) {

       String checkSecuSQL = "SELECT * FROM patient WHERE numero_securite_sociale = ?";
       String insertSQL = "INSERT INTO patient (nom, prenom, date_naissance, sexe, numero_telephone, numero_securite_sociale) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connect = Database.connectDB();
             PreparedStatement checkStmt = connect.prepareStatement(checkSecuSQL);
             PreparedStatement insertStmt = connect.prepareStatement(insertSQL)) {

            checkStmt.setString(1, secu);
            ResultSet result = checkStmt.executeQuery();
            if (result.next()) {
                AlertMessage.showErrorAlert(
                "Erreur", 
                "Patient déjà existant", 
                secu + " existe déjà !");
                return;
            }

            insertStmt.setString(1, patient.getNom());
            insertStmt.setString(2, patient.getPrenom());
            insertStmt.setString(3, patient.getDateNaissance());
            insertStmt.setString(4, patient.getSexe());
            insertStmt.setString(5, tele);
            insertStmt.setString(6, secu);

            insertStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            AlertMessage.showErrorAlert(
            "Erreur", 
            "Erreur BDD : ", 
            e.getMessage());
        }
    }

    // =======================================================================
    // ========== ACTIONS SUR LA VUE DU FORMULAIRE DE MODIFICATION ===========
    // =======================================================================
    @FXML
    private void handleConfirmerModificationClick() {
        if (!validatePatientInputs(editNom, editPrenom, editDateNaissance)) {
            return;
        }

        patientToEdit.setNom(editNom.getText().trim());
        patientToEdit.setPrenom(editPrenom.getText().trim());
        patientToEdit.setDateNaissance(editDateNaissance.getText().trim());
        patientToEdit.setSexe(editRadioHomme.isSelected() ? "Homme" : (editRadioFemme.isSelected() ? "Femme" : ""));

        tablePatients.refresh();

        patientsListView.setVisible(true);
        patientsEditView.setVisible(false);

        AlertMessage.showInfoAlert(
            "Succès", 
            "Modification réussie", 
            "Patient modifié avec succès !"
        );
    }

    @FXML
    private void handleAnnulerModificationClick() {
        patientsListView.setVisible(true);
        patientsEditView.setVisible(false);
    }

    // ============================================================
    // =================== MÉTHODES AUXILIAIRES ===================
    // ============================================================
    // ---- Gestion de la base de données ----
    private void loadPatientsFromDatabase() {
        patients.clear();
        String url = "jdbc:sqlite:src/utils/MedicalTreatmentApp_DB.db";

        String query = "SELECT nom, prenom, date_naissance, sexe FROM patient";

        try (Connection connect = DriverManager.getConnection(url);
             PreparedStatement prepare = connect.prepareStatement(query);
             ResultSet result = prepare.executeQuery();){
            
            while (result.next()) {
                String nom = result.getString("nom");
                String prenom = result.getString("prenom");
                String dateNaissance = result.getString("date_naissance");
                String sexe = result.getString("sexe");

                patients.add(new Patient(nom, prenom, dateNaissance, sexe));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertMessage.showErrorAlert("Erreur", "Erreur de la base de données", 
                "Erreur de la base de données : " + e.getMessage());
        }
    }

    // ---- Gestion de la recherche et filtrage ----
    private void setupSearch() {
        filteredPatients = new FilteredList<>(patients, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPatients.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return patient.getNom().toLowerCase().contains(lowerCaseFilter) ||
                       patient.getPrenom().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }
    
    // ---- Configuration du tableau ----
    private void configureCheckboxColumn() {
        checkboxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxColumn));
        checkboxColumn.setEditable(true);

        for (Patient patient : patients) {
            patient.selectedProperty().addListener((obs, oldVal, newVal) -> {
                updateDeleteButtonState();

                boolean allSelected = true;
                for (Patient p : patients) {
                    if (!p.isSelected()) {
                        allSelected = false;
                        break;
                    }
                }

                if (selectAllCheckbox.isSelected() != allSelected) {
                    selectAllCheckbox.setSelected(allSelected);
                }
            });
        }
    }

    private void configureDataColumns() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        sexeColumn.setCellValueFactory(new PropertyValueFactory<>("sexe"));
    }

    private void configureActionsColumn() {
        Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Patient, Void> call(final TableColumn<Patient, Void> param) {
                return new TableCell<>() {
                    private final HBox actionsPane = new HBox(5);
                    private final Button btnView = createActionButton("view");
                    private final Button btnEdit = createActionButton("edit");
                    private final Button btnDelete = createActionButton("delete");

                    {
                        actionsPane.setAlignment(Pos.CENTER);
                        actionsPane.getChildren().addAll(btnView, btnEdit, btnDelete);

                        btnView.setOnAction((ActionEvent event) -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            System.out.println("Voir les détails de " + patient.getNom());
                            // TODO: Implémenter la vue détaillée
                        });

                        btnEdit.setOnAction((ActionEvent event) -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            showEditPatientForm(patient);
                        });

                        btnDelete.setOnAction((ActionEvent event) -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            deletePatient(patient);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : actionsPane);
                    }
                };
            }
        };

        actionsColumn.setCellFactory(cellFactory);
    }

    private Button createActionButton(String type) {
        Button btn = new Button();
        btn.getStyleClass().add("action-button");
        btn.setStyle("-fx-background-color: transparent;");
        
        ImageView icon = new ImageView();
        icon.setFitWidth(16);
        icon.setFitHeight(16);
        icon.setPreserveRatio(true);
        
        switch (type) {
            case "view":
                icon.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/resources/icons/Eye.png")));
                break;
            case "edit":
                icon.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/resources/icons/Edit 3.png")));
                break;
            case "delete":
                icon.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/resources/icons/Trash 3.png")));
                break;
        }
        
        btn.setGraphic(icon);
        return btn;
    }
    
    // ---- Gestion du formulaire d'ajout ----
    private void resetAddPatientForm() {
        inputNom.clear();
        inputPrenom.clear();
        inputSecuriteSociale.clear();
        inputTelephone.clear();
        inputDateNaissance.clear();
        
        radioHomme.setSelected(false);
        radioFemme.setSelected(false);
    }

    // ---- Gestion du formulaire de modification ----
    private void showEditPatientForm(Patient patient) {
        patientToEdit = patient;
        
        editNom.setText(patient.getNom());
        editPrenom.setText(patient.getPrenom());
        editDateNaissance.setText(patient.getDateNaissance());
        
        if ("Homme".equals(patient.getSexe())) {
            editRadioHomme.setSelected(true);
        } else if ("Femme".equals(patient.getSexe())) {
            editRadioFemme.setSelected(true);
        }
        
        patientsListView.setVisible(false);
        patientsAddView.setVisible(false);
        patientsEditView.setVisible(true);
    }

    // ---- Gestion de la suppression ----
    private void deletePatient(Patient patient) {
        boolean confirmed = AlertMessage.showConfirmationAlert(
            "Confirmation", 
            "Suppression de patient", 
            "Êtes-vous sûr de vouloir supprimer " + patient.getNom() + " " + patient.getPrenom() + " ?"
        );

        if (confirmed) {
            patients.remove(patient);
            AlertMessage.showInfoAlert(
                "Succès", 
                "Suppression réussie", 
                "Patient supprimé avec succès !"
            );
        }
    }

    // ---- Validation des formulaires ----
    private boolean validatePatientInputs(TextField nom, TextField prenom, TextField dateNaissance) {
        StringBuilder errorMessage = new StringBuilder();

        if (nom.getText().trim().isEmpty()) {
            errorMessage.append("- Le nom est obligatoire\n");
        }

        if (prenom.getText().trim().isEmpty()) {
            errorMessage.append("- Le prénom est obligatoire\n");
        }

        if (dateNaissance.getText().trim().isEmpty()) {
            errorMessage.append("- La date de naissance est obligatoire\n");
        } else {
            // Validation du format de date (JJ/MM/AAAA)
            String datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
            if (!dateNaissance.getText().trim().matches(datePattern)) {
                errorMessage.append("- Format de date invalide (JJ/MM/AAAA)\n");
            }
        }

        if (errorMessage.length() > 0) {
            AlertMessage.showErrorAlert(
                "Erreur", 
                "Validation échouée", 
                "Veuillez corriger les erreurs suivantes :\n" + errorMessage
            );
            return false;
        }

        return true;
    }
    
    // ---- Gestion de l'état du bouton de suppression d'un patient ----
    private void updateDeleteButtonState() {
        boolean atLeastOneSelected = false;

        for (Patient patient : patients) {
            if (patient.isSelected()) {
                atLeastOneSelected = true;
                break;
            }
        }

        anyPatientSelected.set(atLeastOneSelected);
    }

    // ============================================================
    // ============ PROPRIÉTÉS OBSERVABLES ET BINDINGS ============
    // ============================================================
    public BooleanProperty anyPatientSelectedProperty() {
        return anyPatientSelected;
    }

    public boolean isAnyPatientSelected() {
        return anyPatientSelected.get();
    }
}