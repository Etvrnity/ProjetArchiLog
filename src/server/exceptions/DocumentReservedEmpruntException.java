package server.exceptions;

public class DocumentReservedEmpruntException extends EmpruntException {
    public DocumentReservedEmpruntException(String hourEnd) {
        super("Erreur : ce document est réservé jusqu'à " + hourEnd);
    }
}
