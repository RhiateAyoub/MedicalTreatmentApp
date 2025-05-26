/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import model.RendezVous;
import utils.SceneManager;

/**
 * Contrôleur de la vue d'accueil (tableau de bord)
 * Gère l'affichage et les interactions sur la page principale de l'application
 * 
 * @author pc
 */
public class AccueilController implements Initializable {

    // ============================================================
    // ================ COMPOSANTS INTERFACE FXML =================
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
    @FXML private Button btnAjouterTraitement;
    @FXML private Button btnPlanifierRendezVous;

    // --- Affichage des données
    @FXML private Label lblTotalPatients;
    @FXML private Label lblTraitementsActifs;
    @FXML private Label lblProchainsRdv;

    // --- Tableau de rendez-vous
    @FXML private TableView<RendezVous> tableRendezVous;
    @FXML private TableColumn<RendezVous, String> colNomPatient;
    @FXML private TableColumn<RendezVous, String> colDate;
    @FXML private TableColumn<RendezVous, String> colType;

    // ============================================================
    // ============== ATTRIBUTS ET STRUCTURES DE DONNÉES ==========
    // ============================================================
    // --- List contenant les rendez-vous
    private ObservableList<RendezVous> rendezVousList = FXCollections.observableArrayList();

    // ==============================================================================================================================
    // ================= MÉTHODE D’INITIALISATION (Méthode appelée automatiquement après le chargement du fichier FXML) =============
    // ==============================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuration des colonnes de la table des rendez-vous
        colNomPatient.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Chargement des données de test
        chargerDonneesTest();

        // Mise à jour des statistiques affichées sur le tableau de bord
        mettreAJourStatistiques();

        // Attacher les données à la table
        tableRendezVous.setItems(rendezVousList);
    }
    
    // =============================================================================
    // ============== ACTIONS SUR LE MENU LATÉRAL (Navigation entre les vues) ======
    // =============================================================================
    @FXML
    private void handleAccueilClick(ActionEvent event) {
        System.out.println("Page actuelle");
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
    // --- Exporter
    @FXML
    private void handleExporterClick(ActionEvent event) {
        System.out.println("Exportation des données");
    }

    // --- Accées rapide
    @FXML
    private void handleAjouterPatientClick(ActionEvent event) {
        SceneManager.switchScene(btnPatients, "/view/PatientView.fxml", "MediConnect - Patients", 1500, 750);
        System.out.println("Ajout d'un nouveau patient");
    }

    @FXML
    private void handleAjouterTraitementClick(ActionEvent event) {
        SceneManager.switchScene(btnTraitements, "/view/TraitementView.fxml", "MediConnect - Traitments", 1500, 750);
        System.out.println("Ajout d'un nouveau traitement");
    }

    @FXML
    private void handlePlanifierRendezVousClick(ActionEvent event) {
        SceneManager.switchScene(btnRendezVous, "/view/RendezVousView.fxml", "MediConnect - RendezVous", 1500, 750);
        System.out.println("Planification d'un nouveau rendez-vous");
    }
    // ============================================================
    // =================== MÉTHODES AUXILIAIRES ===================
    // ============================================================

    // --- Chargement des données de démonstration
    private void chargerDonneesTest() {
        rendezVousList.add(new RendezVous("Dupont Jean", LocalDate.of(2025, 06, 05), "Consultation"));
        rendezVousList.add(new RendezVous("Lambert Sophie", LocalDate.of(2025, 07, 05), "Suivi traitement"));
        rendezVousList.add(new RendezVous("Dubois Marie", LocalDate.of(2025, 10, 05), "Contrôle tension"));
    }

    // --- Calcul des statistiques affichées
    private void mettreAJourStatistiques() {
        lblTotalPatients.setText("9,024");
        lblTraitementsActifs.setText("1,405");
        lblProchainsRdv.setText(String.valueOf(rendezVousList.size()));
    }
}
