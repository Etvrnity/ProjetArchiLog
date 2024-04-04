package server.exceptions;

public class EmpruntException extends Exception {

    public EmpruntException(String message) {
        super(message);
    }

    public EmpruntException() {
        super("Erreur : réservation/emprunt impossible");//TODO différenciation
    }
}
