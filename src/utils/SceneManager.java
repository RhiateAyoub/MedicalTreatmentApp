/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe utilitaire pour gérer les transitions entre les différentes scènes de l'application
 * Facilite la navigation entre les vues dans l'application MediConnect
 * @author pc
 */

public class SceneManager {

    // ========== Navigation entre les scènes ==========
    /**
     * Permet de passer à une nouvelle scène.
     *
     * @param triggerNode Un nœud de la scène actuelle (ex: un bouton) utilisé pour obtenir la fenêtre actuelle.
     * @param fxmlPath Chemin vers le fichier FXML (ex: "/view/RegisterView.fxml").
     * @param title Titre de la fenêtre après le changement de scène.
     * @param width Largeur souhaitée pour la nouvelle scène.
     * @param height Hauteur souhaitée pour la nouvelle scène.
     */
    public static void switchScene(Node triggerNode, String fxmlPath, String title, double width, double height) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            // Récupérer la fenêtre actuelle à partir du nœud déclencheur
            Stage stage = (Stage) triggerNode.getScene().getWindow();
            
            // Créer et configurer la nouvelle scène
            Scene newScene = new Scene(root, width, height);
            
            // Définir la nouvelle scène et mettre à jour le titre de la fenêtre
            stage.setScene(newScene);
            stage.setTitle(title);
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertMessage.showErrorAlert("Erreur", "Erreur de navigation", "Impossible de charger la vue : " + e.getMessage());
        }
    }
}
