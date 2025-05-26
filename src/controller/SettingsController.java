/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;

import utils.AlertMessage;
import utils.SceneManager;

/**
 * Contrôleur pour la gestion des paramètres dans l'application MediConnect.
 * Permet la configuration des préférences utilisateur et l'accès aux fonctionnalités système.
 * @author pc
 */


public class SettingsController implements Initializable {

    // ============================================================
    // ================= COMPOSANTS INTERFACE FXML ================
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
    @FXML private Button btnDeconnexion;
    @FXML private Button btnChangerMotDePasse;
    
    // --- Configuration
    @FXML private ComboBox<String> comboTheme;
    @FXML private ComboBox<String> comboLangue;

    // ==============================================================================================================================
    // ================= MÉTHODE D’INITIALISATION (Méthode appelée automatiquement après le chargement du fichier FXML) =============
    // ==============================================================================================================================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboTheme.setItems(FXCollections.observableArrayList("Clair", "Sombre", "Système"));
        comboTheme.setValue("Clair");

        comboLangue.setItems(FXCollections.observableArrayList("Français", "English", "Español"));
        comboLangue.setValue("Français");
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
        System.out.println("Page actuelle");
    }
    
    @FXML
    private void handleAideClick(ActionEvent event) {
        SceneManager.switchScene(btnAide, "/view/AideView.fxml", "MediConnect - Aide", 1500, 750);
        System.out.println("Navigation vers la vue Aide");
    }

    // ============================================================
    // ================= ACTIONS SUR LA VUE PRINCIPALE ============
    // ============================================================
    @FXML
    private void handleChangerMotDePasse() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Changer le mot de passe");
        alert.setHeaderText("Fonctionnalité à venir");
        alert.setContentText("La fonctionnalité de changement de mot de passe sera disponible dans une prochaine version.");
        alert.showAndWait();
    }

    @FXML
    private void handleDeconnexion() {
        boolean confirmed = AlertMessage.showConfirmationAlert("Confirmation", "Déconnexion", "Êtes-vous sûr de vouloir vous déconnecter ?");

        if (confirmed) {
            SceneManager.switchScene(btnDeconnexion, "/view/LoginView.fxml", "MediConnect - Login", 1500, 750);
            System.out.println("Navigation vers LoginView");
        }
    }
}