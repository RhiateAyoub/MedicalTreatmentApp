/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import model.RendezVous;
import utils.AlertMessage;
import utils.SceneManager;

/**
 * Contrôleur pour la gestion des rendez-vous dans l'application MediConnect.
 * Permet la création, modification et suppression des rendez-vous patient.
 * 
 * @author pc
 */

public class RendezVousController implements Initializable {
    
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
    @FXML private Button btnPlanifier;
    @FXML private Button btnSupprimer;
    
    // --- Organisation des vues
    @FXML private VBox rendezVousListView;
    @FXML private VBox planifierRendezVousView;
    @FXML private VBox modifierRendezVousView;

    // --- Tableau de rendez-vous
    @FXML private TableView<RendezVous> tableRendezVous;
    @FXML private TableColumn<RendezVous, Boolean> colCheckbox;
    @FXML private TableColumn<RendezVous, Integer> colId;
    @FXML private TableColumn<RendezVous, String> colNom;
    @FXML private TableColumn<RendezVous, LocalDate> colDate;
    @FXML private TableColumn<RendezVous, LocalTime> colHeure;
    @FXML private TableColumn<RendezVous, Void> colActions;

    // --- Bar de recherche
    @FXML private TextField searchField;
    
    // --- Select all checkbox
    @FXML private CheckBox selectAllCheckbox;
    
    // --- Formulaire de planification
    @FXML private ComboBox<String> comboPatient;
    @FXML private DatePicker datePickerRendezVous;
    @FXML private TextField inputHeure;
    @FXML private TextField inputMotif;
    @FXML private TextArea textAreaCommentaire;
    @FXML private Button btnConfirmerPlanification;
    @FXML private Button btnAnnulerPlanification;
    
    // --- Formulaire de modification
    @FXML private Label labelRendezVousId;
    @FXML private ComboBox<String> comboPatientModif;
    @FXML private DatePicker datePickerRendezVousModif;
    @FXML private TextField inputHeureModif;
    @FXML private TextField inputMotifModif;
    @FXML private TextArea textAreaCommentaireModif;
    @FXML private Button btnConfirmerModification;
    @FXML private Button btnAnnulerModification;
    
    // ============================================================
    // ============== ATTRIBUTS ET STRUCTURES DE DONNÉES ==========
    // ============================================================
    
    // --- Collections de données
    private ObservableList<RendezVous> rendezVousList = FXCollections.observableArrayList();
    private FilteredList<RendezVous> filteredRendezVous;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private RendezVous rendezVousEnModification;
    
    // --- Données de référence
    private final ObservableList<String> patientsList = FXCollections.observableArrayList(
        "Martin Dupont", "Sophie Leclerc", "Thomas Bernard", "Julie Moreau", 
        "Laurent Garcia", "Emma Dubois", "Lucas Martinez", "Camille Petit"
    );

    // ==============================================================================================================================
    // ================= MÉTHODE D'INITIALISATION (Méthode appelée automatiquement après le chargement du fichier FXML) =============
    // ==============================================================================================================================
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        chargerDonneesTest();
        setupSearch();
        
        comboPatient.setItems(patientsList);
        comboPatientModif.setItems(patientsList);
        
        btnExporter.setOnAction(event -> exporterRendezVous());
        
        btnSupprimer.setDisable(true);
        updateDeleteButtonState();
        
        tableRendezVous.setOnMouseClicked(event -> updateDeleteButtonState());
        
