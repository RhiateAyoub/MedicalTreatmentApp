/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

import utils.SceneManager;

/**
 * Contrôleur pour la gestion des statistiques dans l'application MediConnect.
 * Affiche les données statistiques et graphiques des patients et traitements.
 * 
 * @author pc
 */

public class StatistiquesController implements Initializable {

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
    @FXML private Button btnOptions;
    
    // --- Affichage des données
    @FXML private Label lblTotalPatients;
    @FXML private Label lblTraitementsEnCours;
    @FXML private Label lblTraitementsTermines;
    @FXML private Label lblRdvMois;
    
    // --- Graphiques
    @FXML private StackPane barChartContainer;
    
    // ============================================================
    // ============== ATTRIBUTS ET STRUCTURES DE DONNÉES ==========
    // ============================================================
    
    private BarChart<String, Number> barChartTraitementsMois;

    // ==============================================================================================================================
    // ================= MÉTHODE D’INITIALISATION (Méthode appelée automatiquement après le chargement du fichier FXML) =============
    // ==============================================================================================================================
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Création des axes pour le graphique
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setSide(Side.BOTTOM);
        yAxis.setSide(Side.LEFT);

        // Création du graphique à barres avec les axes configurés
        barChartTraitementsMois = new BarChart<>(xAxis, yAxis);
        barChartTraitementsMois.setLegendVisible(false);
        
        // Ajout du graphique au conteneur StackPane
        barChartContainer.getChildren().add(barChartTraitementsMois);
        
        // Chargement des données statistiques
        initStatistiques();
        
        // Configuration du graphique avec les données mensuelles
        initBarChart();
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
        System.out.println("Page actuelle");
    }

    @FXML
    private void handleParametresClick(ActionEvent event) {
        SceneManager.switchScene(btnParametres, "/view/SettingsView.fxml", "MediConnect - Settings", 1500, 750);
        System.out.println("Navigation vers la vue ParamÃ¨tres");
    }
    
    @FXML
    private void handleAideClick() {
        SceneManager.switchScene(btnAide, "/view/AideView.fxml", "MediConnect - Aide", 1500, 750);
        System.out.println("Navigation vers la vue Aide");
    }
    
    // ============================================================
    // ========== ACTIONS SUR LA VUE PRINCIPALE ===================
    // ============================================================
    
    @FXML
    void handleExporterClick(ActionEvent event) {
        System.out.println("Exportation des statistiques");
    }
    
    @FXML
    void handleOptionsClick(ActionEvent event) {
        System.out.println("Affichage des options");
    }
    
    // ============================================================
    // =================== MÉTHODES AUXILIAIRES ===================
    // ============================================================
    
    private void initStatistiques() {
        lblTotalPatients.setText("9,024");
        lblTraitementsEnCours.setText("1,405");
        lblTraitementsTermines.setText("8,353");
        lblRdvMois.setText("26");
    }
    
    private void initBarChart() {
        // Création d'une série de données pour le graphique
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        // Ajout de données mensuelles (données fictives)
        series.getData().add(new XYChart.Data<>("Janvier", 30));
        series.getData().add(new XYChart.Data<>("Février", 37));
        series.getData().add(new XYChart.Data<>("Mars", 30));
        series.getData().add(new XYChart.Data<>("Avril", 28));
        series.getData().add(new XYChart.Data<>("Mai", 40));
        
        // Ajout de la série au graphique
        barChartTraitementsMois.getData().add(series);
        
        // Application d'un style aux barres (couleur noire)
        String css = ".default-color0.chart-bar { -fx-bar-fill: black; }";
        barChartTraitementsMois.getStylesheets().add("data:text/css," + css);
    }
}