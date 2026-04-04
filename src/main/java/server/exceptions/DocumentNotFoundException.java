package server.exceptions;

public class DocumentNotFoundException extends Exception {
    public DocumentNotFoundException() {
        super("Erreur : ce document n'existe pas");
    }
}
