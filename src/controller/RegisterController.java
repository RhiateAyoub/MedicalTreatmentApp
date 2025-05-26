/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import utils.AlertMessage;
import utils.Database;
import utils.SceneManager;

/**
 * Contrôleur pour la gestion de l'inscription dans l'application MediConnect.
 * Permet aux nouveaux utilisateurs de créer un compte pour accéder à l'application.
 * 
 * @author pc
 */
public class RegisterController {
    
    // ============================================================
    // ================= COMPOSANTS INTERFACE FXML ================
    // ============================================================
    // --- Champs de saisie
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> specialtyComboBox;
    
    // --- Actions principales
    @FXML private Button registerButton;
    @FXML private Hyperlink loginLink;
    
    // ============================================================
    // ============== ATTRIBUTS ET STRUCTURES DE DONNÉES ==========
    // ============================================================
    // --- Base de données
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    // ==============================================================================================================================
    // ================= MÉTHODE D’INITIALISATION (Méthode appelée automatiquement après le chargement du fichier FXML) =============
    // ==============================================================================================================================
    @FXML
    private void initialize() {
        ObservableList<String> specialties = FXCollections.observableArrayList("Médecin", "Secrétaire", "Autre");
        specialtyComboBox.setItems(specialties);
    }
    
    // ============================================================
    // ========== ACTIONS SUR LA VUE PRINCIPALE ===================
    // ============================================================
    // --- Inscription de l'utilisateur
    @FXML
    private void handleRegisterClick(ActionEvent event) {
        if (!validateForm()) {
            AlertMessage.showErrorAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs vides.");
            return;
        }
        
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String selectedSpecialty = specialtyComboBox.getValue();
        
        if (password.length() < 8) {
            AlertMessage.showErrorAlert("Erreur", "Mot de passe invalide", "Le mot de passe doit contenir au moins 8 caractères.");
            return;
        }
        
        connect = Database.connectDB();
        
        String checkUsernameSQL = "SELECT * FROM utilisateur WHERE username = ?";
        String insertSQL = "INSERT INTO utilisateur (username, password, role) VALUES (?, ?, ?)";
        
        try {
            prepare = connect.prepareStatement(checkUsernameSQL);
            prepare.setString(1, username);
            result = prepare.executeQuery();
            
            if (result.next()) {
                AlertMessage.showErrorAlert("Erreur", "Nom d'utilisateur existant", username + " existe déjà !");
                return;
            }
            
            prepare = connect.prepareStatement(insertSQL);
            prepare.setString(1, username);
            prepare.setString(2, password); // 🔒 Hachage recommandé pour la production
            prepare.setString(3, selectedSpecialty);
            
            int rowsInserted = prepare.executeUpdate();
            
            if (rowsInserted > 0) {
                AlertMessage.showInfoAlert("Succès", "Inscription réussie", "Inscription effectuée avec succès !");
                registerClear();
                SceneManager.switchScene(registerButton, "/view/LoginView.fxml", "MediConnect - Login", 1500, 750);
            } else {
                AlertMessage.showErrorAlert("Erreur", "Échec de l'inscription", "L'inscription a échoué. Veuillez réessayer.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            AlertMessage.showErrorAlert("Erreur", "Erreur de la base de données", "Erreur de la base de données : " + e.getMessage());
        }
    }
    
    // --- Redirection vers login
    @FXML
    private void handleLoginClick(ActionEvent event) throws IOException {
        SceneManager.switchScene(loginLink, "/view/LoginView.fxml", "MediConnect - Login", 1500, 750);
    }
    
    // ============================================================
    // =================== MÉTHODES AUXILIAIRES ===================
    // ============================================================
    // --- Validation des données
    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (isEmptyField(usernameField.getText()) || !isValidUsername(usernameField.getText())) {
            errorMessage.append("- Le username est obligatoire\n");
        }
        
        if (isEmptyField(passwordField.getText()) || passwordField.getText().length() < 6) {
            errorMessage.append("- Le mot de passe doit contenir au moins 6 caractères\n");
        }
        
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errorMessage.append("- Les mots de passe ne correspondent pas\n");
        }
        
        if (specialtyComboBox.getSelectionModel().isEmpty()) {
            errorMessage.append("- Veuillez sélectionner une spécialité\n");
        }
        
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'inscription");
            alert.setHeaderText("Veuillez corriger les erreurs suivantes :");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    // --- Vérification des champs
    private boolean isEmptyField(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z][a-zA-Z0-9._-]{2,19}$");
    }
    
    // --- Réinitialisation du formulaire
    private void registerClear() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        specialtyComboBox.setValue(null);
    }
}