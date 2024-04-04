package server.exceptions;

public class DocumentBookedEmpruntException extends EmpruntException {
    public DocumentBookedEmpruntException(String hourEnd) {
        super("Erreur : ce document est réservé jusqu'à " + hourEnd);
    }
}
