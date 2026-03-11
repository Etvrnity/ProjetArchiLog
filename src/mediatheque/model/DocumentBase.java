package mediatheque.model;

import mediatheque.exceptions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Classe abstraite factorisant la logique commune aux documents (Livre, DVD).
 * Gère les états : DISPONIBLE, RESERVÉ, EMPRUNTÉ.
 */
public abstract class DocumentBase implements Document {

    public enum Etat { DISPONIBLE, RESERVE, EMPRUNTE }

    private static final long DUREE_RESERVATION_MS = 2 * 60 * 60 * 1000L; // 2 heures
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH'h'mm");

    private final String id;
    private final String titre;

    protected Etat etat = Etat.DISPONIBLE;
    protected Abonne abonneEnCours = null;   // abonné ayant réservé ou emprunté
    private LocalDateTime finReservation = null;
    private Timer timerReservation = null;

    protected DocumentBase(String id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    @Override
    public String idDoc() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public Etat getEtat() {
        return etat;
    }

    // ------------------------------------------------------------------ //
    //  Méthode de vérification spécifique au type de document (ex: âge)  //
    // ------------------------------------------------------------------ //
    protected abstract void verifierEmprunt(Abonne ab) throws EmpruntException;

    // ------------------------------------------------------------------ //
    //  Implémentation de l'interface Document                             //
    // ------------------------------------------------------------------ //

    @Override
    public synchronized void reservation(Abonne ab) throws ReservationException {
        switch (etat) {
            case RESERVE:
                throw new ReservationException(
                    "« " + titre + " » est déjà réservé jusqu'à "
                    + finReservation.format(FMT) + ".");
            case EMPRUNTE:
                throw new ReservationException(
                    "« " + titre + " » est actuellement emprunté, impossible de le réserver.");
            case DISPONIBLE:
                etat = Etat.RESERVE;
                abonneEnCours = ab;
                finReservation = LocalDateTime.now().plusNanos(DUREE_RESERVATION_MS * 1_000_000L);
                lancerTimerAnnulation();
        }
    }

    @Override
    public synchronized void emprunt(Abonne ab) throws EmpruntException {
        // vérifications spécifiques (ex: age DVD adulte)
        verifierEmprunt(ab);

        switch (etat) {
            case EMPRUNTE:
                throw new EmpruntException(
                    "« " + titre + " » est déjà emprunté.");
            case RESERVE:
                if (!abonneEnCours.equals(ab)) {
                    throw new EmpruntException(
                        "« " + titre + " » est réservé jusqu'à "
                        + finReservation.format(FMT)
                        + " pour l'abonné " + abonneEnCours.getNom() + ".");
                }
                // réservé pour cet abonné : on peut emprunter
                annulerTimer();
                etat = Etat.EMPRUNTE;
                abonneEnCours = ab;
                finReservation = null;
                break;
            case DISPONIBLE:
                etat = Etat.EMPRUNTE;
                abonneEnCours = ab;
                break;
        }
    }

    @Override
    public synchronized void retour() throws RetourException {
        if (etat == Etat.DISPONIBLE) {
            throw new RetourException(
                "« " + titre + " » est déjà disponible, retour impossible.");
        }
        annulerTimer();
        etat = Etat.DISPONIBLE;
        abonneEnCours = null;
        finReservation = null;
    }

    // ------------------------------------------------------------------ //
    //  Timer d'annulation de réservation                                  //
    // ------------------------------------------------------------------ //

    private void lancerTimerAnnulation() {
        timerReservation = new Timer(true);
        timerReservation.schedule(new TimerTask() {
            @Override
            public void run() {
                annulerReservationExpiree();
            }
        }, DUREE_RESERVATION_MS);
    }

    private synchronized void annulerReservationExpiree() {
        if (etat == Etat.RESERVE) {
            System.out.println("[Timer] Réservation expirée pour « " + titre
                + " » (abonné : " + abonneEnCours.getNom() + ")");
            etat = Etat.DISPONIBLE;
            abonneEnCours = null;
            finReservation = null;
        }
        timerReservation = null;
    }

    private void annulerTimer() {
        if (timerReservation != null) {
            timerReservation.cancel();
            timerReservation = null;
        }
    }

    // ------------------------------------------------------------------ //
    //  Infos                                                               //
    // ------------------------------------------------------------------ //

    public String getInfoEtat() {
        return switch (etat) {
            case DISPONIBLE -> "disponible";
            case RESERVE    -> "réservé jusqu'à " + finReservation.format(FMT)
                                + " pour " + abonneEnCours.getNom();
            case EMPRUNTE   -> "emprunté par " + abonneEnCours.getNom();
        };
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + "] « " + titre + " » — " + getInfoEtat();
    }
}
