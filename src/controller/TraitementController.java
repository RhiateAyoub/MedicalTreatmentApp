/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import model.Traitement;
import utils.AlertMessage;
import utils.SceneManager;

/**
 * Contrôleur pour la gestion des traitements médicaux dans l'application MediConnect.
 * Permet d'afficher, ajouter, modifier et supprimer des traitements.
 * 
 * @author pc
 */

public class TraitementController implements Initializable {

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
    @FXML private Button btnAjouterTraitement;
    @FXML private Button btnSupprimerTraitements;
    @FXML private Button btnExporter;
    
    // --- Conteneurs principaux
    @FXML private BorderPane mainBorderPane;
    @FXML private StackPane contentArea;
    
    // --- Organisation des vues
    @FXML private VBox traitementsListView;
    @FXML private VBox traitementsAddView;
    @FXML private VBox modifierTraitementView;
    
    // --- Tableau des traitements
    @FXML private TableView<Traitement> tableTraitements;
    @FXML private TableColumn<Traitement, Boolean> checkboxColumn;
    @FXML private TableColumn<Traitement, String> nomColumn;
    @FXML private TableColumn<Traitement, String> typeColumn;
    @FXML private TableColumn<Traitement, String> dateDebutColumn;
    @FXML private TableColumn<Traitement, String> dateFinColumn;
    @FXML private TableColumn<Traitement, String> statutColumn;
    @FXML private TableColumn<Traitement, Void> actionsColumn;
    
    // --- Bar de recherche
    @FXML private TextField searchField;
    
    // --- Select all checkbox
    @FXML private CheckBox selectAllCheckbox;
    
    // --- Formulaire d'ajout
    @FXML private ComboBox<String> comboPatient;
    @FXML private TextField inputType;
    @FXML private DatePicker inputDateDebut;
    @FXML private DatePicker inputDateFin;
    @FXML private TextArea inputDescription;
    @FXML private Button btnConfirmerAjout;
    @FXML private Button btnAnnulerAjout;
    
    // --- Formulaire d'édition
    @FXML private Label labelTraitementId;
    @FXML private ComboBox<String> comboPatientModif;
    @FXML private TextField inputNomTraitementModif;
    @FXML private TextField inputPosologieModif;
    @FXML private TextField inputTypeModif;
    @FXML private DatePicker datePickerDebutModif;
    @FXML private DatePicker datePickerFinModif;
    @FXML private CheckBox checkBoxActifModif;
    @FXML private TextArea textAreaObservationsModif;
    @FXML private Button btnConfirmerModification;
    @FXML private Button btnAnnulerModification;

    // ============================================================
    // ============== ATTRIBUTS ET STRUCTURES DE DONNÉES ==========
    // ============================================================
    // --- Propriété booléenne indiquant si au moins un traitement est actuellement sélectionné.
    private BooleanProperty anyTraitementSelected = new SimpleBooleanProperty(false);
    
    // --- Référence au traitement sélectionné pour une opération de modification.
    private Traitement traitementEnEdition;
    
    // --- List contenant les traitements
    private ObservableList<Traitement> traitementsList = FXCollections.observableArrayList();
    
    // --- List contenant les traitements correspondant à la recherche.
    private FilteredList<Traitement> filteredTraitements;
    
    // --- List contenant les patients disponibles
    private ObservableList<String> patientsList = FXCollections.observableArrayList(
        "Dupont Jean", "Lambert Sophie", "Dubois Marie", "Girard Paul"
    );
    
    // --- Formatage des dates
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ==============================================================================================================================
    // ================= MÉTHODE D'INITIALISATION (Méthode appelée automatiquement après le chargement du fichier FXML) =============
    // ==============================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuration des vues
        setupViews();
        
        // Charge les traitements avec des données d'exemple
        initializeData();

        // Configure la recherche avec filtrage dynamique
        setupSearchFilter();
        
        // Configure les éléments du tableau
        tableTraitements.setItems(filteredTraitements);
        tableTraitements.setEditable(true);
        
        comboPatient.setItems(patientsList);
        
        setupCheckboxColumn();
        setupDataColumns();
        setupActionsColumn();
        
        // Configure les interactions entre éléments de l'interface
        btnSupprimerTraitements.disableProperty().bind(anyTraitementSelected.not());
        
