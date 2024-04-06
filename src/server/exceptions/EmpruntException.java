package server.exceptions;

public class EmpruntException extends Exception {

    public EmpruntException(String message) {
        super(message);
    }

    public EmpruntException(boolean isReservation) {
        super(isReservation ? "Erreur : réservation impossible" : "Erreur : emprunt impossible");
    }
}
