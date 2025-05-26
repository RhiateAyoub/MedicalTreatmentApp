/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe modèle représentant un traitement médical dans l'application MediConnect.
 * Stocke les informations d'un traitement et l'état de sélection.
 * 
 * @author pc
 */
public class Traitement {
    // ============================================================
    // ================ ATTRIBUTS ET PROPRIÉTÉS ===================
    // ============================================================
    // --- Informations sur le traitement
    private String nomPatient;           // Nom du patient associé au traitement
    private String type;                 // Type de traitement (ex: chimiothérapie, radiothérapie)
    private LocalDate dateDebut;         // Date de début du traitement
    private LocalDate dateFin;           // Date de fin du traitement (peut être null si en cours)
    private String statut;               // Statut du traitement (ex: En cours, Terminé)
    private String description;          // Description supplémentaire du traitement

    // --- État de sélection pour intégration avec les vues (ex: TableView)
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    // --- Formatage des dates pour affichage utilisateur
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ============================================================
    // ========================= CONSTRUCTEURS =========================
    // ============================================================
    // --- Constructeur sans description
    public Traitement(String nomPatient, String type, LocalDate dateDebut, LocalDate dateFin, String statut) {
        this.nomPatient = nomPatient;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    // --- Constructeur avec description
    public Traitement(String nomPatient, String type, LocalDate dateDebut, LocalDate dateFin, String statut, String description) {
        this(nomPatient, type, dateDebut, dateFin, statut);
        this.description = description;
    }

    // ============================================================
    // ===================== ACCESSEURS / MUTATEURS =====================
    // ============================================================
    // --- Getters et Setters principaux
    public String getNomPatient() { return nomPatient; }
    public void setNomPatient(String nomPatient) { this.nomPatient = nomPatient; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // --- Formatage des dates pour affichage
    public String getDateDebutFormatted() {
        return dateDebut != null ? dateDebut.format(DATE_FORMATTER) : "";
    }
    public String getDateFinFormatted() {
        return dateFin != null ? dateFin.format(DATE_FORMATTER) : "-";
    }

    // ============================================================
    // ========== PROPRIÉTÉS OBSERVABLES ET BINDINGS (non @FXML) ==========
    // ============================================================
    // --- Propriété de sélection pour liaison avec des composants JavaFX
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    // ============================================================
    // ================ MÉTHODES UTILITAIRES INTERNES =================
    // ============================================================
    // --- Représentation texte de l'objet (utile pour le débogage ou l'affichage)
    @Override
    public String toString() {
        return "Traitement{" +
                "nomPatient='" + nomPatient + '\'' +
                ", type='" + type + '\'' +
                ", dateDebut=" + getDateDebutFormatted() +
                ", dateFin=" + getDateFinFormatted() +
                ", statut='" + statut + '\'' +
                '}';
    }
}