        datePickerRendezVous.setValue(LocalDate.now());
    }
    
    // =============================================================================
    // ============== ACTIONS SUR LE MENU LATÉRAL (Navigation entre les vues) ======
    // =============================================================================
    
    @FXML
    private void handleAccueilClick(ActionEvent event) {
        SceneManager.switchScene(btnAccueil, "/view/AcceuilView.fxml", "MediConnect - Acceuil", 1500, 750);
        System.out.println("Navigation vers la vue d'acceuil");
    }
    
    @FXML
    private void handlePatientsClick(ActionEvent event) {
        SceneManager.switchScene(btnPatients, "/view/PatientView.fxml", "MediConnect - Patients", 1500, 750);
        System.out.println("Navigation vers la vue Patients");
    }
    
    @FXML
    private void handleTraitementsClick(ActionEvent event) {
        SceneManager.switchScene(btnTraitements, "/view/TraitementView.fxml", "MediConnect - Traitments", 1500, 750);
        System.out.println("Navigation vers la vue Traitements");
    }
    
    @FXML
    private void handleRendezVousClick(ActionEvent event) {
        System.out.println("Page actuelle");
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
    private void handleAideClick() {
        SceneManager.switchScene(btnAide, "/view/AideView.fxml", "MediConnect - Aide", 1500, 750);
        System.out.println("Navigation vers la vue Aide");
    }

    // ============================================================
    // ========== ACTIONS SUR LA VUE PRINCIPALE ===================
    // ============================================================
    
    // --- Gestion de select all
    @FXML
    void handleSelectAllCheckbox(ActionEvent event) {
        boolean selectAll = selectAllCheckbox.isSelected();
        
        for (RendezVous rdv : rendezVousList) {
            rdv.setSelected(selectAll);
        }
        
        updateDeleteButtonState();
    }

    // --- Navigation vers le formulaire de planification
    @FXML
    void handlePlanifierClick(ActionEvent event) {
        rendezVousListView.setVisible(false);
        modifierRendezVousView.setVisible(false);
        planifierRendezVousView.setVisible(true);
        
        comboPatient.setValue(null);
        datePickerRendezVous.setValue(LocalDate.now());
        inputHeure.setText("");
        inputMotif.setText("");
        textAreaCommentaire.setText("");
    }
    
    // --- GESTION DE LA SUPPRESSION (événements @FXML) ========
    @FXML
    void handleSupprimerClick(ActionEvent event) {
        long selectedCount = rendezVousList.stream().filter(RendezVous::isSelected).count();
        
        if (selectedCount == 0) {
            AlertMessage.showInfoAlert("Information", "Aucun rendez-vous sélectionné", 
                                   "Veuillez sélectionner au moins un rendez-vous à supprimer.");
            return;
        }
        
        String message = selectedCount == 1 
                       ? "Êtes-vous sûr de vouloir supprimer le rendez-vous sélectionné ?" 
                       : "Êtes-vous sûr de vouloir supprimer ces " + selectedCount + " rendez-vous ?";
        
        boolean result = AlertMessage.showConfirmationAlert("Confirmation", 
                                                                    "Suppression de rendez-vous", 
                                                                    message);
        
        if (result) {
            List<RendezVous> toRemove = new ArrayList<>();
            
            for (RendezVous rdv : rendezVousList) {
                if (rdv.isSelected()) {
                    toRemove.add(rdv);
                }
            }
            
            rendezVousList.removeAll(toRemove);
            
            selectAllCheckbox.setSelected(false);
            
            updateDeleteButtonState();
            
            AlertMessage.showInfoAlert("Succès", "Suppression réussie", 
                                   selectedCount + " rendez-vous ont été supprimés avec succès.");
        }
    }

    // =======================================================================
    // ========== ACTIONS SUR LA VUE DU FORMULAIRE DE PLANIFICATION ==========
    // =======================================================================
    
    @FXML
    void handleConfirmerPlanificationClick(ActionEvent event) {
        if (!validerFormulairePlanification()) {
            return;
        }
        
        try {
            String nomPatient = comboPatient.getValue();
            LocalDate date = datePickerRendezVous.getValue();
            LocalTime heure = LocalTime.parse(inputHeure.getText(), timeFormatter);
            
            int nouveauId = rendezVousList.isEmpty() ? 1 : rendezVousList.get(rendezVousList.size() - 1).getId() + 1;
            
            RendezVous nouveauRdv = new RendezVous(nouveauId, nomPatient, date, heure);
            
            ajouterRendezVous(nouveauRdv);
            
            planifierRendezVousView.setVisible(false);
            rendezVousListView.setVisible(true);
            
        } catch (DateTimeParseException e) {
            AlertMessage.showErrorAlert("Erreur", "Format d'heure invalide", "Veuillez entrer l'heure au format HH:MM.");
        }
    }
    
    @FXML
    void handleAnnulerPlanificationClick(ActionEvent event) {
        planifierRendezVousView.setVisible(false);
        rendezVousListView.setVisible(true);
    }

    // =======================================================================
    // ========== ACTIONS SUR LA VUE DU FORMULAIRE DE MODIFICATION ===========
    // =======================================================================
    
    @FXML
    void handleConfirmerModificationClick(ActionEvent event) {
        if (!validerFormulaireModification()) {
            return;
        }
        
        try {
            String nomPatient = comboPatientModif.getValue();
            LocalDate date = datePickerRendezVousModif.getValue();
            LocalTime heure = LocalTime.parse(inputHeureModif.getText(), timeFormatter);
            
            mettreAJourRendezVous(rendezVousEnModification, nomPatient, date, heure);
            
            modifierRendezVousView.setVisible(false);
            rendezVousListView.setVisible(true);
            
        } catch (DateTimeParseException e) {
            AlertMessage.showErrorAlert("Erreur", "Format d'heure invalide", 
                              "Veuillez entrer l'heure au format HH:MM.");
        }
    }
    
    @FXML
    void handleAnnulerModificationClick(ActionEvent event) {
        modifierRendezVousView.setVisible(false);
        rendezVousListView.setVisible(true);
    }

    // ============================================================
    // =================== MÉTHODES AUXILIAIRES ===================
    // ============================================================
    
    // ---- Gestion de la recherche et filtrage ----
    private void setupSearch() {
        filteredRendezVous = new FilteredList<>(rendezVousList, p -> true);
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRendezVous.setPredicate(createSearchPredicate(newValue));
        });
        
        tableRendezVous.setItems(filteredRendezVous);
    }
    
    private Predicate<RendezVous> createSearchPredicate(String searchText) {
        return rendezVous -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            
            String lowerCaseFilter = searchText.toLowerCase();
            
            if (rendezVous.getNomPatient().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            
            if (String.valueOf(rendezVous.getId()).contains(lowerCaseFilter)) {
                return true;
            }
            
            if (rendezVous.getDate().toString().contains(lowerCaseFilter) ||
                rendezVous.getHeure().toString().contains(lowerCaseFilter)) {
                return true;
            }
            
            return false;
        };
    }
    
    // ---- Configuration du tableau ----
    private void setupTableColumns() {
        colCheckbox.setCellValueFactory(cellData -> {
            RendezVous rdv = cellData.getValue();
            BooleanProperty selected = rdv.selectedProperty();
            
            selected.addListener((obs, oldVal, newVal) -> updateDeleteButtonState());
            
            return selected;
        });
        colCheckbox.setCellFactory(col -> new CheckBoxTableCell<>());
        colCheckbox.setEditable(true);
        
        tableRendezVous.setEditable(true);
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colHeure.setCellValueFactory(new PropertyValueFactory<>("heure"));
        
        setupActionsColumn();
    }
    
    private void setupActionsColumn() {
        colActions.setCellFactory(new Callback<TableColumn<RendezVous, Void>, TableCell<RendezVous, Void>>() {
            @Override
            public TableCell<RendezVous, Void> call(final TableColumn<RendezVous, Void> param) {
                return new TableCell<RendezVous, Void>() {
                    private final Button btnEdit = new Button();
                    private final Button btnDelete = new Button();
                    private final HBox actionPane = new HBox(5);

                    {
                        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/icons/Edit 3.png")));
                        editIcon.setFitHeight(16);
                        editIcon.setFitWidth(16);
                        btnEdit.setGraphic(editIcon);
                        btnEdit.setStyle("-fx-background-color: transparent;");

                        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/icons/Trash 3.png")));
                        deleteIcon.setFitHeight(16);
                        deleteIcon.setFitWidth(16);
                        btnDelete.setGraphic(deleteIcon);
                        btnDelete.setStyle("-fx-background-color: transparent;");

                        btnEdit.setOnAction(event -> {
                            RendezVous rdv = getTableView().getItems().get(getIndex());
                            modifierRendezVous(rdv);
                        });

                        btnDelete.setOnAction(event -> {
                            RendezVous rdv = getTableView().getItems().get(getIndex());
                            supprimerRendezVous(rdv);
                        });

                        actionPane.getChildren().addAll(btnEdit, btnDelete);
                        actionPane.setAlignment(Pos.CENTER);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(actionPane);
                        }
                    }
                };
            }
        });
    }
    
    // ---- Gestion des données test ----
    private void chargerDonneesTest() {
        rendezVousList.add(new RendezVous(1, "Martin Dupont", LocalDate.of(2025, 5, 20), LocalTime.of(9, 0)));
        rendezVousList.add(new RendezVous(2, "Sophie Leclerc", LocalDate.of(2025, 5, 20), LocalTime.of(10, 30)));
        rendezVousList.add(new RendezVous(3, "Thomas Bernard", LocalDate.of(2025, 5, 21), LocalTime.of(14, 15)));
        rendezVousList.add(new RendezVous(4, "Julie Moreau", LocalDate.of(2025, 5, 22), LocalTime.of(11, 0)));
        rendezVousList.add(new RendezVous(5, "Laurent Garcia", LocalDate.of(2025, 5, 23), LocalTime.of(16, 30)));
        
        tableRendezVous.setItems(rendezVousList);
        
        for (RendezVous r : rendezVousList) {
            attachRendezVousListener(r);
        }
        
        rendezVousList.addListener((ListChangeListener<RendezVous>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (RendezVous r : change.getAddedSubList()) {
                        attachRendezVousListener(r);
                    }
                }
            }
        });
    }
    
    private void attachRendezVousListener(RendezVous rdv) {
        rdv.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateDeleteButtonState();
            
            boolean allSelected = true;
            for (RendezVous r : rendezVousList) {
                if (!r.isSelected()) {
                    allSelected = false;
                    break;
                }
            }
            
            if (selectAllCheckbox.isSelected() != allSelected) {
                selectAllCheckbox.setSelected(allSelected);
            }
        });
    }
    
    // ---- Gestion du formulaire de planification ----
    private boolean validerFormulairePlanification() {
        StringBuilder erreurs = new StringBuilder();
        
        if (comboPatient.getValue() == null || comboPatient.getValue().trim().isEmpty()) {
            erreurs.append("- Veuillez sélectionner un patient.\n");
        }
        
        if (datePickerRendezVous.getValue() == null) {
            erreurs.append("- Veuillez sélectionner une date.\n");
        }
        
        if (inputHeure.getText() == null || inputHeure.getText().trim().isEmpty()) {
            erreurs.append("- Veuillez saisir une heure.\n");
        } else {
            try {
                LocalTime.parse(inputHeure.getText(), timeFormatter);
            } catch (DateTimeParseException e) {
                erreurs.append("- Format d'heure invalide. Utilisez le format HH:MM.\n");
            }
        }
        
        if (erreurs.length() > 0) {
            AlertMessage.showErrorAlert("Erreur de validation", "Veuillez corriger les erreurs suivantes:", 
                              erreurs.toString());
            return false;
        }
        
        return true;
    }

    // ---- Gestion du formulaire de modification ----
    private boolean validerFormulaireModification() {
        StringBuilder erreurs = new StringBuilder();
        
        if (comboPatientModif.getValue() == null || comboPatientModif.getValue().trim().isEmpty()) {
            erreurs.append("- Veuillez sélectionner un patient.\n");
        }
        
        if (datePickerRendezVousModif.getValue() == null) {
            erreurs.append("- Veuillez sélectionner une date.\n");
        }
        
        if (inputHeureModif.getText() == null || inputHeureModif.getText().trim().isEmpty()) {
            erreurs.append("- Veuillez saisir une heure.\n");
        } else {
            try {
                LocalTime.parse(inputHeureModif.getText(), timeFormatter);
            } catch (DateTimeParseException e) {
                erreurs.append("- Format d'heure invalide. Utilisez le format HH:MM.\n");
            }
        }
        
        if (erreurs.length() > 0) {
            AlertMessage.showErrorAlert("Erreur de validation", "Veuillez corriger les erreurs suivantes:", 
                              erreurs.toString());
            return false;
        }
        
        return true;
    }
    
    // ---- Gestion de la suppression ----
    private void supprimerRendezVous(RendezVous rdv) {
        boolean result = AlertMessage.showConfirmationAlert("Confirmation", 
                                                                   "Suppression de rendez-vous", 
                                                                   "Êtes-vous sûr de vouloir supprimer ce rendez-vous ?");
        
        if (result) {
            rendezVousList.remove(rdv);
            
            AlertMessage.showInfoAlert("Succès", "Suppression réussie", 
                                   "Le rendez-vous a été supprimé avec succès.");
        }
    }

    // ---- Gestion de l'état du bouton de suppression ----
    private void updateDeleteButtonState() {
        boolean hasSelectedItems = rendezVousList.stream().anyMatch(RendezVous::isSelected);
        btnSupprimer.setDisable(!hasSelectedItems);
    }
    
    // ---- Manipulation des données ----
    public void ajouterRendezVous(RendezVous nouveauRdv) {
        rendezVousList.add(nouveauRdv);
        
        tableRendezVous.refresh();
        
        AlertMessage.showInfoAlert("Succès", "Ajout réussi", 
                               "Le rendez-vous a été planifié avec succès.");
    }
    
    private void modifierRendezVous(RendezVous rdv) {
        rendezVousEnModification = rdv;
        
        labelRendezVousId.setText(String.valueOf(rdv.getId()));
        comboPatientModif.setValue(rdv.getNomPatient());
        datePickerRendezVousModif.setValue(rdv.getDate());
        inputHeureModif.setText(rdv.getHeure().format(timeFormatter));
        
        rendezVousListView.setVisible(false);
        planifierRendezVousView.setVisible(false);
        modifierRendezVousView.setVisible(true);
    }
    
    public void mettreAJourRendezVous(RendezVous rdv, String nouveauNom, LocalDate nouvelleDate, LocalTime nouvelleHeure) {
        rdv.setNomPatient(nouveauNom);
        rdv.setDate(nouvelleDate);
        rdv.setHeure(nouvelleHeure);
        
        tableRendezVous.refresh();
        
        AlertMessage.showInfoAlert("Succès", "Modification réussie", 
                               "Le rendez-vous a été modifié avec succès.");
    }
    
    private void exporterRendezVous() {
        AlertMessage.showInfoAlert("Information", "Exportation", 
                               "La fonctionnalité d'exportation sera implémentée prochainement.");
    }
}