package main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
/**
 * Classe principale de l'application MedicalTreatmentApp
 * Cette classe est responsable du lancement de l'application JavaFX
 * 
 * @author pc
 */
public class MedicalTreatmentApp extends Application {
    
    // ============================================================
    // =============== CONFIGURATION DE L'APPLICATION =============
    // ============================================================
    
    /**
     * Point de départ de l'application après le lancement.
     *
     * @param primaryStage La fenêtre principale de l'application.
     * @throws Exception En cas d'erreur lors du chargement du fichier FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("/view/RegisterView.fxml"));
        
        /*// Créer la scène principale

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        // Configurer la fenêtre principale de l'application
        primaryStage.setTitle("MediConnect - Register");  // Définir le titre de la fenêtre
        primaryStage.setScene(scene);

        primaryStage.setMaximized(true);*/

        Scene scene = new Scene(root, 1500, 750);

        // Configurer la fenêtre principale de l'application
        primaryStage.setTitle("MediConnect - Register");  // Définir le titre de la fenêtre
        primaryStage.setScene(scene);                           // Attacher la scène à la fenêtre

        primaryStage.show();                                    // Afficher la fenêtre à l'écran
    }
    
    // ============================================================
    // =============== POINT D'ENTRÉE DU PROGRAMME ================
    // ============================================================
    
    public static void main(String[] args) {
        launch(args);  // Lancer l'application JavaFX
    }
}