        tableTraitements.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateDeleteButtonState();
        });
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
    public void handleTraitementsClick(ActionEvent event) {
        traitementsListView.setVisible(true);
        traitementsAddView.setVisible(false);
        modifierTraitementView.setVisible(false);
        System.out.println("Déjà sur la page des traitements");
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
    public void handleSelectAllCheckbox(ActionEvent event) {
        boolean selectAll = selectAllCheckbox.isSelected();
        
        for (Traitement traitement : traitementsList) {
            traitement.setSelected(selectAll);
        }
        
        updateDeleteButtonState();
    }

    // --- Navigation vers le formulaire d'ajout
    @FXML
    public void handleAddTraitement(ActionEvent event) {
        comboPatient.setValue(null);
        inputType.clear();
        inputDateDebut.setValue(LocalDate.now());
        inputDateFin.setValue(LocalDate.now());
        inputDescription.setText("");
        
        traitementsListView.setVisible(false);
        traitementsAddView.setVisible(true);
        modifierTraitementView.setVisible(false);
    }
    
    // --- GESTION DE LA SUPPRESSION (événements @FXML) ========
    @FXML
    public void handleDeleteTraitement(ActionEvent event) {
        List<Traitement> selectedTraitements = new ArrayList<>();
        for (Traitement traitement : traitementsList) {
            if (traitement.isSelected()) {
                selectedTraitements.add(traitement);
            }
        }

        if (selectedTraitements.isEmpty()) {
            return;
        }

        String header;
        if (selectedTraitements.size() == 1) {
            header = "Voulez-vous vraiment supprimer ce traitement ?";
        } else {
            header = "Voulez-vous vraiment supprimer ces " + selectedTraitements.size() + " traitements ?";
        }

        String titre = "Confirmation de suppression";
        String content = "Cette action ne peut pas être annulée.";

        boolean confirmed = AlertMessage.showConfirmationAlert(titre, header, content);

        if (confirmed) {
            traitementsList.removeAll(selectedTraitements);
            selectAllCheckbox.setSelected(false);
            updateDeleteButtonState();
        }
    }

    // =======================================================================
    // ========== ACTIONS SUR LA VUE DU FORMULAIRE D'AJOUT ===================
    // =======================================================================
    @FXML
    public void handleConfirmerAjoutClick(ActionEvent event) {
        if (comboPatient.getValue().isEmpty() || inputType.getText().isEmpty() || inputDateDebut.getValue() == null) {
            AlertMessage.showErrorAlert("Erreur", "Champs vides","Veuillez remplir tous les champs obligatoires.");
            return;
        }
        
        try {
            String nomPatient = comboPatient.getValue();
            String type = inputType.getText();
            LocalDate dateDebut = inputDateDebut.getValue();
            LocalDate dateFin = inputDateFin.getValue();
            String description = inputDescription.getText();
            
            String statut = (dateFin == null || dateFin.isAfter(LocalDate.now())) ? "En cours" : "Terminé";
            
            Traitement nouveauTraitement = new Traitement(
                nomPatient,
                type,
                dateDebut,
                dateFin,
                statut,
                description
            );
            
            traitementsList.add(nouveauTraitement);
            
            traitementsListView.setVisible(true);
            traitementsAddView.setVisible(false);
            modifierTraitementView.setVisible(false);
            
        } catch (Exception e) {
            AlertMessage.showErrorAlert("Erreur", "Format de date invalide", "Format de date invalide. Utilisez le format JJ/MM/AAAA.");
        }
    }
    
    @FXML
    public void handleAnnulerAjoutClick(ActionEvent event) {
        traitementsListView.setVisible(true);
        traitementsAddView.setVisible(false);
        modifierTraitementView.setVisible(false);
    }

    // =======================================================================
    // ========== ACTIONS SUR LA VUE DU FORMULAIRE DE MODIFICATION ===========
    // =======================================================================
    @FXML
    public void handleConfirmerModificationClick(ActionEvent event) {
        if (comboPatientModif.getValue() == null || inputTypeModif.getText().isEmpty() || datePickerDebutModif.getValue() == null) {
            AlertMessage.showErrorAlert("Erreur", "Champs vides","Veuillez remplir tous les champs obligatoires.");
            return;
        }
        
        traitementEnEdition.setNomPatient(comboPatientModif.getValue());
        traitementEnEdition.setType(inputTypeModif.getText());
        traitementEnEdition.setDateDebut(datePickerDebutModif.getValue());
        traitementEnEdition.setDateFin(datePickerFinModif.getValue());
        traitementEnEdition.setStatut(checkBoxActifModif.isSelected() ? "En cours" : "Terminé");
        traitementEnEdition.setDescription(textAreaObservationsModif.getText());
        
        tableTraitements.refresh();
        
        traitementsListView.setVisible(true);
        traitementsAddView.setVisible(false);
        modifierTraitementView.setVisible(false);
    }
    
    @FXML
    public void handleAnnulerModificationClick(ActionEvent event) {
        traitementsListView.setVisible(true);
        traitementsAddView.setVisible(false);
        modifierTraitementView.setVisible(false);
    }

    // ============================================================
    // =================== MÉTHODES AUXILIAIRES ===================
    // ============================================================
    // ---- Gestion des données d'exemple ----
    private void initializeData() {
        traitementsList.add(new Traitement("Dupont Jean", "Antibiothérapie", LocalDate.of(2025, 4, 10), LocalDate.of(2025, 4, 20), "Terminé"));
        traitementsList.add(new Traitement("Lambert Sophie", "Traitement diabète", LocalDate.of(2025, 2, 14), null, "En cours"));
        traitementsList.add(new Traitement("Dubois Marie", "Radiothérapie", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 5, 30), "En cours"));
        traitementsList.add(new Traitement("Girard Paul", "Suivi post-opératoire", LocalDate.of(2025, 1, 21), LocalDate.of(2025, 3, 10), "En cours"));
        
        filteredTraitements = new FilteredList<>(traitementsList);
    }

    // ---- Gestion de la recherche et filtrage ----
    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTraitements.setPredicate(traitement -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (traitement.getNomPatient().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } 
                else if (traitement.getType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (traitement.getStatut().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                
                return false;
            });
        });
    }

    // ---- Configuration du tableau ----
    private void setupCheckboxColumn() {
        checkboxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxColumn));
        checkboxColumn.setEditable(true);
        
        tableTraitements.setEditable(true);
        
        for (Traitement patient : traitementsList) {
            patient.selectedProperty().addListener((obs, oldVal, newVal) -> {
                updateDeleteButtonState();

                boolean allSelected = true;
                for (Traitement t : traitementsList) {
                    if (!t.isSelected()) {
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
    
    private void setupDataColumns() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateDebutColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebutFormatted()));
        dateFinColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFinFormatted()));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }
    
    private void setupActionsColumn() {
        actionsColumn.setCellFactory(new Callback<TableColumn<Traitement, Void>, TableCell<Traitement, Void>>() {
            @Override
            public TableCell<Traitement, Void> call(final TableColumn<Traitement, Void> param) {
                return new TableCell<Traitement, Void>() {
                    private final Button btnView = new Button();
                    private final Button btnEdit = new Button();
                    private final Button btnDelete = new Button();
                    private final HBox actionPane = new HBox(5);
                    
                    {
                        // Configuration des icônes pour les boutons
                        ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/icons/Eye.png")));
                        viewIcon.setFitHeight(16);
                        viewIcon.setFitWidth(16);
                        btnView.setGraphic(viewIcon);
                        btnView.setStyle("-fx-background-color: transparent;");
                        
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
                        
                        btnView.setOnAction(event -> {
                            Traitement traitement = getTableView().getItems().get(getIndex());
                            viewTraitement(traitement);
                        });
                        
                        btnEdit.setOnAction(event -> {
                            Traitement traitement = getTableView().getItems().get(getIndex());
                            editTraitement(traitement);
                        });
                        
                        btnDelete.setOnAction(event -> {
                            Traitement traitement = getTableView().getItems().get(getIndex());
                            deleteTraitement(traitement);
                        });
                        
                        actionPane.getChildren().addAll(btnView, btnEdit, btnDelete);
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

    // ---- Configuration des vues ----
    private void setupViews() {
        traitementsListView.setVisible(true);
        traitementsAddView.setVisible(false);
        modifierTraitementView.setVisible(false);
        
        comboPatientModif.setItems(patientsList);
    }

    // ---- Gestion des traitements ----
    private void viewTraitement(Traitement traitement) {
        String titre = "Détails du traitement";
        String header = "Traitement de " + traitement.getNomPatient();

        String content = "Type: " + traitement.getType() + "\n" +
                         "Date de début: " + traitement.getDateDebutFormatted() + "\n" +
                         "Date de fin: " + traitement.getDateFinFormatted() + "\n" +
                         "Statut: " + traitement.getStatut();

        if (traitement.getDescription() != null && !traitement.getDescription().isEmpty()) {
            content += "\n\nDescription: " + traitement.getDescription();
        }

        AlertMessage.showInfoAlert(titre, header, content);
    }
    
    private void editTraitement(Traitement traitement) {
        traitementEnEdition = traitement;
        
        labelTraitementId.setText(String.valueOf(traitementsList.indexOf(traitement) + 1));
        comboPatientModif.setValue(traitement.getNomPatient());
        inputTypeModif.setText(traitement.getType());
        datePickerDebutModif.setValue(traitement.getDateDebut());
        datePickerFinModif.setValue(traitement.getDateFin());
        checkBoxActifModif.setSelected(traitement.getStatut().equals("En cours"));
        textAreaObservationsModif.setText(traitement.getDescription());
        inputNomTraitementModif.setText(traitement.getType());
        
        traitementsListView.setVisible(false);
        traitementsAddView.setVisible(false);
        modifierTraitementView.setVisible(true);
    }
    
    private void deleteTraitement(Traitement traitement) {
        boolean confirmed = AlertMessage.showConfirmationAlert("Confirmation de suppression", "Voulez-vous vraiment supprimer ce traitement ?", "Cette action ne peut pas être annulée."
        );

        if (confirmed) {
            traitementsList.remove(traitement);
            updateDeleteButtonState();
        }
    }

    // ---- Gestion de l'état du bouton de suppression d'un traitement ----
    private void updateDeleteButtonState() {
        boolean atLeastOneSelected = false;
        
        for (Traitement traitement : traitementsList) {
            if (traitement.isSelected()) {
                atLeastOneSelected = true;
                break;
            }
        }
        
        anyTraitementSelected.set(atLeastOneSelected);
    }

    // ============================================================
    // ============ PROPRIÉTÉS OBSERVABLES ET BINDINGS ============
    // ============================================================
    public BooleanProperty anyTraitementSelectedProperty() {
        return anyTraitementSelected;
    }
    
    public boolean isAnyTraitementSelected() {
        return anyTraitementSelected.get();
    }
}