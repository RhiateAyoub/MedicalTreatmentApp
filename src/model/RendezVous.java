/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe modèle représentant un rendez-vous médical dans l'application MediConnect
 * Stocke les informations d'un rendez-vous et l'état de sélection
 * 
 * @author pc
 */

public class RendezVous {
    
    // ============================================================
    // ================ ATTRIBUTS ET PROPRIÉTÉS ===================
    // ============================================================
    
    // --- Informations du rendez-vous
    private int id;
    private String nomPatient;  // Nom du patient associé au rendez-vous
    private LocalDate date;     // Date du rendez-vous
    private String type;        // Type de rendez-vous (ex: consultation, suivi, urgence)
    private LocalTime heure;    // Heure du rendez-vous
    
    // --- Propriétés observables
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    
    // ============================================================
    // ==================== CONSTRUCTEURS =========================
    // ============================================================
    
    // --- Constructeur pour AcceuilController (sans id, heure)
    public RendezVous(String nomPatient, LocalDate date, String type) {
        this.nomPatient = nomPatient;
        this.date = date;
        this.type = type;
    }
    
    // --- Constructeur pour RendezVousController (sans type)
    public RendezVous(int id, String nomPatient, LocalDate date, LocalTime heure) {
        this.id = id;
        this.nomPatient = nomPatient;
        this.date = date;
        this.heure = heure;
    }
    
    // ============================================================
    // ================ ACCESSEURS ET MUTATEURS ===================
    // ============================================================
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNomPatient() { return nomPatient; }
    public void setNomPatient(String nomPatient) { this.nomPatient = nomPatient; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public LocalTime getHeure() { return heure; }
    public void setHeure(LocalTime heure) { this.heure = heure; }
    
    // ============================================================
    // ============= GESTION DE L'ÉTAT DE SÉLECTION ===============
    // ============================================================
    
    // Récupère l'état de sélection du rendez-vous
    public BooleanProperty selectedProperty() {
        return selected; 
    }
    // Vérifie si le rendez-vous est sélectionné
    public boolean isSelected() { 
        return selected.get(); 
    }
    // Modifie l'état de sélection du rendez-vous
    public void setSelected(boolean selected) { 
        this.selected.set(selected); 
    }
    
    // ============================================================
    // ================ MÉTHODES DE CONVERSION ====================
    // ============================================================
    
    @Override
    public String toString() {
        return "RendezVous{" + "id=" + id + ", nomPatient='" + nomPatient + '\'' + ", date=" + date + ", heure=" + heure + '}';
    }
}
